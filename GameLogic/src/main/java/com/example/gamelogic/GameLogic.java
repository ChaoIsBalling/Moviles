package com.example.gamelogic;

import com.example.engine.Graphics;
import com.example.engine.State;
import com.example.engine.TouchEvent;
import com.example.engine.Engine;
import com.example.engine.Audio;

import org.json.JSONObject;
import org.json.JSONArray;
import java.util.ArrayList;
import java.lang.Integer;

/**
 * Clase que representa la interfaz principal de juego, donde se desarrolla toda su lógica de gameplay
 */
public class GameLogic implements State {

    //Botones
    private Button botonMejoraTriangulos;
    private Button botonMejoraCuadrados;
    private Button botonMejoraHexagonos;

    private Button botonMejoraAtaque;
    private Button botonMejoraRango;
    private Button botonMejoraVelocidad;

    //Figuras que tenrán los botones
    private Square figuraBotonCuadrado;
    private Triangle figuraBotonTriangulo;
    private Hexagon figuraBotonHexagono;

    private Text costeMejoraTriangulos;
    private Text costeMejoraCuadrados;
    private Text costeMejoraHexagonos;

    private Text costeMejoraAtaque;
    private Text costeMejoraRango;
    private Text costeMejoraVelocidad;


    //Imagenes de los botones en modo torre
    private Image imagenMejoraAtaque;
    private Image imagenMejoraRango;
    private Image imagenMejoraVelocidad;

    //Imagen de los stats
    private Image imagenVida;
    private Image imagenDinero;

    //Franja en la que están los botones
    private Square franjaGris;

    //Numero de filas y columnas
    int fil;
    int col;

    int vida = 0;
    float dinero = 0;

    //Posiciones iniciales y finales
    float IniX;
    float IniY;
    float FinX;
    float FinY;

    //Las dimensiones de una casilla
    float anchoCasilla = 35;
    float altoCasilla = 35;


    //Arrays de casillas, torres y enemigos
    ArrayList<ArrayList<Casilla>> casillas;
    ArrayList<Tower> torres;
    ArrayList<Enemy> enemigos;
    ArrayList<Enemy> deadEnemies;

    //referencias a módulos del motor
    Engine engine;
    Audio audio;
    Graphics gr;

    //La torre que mantengamos seleccionada
    Tower torreSeleccionada;

    //Textos de vida y dinero
    Text textoV;
    Text textoD;

    float damTorre = 2;//mejora de daño
    float ranTorre = (float) 11.7;//mejora de rango
    float velTorre = (float) -0.2;//mejora de velocidad

    float mejVidaEn = 2;//mejora de vida de enemigo
    float mejVelEn = 2;//mejora de velocidad de enemigo
    float mejDefEn = 1;//mejora de defensa de enemigo
    float mejResEn = 1;//mejora de resistencia de enemigo

    int tipoResEn = 0;

    int oleada;//numero oleada
    int oleadasRestantes;
    int grupos;//grupos en oleada
    int enemigosGrupo;//enemigos en grupo

    float tiempoGrupos;//tiempo de espera entre grupo y grupo
    float tiempoOleada;//tiempo de espera entre oleadas

    int numG = 0;//grupos generados
    float tiempGr;//tiempo que falta para el nuevo grupo
    float tiempOl;//tiempo que falta para la nueva oleada

    int numE = 0;//enemigos generados
    float tiempoEnGrupo;//tiempo de espera entre enemigos de un grupo
    float tiempEnG;//tiempo que falta para generar un nuevo enemigo en el grupo
    int numOl;
    Text textoOleadas;//Numero de oleadas en texto
      //Enumaerado que determina en que estado de juego estamos
    private enum Estado {
        normal, botonRayo, botonFuego, botonHielo, torre
    }

    //Estado actual de juego
    private Estado estado = Estado.normal;

    public enum Dificultad {
        corto, largo, infinito, aventura
    }

