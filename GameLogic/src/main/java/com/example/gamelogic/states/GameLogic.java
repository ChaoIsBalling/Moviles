package com.example.gamelogic.states;
import com.example.androidengine.AndroidEngine;
import com.example.androidengine.State;
import com.example.androidengine.TouchEvent;
import com.example.androidengine.AndroidGraphics;
import com.example.androidengine.AndroidAudio;
import com.example.androidengine.AndroidMobile;
import com.example.gamelogic.Color;
import com.example.gamelogic.button.Button;
import com.example.gamelogic.Casilla;
import com.example.gamelogic.Enemy;
import com.example.gamelogic.managers.UIManager;
import com.example.gamelogic.managers.WaveManager;
import com.example.gamelogic.towers.FireTower;
import com.example.gamelogic.towers.IceTower;
import com.example.gamelogic.Image;
import com.example.gamelogic.towers.MiniThunderTower;
import com.example.gamelogic.figure.Square;
import com.example.gamelogic.Text;
import com.example.gamelogic.towers.ThunderTower;
import com.example.gamelogic.TipoTorre;
import com.example.gamelogic.towers.Tower;
import com.example.gamelogic.Vector2D;
import com.example.gamelogic.towers.TowerFactory;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import java.util.HashMap;



/**
 * Clase que representa la interfaz principal de juego, donde se desarrolla toda su lógica de gameplay
 */
public class GameLogic implements State {
    //Botones
    private Button botonMejoraTriangulos;
    private Button botonMejoraCuadrados;
    private Button botonMejoraHexagonos;
    private Button botonMejoraMini;

    private TowerFactory towerFactory = new TowerFactory();

    private HashMap<TipoTorre, String> skins = new HashMap<>();

    //IDs para elementos que queramos cambiar durante la partida
    private final String BUT_RAYO_ID = "BUT_RAYO", BUT_HIELO_ID = "BUT_HIELO", BUT_FUEGO_ID = "BUT_FUEGO", BUT_MINI_ID = "BUT_MINI";
    private final String TEXT_VIDA_ID = "TEXT_VIDA" , TEXT_DINERO_ID= "TEXT_DINERO", TEXT_OLEADA_ID = "TEXT_OLEADA";


    boolean mini = false;//si esta desbloqueada la nueva torre

    private Button botonMejoraAtaque;
    private Button botonMejoraRango;
    private Button botonMejoraVelocidad;

    //Franja en la que están los botones
    private Square franjaGris;

    //Numero de filas y columnas
    int fil;
    int col;

    int vida = 0;
    float dinero = 0;

    //Las dimensiones de una casilla
    float anchoCasilla = 35;
    float altoCasilla = 35;

    //Variable que indica en que nivel estamos
    int levelNumber;

    //Json que maneja el estilo de los botones y textos
    JSONObject style;

    //Arrays de casillas, torres y enemigos
    ArrayList<ArrayList<Casilla>> casillas;
    ArrayList<Tower> torres;
    ArrayList<Enemy> enemigos;
    ArrayList<Enemy> deadEnemies;

    //referencias a módulos del motor
    AndroidEngine engine;
    AndroidAudio audio;
    AndroidGraphics gr;

    //La torre que mantengamos seleccionada
    Tower torreSeleccionada;

    //Textos de vida y dinero
    Text textoV;
    Text textoD;

    float damTorre = 2;//mejora de daño
    float ranTorre = (float) 11.7;//mejora de rango
    float velTorre = (float) -0.2;//mejora de velocidad

    int oleada;//numero oleada
    int oleadasRestantes;//oleadas restantes
    Text textoOleadas;//Numero de oleadas en texto

    private JSONObject save; //Archivo de Guardado del Juego
    JSONArray camino; //Array con el numero de puntos que debe recorrer el enemigo en JSON
    ArrayList<Vector2D> caminoEnemigos;//el camino que recorren los enemigos

    //offsets para centrar
    float offsetX = 30;
    float offsetY = 50;

    int ancho = 0;

    int alto = 0;
    //Indica si el nivel se ha completado en el modo aventura
    boolean isCompleted;

    //nivel y mundo actual
    int nivel;
    int mundo;

