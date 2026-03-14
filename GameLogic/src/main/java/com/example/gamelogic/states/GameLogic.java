package com.example.gamelogic.states;
import com.example.androidengine.AndroidEngine;
import com.example.androidengine.State;
import com.example.androidengine.TouchEvent;
import com.example.androidengine.AndroidGraphics;
import com.example.androidengine.AndroidAudio;
import com.example.androidengine.AndroidMobile;
import com.example.gamelogic.Button;
import com.example.gamelogic.Casilla;
import com.example.gamelogic.Enemy;
import com.example.gamelogic.towers.FireTower;
import com.example.gamelogic.figure.Hexagon;
import com.example.gamelogic.towers.IceTower;
import com.example.gamelogic.Image;
import com.example.gamelogic.towers.MiniThunderTower;
import com.example.gamelogic.figure.Square;
import com.example.gamelogic.Text;
import com.example.gamelogic.towers.ThunderTower;
import com.example.gamelogic.Tipo;
import com.example.gamelogic.towers.Tower;
import com.example.gamelogic.figure.Triangle;
import com.example.gamelogic.Vector2D;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;



/**
 * Clase que representa la interfaz principal de juego, donde se desarrolla toda su lógica de gameplay
 */
public class GameLogic implements State {
    //Botones
    private Button botonMejoraTriangulos;
    private Button botonMejoraCuadrados;
    private Button botonMejoraHexagonos;
    private Button botonMejoraMini;

    boolean mini = false;//si esta desbloqueada la nueva torre

    private Button botonMejoraAtaque;
    private Button botonMejoraRango;
    private Button botonMejoraVelocidad;

    //Imagen de los stats
    private Image imagenVida;
    private Image imagenDinero;

    private Image imagenFondo;

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

    //Json que maneja el estilo del nivel
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

    float mejVidaEn = 2;//mejora de vida de enemigo
    float mejVelEn = 2;//mejora de velocidad de enemigo
    float mejDefEn = 1;//mejora de defensa de enemigo
    float mejResEn = 1;//mejora de resistencia de enemigo

    int oleada;//numero oleada
    int oleadasRestantes;//oleadas restantes
    int enemigosGenerar;//enemigos a generar
    int oleadaGenerar;//oleada a generar del json
    int oleadasT;//cantidad de oleadas del json

    float tiempoOleada;//tiempo de espera entre oleadas

    float tiempOl;//tiempo que falta para la nueva oleada

    int numE = 0;//enemigos generados
    float tiempoEnGenerar;//tiempo de espera entre enemigos
    float tiempEnG;//tiempo que falta para generar un nuevo enemigo
    int recompensas;//cantidad de gemas que consigues al terminar un nivel
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

      //Enumaerado que determina en que estado de juego estamos
    private enum Estado {
        normal, botonRayo, botonFuego, botonHielo, torre, botonMini
    }

    //Estado actual de juego
    private Estado estado = Estado.normal;

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