    private Dificultad dificultad;
   
   
       /**
     * Constructora del estado principal de juego
     * @param engine Motor
     */
    public GameLogic(Engine engine, Dificultad dificultad){
        switch(dificultad) {
            case corto:
                this.oleadasRestantes = 3;
                break;
            case largo:
                this.oleadasRestantes=7;
                break;
            case infinito:
                this.oleadasRestantes =-1;
                break;
            case aventura:
                this.oleadasRestantes=3;
                break;
        }
        this.engine=engine;
        this.init();
        this.dificultad = dificultad;
        this.leerMapa("mapa1.json");

    }

    public GameLogic(Engine engine, String mapa){
        this.engine=engine;
        this.dificultad = Dificultad.aventura;
        this.init();
        this.leerMapa(mapa);

    }

    private void init()
    {
    this.vida=10;
    this.dinero = 300;
    this.oleada =1;
    this.grupos = 2;
    this.enemigosGrupo = 1;
    this.tiempoGrupos = 5;
    this.tiempGr = this.tiempoGrupos;
    this.tiempoOleada = this.tiempoGrupos*this.grupos + 5;
    this.tiempOl = this.tiempoOleada;
    this.tiempoEnGrupo = (float) 0.3;
    this.tiempEnG =0;
    this.textoOleadas = new Text("Inika-Regular.ttf","Oleada:" + this.oleada,60,15,25);
    this.torres = new ArrayList<Tower>();
    this.enemigos=new ArrayList<Enemy>();
    this.deadEnemies=new ArrayList<Enemy>();
    this.casillas = new ArrayList<ArrayList<Casilla>>();
    this.franjaGris = new Square(300,370,600,100,true);
    this.franjaGris.setColor(0xFF999999);
}
 /**
     * Metodo que lee mapa de un archivo txt
     * @param mapa ruta del archivo
     */
    private void leerMapa(String mapa)
    {
        JSONObject obj=engine.readJsonFile(mapa);
        this.fil=obj.getInt("fila");
        this.col=obj.getInt("columna");
        JSONArray arr= obj.getJSONArray("mapa");
        for (int i =0; i<this.fil;i++){
            ArrayList<Casilla> fila = new ArrayList<Casilla>();
            for(int j =0; j<this.col;j++){
                if(arr.get(i).toString().charAt(j) == 'h'){
                    Casilla casilla = new Casilla((float)(j*35+30),(float)(i*35+50),this.anchoCasilla,this.altoCasilla,false,false);
                                 casilla.setColor(0xff000000);
                    casilla.setCoor(new Vector2D(i, j));
                    fila.add(casilla);
                } else {
                    Casilla casilla = new Casilla((float) (j * 35 + 30), (float) (i * 35 + 50), this.anchoCasilla, this.altoCasilla, true, true);
                    casilla.setColor(0xff944d03);
                    casilla.setCoor(new Vector2D(i, j));
                    fila.add(casilla);
                    if (j == 0) {
                        this.IniX = j * 35 + 30;
                        this.IniY = i * 35 + 50;
                    }
                    if (j == this.col - 1) {
                        this.FinX = j * 35 + 30;
                        this.FinY = i * 35 + 50;
                    }
                }

            }
            this.casillas.add(fila);
        }
    }


    /**
     * Actualiza los contadores de tiempo de acuerdo al deltatime
     */
    private void actualizarTiempos(double deltaTime){
        this.tiempEnG -= deltaTime;
        this.tiempGr -= deltaTime;
        this.tiempOl -= deltaTime;
    }

    /**
     * Actualiza la lista de torres
     */
    private void actualizarTorres(double deltaTime){
        for (int i = 0; i < this.torres.size(); i++) {
            this.torres.get(i).Update(deltaTime);
        }
    }

    /**
     * Determina si el enemigo ha llegado al final del mapa
     * @param e Enemigo
     */
    private boolean haAcabado (Enemy e){
        return e.getX() >= this.FinX && e.getY() >= this.FinY;
    }

    /**
     * Elimina los enemigos derrotados de la lista de enemigos
     */
    private void limpiarListaEnemigos(){
        //Comprobamos si hay enemigos en la lista de muertos
        for (int i = 0; i < deadEnemies.size(); i++) {
            this.enemigos.remove(this.deadEnemies.get(i));
        }
        this.deadEnemies.clear();
    }

