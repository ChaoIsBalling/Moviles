package com.example.gamelogic.managers;

import com.example.androidengine.AndroidEngine;
import com.example.androidengine.AndroidGraphics;
import com.example.gamelogic.VisualElements.Image;
import com.example.gamelogic.Tipos.TipoTorre;
import com.example.gamelogic.states.GameLogic;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Clase que implementa el manager de oleadas. Se encarga de la generacion de enemigos, grupos de enemigos y oleadas de enemigos
 * en GameLogic. Comunica a GameLogic cuando debe añadir un enemigo a traves de contadores y temporizadores
 * o cuando debe actualizar el hud con el numero de oleada actual
 */
public class WaveManager {
    //Logica del juego
    private GameLogic gl;

    // Control general
    private int oleadaActual; //Oleada en la que estamos ahora
    private int oleadasRestantes; //Oleadas que quedan para acabar el nivel

    // Control del grupo activo
    private int enemigosPorGrupo;  // Numero total de enemigos en un grupo
    private int enemigosGenerados; // Contador de enemigos generados en el grupo

    // Temporizadores
    private double timerOleada; //Tiempo hasta la generacion de la proxima oleada
    private double timerEnemigo; //Tiempo hasta la generacion del proximo enemigo

    // Tiempos fijos calculados
    private double tiempoEntreGrupos = 10.0; //Tiempo de salida entre grupos de una oleada de enemigos
    private double tiempoEntreEnemigos = 0.8; //Tiempo de salida entre enemigos

    // Escalado de stats a medida que se van spawneando enemigos
    private float mejoraVida = 2;
    private float mejoraVelocidad = 5;
    private float mejoraDefensa = 1;
    private float mejoraResistencia = 1;


    //Objetos de JSON para determinar como generar enemigos
    private JSONArray oleadasDatos;

    //JSON que contiene las imagenes de los enemigos
    private JSONObject enemiesJSON;
    //private JSONObject styleLevel;

    //Referencia al graphics de Android
    private AndroidGraphics gr;

    /**
     * Constructor del Manager de oleadas del juego
     * @param gl Refrencia al gameLogic
     * @param oleadasTotales Numero de oleadas totales que debe manejar el Manager
     * @param olDatos JSON con los datos de la oleada
     * @param gr Interfaz Gráfica de Android
     */
    public WaveManager(GameLogic gl, AndroidEngine engine, int oleadasTotales, JSONArray olDatos, AndroidGraphics gr){
        this.gl = gl;
        this.oleadasDatos = olDatos;
        this.gr = gr;

        this.enemiesJSON = engine.readJsonFile("GameLogic/enemies.json");

        //Si no hay ningun enemigo, paramos la incializacion
        if(this.oleadasDatos.length() == 0) {
            this.enemigosPorGrupo = 0;
            return;
        }

        this.oleadasRestantes = oleadasTotales;
        this.oleadaActual = 1;

        //Incializamos contadores de grupos y enemigos
        this.enemigosGenerados = 0;

        try {
            this.enemigosPorGrupo = this.oleadasDatos.getJSONObject(0).getInt("amount");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        //Inicializamos timers
        this.timerEnemigo= tiempoEntreEnemigos;
        this.timerOleada = tiempoEntreGrupos;
    }

    /**
     * Metodo que se llama en cada frame (desde el GameLogic)
     * @param deltaTime tiempo transcurrido
     */
    public void update(double deltaTime) {
        //Si el juego ya no hay más oleadas, el WaveManager se desactiva.
        if (oleadasRestantes == 0) return;

        //Si aun no hemos generado todos los enemigos del grupo y ya se ha terminado la espera
        if(enemigosGenerados < enemigosPorGrupo && timerEnemigo <= 0)
            generarEnemigo();
        //Preparamos la siguiente oleada
        else
            prepararSiguienteOleada();

        //Actualizamos los temporizadores
        actualizarTemporizadores(deltaTime);
    }

    /**
     * Metodo para prepararla siguiente oleada de enemigos en caso de que se pueda hacer
     */
    private void prepararSiguienteOleada() {
        //Si ya es tiempo de generar otra oleada...
        if(timerOleada <= 0){
            //Actualizamos los valores de las oleadas
            this.oleadaActual++;
            this.oleadasRestantes--;

            //Reseteamos contador de enemigos por grupoGenerados y su timer
            this.enemigosGenerados = 0;
            // El primer enemigo del grupo sale de inmediato
            this.timerEnemigo = 0;

            // Calculamos cuánto tardará en salir la SIGUIENTE oleada cuando la actual termine
            this.timerOleada = (this.tiempoEntreGrupos) + (2 * this.oleadaActual);

            // Decimos al gl que actualice el HUD correctamente
            if (this.oleadasRestantes != 0) {
                gl.actualizaOleadas(this.oleadaActual);
            }
            try {
                this.enemigosPorGrupo = this.oleadasDatos.getJSONObject(
                        (this.oleadaActual - 1)%this.oleadasDatos.length()).getInt("amount");
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Genera un único enemigo con las estadísticas escaladas
     */
    private void generarEnemigo() {
        TipoTorre tipo;
        JSONObject enemy = null;
        Image im;
        try {
            //Los indices van de 0 a numOleadas-1
            enemy =enemiesJSON.getJSONObject(this.oleadasDatos.getJSONObject(
                    (this.oleadaActual - 1)%this.oleadasDatos.length()).getString("enemy"));
            tipo=TipoTorre.valueOf(enemy.getString("tipo"));
            im = new Image(enemy.getJSONObject("image"),this.gr);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        float vida = 8 + (this.mejoraVida * (this.oleadaActual - 1));
        float vel = 30 + (this.mejoraVelocidad * (this.oleadaActual - 1));
        float def = (this.mejoraDefensa * (this.oleadaActual - 1));
        float res = (this.mejoraResistencia * (this.oleadaActual - 1));

        gl.nuevoEnemigo(vida,vel,def,res,tipo,im);

        //Aumentamos el contador de enemigos generados en el grupo
        this.enemigosGenerados++;

        // Reiniciamos el reloj para el próximo enemigo
        this.timerEnemigo = this.tiempoEntreEnemigos;
    }

    private void actualizarTemporizadores(double deltaTime){
        this.timerEnemigo -= deltaTime;
        this.timerOleada -= deltaTime;
    }

    /**
     * Getter del numero de oleadas restantes para informar a GameLogic
     * @return oleadas restantes
     */
    public int getNumOleadasRestantes(){
        return this.oleadasRestantes;
    }
    /**
     * Getter del numero de oleadas actuales
     * @return oleadas actuales
     */
    public int getNumOleadas(){return this.oleadaActual;}
}
