package com.example.gamelogic.managers;

import com.example.androidengine.AndroidGraphics;
import com.example.gamelogic.Image;
import com.example.gamelogic.TipoTorre;
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

    // Control de la oleada activa
    private int totalGrupos;       // Numero total de grupos en la oleada
    private int gruposGenerados;   // Contador de grupos generados

    // Control del grupo activo
    private int enemigosPorGrupo;  // Numero total de enemigos en un grupo
    private int enemigosGenerados; // Contador de enemigos generados en el grupo

    // Temporizadores
    private double timerOleada; //Tiempo hasta la generacion de la proxima oleada
    private double timerGrupo; //Tiempo hasta la generacion del proximo grupo
    private double timerEnemigo; //Tiempo hasta la generacion del proximo enemigo

    // Tiempos fijos calculados
    private double tiempoEntreGrupos = 5.0; //Tiempo de salida entre grupos de una oleada de enemigos
    private double tiempoEntreEnemigos = 0.5; //Tiempo de salida entre enemigos

    // Escalado de stats a medida que se van spawneando enemigos
    private float mejoraVida = 2;
    private float mejoraVelocidad = 5;
    private float mejoraDefensa = 1;
    private float mejoraResistencia = 1;


    //Objetos de JSON para determinar como generar enemigos
    private JSONArray oleadasDatos;
    private JSONObject styleLevel;

    //Referencia al graphics de Android
    private AndroidGraphics gr;

    //Para acceder al repertorio de imagenes que puede renderizar
    private UIManager ui;


    /**
     * Constructor del Manager de oleadas del juego
     * @param gl Refrencia al gameLogic
     * @param oleadasTotales Numero de oleadas totales que debe manejar el Manager
     * @param style JSON con el estilo de imagenes y botones
     * @param olDatos JSON con los datos de la oleada
     * @param gr Interfaz Gráfica de Android
     */
    public WaveManager(GameLogic gl, int oleadasTotales,
                       JSONObject style, JSONArray olDatos, AndroidGraphics gr){
        this.gl = gl;
        this.styleLevel = style;
        this.oleadasDatos = olDatos;
        this.gr = gr;

        this.oleadasRestantes = oleadasTotales;
        this.oleadaActual = 1;

        //Incializamos contadores de grupos y enemigos
        this.gruposGenerados = 0;
        this.enemigosGenerados = 0;

        //Numero de enemigos en un grupo y total de grupos -> en la primera oleada

        //this.enemigosPorGrupo = 1;
        this.totalGrupos = 2;

        try {
            this.enemigosPorGrupo = this.oleadasDatos.getJSONObject(0).getInt("amount");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        //Inicializamos timers
        this.timerEnemigo= tiempoEntreEnemigos;
        this.timerGrupo = tiempoEntreGrupos;
        this.timerOleada = this.timerGrupo * this.totalGrupos;

        this.ui = this.gl.getManagerUI();
    }

    /**
     * Metodo que se llama en cada frame (desde el GameLogic)
     * @param deltaTime tiempo transcurrido
     */
    public void update(double deltaTime) {
        //Si el juego ya no hay más oleadas, el WaveManager se desactiva.
        if (oleadasRestantes <= 0) return;


        //Si aun no hemos generado todos los enemigos del grupo...
        if(enemigosGenerados < enemigosPorGrupo){
            prepararSiguienteEnemigo();
        }
        //Si aun no hemos generado todos los grupos de la oleada...
        else if(gruposGenerados < totalGrupos){
            prepararSiguienteGrupo();
        }
        //Preparamos la siguiente oleada
        else{
            prepararSiguienteOleada();
        }

        //Actualizamos los temporizadores
        actualizarTemporizadores(deltaTime);
    }

    /**
     * Metodo para preparar el siguiente enemigo en caso de que se pueda hacer
     */
    private void prepararSiguienteEnemigo(){
        if(timerEnemigo <= 0){
            generarEnemigo(this.gr);
        }
    }

    /**
     * Metodo para preparar el siguiente grupo de enemigos en caso de que se pueda hacer
     */
    private void prepararSiguienteGrupo() {
        if(timerGrupo <= 0){
            this.gruposGenerados++; //aumentamos el numero de grupos
            this.timerGrupo = this.tiempoEntreGrupos; //reseteamos el timer del grupo

            //Reseteamos contador de enemigos por grupoGenerados y su timer
            this.enemigosGenerados = 0;
            // El primer enemigo del grupo sale de inmediato
            this.timerEnemigo = 0;

            int oleadaGenerar =this.oleadaActual - 1;
            //int oleadasT = this.oleadasDatos.length();
            try {
                this.enemigosPorGrupo = this.oleadasDatos.getJSONObject(oleadaGenerar).getInt("amount");
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
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

            // Reseteamos contador de grupos para continuar generando grupos en la sig oleada
            this.gruposGenerados = 0;

            aumentarDificultad();
            resetearTemporizadorOleadas();

            // Decimos al gl que actualice el HUD correctamente
            if (this.oleadasRestantes > 0) {
                gl.actualizaOleadas(this.oleadaActual);
            }
        }
    }




    /**
     * Genera un único enemigo con las estadísticas escaladas
     */
    private void generarEnemigo(AndroidGraphics gr) {
        TipoTorre tipo;
        String enemy = null;
        Image im;
        try {
            //Los indices van de 0 a numOleadas-1
            enemy = this.oleadasDatos.getJSONObject(this.oleadaActual - 1).getString("enemy");



            if(enemy.equals("goblin")) {
                tipo = TipoTorre.RAYO;
                im = this.ui.getImageUI("Goblin");
                //im=new Image(this.ui.getImageUI("Goblin"),gr);
            }
            else if(enemy.equals("imp")) {
                tipo = TipoTorre.FUEGO;
                im = this.ui.getImageUI("Imp");
                //im=new Image(this.styleLevel.getJSONObject("ImagenImp"),gr);
            }
            else {
                tipo = TipoTorre.HIELO;
                im = this.ui.getImageUI("Ogre");
                //im=new Image(this.styleLevel.getJSONObject("ImagenOgre"),gr);
            }
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
        this.timerGrupo -= deltaTime;
        this.timerOleada -= deltaTime;
    }

    /**
     * Metodo que aumenta la dificiltad del juego modificando los parametros que determinan
     * cuantos grupos se generan por oleada y los enemigos en dicho grupo
     */
    private void aumentarDificultad(){
        this.totalGrupos++; //se aumenta en 1 el grupo de ls siguiente oleada y los enemigos en el grupo
        this.enemigosPorGrupo++;
        this.tiempoEntreGrupos += (this.oleadaActual - 1); //Se tarda más en generar los grupos de la siguiente oleada
    }

    /**
     * Metodo que resetea los temporizador de las oleadas en funcion de la oleada actual
     * cuanto mayor sea la oleada actual, más aumentará el valor del timer de oleadas
     */
    private void resetearTemporizadorOleadas(){
        // Calculamos cuánto tardará en salir la SIGUIENTE oleada cuando la actual termine
        this.timerOleada = (this.tiempoEntreGrupos * this.totalGrupos) + (2 * this.oleadaActual);
    }

    /**
     * Getter del numero de oleadas restantes para informar a GameLogic
     * @return oleadas restantes
     */
    public int getNumOleadasRestantes(){
        return this.oleadasRestantes;
    }
}