    /**
     * Actualiza la lista de enemigos
     */
    private void actualizarEnemigos(double deltaTime){

        //Vamos actualizando la lista de enemigos
        for (int i = 0; i < this.enemigos.size(); i++) {
            this.enemigos.get(i).Update(deltaTime);
            //Si el enemigo llega a la casilla final
            if (haAcabado(this.enemigos.get(i))) {
                this.vida--;
                this.textoV.setText(String.valueOf(this.vida));
                //Añadimos al enemigo en la lista de ganadores para que sea eliminado (se comprueba en el tercer if)
                this.enemigos.get(i).setWin();
            }
            if (this.enemigos.get(i).Dead()) { //En caso de morir nos da dinero y lo eliminamos
                deadEnemies.add(this.enemigos.get(i));
                this.dinero += 50;
                this.textoD.setText(String.valueOf(this.dinero));
            }
            if (this.enemigos.get(i).Win()) {//Si un enemigo gana se mete en la lista de muertos para ser eliminado
                deadEnemies.add(this.enemigos.get(i));
            }
        }

        limpiarListaEnemigos();
    }

    /**
     * Comprueba si se ha acabado la partida
     */
    private void comprobarFinal(){
        //si nos quedamos sin oleadas parar
        //En caso de que haya ganado, no habrá oleadas, enemigos y la vida es mayor a 0
        if (this.oleadasRestantes == 0 && this.vida > 0 && this.enemigos.isEmpty()) {
            this.stopSoundTorres();
            GameOver gameOver = new GameOver(this.engine, this.audio, true);
            this.engine.setState(gameOver);
        }

        //En caso de que haya perdido
        if (this.vida <= 0) {
            this.stopSoundTorres();
            GameOver gameOver = new GameOver(this.engine, this.audio, false);
            this.engine.setState(gameOver);
        }
    }

    /**
     * Metodo que gestiona la aparición de nuevas oleadas
     */
    private void gestionarOleadas(){

        if(oleadasRestantes==0)
            return;

        if (this.tiempOl <= 0) {//si tiempo entre oleadas es menor o igual a 0
            //siguiente oleada
            this.oleada++;
            this.oleadasRestantes--;

            //aumentamos grupos y enemigos por grupo y mas tiempo entre grupos
            this.grupos++;
            this.enemigosGrupo++;
            this.tiempoGrupos = 5 + (this.oleada - 1);

            //Resetear numero de grupos generados y numero de enemigos generados
            this.numG = 0;
            this.numE = 0;

            this.tiempGr = 0;//sale el primer grupo de imediato

            this.tiempoOleada = this.tiempoGrupos * this.grupos + 2 * this.oleada;//mas tiempo entre oleadas
            this.tiempOl = this.tiempoOleada;//resetear tiempo entre oleadas

            this.tiempEnG = 0;//que salga el primer enemigo de inmediato

            //actualizar texto de oleadas
            if(this.oleadasRestantes!=0)
                this.textoOleadas.setText("Oleada:" + this.oleada);
        }
    }

    /**
     * Metodo que determina cuando generar un nuevo enemigo
     */
    private void generarEnemigo(){

        //Si no hay oleadas, no generamos enemigos
        if(this.oleadasRestantes == 0)
            return;

        //Si el tiempo que falta para un nuevo grupo es mayor a 0
        //Si el número de grupos generado es mayor o igual al número total de grupos en la oleada
        //Si el número para generar un nuevo enemigo en el grupo es mayor a 0

        //No generamos más enemigos
        if(this.tiempGr > 0 || this.numG >= this.grupos || this.tiempEnG > 0)
            return;

        //si el numero de enemigos generados es menor al numero de enemigos en su grupo
        if (this.numE < this.enemigosGrupo) {
            //Generamos enemigo
            this.enemigos.add(new Enemy(this.IniX, this.IniY,
                    8 + (this.mejVidaEn * (this.oleada - 1)),
                    30 + (this.mejVelEn * (this.oleada - 1)),
                    0 + (this.mejDefEn * (this.oleada - 1)),
                    0 + (this.mejResEn * (this.oleada - 1)),
                    Tipo.getRandomType(),
                    this));

            //Incrementamos número de grupo
            this.numE++;
        }
        //si se han creado todos los enemigos del grupo
        else {
            this.numE = 0;//resetear numero de enemigos generados
            this.numG++;//incrementamos numero de grupos generados
            this.tiempGr = this.tiempoGrupos;//resetear tiempo entre grupos para que se vuelva a generar uno nuevo
        }

        this.tiempEnG = this.tiempoEnGrupo;//resetear tiempo entre enemigos
    }

