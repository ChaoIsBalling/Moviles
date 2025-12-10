package com.example.gamelogic;

import com.example.engine.Graphics;
import com.example.engine.Mobile;
import com.example.engine.State;
import com.example.engine.TouchEvent;
import com.example.engine.Engine;
import com.example.engine.Audio;

import org.json.JSONObject;
import org.json.JSONArray;
import java.util.ArrayList;

/**
 * Clase que representa la interfaz principal de juego, donde se desarrolla toda su lógica de gameplay
 */
public class GameLogic implements State {
    //Botones
    private Button botonMejoraTriangulos;
    private Button botonMejoraCuadrados;
    private Button botonMejoraHexagonos;

    private Button botonMejoraMini;
    boolean mini = false;

    private Button botonMejoraAtaque;
    private Button botonMejoraRango;
    private Button botonMejoraVelocidad;

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

    int oleada;//numero oleada
    int oleadasRestantes;
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

    JSONArray camino; //Array con el numero de puntos que debe recorrer el enemigo en JSON
    ArrayList<Vector2D> caminoEnemigos;

    float offsetX = 30;
    float offsetY = 50;

      //Enumaerado que determina en que estado de juego estamos
    private enum Estado {
        normal, botonRayo, botonFuego, botonHielo, torre, botonMini
    }

    //Estado actual de juego
    private Estado estado = Estado.normal;

    private Mobile mobile;

    public enum Dificultad {
        corto, largo, infinito, aventura
    }

    private Dificultad dificultad;
    //JSONArray que gestiona las oleadas en el juego
    JSONArray oleadasDatos;
    //JsonObject que representa los datos de partida guardada
    JSONObject save;

