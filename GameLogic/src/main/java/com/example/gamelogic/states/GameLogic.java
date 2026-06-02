package com.example.gamelogic.states;
import com.example.androidengine.AndroidEngine;
import com.example.androidengine.State;
import com.example.androidengine.TouchEvent;
import com.example.androidengine.AndroidGraphics;
import com.example.androidengine.AndroidAudio;
import com.example.androidengine.AndroidMobile;
import com.example.gamelogic.Color;
import com.example.gamelogic.Tipos.TipoMejora;
import com.example.gamelogic.VisualElements.VisualElement;
import com.example.gamelogic.VisualElements.button.Button;
import com.example.gamelogic.Casilla;
import com.example.gamelogic.Enemy;
import com.example.gamelogic.VisualElements.figure.Figure;
import com.example.gamelogic.managers.UIManager;
import com.example.gamelogic.managers.WaveManager;
import com.example.gamelogic.VisualElements.Image;
import com.example.gamelogic.VisualElements.figure.Square;
import com.example.gamelogic.Tipos.TipoTorre;
import com.example.gamelogic.towers.Tower;
import com.example.gamelogic.Vector2D;
import com.example.gamelogic.towers.TowerFactory;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import java.util.ArrayList;
import java.util.Random;

/**
 * Clase que representa la interfaz principal de juego, donde se desarrolla toda su lógica de gameplay
 */
public class GameLogic implements State {
    //Factoria de las torres
    private TowerFactory towerFactory = new TowerFactory();
    private final String TEXT_VIDA_ID = "TEXT_VIDA" , TEXT_DINERO_ID= "TEXT_DINERO", TEXT_OLEADA_ID = "TEXT_OLEADA";
    //Franja en la que están los botones
    private Square placeGrey;

    //Numero de filas y columnas
    int fil, col;
    int vida = 0, dinero = 0;

    //Las dimensiones de una casilla
    float anchoCasilla = 35, altoCasilla = 35;

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

    float damTorre = 2;//mejora de daño
    float ranTorre = (float) 11.7;//mejora de rango
    float velTorre = (float) -0.2;//mejora de velocidad

    int oleadasRestantes;//oleadas restantes

    private JSONObject save; //Archivo de Guardado del Juego
    JSONArray camino; //Array con el numero de puntos que debe recorrer el enemigo en JSON
    ArrayList<Vector2D> caminoEnemigos;//el camino que recorren los enemigos

    //offsets para centrar
    float offsetX = 30, offsetY = 50;

    int ancho = 0, alto = 0;
    //nivel y mundo actual
    int nivel, mundo;
    private int precioAPagar;
    private TipoTorre tipoTorreSeleccionado;
    private String CURRENT_BUT_ID = " ";

    //Enumaerado que determina en que estado de juego estamos
    public enum Estado {
        NORMAL, TORRE, CONSTRUCCION
    }
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
    JSONObject mapaObj;

    //Fondo del propio nivel
    private Image fondoNivel;

    //Generador de numeros aleatorios de java
    Random rnd;

    //Manager de las oleadas de enemigos
    private WaveManager wave;
    //UIManager de la pantalla de juego
    private UIManager ui;