    /**
     * Bucle principal del estado de juego
     * @param deltaTime Tiempo trascurrido
     */
    @Override
    public void update(double deltaTime) {
        //Primero gestionamos las oleadas y después los enemigos siempre y cuando haya oleadas
        gestionarOleadas();
        generarEnemigo();

        //Actualizamos las variables y entidades necesarias
        actualizarTiempos(deltaTime);
        actualizarTorres(deltaTime);
        actualizarEnemigos(deltaTime);

        //Comprobamos si se ha acabado la partida
        comprobarFinal();
    }

       

    /**
     * Dada una posición (x,y) se determina en que casilla está a partir
     * del ancho y alto de la casilla
     */
    public Vector2D determinaCasilla(float x, float y) {

        int offsetX = 30;
        int offsetY = 50;

        int j = (int) ((x - offsetX) / this.anchoCasilla);
        int i = (int) ((y - offsetY) / this.altoCasilla);

        Vector2D c = new Vector2D(i, j);
        //System.out.println("("+c.getX()+","+c.getY()+")");
        return c;
    }

    /**
     * Dada una posición (x,y) del ratón se determina a que casilla esta clicando
     */
    public Vector2D determinaCasillaRaton(float x, float y) {
        if (x < 30 - this.anchoCasilla / 2 || y < 50 - this.altoCasilla / 2) {
            return new Vector2D(-1, -1);
        }
        int offsetX = 30;
        int offsetY = 50;

        int j = (int) (((x + (this.anchoCasilla / 2) - offsetX)) / this.anchoCasilla);
        int i = (int) (((y + (this.altoCasilla / 2) - offsetY)) / this.altoCasilla);

        Vector2D c = new Vector2D(i, j);
        //System.out.println("("+c.getX()+","+c.getY()+")");

        return c;
    }

    /**
     * Metodo que renderiza el tablero, entidades y botones
     * @param gr Graphics del motor
     */
    @Override
    public void render(Graphics gr) {
        //gr.setColor(0x00000000);
        gr.clear();
        for (int i = 0; i < this.fil; i++) {
            for (int j = 0; j < this.col; j++) {
                this.casillas.get(i).get(j).Render(gr);
            }
        }
        for (int i = 0; i < this.enemigos.size(); i++) {
            this.enemigos.get(i).Render(gr);
        }
        for (int i = 0; i < this.torres.size(); i++) {
            this.torres.get(i).Render(gr);
        }
        this.franjaGris.Render(gr);
        if (this.estado != Estado.torre) {
            this.botonMejoraCuadrados.Render(gr);
            this.botonMejoraTriangulos.Render(gr);
            this.botonMejoraHexagonos.Render(gr);
        } else {
            this.botonMejoraAtaque.Render(gr);
            this.botonMejoraRango.Render(gr);
            this.botonMejoraVelocidad.Render(gr);
            gr.pintarCirculo(this.torreSeleccionada.getX(), this.torreSeleccionada.getY(), this.torreSeleccionada.getRange());
        }
        this.textoV.Render(gr);
        this.textoD.Render(gr);
        this.imagenVida.Render();
        this.imagenDinero.Render();
        this.textoOleadas.Render(gr);
    }