    private int precioAPagar;
    private TipoTorre tipoTorreSeleccionado;
    private int costeRayo, costeHielo, costeFuego,costeMini;

    private String CURRENT_BUT_ID = " ";

    //Enumaerado que determina en que estado de juego estamos
    public enum Estado {
        NORMAL, botonRayo, botonFuego, botonHielo, TORRE, botonMini, CONSTRUCCION
    }

    //Enumaerado que determina en que estado de juego estamos
    /*public enum Estado {
        NORMAL, TORRE, CONSTRUCCION
    }*/


    //Estado actual de juego
    private Estado estado = Estado.NORMAL;

    private AndroidMobile mobile;

    public enum Dificultad {//si estamos en la aventura, partida corta, partida larga o modo infinito
        corto, largo, infinito, aventura
    }
    private Dificultad dificultad;
    //JSONArray que gestiona las oleadas en el juego
    JSONArray oleadasDatos;
    //Para leer el mapa
    JSONObject obj;

    private String fondo;

    //Generador de numeros aleatorios de java
    Random rnd;

    //Manager de las oleadas de enemigos
    private WaveManager wave;

    //Manager de la HUD del juego
    //private GameHUDManager hud;


    private UIManager ui;

    /**
     * Constructora del estado principal de juego en el modo normal
     * @param engine Motor
     */
    public GameLogic(AndroidEngine engine, AndroidMobile mobile, Dificultad dificultad,JSONObject save){
        this.save = save;
        this.engine=engine;
        this.dificultad = dificultad;

        //Elegimos un nivel entre un subcojunto de niveles de la carpeta Mapas
        int l = this.engine.getDirectoryLenght("Mapas");
        rnd = new Random();
        int level = rnd.nextInt(l) + 1;

        //Inicializar parámetros
        this.init();

        //Inicializamos el nivel correspondiente
        this.inicializarNivel("Mapas/mapa" + level + ".json");
        this.mobile = mobile;
        this.mobile.setVisibleAdBanner(false);
    }
    /**
     * Constructora del estado principal de juego en el modo aventura a partir de la lectura del mapa del nivel
     */
    public GameLogic(AndroidEngine engine, AndroidMobile mobile, String mapa, boolean isCompleted, int nivel, int mundo,JSONObject save){
        this.save=save;
        this.engine=engine;
        this.dificultad = Dificultad.aventura;
        this.oleadasRestantes=0;
        this.isCompleted = isCompleted;
        this.inicializarNivel(mapa);
        this.init();
        this.mobile = mobile;
        this.mobile.setVisibleAdBanner(false);
        this.nivel=nivel;
        this.mundo=mundo;
    }
    //inicializa parametros
    private void init() {
        this.vida=10;
        this.dinero = 300;
        this.oleada =1;
        //Inicializamos listas de entidades
        this.torres = new ArrayList<Tower>();
        this.enemigos = new ArrayList<Enemy>();
        this.deadEnemies = new ArrayList<Enemy>();
        this.casillas = new ArrayList<ArrayList<Casilla>>();
        this.caminoEnemigos = new ArrayList<Vector2D>();

        //Esto hay que hacerlo en el UI Manager
        //this.textoOleadas = new Text("Inika-Regular.ttf","Oleada:" + this.oleada,60,15,25);
        this.franjaGris = new Square(300,370,600,100,true);
        this.franjaGris.setColor("#FF999999");

    }
 /**
     * Metodo que lee los datos del nivel desde un archivo json
     * @param mapa ruta del archivo
     */
    private void inicializarNivel(String mapa)
    {
        //Cargamos los datos del json del mapa
        this.cargarDatos();
        this.obj=engine.readJsonFile(mapa);
        JSONArray arr= null; //array del mapa
        try {
            arr = obj.getJSONArray("mapa");
            this.levelNumber=obj.getInt("level"); //numero de nivel
            this.oleadasDatos =obj.getJSONArray("waves"); //oleadas de enemigos
            this.camino = obj.getJSONArray("road"); //camino de puntos de los enemigos
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        //Oleadas que hay en total dependiendo del modo de juego
        switch(this.dificultad) {
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
                this.oleadasRestantes=this.oleadasDatos.length();
                break;
        }

        //Esto lo hace el waveManager
        /*this.oleadaGenerar =0;
        this.oleadasT = this.oleadasDatos.length();
        try {
            this.enemigosGenerar = this.oleadasDatos.getJSONObject(this.oleadaGenerar).getInt("amount");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        this.tiempoOleada = 3*this.enemigosGenerar;
        this.tiempOl = this.tiempoOleada;
        this.tiempoEnGenerar = (float) 0.3;
        this.tiempEnG =0;*/

        //dimensiones tablero
        this.fil=arr.length();
        try {
            this.col=arr.get(0).toString().length();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        this.ancho = col*(int)anchoCasilla;
        this.alto= fil*(int)altoCasilla;

        float anchoM = this.anchoCasilla*this.col;
        this.offsetX = ((600-anchoM)/2)+(this.altoCasilla/2);//calcular offset

        int numPuntos = this.camino.length(); //tamaño del array del json
        this.caminoEnemigos = new ArrayList<>(numPuntos);

        //Vamos metiendo cada una de las coordenadas del
        //vector road del JSON al camino de enemigos en forma de coordenadas del tablero
        for(int i = 0; i < numPuntos;i++){
            JSONArray pair = null;
            int x=0; int y=0;

            try {
                pair = this.camino.getJSONArray(i);
                 x = pair.getInt(0);
                 y = pair.getInt(1);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            this.caminoEnemigos.add(new Vector2D(x,y));
        }

        //rellenamos el mapa
        for (int i =0; i<this.fil;i++){
            ArrayList<Casilla> fila = new ArrayList<Casilla>();
            for(int j =0; j<this.col;j++){
                Casilla casilla;
                //Calculamos la posicion de la casilla
                float posX = j * altoCasilla + this.offsetX;
                float posY = i * anchoCasilla + this.offsetY;

                try {
                    char tipoCasilla = arr.get(i).toString().charAt(j);

                    //Es caminable si en el json no es una h
                    boolean caminable = (tipoCasilla != 'h');
                    casilla = new Casilla(posX, posY, this.anchoCasilla, this.altoCasilla, caminable, caminable);

                    if(tipoCasilla == 'h')
                         casilla.setColor(Color.NEGRO.getHex());
                    else
                        casilla.setColor(Color.MARRON.getHex());
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                casilla.setCoor(new Vector2D(i, j));
                fila.add(casilla);
            }
            this.casillas.add(fila);
        }
    }

    //carga el progreso y comprueba que no ha sido modificado
    private void cargarDatos(){
        try {
            this.fondo = this.save.getString("fondo");
            this.mini = true;
            // this.mini = this.save.getBoolean("mini");

            //Leemos los valores de las skins del juego
            String skin = this.save.getString("skinRayo");
            skins.put(TipoTorre.RAYO, skin);

            skin = this.save.getString("skinHielo");
            skins.put(TipoTorre.HIELO,skin);

            skin = this.save.getString("skinFuego");
            skins.put(TipoTorre.FUEGO,skin);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
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
            if (this.enemigos.get(i).Win()) {
                this.vida--;
                //this.hud.actualizaVidas(this.vida);
                this.ui.setTextUI(TEXT_VIDA_ID, String.valueOf(this.vida));
                //Añadimos al enemigo en la lista de ganadores para que sea eliminado (se comprueba en el tercer if)
                deadEnemies.add(this.enemigos.get(i));
            }
            if (this.enemigos.get(i).Dead()) { //En caso de morir nos da dinero y lo eliminamos
                deadEnemies.add(this.enemigos.get(i));
                this.dinero += 50;
                this.ui.setTextUI(TEXT_DINERO_ID, String.valueOf(this.dinero));
                //this.hud.actualizaDinero(this.dinero);
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
        int oleadasRestantes = wave.getNumOleadasRestantes();
        if (oleadasRestantes == 0 && this.vida > 0 && this.enemigos.isEmpty()) {
            this.stopSoundTorres();

            //comprobamos si el nivel que hemos derrotado es un nivel nuevo
            try {
                if(this.levelNumber>save.getInt("completed"))
                {
                    save.put("completed",this.levelNumber);
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            //Vamos al estado de GameOver
            GameOver gameOver = new GameOver(this.engine, this.audio, this.mobile,this.dificultad,true, this.isCompleted, this.nivel, this.mundo, this.oleada,this.save);
            this.engine.setState(gameOver);
        }

        //En caso de que haya perdido
        if (this.vida <= 0) {
            this.stopSoundTorres();
            GameOver gameOver = new GameOver(this.engine, this.audio, this.mobile,this.dificultad,false, this.isCompleted, this.nivel, this.mundo, this.oleada,this.save);
            this.engine.setState(gameOver);
        }
    }

    /**
     * Bucle principal del estado de juego
     * @param deltaTime Tiempo trascurrido
     */
    @Override
    public void update(double deltaTime) {

        //Actualizamos el gestor de oleadas
        this.wave.update(deltaTime);

        //Actualizamos las variables y entidades necesarias
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

        int j = (int) ((x - this.offsetX) / this.anchoCasilla);
        int i = (int) ((y - this.offsetY) / this.altoCasilla);

        Vector2D c = new Vector2D(i, j);
        //System.out.println("("+c.getX()+","+c.getY()+")");
        return c;
    }

    /**
     * Dada una posición (x,y) del ratón se determina a que casilla esta clicando
     */
    public Vector2D determinaCasillaRaton(float x, float y) {
        if (x < this.offsetX - this.anchoCasilla / 2 || y < this.offsetY - this.altoCasilla / 2) {
            return new Vector2D(-1, -1);
        }

        int j = (int) (((x + (this.anchoCasilla / 2) - this.offsetX)) / this.anchoCasilla);
        int i = (int) (((y + (this.altoCasilla / 2) - this.offsetY)) / this.altoCasilla);

        Vector2D c = new Vector2D(i, j);
        //System.out.println("("+c.getX()+","+c.getY()+")");

        return c;
    }

    /**
     * Metodos para obtener las coordenadas reales de una casilla a partir de
     * sus coordenadas en el tablero
     * @return coordenada real
     */
    public float getRealX(int colCoor) {
        return this.casillas.get(0).get(colCoor).getX();
    }

    public float getRealY(int filCoor){ return this.casillas.get(filCoor).get(0).getY(); }

    public  ArrayList<ArrayList<Casilla>> getCasillas(){return this.casillas;}

    /**
     * Metodo que renderiza el tablero, entidades y botones
     * @param gr Graphics del motor
     */
    @Override
    public void render(AndroidGraphics gr) {
        //gr.setColor(0x00000000);
        gr.clear();

        this.franjaGris.Render(gr);
        //Renderizado del mapa
        for (int i = 0; i < this.fil; i++) {
            for (int j = 0; j < this.col; j++) {
                this.casillas.get(i).get(j).Render(gr);
            }
        }
        //renderizado de enemigos y torres
        for (int i = 0; i < this.enemigos.size(); i++) {
            this.enemigos.get(i).Render(gr);
        }
        for (int i = 0; i < this.torres.size(); i++) {
            this.torres.get(i).Render(gr);
        }

        //Renderizado de todos los elementos del HUD
        this.ui.render(gr);
    }

    /**
     * Metodo que inicializa todos los botones y otros elementos de la UI
     */
    public void inicializarUI() {
        //Leemos el json de estilos de botones y elementos del juego
        this.style = engine.readJsonFile("GameLogic/style2.json");
        //Inicializamos todos los elementos de la UI
        this.ui = new UIManager(this.style,this.engine, this.gr);
        this.aplicarSkins();
        this.aplicarCompras();
        this.inicializarBotones();
    }

    /**
     * Metodo que inicaliza los botones de la UI asignandole su correspondiente callback
     */
    void inicializarBotones(){
        //Primero leemos el valor del texto de los botones (es el precio)
        this.costeRayo = Integer.parseInt(this.ui.getButtonUIText(BUT_RAYO_ID));
        this.costeHielo = Integer.parseInt(this.ui.getButtonUIText(BUT_HIELO_ID));
        this.costeFuego = Integer.parseInt(this.ui.getButtonUIText(BUT_FUEGO_ID));

        if(this.mini)
            this.costeMini = Integer.parseInt(this.ui.getButtonUIText(BUT_MINI_ID));

        //Una vez hemos leido los valores, seteamos los listeners con el valor de los precios
        this.ui.getButtonUI(BUT_RAYO_ID).setOnClickListener(() -> this.prepararRayo());
        this.ui.getButtonUI(BUT_HIELO_ID).setOnClickListener(() -> this.prepararHielo());
        this.ui.getButtonUI(BUT_FUEGO_ID).setOnClickListener(() -> this.prepararFuego());

        //la torre esta desbloqueada...
        if(this.mini)
            this.ui.getButtonUI(BUT_MINI_ID).setOnClickListener(() -> this.prepararMini());

    }

    /**
     * Metodo que se encarga de comprobar si el jugador ha comprado skin, y si es así, aplicarla
     */
    public void aplicarSkins(){
        //depende de la skin el boton es figura o skin seleccionada
        Button b = this.ui.getButtonUI(BUT_RAYO_ID);
        if (!Objects.equals(skins.get(TipoTorre.RAYO), "Figura")) {
            b.getImgButton().setVisible(true);
            b.getFigButton().setVisible(false);
        } else {
            b.getImgButton().setVisible(false);
            b.getFigButton().setVisible(true);
        }
        //b.getImgButton().setVisible(true);
        //b.getFigButton().setVisible(false);

        b= this.ui.getButtonUI(BUT_FUEGO_ID);
        if (!Objects.equals(skins.get(TipoTorre.FUEGO), "Figura")) {
            b.getImgButton().setVisible(true);
            b.getFigButton().setVisible(false);
        } else {
            b.getImgButton().setVisible(false);
            b.getFigButton().setVisible(true);
        }
        b=  this.ui.getButtonUI(BUT_HIELO_ID);
        if (!Objects.equals(skins.get(TipoTorre.HIELO), "Figura")) {
            b.getImgButton().setVisible(true);
            b.getFigButton().setVisible(false);
        } else {
            b.getImgButton().setVisible(false);
            b.getFigButton().setVisible(true);
        }
    }

    /**
     * Metodo que comprueba si el jugador ha comprado alguna torre nueva en la tienda y si es asi la habilita
     */
    private void aplicarCompras(){
        //Si no tiene mas cosas de la tienda, como la torre mini... se desactiva
        if(!this.mini){
            this.ui.getButtonUI(BUT_MINI_ID).setVisible(false);
            this.ui.getButtonUI(BUT_MINI_ID).setEnabled(false);
        }

    }

    /**
     * Gestiona la interacción de la entrada con el juego
     * @param list Lista de eventos
     * @param elapseTime Tiempo trascurrido
     */
    @Override
    public void handleInput(ArrayList<TouchEvent> list, double elapseTime) {
        for (TouchEvent e : list) {
            //Si es nulo no se procesa
            if (e == null || e.type == null) {
                continue;
            }
            switch (e.type) {
                case TOUCH_DOWN:
                    gestionEstadosJuego(e);
            }
        }
    }

    /**
     * Inicializa un audio a las torres
     * @param audio Interfaz Audio
     */
    @Override
    public void setAudio(AndroidAudio audio) {
        this.audio = audio;
        for (int i = 0; i < this.torres.size(); i++) {
            this.torres.get(i).setAudio(this.audio);
        }
    }

    @Override
    public void setMobile(AndroidMobile mobile) {
        this.mobile = mobile;
    }

    @Override
    public JSONObject getSave() {
        return this.save;
    }

    /**
     * Inicializa graphics y la UI
     * @param gr Graphics
     */
    @Override
    public void setGraphics(AndroidGraphics gr) {
        this.gr = gr;
        this.inicializarUI();

        //Inicializamos manager de oleadas (lo pongo aqui porque se debe iniclizar despues del setState)
        this.wave = new WaveManager(this, this.oleadasRestantes, this.style, this.oleadasDatos,this.gr);
    }

    /**
     * Metodo que gestiona los estados del juego
     */
    private void gestionEstadosJuego(TouchEvent e) //maneja los estados del juego cuando pulsas botones o las torres
    {
        if(this.ui.handleInput(e))
            return;

        //Determinamos si la casilla que hemos pulsado es valida
        Vector2D casillaCoor = this.determinaCasillaRaton(e.x, e.y);

        //Si no es valida, entonces no se devuelve nada y volvemos al estado Normal del juego
        if(!casillaValida(casillaCoor.getX(), casillaCoor.getY())){
            resetearEstado();
            return;
        }

        //Como la casilla es valida, ahora compruebo si en esa casilla había una torre o no
        Casilla casillaActual = this.casillas.get(casillaCoor.getX()).get(casillaCoor.getY());
        Tower torreEnCasilla = casillaActual.getTorre();

        //Si la habia -> modo torre
        //Si no la habia y estoy en modo construccion -> la intento construir
        if(torreEnCasilla != null){
            this.torreSeleccionada = torreEnCasilla;
            this.estado = Estado.TORRE;
        }else if(this.estado == Estado.CONSTRUCCION) {
            comprarTorre(casillaActual, e);
        }
    }

    /**
     * Metodo que maneja la logica cuando tocas un boton de torre
     * @param e touch event
     */
    private void comprarTorre(Casilla casillaObjetivo, TouchEvent e)
    {
        // Ver si la skin de la torre está activa
        Image skin = null;
        if (!Objects.equals(skins.get(tipoTorreSeleccionado), "Figura"))
            skin = this.ui.getButtonImage(CURRENT_BUT_ID);


        // Decimos a la factoría que fabrique la torre
        Tower torreR = towerFactory.getTower(
                tipoTorreSeleccionado,
                casillaObjetivo.getX(),
                casillaObjetivo.getY(),
                skin
        );

        // Ponemos la torre en la posicion que corresponda
        if (torreR != null) {
            ponerTorre(casillaObjetivo, torreR);
            this.dinero -= precioAPagar;
            this.ui.getTextUI(TEXT_DINERO_ID).setText(String.valueOf(dinero));
            resetearEstado();
        }
    }

    /**
     * Metodo que construye la torre del tipo que quieras
     * @param tipo Tipo del que sera la torre
     * @param cx coordenada x
     * @param cy coordanada y
     * @param buttonID string de la ID del boton
     * @return
     */
    /*private Tower construirTorre(TipoTorre tipo,float cx, float cy, String buttonID){
        Image skin = null;
        if(!Objects.equals(skins.get(tipo), "Figura")){
            skin = this.ui.getButtonImage(buttonID);

            return new ThunderTower(cx, cy,skin);
        }else
            return new ThunderTower(cx,cy);
    }*/

    /**
     * Metodo que se encarga de instanciar una torre en el tablero
     * @param c casilla en la que la queremos poner
     * @param torre la torre que vamos a poner
     */
    private void ponerTorre(Casilla c, Tower torre){
        torre.setListaEnemigos(this.enemigos);
        torre.setAudio(this.audio);
        this.casillas.get(c.getCoor().getX()).get(c.getCoor().getY()).setTorre(torre);
        this.torres.add(torre);
    }

    /**
     * Metodo que devuelve al juego a su estado base, es decir, al estado normal y sin ninguna torre seleccionada
     */
    private void resetearEstado(){
        this.estado = Estado.NORMAL;
        this.tipoTorreSeleccionado = null;
        if(this.ui.getButtonUI(CURRENT_BUT_ID) != null) {
            this.ui.getButtonUI(CURRENT_BUT_ID).setColor(Color.BLANCO.getHex());
            CURRENT_BUT_ID = " ";
        }
    }

    /**
     * Metodo que determina si la casilla que hemos clicado no se sale de los limites del mapa
     * @param cx coordenada x de la casilla
     * @param cy coordenada y de la casilla
     * @return
     */
    public boolean casillaValida(int cx, int cy){
        return (cx < this.fil && cy < this.col
                && cx >= 0 && cy >= 0);
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
        }else if (this.botonMejoraMini.contains(e.x, e.y) && this.mini) {
            this.cambiarEstado(Estado.botonMini);
        } else {
            Vector2D casilla = this.determinaCasillaRaton(e.x, e.y);
            if (casilla.getX() < this.fil && casilla.getY() < this.col && casilla.getX() >= 0 && casilla.getY() >= 0) {
                Tower torre = this.casillas.get(casilla.getX()).get(casilla.getY()).getTorre();
                if (torre != null) {
                    this.torreSeleccionada = torre;
                    this.cambiarEstado(Estado.TORRE);
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
            String torre;
            try {
            switch (this.estado) {
                //en cada caso hacemos una comprobación de cosmeticos para poder darle el cosmetico correspondiente
                //a cada torre
                    case botonRayo:

                            if(save.getBoolean("rayo")&& !Objects.equals(save.getString("skinRayo"), "Figura")) {
                                torre = save.getString("skinRayo");
                                torreR = new ThunderTower
                                        (this.casillas.get(casillaR.getX()).get(casillaR.getY()).getX(),
                                                this.casillas.get(casillaR.getX()).get(casillaR.getY()).getY(),
                                                new Image(this.style.getJSONObject(torre), this.gr));
                            }
                            else {
                                torreR = new ThunderTower
                                        (this.casillas.get(casillaR.getX()).get(casillaR.getY()).getX(),
                                                this.casillas.get(casillaR.getX()).get(casillaR.getY()).getY());
                            }

                        break;
                    case botonFuego:
                        if(save.getBoolean("fuego")&& !Objects.equals(save.getString("skinFuego"), "Figura")) {
                            torre = save.getString("skinFuego");
                            torreR = new FireTower
                                    (this.casillas.get(casillaR.getX()).get(casillaR.getY()).getX(),
                                            this.casillas.get(casillaR.getX()).get(casillaR.getY()).getY(),
                                            new Image(this.style.getJSONObject(torre), this.gr));
                        }
                        else{
                            torreR = new FireTower
                                    (this.casillas.get(casillaR.getX()).get(casillaR.getY()).getX(),
                                            this.casillas.get(casillaR.getX()).get(casillaR.getY()).getY()                                     );
                        }
                        break;
                    case botonMini:
                        torre="TorreMini";
                        torreR = new MiniThunderTower
                                (this.casillas.get(casillaR.getX()).get(casillaR.getY()).getX(),
                                        this.casillas.get(casillaR.getX()).get(casillaR.getY()).getY(),
                                        new Image(this.style.getJSONObject(torre),this.gr));
                        break;
                    default:
                        if(save.getBoolean("hielo")&& !Objects.equals(save.getString("skinHielo"), "Figura")) {
                            torre = save.getString("skinHielo");
                            torreR = new IceTower(this.casillas.get(casillaR.getX()).get(casillaR.getY()).getX(),
                                    this.casillas.get(casillaR.getX()).get(casillaR.getY()).getY(),
                                    new Image(this.style.getJSONObject(torre), this.gr));
                        }
                        else
                        {
                            torreR = new IceTower(this.casillas.get(casillaR.getX()).get(casillaR.getY()).getX(),
                                    this.casillas.get(casillaR.getX()).get(casillaR.getY()).getY());
                        }

                        break;
                }
            }
            catch (JSONException ex) {
                throw new RuntimeException(ex);
             }
                torreR.setListaEnemigos(this.enemigos);
                torreR.setAudio(this.audio);
                this.casillas.get(casillaR.getX()).get(casillaR.getY()).setTorre(torreR);
                this.torres.add(torreR);
                this.dinero -= precio;
                //this.textoD.setText(String.valueOf(this.dinero));
                //this.hud.actualizaDinero(this.dinero);
                this.cambiarEstado(Estado.NORMAL);
        } else {
            this.cambiarEstado(Estado.NORMAL);
        }
    }

    /**
     * Metodo que cambia el estado del juego y el color de los botones
     * @param nuevoEstado
     */
    private void cambiarEstado(Estado nuevoEstado) {
        switch (nuevoEstado) {
            case NORMAL:
                this.botonMejoraTriangulos.setColor(this.fondo);
                this.botonMejoraHexagonos.setColor(this.fondo);
                this.botonMejoraCuadrados.setColor(this.fondo);
                this.botonMejoraMini.setColor(this.fondo);
                this.estado = nuevoEstado;
                break;
            case TORRE:
                this.estado = nuevoEstado;
                break;
            case botonRayo:
                this.botonMejoraTriangulos.setColor("#FFFFFB64");
                this.botonMejoraHexagonos.setColor(this.fondo);
                this.botonMejoraCuadrados.setColor(this.fondo);
                this.botonMejoraMini.setColor(this.fondo);
                this.estado = nuevoEstado;
                break;
            case botonFuego:
                this.botonMejoraTriangulos.setColor(this.fondo);
                this.botonMejoraHexagonos.setColor("#FFFFFB64");
                this.botonMejoraCuadrados.setColor(this.fondo);
                this.botonMejoraMini.setColor(this.fondo);
                this.estado = nuevoEstado;
                break;
            case botonHielo:
                this.botonMejoraTriangulos.setColor(this.fondo);
                this.botonMejoraHexagonos.setColor(this.fondo);
                this.botonMejoraCuadrados.setColor("#FFFFFB64");
                this.botonMejoraMini.setColor(this.fondo);
                this.estado = nuevoEstado;
                break;
            case botonMini:
                this.botonMejoraTriangulos.setColor(this.fondo);
                this.botonMejoraHexagonos.setColor(this.fondo);
                this.botonMejoraCuadrados.setColor(this.fondo);
                this.botonMejoraMini.setColor("#FFFFFB64");
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

    /**
     * Metodo que utiliza el gestor de oleadas para comunicarse con gameLogic y añadir un nuevo enemigo
     * @param v num vida enemigo
     * @param vel velocidad
     * @param def defensa
     * @param res resistencia
     */
    public void nuevoEnemigo(float v, float vel, float def, float res, TipoTorre tipo, Image im){
        // Añadimos el enemigo al GameLogic
        Enemy nuevoEnemigo = new Enemy(v, vel, def, res,
                tipo, this.caminoEnemigos, this);

        nuevoEnemigo.setImagen(im);
        this.enemigos.add(nuevoEnemigo);
    }

    /**
     * Actualiza el hud de numero de oleadas
     * @param oleada numero oleada
     */
    public void actualizaOleadas(int oleada){
        this.ui.setTextUI(TEXT_OLEADA_ID, "Oleada: " + String.valueOf(oleada));
    }

     public UIManager getManagerUI(){
        return this.ui;
    }

    /**
     * Metodo que pone el juego en modo construccion y setea los valores para posteriormente, internar comprar una torre
     * @param precio precio que cuesta la torre que queremos construir
     * @param tipoTorre el tipo de la torre
     * @param id el indice del boton en el UIManager
     */
    public void prepararConstruccion(int precio, TipoTorre tipoTorre, String id){
        this.estado = Estado.CONSTRUCCION;
        this.precioAPagar = precio;
        this.tipoTorreSeleccionado = tipoTorre;

        //Cambiamos el color a Amarillo del boton correspondiente
        this.ui.getButtonUI(id).setColor(Color.AMARILLO_CLARO.getHex());
        this.CURRENT_BUT_ID = id;
    }

    // Estos métodos son los que "disparan" los botones del HUD
    public void prepararRayo() { prepararConstruccion(costeRayo, TipoTorre.RAYO, BUT_RAYO_ID);
        System.out.println("Boton rayo pulsado");}
    public void prepararHielo() { prepararConstruccion(costeHielo, TipoTorre.HIELO, BUT_HIELO_ID);
        System.out.println("Boton hielo pulsado");}
    public void prepararFuego() { prepararConstruccion(costeFuego, TipoTorre.FUEGO, BUT_FUEGO_ID);
        System.out.println("Boton fuego pulsado");}

    public void prepararMini() {
        prepararConstruccion(costeMini,TipoTorre.MINI, BUT_MINI_ID);
    }

    /*
    public void mejorarAtaque(){ prepararMejora(costeMejoraAtaque, TipoMejora.ATAQUE);}
    public void mejorarRango(){prepararMejora(costeMejoraRango, TipoMejora.RANGO);}
    public void mejorarVelocidad() {prepararMejora(costeMejoraVelocidad, TipoMejora.VELOCIDAD);}
    */
}