       /**
     * Constructora del estado principal de juego en el modo normal
     * @param engine Motor
     */
    public GameLogic(AndroidEngine engine, AndroidMobile mobile, Dificultad dificultad,JSONObject save){
        this.save = save;
        this.engine=engine;
        this.init();
        this.dificultad = dificultad;

        //Elegimos un nivel entre un subcojunto de niveles de la carpeta Mapas
        int l = this.engine.getDirectoryLenght("Mapas");
        rnd = new Random();
        int level = rnd.nextInt(l) + 1;

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
        this.init();
        this.inicializarNivel(mapa);
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
        this.textoOleadas = new Text("Inika-Regular.ttf","Oleada:" + this.oleada,60,15,25);
        this.torres = new ArrayList<Tower>();
        this.enemigos=new ArrayList<Enemy>();
        this.deadEnemies=new ArrayList<Enemy>();
        this.casillas = new ArrayList<ArrayList<Casilla>>();
        this.franjaGris = new Square(300,370,600,100,true);
        this.franjaGris.setColor("#FF999999");
    }
 /**
     * Metodo que lee los datos del nivel desde un archivo json
     * @param mapa ruta del archivo
     */
    private void inicializarNivel(String mapa)
    {
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
        this.oleadaGenerar =0;
        this.oleadasT = this.oleadasDatos.length();
        try {
            this.enemigosGenerar = this.oleadasDatos.getJSONObject(this.oleadaGenerar).getInt("amount");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        this.tiempoOleada = 3*this.enemigosGenerar;
        this.tiempOl = this.tiempoOleada;
        this.tiempoEnGenerar = (float) 0.3;
        this.tiempEnG =0;

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

        for(int i = 0; i<numPuntos;i++){
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
                try {
                    if(arr.get(i).toString().charAt(j) == 'h'){
                        //el fill lo pongo a true para que haya mayor contraste entre la casilla y las torres
                         casilla = new Casilla((float)(j*this.anchoCasilla+this.offsetX),(float)(i*this.altoCasilla+this.offsetY),this.anchoCasilla,this.altoCasilla,false,false);
                         casilla.setColor("#ff000000");
                    } else {
                        casilla = new Casilla((float) (j * this.anchoCasilla + this.offsetX), (float) (i * this.altoCasilla + this.offsetY), this.anchoCasilla, this.altoCasilla, true, true);
                        casilla.setColor("#ff944d03");
                    }
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
            this.mini = this.save.getBoolean("mini");
            this.fondo = this.save.getString("fondo");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Actualiza los contadores de tiempo de acuerdo al deltatime
     */
    private void actualizarTiempos(double deltaTime){
        this.tiempEnG -= deltaTime;
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
                this.textoV.setText(String.valueOf(this.vida));
                //Añadimos al enemigo en la lista de ganadores para que sea eliminado (se comprueba en el tercer if)
                deadEnemies.add(this.enemigos.get(i));
            }
            if (this.enemigos.get(i).Dead()) { //En caso de morir nos da dinero y lo eliminamos
                deadEnemies.add(this.enemigos.get(i));
                this.dinero += 50;
                this.textoD.setText(String.valueOf(this.dinero));
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
     * Metodo que gestiona la aparición de nuevas oleadas
     */
    private void gestionarOleadas(){

        if(oleadasRestantes==0)
            return;

        if (this.tiempOl <= 0) {//si tiempo entre oleadas es menor o igual a 0
            //siguiente oleada
            this.oleada++;
            this.oleadasRestantes--;

            //para evitar que esto pete
            if(this.oleadasRestantes!=0) {
                this.oleadaGenerar = (this.oleada-1)%this.oleadasT;
                try {
                    this.enemigosGenerar = this.oleadasDatos.getJSONObject(this.oleadaGenerar).getInt("amount") + this.oleada/this.oleadasT;
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            //Resetear numero de enemigos generados
            this.numE = 0;

            this.tiempoOleada = 3*this.enemigosGenerar;//mas tiempo entre oleadas
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
        if(this.tiempEnG > 0)
            return;

        //si el numero de enemigos generados es menor al numero de enemigos en su grupo
        if (this.numE < this.enemigosGenerar) {
            Tipo tipo;
            //dependiendo del tipo de enemigo tiene un tipo distinto
            String enemy = null;
            Image im;
            try {
                enemy = this.oleadasDatos.getJSONObject(this.oleadaGenerar).getString("enemy");

                if(enemy.equals("goblin")) {
                    tipo = Tipo.rayo;
                    im=new Image(this.style.getJSONObject("ImagenGoblin"),this.gr);
                }
                else if(enemy.equals("imp")) {
                    tipo = Tipo.fuego;
                    im=new Image(this.style.getJSONObject("ImagenImp"),this.gr);
                }
                else {
                    tipo = Tipo.hielo;
                    im=new Image(this.style.getJSONObject("ImagenOgre"),this.gr);
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            //Generamos enemigo
            this.enemigos.add(new Enemy(
                    8 + (this.mejVidaEn * (this.oleada/this.oleadasT)),
                    30 + (this.mejVelEn * (this.oleada/this.oleadasT)),
                    0 + (this.mejDefEn * (this.oleada/this.oleadasT)),
                    0 + (this.mejResEn * (this.oleada/this.oleadasT)),
                    tipo,
                    this.caminoEnemigos,
                    this));
            this.enemigos.get(this.enemigos.size()-1).setImagen(im);

            //Incrementamos número de grupo
            this.numE++;
        }

        this.tiempEnG = this.tiempoEnGenerar;//resetear tiempo entre enemigos
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

        this.imagenFondo.RenderEscalado();
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
            if(this.mini){
                this.botonMejoraMini.Render(gr);
            }
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

        this.style =engine.readJsonFile("GameLogic/style.json");
        try {
            this.botonMejoraCuadrados = new Button(style.getJSONObject("BotonMejoraCuadrados"));

        this.botonMejoraTriangulos = new Button(style.getJSONObject("BotonMejoraTriangulos"));
        this.botonMejoraHexagonos = new Button(style.getJSONObject("BotonMejoraHexagonos"));
        this.botonMejoraMini = new Button(style.getJSONObject("BotonMejoraMini"));

        //Los botones de cuadrado triangulos y hexagonos usan los mismos valores de apariencia
        //que los botones de ataque rango y velocidad
        this.botonMejoraAtaque = new Button(style.getJSONObject("BotonMejoraTriangulos"));
        this.botonMejoraRango = new Button(style.getJSONObject("BotonMejoraCuadrados"));
        this.botonMejoraVelocidad = new Button(style.getJSONObject("BotonMejoraHexagonos"));
        //depende de la skin el boton es figura o skin seleccionada
        if(!Objects.equals(save.getString("skinRayo"), "Figura")){
            this.botonMejoraTriangulos.setImagen(new Image(style.getJSONObject(save.getString("skinRayo")),this.gr));
        }
        else{
            Triangle tri = new Triangle(0,0,15,true);
            tri.setColor("#FF000000");
            this.botonMejoraTriangulos.setFigura(tri);
        }
        if(!Objects.equals(save.getString("skinFuego"), "Figura")){
            this.botonMejoraHexagonos.setImagen(new Image(style.getJSONObject(save.getString("skinFuego")),this.gr));
        }
        else{
            Hexagon hex = new Hexagon(0,-5,15,true);
            hex.setColor("#FFFF0000");
            this.botonMejoraHexagonos.setFigura(hex);
        }
        if(!Objects.equals(save.getString("skinHielo"), "Figura")){
            this.botonMejoraCuadrados.setImagen(new Image(style.getJSONObject(save.getString("skinHielo")),this.gr));
        }
        else{
            Square sq = new Square(0,-5,30,30,true);
            sq.setColor("#FFC8A2C8");
            this.botonMejoraCuadrados.setFigura(sq);
        }
        this.botonMejoraMini.setImagen(new Image(style.getJSONObject("TorreMini"),this.gr));

        this.botonMejoraCuadrados.setText(new Text(style.getJSONObject("CosteMejoraCuadrados")));
        this.botonMejoraTriangulos.setText(new Text(style.getJSONObject("CosteMejoraTriangulos")));
        this.botonMejoraHexagonos.setText(new Text(style.getJSONObject("CosteMejoraHexagonos")));
        this.botonMejoraMini.setText(new Text(style.getJSONObject("CosteMejoraMini")));

        this.botonMejoraAtaque.setText(new Text(style.getJSONObject("CosteMejoraAtaques")));
        this.botonMejoraRango.setText(new Text(style.getJSONObject("CosteMejoraAtaques")));
        this.botonMejoraVelocidad.setText(new Text(style.getJSONObject("CosteMejoraTriangulos")));

        this.botonMejoraAtaque.setImagen(new Image(style.getJSONObject("ImagenAtaque"),this.gr));
        this.botonMejoraRango.setImagen(new Image(style.getJSONObject("ImagenRango"),this.gr));
        this.botonMejoraVelocidad.setImagen(new Image(style.getJSONObject("ImagenVelocidad"),this.gr));

        this.textoV = new Text("Inika-Regular.ttf", String.valueOf(this.vida), 30, 340, 20);
        this.textoD = new Text("Inika-Regular.ttf", String.valueOf(this.dinero), 30, 370, 20);
        this.imagenVida = new Image( style.getJSONObject("ImagenVida"), this.gr);
        this.imagenDinero = new Image(style.getJSONObject("ImagenDinero"), this.gr);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        this.botonMejoraTriangulos.setColor(this.fondo);
        this.botonMejoraCuadrados.setColor(this.fondo);
        this.botonMejoraHexagonos.setColor(this.fondo);
        this.botonMejoraMini.setColor(this.fondo);
        this.botonMejoraAtaque.setColor(this.fondo);
        this.botonMejoraRango.setColor(this.fondo);
        this.botonMejoraVelocidad.setColor(this.fondo);
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

        //Inicializamos posicion y escalado del fondo
        try {
            this.imagenFondo=new Image(obj.getJSONObject("background"),this.gr); //Fondo del nivel
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        this.imagenFondo.setX((int)this.offsetX -17); this.imagenFondo.setY((int)this.offsetY -17);
        this.imagenFondo.setW(this.ancho); this.imagenFondo.setH(this.alto);
    }

    /**
     * Metodo que gestiona los estados del juego
     */
    private void gestionEstadosJuego(TouchEvent e) //maneja los estados del juego cuando pulsas botones o las torres
    {
        switch (this.estado) {
            case normal://cuando ningun boton o torre está seleccionado
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
                } else if (this.botonMejoraVelocidad.contains(e.x, e.y) && this.dinero >= 100) {
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
            case botonMini://has tocado el boton para crear una torre de hielo
                pulsarBotones(e, 50);
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
        }else if (this.botonMejoraMini.contains(e.x, e.y) && this.mini) {
            this.cambiarEstado(Estado.botonMini);
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
                this.botonMejoraTriangulos.setColor(this.fondo);
                this.botonMejoraHexagonos.setColor(this.fondo);
                this.botonMejoraCuadrados.setColor(this.fondo);
                this.botonMejoraMini.setColor(this.fondo);
                this.estado = nuevoEstado;
                break;
            case torre:
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
}