    /**
     * Metodo que inicializa todos los botones y otros elementos de la UI
     */
    public void inicializarUI() {
        this.botonMejoraCuadrados = new Button(500, 360, 50, 50, true, 20);
        this.botonMejoraTriangulos = new Button(440, 360, 50, 50, true, 20);
        this.botonMejoraHexagonos = new Button(560, 360, 50, 50, true, 20);

        this.botonMejoraAtaque = new Button(440, 360, 50, 50, true, 20);
        this.botonMejoraRango = new Button(500, 360, 50, 50, true, 20);
        this.botonMejoraVelocidad = new Button(560, 360, 50, 50, true, 20);

        this.botonMejoraCuadrados.setColor("#FFFFFFFF");
        this.botonMejoraTriangulos.setColor("#FFFFFFFF");
        this.botonMejoraHexagonos.setColor("#FFFFFFFF");

        this.botonMejoraAtaque.setColor("#FFFFFFFF");
        this.botonMejoraRango.setColor("#FFFFFFFF");
        this.botonMejoraVelocidad.setColor("#FFFFFFFF");

        this.figuraBotonCuadrado = new Square(1, -5, 30, 30, true);
        this.figuraBotonCuadrado.setColor(0xFFC8A2C8);
        this.figuraBotonTriangulo = new Triangle(1, 1, 15, true);
        this.figuraBotonHexagono = new Hexagon(1, -5, 15, true);
        this.figuraBotonHexagono.setColor(0xFFFF0000);

        this.botonMejoraCuadrados.setFigura(this.figuraBotonCuadrado);
        this.botonMejoraTriangulos.setFigura(this.figuraBotonTriangulo);
        this.botonMejoraHexagonos.setFigura(this.figuraBotonHexagono);

        this.costeMejoraCuadrados = new Text("Inika-Regular.ttf", "150", 0, 15, 15, true, true);
        this.botonMejoraCuadrados.setText(this.costeMejoraCuadrados);

        this.costeMejoraTriangulos = new Text("Inika-Regular.ttf", "100", 0, 15, 15, true, true);
        this.botonMejoraTriangulos.setText(this.costeMejoraTriangulos);

        this.costeMejoraHexagonos = new Text("Inika-Regular.ttf", "200", 0, 15, 15, true, true);
        this.botonMejoraHexagonos.setText(this.costeMejoraHexagonos);

        this.costeMejoraAtaque = new Text("Inika-Regular.ttf", "75", 0, 15, 15, true, true);
        this.botonMejoraAtaque.setText(this.costeMejoraAtaque);

        this.costeMejoraRango = new Text("Inika-Regular.ttf", "75", 0, 15, 15, true, true);
        this.botonMejoraRango.setText(this.costeMejoraRango);

        this.costeMejoraVelocidad = new Text("Inika-Regular.ttf", "100", 0, 15, 15, true, true);
        this.botonMejoraVelocidad.setText(this.costeMejoraVelocidad);

        this.imagenMejoraAtaque = new Image("Espada.png", -15, -20, 30, 30, this.gr);
        this.botonMejoraAtaque.setImagen(this.imagenMejoraAtaque);

        this.imagenMejoraRango = new Image("Arco.png", -15, -20, 30, 30, this.gr);
        this.botonMejoraRango.setImagen(this.imagenMejoraRango);

        this.imagenMejoraVelocidad = new Image("Reloj.png", -15, -20, 30, 30, this.gr);
        this.botonMejoraVelocidad.setImagen(this.imagenMejoraVelocidad);

        this.textoV = new Text("Inika-Regular.ttf", String.valueOf(this.vida), 30, 340, 20);
        this.textoD = new Text("Inika-Regular.ttf", String.valueOf(this.dinero), 30, 370, 20);
        this.imagenVida = new Image("Vida.png", 60, 330, 30, 30, this.gr);
        this.imagenDinero = new Image("Dinero.png", 60, 360, 30, 30, this.gr);
    }