    //String que determina el color del nivel
    String colorNivel;
       /**
     * Constructora del estado principal de juego en el modo normal
     * @param engine Motor
     */
    public GameLogic(Engine engine, Mobile mobile, Dificultad dificultad){
        this.engine=engine;
        this.init();
        this.dificultad = dificultad;
        this.inicializarNivel("mapa1.json");
        this.mobile = mobile;
        this.mobile.setVisibleAdBanner(false);
    }
    /**
     * Constructora del estado principal de juego en el modo aventura a partir de la lectura del mapa del nivel
     * @param engine Motor
     */
    public GameLogic(Engine engine, Mobile mobile, String mapa){
        this.engine=engine;
        this.dificultad = Dificultad.aventura;
        this.oleadasRestantes=0;
        this.init();
        this.inicializarNivel(mapa);
        this.mobile = mobile;
        this.mobile.setVisibleAdBanner(false);
    }
    private void init()
    {
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
        this.save=this.engine.readJsonFile2("save");
        JSONObject obj=engine.readJsonFile(mapa);
        colorNivel=obj.getString("colorNivel");
        JSONArray arr= obj.getJSONArray("mapa");
        this.levelNumber=obj.getInt("level");
        this.oleadasDatos =obj.getJSONArray("waves");
        this.camino = obj.getJSONArray("road"); //camino de puntos de los enemigos

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
        this.enemigosGenerar = this.oleadasDatos.getJSONObject(this.oleadaGenerar).getInt("amount");
        this.tiempoOleada = 5*this.enemigosGenerar;
        this.tiempOl = this.tiempoOleada;
        this.tiempoEnGenerar = (float) 0.3;
        this.tiempEnG =0;
        this.fil=arr.length();
        this.col=arr.get(0).toString().length();
        this.recompensas=obj.getInt("reward");


        int numPuntos = this.camino.length(); //tamaño del array del json
        this.caminoEnemigos = new ArrayList<>(numPuntos);
        //Vamos metiendo cada una de las coordenadas del
        //vector road del JSON al camino de enemigos en forma de coordenadas
        for(int i = 0; i<numPuntos;i++){
            JSONArray pair = this.camino.getJSONArray(i);
            int x = pair.getInt(0);
            int y = pair.getInt(1);
            this.caminoEnemigos.add(new Vector2D(x,y));
        }

        for (int i =0; i<this.fil;i++){
            ArrayList<Casilla> fila = new ArrayList<Casilla>();
            for(int j =0; j<this.col;j++){
                Casilla casilla;
                if(arr.get(i).toString().charAt(j) == 'h'){
                    //el fill lo pongo a true para que haya mayor contraste entre la casilla y las torres
                     casilla = new Casilla((float)(j*this.anchoCasilla+this.offsetX),(float)(i*this.altoCasilla+this.offsetY),this.anchoCasilla,this.altoCasilla,true,false);
                     casilla.setColor(this.colorNivel);
                } else {
                    casilla = new Casilla((float) (j * this.anchoCasilla + this.offsetX), (float) (i * this.altoCasilla + this.offsetY), this.anchoCasilla, this.altoCasilla, true, true);
                    casilla.setColor("#ff944d03");
                    if (j == 0) {
                        this.IniX = j * this.anchoCasilla + this.offsetX;
                        this.IniY = i * this.altoCasilla + this.offsetY;
                    }
                    if (j == this.col - 1) {
                        this.FinX = j * this.anchoCasilla + this.offsetX;
                        this.FinY = i * this.altoCasilla + this.offsetY;
                    }
                }
                casilla.setCoor(new Vector2D(i, j));
                fila.add(casilla);
            }
            this.casillas.add(fila);
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
            GameOver gameOver = new GameOver(this.engine, this.audio, this.mobile,this.dificultad,true);
            save.put("gems",save.getInt("gems") + this.recompensas);
            //comprobamos si el nivel que hemos derrotado es un nivel nuevo
            if(this.levelNumber>save.getInt("completed"))
            {
                save.put("completed",this.levelNumber);
            }
            this.engine.writeFile("hash",this.engine.createHash(save.toString()));
            this.engine.writeFile("save",save.toString());
            this.engine.setState(gameOver);
        }

        //En caso de que haya perdido
        if (this.vida <= 0) {
            this.stopSoundTorres();
            GameOver gameOver = new GameOver(this.engine, this.audio, this.mobile,this.dificultad,false);
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
                this.enemigosGenerar = this.oleadasDatos.getJSONObject(this.oleadaGenerar).getInt("amount") + this.oleada/this.oleadasT;
            }

            //Resetear numero de enemigos generados
            this.numE = 0;

            this.tiempoOleada = 5*this.enemigosGenerar;//mas tiempo entre oleadas
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
            String enemy =this.oleadasDatos.getJSONObject(this.oleadaGenerar).getString("enemy");
            Image im;
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
            //Generamos enemigo
            this.enemigos.add(new Enemy(this.IniX, this.IniY,
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
        int offsetX = 10;
        int col  = colCoor;
        float x = offsetX + col * this.anchoCasilla + this.anchoCasilla / 2f;
        return x;
    }

    public float getRealY(int filCoor){
        int offsetY = 35;
        int fila = filCoor;
        float y = offsetY + fila * this.altoCasilla + this.altoCasilla / 2f;
        return y;
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
        this.botonMejoraCuadrados = new Button(style.getJSONObject("BotonMejoraCuadrados"));
        this.botonMejoraTriangulos = new Button(style.getJSONObject("BotonMejoraTriangulos"));
        this.botonMejoraHexagonos = new Button(style.getJSONObject("BotonMejoraHexagonos"));
        this.botonMejoraMini = new Button(style.getJSONObject("BotonMejoraMini"));

        //Los botones de cuadrado triangulos y hexagonos usan los mismos valores de apariencia
        //que los botones de ataque rango y velocidad
        this.botonMejoraAtaque = new Button(style.getJSONObject("BotonMejoraTriangulos"));
        this.botonMejoraRango = new Button(style.getJSONObject("BotonMejoraCuadrados"));
        this.botonMejoraVelocidad = new Button(style.getJSONObject("BotonMejoraHexagonos"));


        this.botonMejoraCuadrados.setImagen(new Image(style.getJSONObject("ImagenHielo"),this.gr));
        this.botonMejoraTriangulos.setImagen(new Image(style.getJSONObject("ImagenRayo"),this.gr));
        this.botonMejoraHexagonos.setImagen(new Image(style.getJSONObject("ImagenFuego"),this.gr));
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
    public void setAudio(Audio audio) {
        this.audio = audio;

        for (int i = 0; i < this.torres.size(); i++) {
            this.torres.get(i).setAudio(this.audio);
        }
    }

    @Override
    public void setMobile(Mobile mobile) {
            this.mobile = mobile;
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
            switch (this.estado) {
                //en cada caso hacemos una comprobación de cosmeticos para poder darle el cosmetico correspondiente
                //a cada torre
                    case botonRayo:
                        if(save.getBoolean("rayo")) {
                            torre = "TorreRayoCosmetico";
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
                        if(save.getBoolean("fuego")) {
                            torre = "TorreFuegoCosmetico";
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
                        if(save.getBoolean("hielo")) {
                            torre = "TorreHieloCosmetico";
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
                this.botonMejoraMini.setColor("#FFFFFFFF");
                this.estado = nuevoEstado;
                break;
            case torre:
                this.estado = nuevoEstado;
                break;
            case botonRayo:
                this.botonMejoraTriangulos.setColor("#FFFFFB64");
                this.botonMejoraHexagonos.setColor("#FFFFFFFF");
                this.botonMejoraCuadrados.setColor("#FFFFFFFF");
                this.botonMejoraMini.setColor("#FFFFFFFF");
                this.estado = nuevoEstado;
                break;
            case botonFuego:
                this.botonMejoraTriangulos.setColor("#FFFFFFFF");
                this.botonMejoraHexagonos.setColor("#FFFFFB64");
                this.botonMejoraCuadrados.setColor("#FFFFFFFF");
                this.botonMejoraMini.setColor("#FFFFFFFF");
                this.estado = nuevoEstado;
                break;
            case botonHielo:
                this.botonMejoraTriangulos.setColor("#FFFFFFFF");
                this.botonMejoraHexagonos.setColor("#FFFFFFFF");
                this.botonMejoraCuadrados.setColor("#FFFFFB64");
                this.botonMejoraMini.setColor("#FFFFFFFF");
                this.estado = nuevoEstado;
                break;
            case botonMini:
                this.botonMejoraTriangulos.setColor("#FFFFFFFF");
                this.botonMejoraHexagonos.setColor("#FFFFFFFF");
                this.botonMejoraCuadrados.setColor("#FFFFFFFF");
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