    /**
     * Constructora del estado principal de juego en el modo normal
     * @param engine Motor
     */
    public GameLogic(AndroidEngine engine, AndroidMobile mobile, Dificultad dificultad,JSONObject save){
        this.dificultad = dificultad;
        //Elegimos un nivel entre un subcojunto de niveles de la carpeta Mapas
        this.init(engine,mobile,save);
        int l = this.engine.getDirectoryLenght("Mapas");
        rnd = new Random();
        int level = rnd.nextInt(l) + 1;
        //Inicializar parámetros

        //Inicializamos el nivel correspondiente
        this.inicializarNivel("Mapas/mapa" + level + ".json");
    }
    /**
     * Constructora del estado principal de juego en el modo aventura a partir de la lectura del mapa del nivel
     */
    public GameLogic(AndroidEngine engine, AndroidMobile mobile, String mapa, int nivel, int mundo,JSONObject save){
        this.dificultad = Dificultad.aventura;
        this.oleadasRestantes=0;
        this.init(engine,mobile,save);
        this.inicializarNivel(mapa);

        this.nivel=nivel;
        this.mundo=mundo;
    }
    //inicializa parametros
    private void init(AndroidEngine engine, AndroidMobile mobile,JSONObject save) {
        this.save=save;
        this.engine=engine;
        this.vida=10;
        this.dinero = 300;
        //Inicializamos listas de entidades
        this.torres = new ArrayList<Tower>();
        this.enemigos = new ArrayList<Enemy>();
        this.deadEnemies = new ArrayList<Enemy>();
        this.casillas = new ArrayList<ArrayList<Casilla>>();
        this.caminoEnemigos = new ArrayList<Vector2D>();
        this.mobile = mobile;
        this.mobile.setVisibleAdBanner(false);

    }
 /**
     * Metodo que lee los datos del nivel desde un archivo json
     * @param mapa ruta del archivo
     */
    private void inicializarNivel(String mapa)
    {
        this.mapaObj =engine.readJsonFile(mapa);
        JSONArray arr= null; //array del mapa
        try {
            arr = mapaObj.getJSONArray("mapa");
            this.levelNumber= mapaObj.getInt("level"); //numero de nivel
            this.oleadasDatos = mapaObj.getJSONArray("waves"); //oleadas de enemigos
            this.camino = mapaObj.getJSONArray("road"); //camino de puntos de los enemigos
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
        boolean isCompleted=true;
        if (oleadasRestantes == 0 && this.vida > 0 && this.enemigos.isEmpty()) {
            this.stopSoundTorres();

            //comprobamos si el nivel que hemos derrotado es un nivel nuevo
            try {
                if(this.levelNumber>save.getInt("completed")) {
                    save.put("completed",this.levelNumber);
                    isCompleted=false;
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            //Vamos al estado de GameOver
            GameOver gameOver = new GameOver(this.engine, this.audio, this.mobile,this.dificultad,true,
                    isCompleted, this.nivel, this.mundo, this.wave.getNumOleadas(),this.save);
            this.engine.setState(gameOver);
        }
        //En caso de que haya perdido
        if (this.vida <= 0) {
            this.stopSoundTorres();
            GameOver gameOver = new GameOver(this.engine, this.audio, this.mobile,this.dificultad,false,
                    isCompleted, this.nivel, this.mundo, this.wave.getNumOleadas(),this.save);
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

    public ArrayList<ArrayList<Casilla>> getCasillas(){return this.casillas;}

    /**
     * Metodo que renderiza el tablero, entidades y botones
     * @param gr Graphics del motor
     */
    @Override
    public void render(AndroidGraphics gr) {
        gr.clear();

        //Renderizar el fondo de pantalla
        this.fondoNivel.Render(gr);
        this.placeGrey.Render(gr);

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

        //Si el juego esta en modo torre, pintamos un circulo con su rango
        if(this.estado == Estado.TORRE){
            gr.pintarCirculo(torreSeleccionada.getPosX(), torreSeleccionada.getPosY(),torreSeleccionada.getRange());
        }
    }

    /**
     * Metodo que inicializa todos los botones y otros elementos de la UI
     */
    public void inicializarUI() {
        //Leemos el json de estilos de botones y elementos del juego
        this.style = engine.readJsonFile("GameLogic/style.json");
        //Inicializamos todos los elementos de la UI
        this.ui = new UIManager(this.style,this.engine, this.gr);
        this.ui.setAllCallbacks();
        //Hacemos que los botones de construccion esten activados desde el principio
        this.ui.changeVisualElementStateOfType("tower",true);
        this.ui.changeVisualElementStateOfType("upgrade",false);
        this.inicializarContadores();
        this.cargarFondoNivel();
    }

    /**
     * Metodo que carga el fondo del nivel y la franja donde se situan los botones
     */
    private void cargarFondoNivel() {
        try {
            this.fondoNivel = new Image(mapaObj.getJSONObject("background"), this.gr);
            this.fondoNivel.setEscalado(true);
            //Seteamos las dimensiones y el comienzo donde se renderiza la imagen
            this.fondoNivel.setX((int) (this.offsetX - (this.anchoCasilla/2)));
            this.fondoNivel.setY((int) (this.offsetY - (this.altoCasilla/2)));
            this.fondoNivel.setW(this.ancho); this.fondoNivel.setH(this.alto);

            //Franja gris donde se situan los botones
            this.placeGrey = new Square(300,370,600,100,true);
            this.placeGrey.setColor(Color.GRIS.getHex());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Metodo que inicializa los contadores que tengamos en la UI
     */
    private void inicializarContadores() {
        this.ui.getTextUI(TEXT_DINERO_ID).setText(String.valueOf(this.dinero));
        this.ui.getTextUI(TEXT_VIDA_ID).setText(String.valueOf(this.vida));
        this.ui.getTextUI(TEXT_OLEADA_ID).setText("Oleada: " + String.valueOf(1));
    }

    public void setCallbackButtonUpgrade(Button b)
    {
        JSONObject callback= b.getCallback();
        try {
            TipoMejora tipo = TipoMejora.valueOf(callback.getString("TipoMejora"));
            b.setOnClickListener(()-> this.prepararMejora(Integer.parseInt(b.getTextButton().getText()),tipo));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
    public void setCallbackButtonTower(Button b)
    {
        JSONObject callback= b.getCallback();
        try {
            TipoTorre tipo = TipoTorre.valueOf(callback.getString("TipoTorre"));
            JSONObject torre=this.save.getJSONObject("torres").getJSONObject(tipo.toString());
            if(torre.getBoolean("active")) {
                b.setOnClickListener(() ->
                        this.prepararConstruccion(Integer.parseInt(b.getTextButton().getText()), tipo,b));
                setButtonSkin(torre.getString("skin"),b);
            }
            else {
                b.setEnabled(false);
                b.setVisible(false);
                this.ui.unloadVisualElementOfType("tower",b);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

    }
    /**
     * Metodo que gestiona la aplicacion de skins en las torres
     * @param skin El String que indica si la torre tiene una skin o no
     * @param b El boton al que aplicamos la skin
     */
    private void setButtonSkin(String skin,Button b)
    {
        if (!skin.equals("Figura")){
            b.getImgButton().setVisible(true);
            b.getFigButton().setVisible(false);
        }
        else {
            b.getImgButton().setVisible(false);
            b.cleanImages();
            b.getFigButton().setVisible(true);
        }
    }

    /**
     * Metodo que activa/desactiva el grupo de botones que corresponda
     * @param type El tipo de boton que queremos activar/desactivar
     * @param active true si se activa, false si se desactiva
     */


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
    public void setMobile(AndroidMobile mobile) { this.mobile = mobile;}

    @Override
    public JSONObject getSave() { return this.save; }

    /**
     * Inicializa graphics y la UI
     * @param gr Graphics
     */
    @Override
    public void setGr(AndroidGraphics gr) {
        this.gr = gr;
        this.inicializarUI();
        //Inicializamos manager de oleadas (lo pongo aqui porque se debe iniclizar despues del setState)
        this.wave = new WaveManager(this, this.engine, this.oleadasRestantes, this.oleadasDatos,this.gr);
    }

    /**
     * Metodo que gestiona los estados del juego
     */
    private void gestionEstadosJuego(TouchEvent e) //maneja los estados del juego cuando pulsas botones o las torres
    {
        //Si detecta que hemos pulsado un boton, ponemos prioridad a la accion de ese boton devolviendo return vacio
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
            //Modo Mejora
            this.torreSeleccionada = torreEnCasilla;
            this.estado = Estado.TORRE;
            this.ui.changeVisualElementStateOfType("tower",false);
            this.ui.changeVisualElementStateOfType("upgrade",true);
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
        if (this.dinero >= precioAPagar && !casillaObjetivo.esCamino()){
            // Ver si la skin de la torre está activa
            Image skin = this.ui.getButtonImage(CURRENT_BUT_ID);
            Figure figure=this.ui.getButtonFigure(CURRENT_BUT_ID);
            // Decimos a la factoría que fabrique la torre del tipo que queremos
            Tower torreR = towerFactory.getTower(
                    tipoTorreSeleccionado,
                    casillaObjetivo.getX(),
                    casillaObjetivo.getY(),
                    skin,figure
            );

            // Ponemos la torre en la posicion que corresponda
            if (torreR != null) {
                ponerTorre(casillaObjetivo, torreR);
                this.dinero -= precioAPagar;
                this.ui.getTextUI(TEXT_DINERO_ID).setText(String.valueOf(dinero));
                resetearEstado();
            }
        }
    }

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

        //Se vuelven a activar los botones de contruccion
        this.ui.changeVisualElementStateOfType("tower",true);
        this.ui.changeVisualElementStateOfType("upgrade",false);

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

    /**
     * Metodo que pone el juego en modo construccion y setea los valores para posteriormente, internar comprar una torre
     * @param precio precio que cuesta la torre que queremos construir
     * @param tipoTorre el tipo de la torre
     * @param but el boton al que le asignamos este metodo
     */
    public void prepararConstruccion(int precio, TipoTorre tipoTorre, Button but){
        this.estado = Estado.CONSTRUCCION;
        this.precioAPagar = precio;
        this.tipoTorreSeleccionado = tipoTorre;

        //Primero coloreamos todos los botones de torres en blanco
        for (VisualElement b : this.ui.getAllVisualElementsOfType("tower")) {
            b.setColor(Color.BLANCO.getHex());
        }
        //Cambiamos el color a Amarillo del boton correspondiente
        but.setColor(Color.AMARILLO_CLARO.getHex());
        this.CURRENT_BUT_ID = but.getId();
    }

    /**
     * Metodo que mejora la torre seleccionada, si es que tenemos el dinero necesario
     * @param precioMejora cantidad de dinero para mejorar un stat de la torre
     * @param mejora el tipo mejora que queremos
     */
    private void prepararMejora(int precioMejora, TipoMejora mejora){
        if (this.dinero >= precioMejora) {
            //Dependiendo del tipo de la mejora se llama a un metodo de mejora u otro
            switch(mejora){
                case ATAQUE:
                    this.torreSeleccionada.UpdateAttack(this.damTorre);
                    break;
                case RANGO:
                    this.torreSeleccionada.UpdateRange(this.ranTorre);
                    break;
                case VELOCIDAD:
                    this.torreSeleccionada.UpdateFireRate(this.velTorre);
                    break;
            }
            this.dinero -= precioMejora;
            this.ui.setTextUI(TEXT_DINERO_ID, String.valueOf(this.dinero));
            this.resetearEstado(); //Se vuelve al estado normal
        }
    }
}