    /**
     * Gestiona la interacción de la entrada con el juego
     * @param list Lista de eventos
     * @param elapseTime Tiempo trascurrido
     */
    @Override
    public void handleInput(ArrayList<TouchEvent> list, double elapseTime) {

        for (TouchEvent e : list) {

            switch (e.type) {
                case TOUCH_DOWN:
                    gestiónEstadosJuego(e);
            }
        }
    }

    /**
     * Inicializa un audio a las torres
     * @param audio Interfaz Audio
     */
    @Override
    public void setAudio(Audio audio) {
        this.audio = audio;

        for (int i = 0; i < this.torres.size(); i++) {
            this.torres.get(i).setAudio(this.audio);
        }
    }

    /**
     * Inicializa graphics y la UI
     * @param gr Graphics
     */
    @Override
    public void setGraphics(Graphics gr) {
        this.gr = gr;
        this.inicializarUI();
    }

    /**
     * Metodo que gestiona los estados del juego
     */
    private void gestiónEstadosJuego(TouchEvent e) //maneja los estados del juego cuando pulsas botones o las torres
    {
        switch (this.estado) {
            case normal://cuando ningun boton o torre está seleccionado
                if (this.botonMejoraTriangulos.contains(e.x, e.y)) {
                    this.cambiarEstado(Estado.botonRayo);
                } else if (this.botonMejoraHexagonos.contains(e.x, e.y)) {
                    this.cambiarEstado(Estado.botonFuego);
                } else if (this.botonMejoraCuadrados.contains(e.x, e.y)) {
                    this.cambiarEstado(Estado.botonHielo);
                } else {
                    Vector2D casilla = this.determinaCasillaRaton(e.x, e.y);
                    if (casilla.getX() < this.fil && casilla.getY() < this.col && casilla.getX() >= 0 && casilla.getY() >= 0) {
                        Tower torre = this.casillas.get(casilla.getX()).get(casilla.getY()).getTorre();
                        if (torre != null) {
                            this.torreSeleccionada = torre;
                            this.cambiarEstado(Estado.torre);
                        }
                    }
                }
                break;
            case torre://esta seleccionada una torre en el mapa
                Vector2D casillaT = this.determinaCasillaRaton(e.x, e.y);
                if (this.botonMejoraAtaque.contains(e.x, e.y) && this.dinero >= 75) {
                    this.torreSeleccionada.UpdateAttack(this.damTorre);
                    this.dinero -= 75;
                    this.textoD.setText(String.valueOf(this.dinero));
                } else if (this.botonMejoraRango.contains(e.x, e.y) && this.dinero >= 75) {
                    this.torreSeleccionada.UpdateRange(this.ranTorre);
                    this.dinero -= 75;
                    this.textoD.setText(String.valueOf(this.dinero));
                } else if (this.botonMejoraVelocidad.contains(e.x, e.y) && this.dinero >= 75) {
                    this.torreSeleccionada.UpdateFireRate(this.velTorre);
                    this.dinero -= 100;
                    this.textoD.setText(String.valueOf(this.dinero));
                } else if (casillaT.getX() < this.fil && casillaT.getY() < this.col && casillaT.getX() >= 0 && casillaT.getY() >= 0) {
                    Tower torre = this.casillas.get(casillaT.getX()).get(casillaT.getY()).getTorre();
                    if (torre != this.torreSeleccionada && torre != null) {
                        this.torreSeleccionada = torre;
                    } else {
                        this.cambiarEstado(Estado.normal);
                    }
                } else {
                    this.cambiarEstado(Estado.normal);
                }
                break;
            case botonRayo://has tocado el boton para crear una torre de rayo
                pulsarBotones(e, 100);
                break;
            case botonFuego://has tocado el boton para crear una torre de fuego
                pulsarBotones(e, 200);
                break;
            case botonHielo://has tocado el boton para crear una torre de hielo
                pulsarBotones(e, 150);
                break;
        }
    }

    /**
     * Metodo que gestiona la pulsación del boton para crear torres
     */
    private void pulsarBotones(TouchEvent e, float precio) {
        if (this.botonMejoraTriangulos.contains(e.x, e.y)) {
            this.cambiarEstado(Estado.botonRayo);
        } else if (this.botonMejoraHexagonos.contains(e.x, e.y)) {
            this.cambiarEstado(Estado.botonFuego);
        } else if (this.botonMejoraCuadrados.contains(e.x, e.y)) {
            this.cambiarEstado(Estado.botonHielo);
        } else {
            Vector2D casilla = this.determinaCasillaRaton(e.x, e.y);
            if (casilla.getX() < this.fil && casilla.getY() < this.col && casilla.getX() >= 0 && casilla.getY() >= 0) {
                Tower torre = this.casillas.get(casilla.getX()).get(casilla.getY()).getTorre();
                if (torre != null) {
                    this.torreSeleccionada = torre;
                    this.cambiarEstado(Estado.torre);
                } else {
                    this.crearTorres(e, precio);
                }
            }
        }
    }

    /**
     * Metodo que maneja la logica cuando tocas un boton de torre
     */
    private void crearTorres(TouchEvent e, float precio)
    {
        Vector2D casillaR = this.determinaCasillaRaton(e.x, e.y);
        if (this.dinero >= precio && !this.casillas.get(casillaR.getX()).get(casillaR.getY()).esCamino()) {
            Tower torreR;
            switch (this.estado) {
                    case botonRayo:
                        torreR = new ThunderTower(this.casillas.get(casillaR.getX()).get(casillaR.getY()).getX(), this.casillas.get(casillaR.getX()).get(casillaR.getY()).getY());
                        break;
                    case botonFuego:
                        torreR = new FireTower(this.casillas.get(casillaR.getX()).get(casillaR.getY()).getX(), this.casillas.get(casillaR.getX()).get(casillaR.getY()).getY());
                        break;
                    default:
                        torreR = new IceTower(this.casillas.get(casillaR.getX()).get(casillaR.getY()).getX(), this.casillas.get(casillaR.getX()).get(casillaR.getY()).getY());
                        break;
                }
                torreR.setListaEnemigos(this.enemigos);
                torreR.setAudio(this.audio);
                this.casillas.get(casillaR.getX()).get(casillaR.getY()).setTorre(torreR);
                this.torres.add(torreR);
                this.dinero -= precio;
                this.textoD.setText(String.valueOf(this.dinero));
                this.cambiarEstado(Estado.normal);
        } else {
            this.cambiarEstado(Estado.normal);
        }
    }

    /**
     * Metodo que cambia el estado del juego y el color de los botones
     * @param nuevoEstado
     */
    private void cambiarEstado(Estado nuevoEstado) {
        switch (nuevoEstado) {
            case normal:
                this.botonMejoraTriangulos.setColor("#FFFFFFFF");
                this.botonMejoraHexagonos.setColor("#FFFFFFFF");
                this.botonMejoraCuadrados.setColor("#FFFFFFFF");
                this.estado = nuevoEstado;
                break;
            case torre:
                this.estado = nuevoEstado;
                break;
            case botonRayo:
                this.botonMejoraTriangulos.setColor("#FFFFFB64");
                this.botonMejoraHexagonos.setColor("#FFFFFFFF");
                this.botonMejoraCuadrados.setColor("#FFFFFFFF");
                this.estado = nuevoEstado;
                break;
            case botonFuego:
                this.botonMejoraTriangulos.setColor("#FFFFFFFF");
                this.botonMejoraHexagonos.setColor("#FFFFFB64");
                this.botonMejoraCuadrados.setColor("#FFFFFFFF");
                this.estado = nuevoEstado;
                break;
            case botonHielo:
                this.botonMejoraTriangulos.setColor("#FFFFFFFF");
                this.botonMejoraHexagonos.setColor("#FFFFFFFF");
                this.botonMejoraCuadrados.setColor("#FFFFFB64");
                this.estado = nuevoEstado;
                break;

        }
    }

    /**
     * Parar los sonidos de la torre
     */
    private void stopSoundTorres(){
        for(int i =0; i < this.torres.size(); i++){
            this.torres.get(i).stopAudio();
        }
    }
}


