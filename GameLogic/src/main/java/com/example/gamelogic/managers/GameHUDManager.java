package com.example.gamelogic.managers;

//import com.example.engine.Graphics;
//import com.example.engine.TouchEvent;
//import com.example.gamelogic.button.Button;
//import com.example.gamelogic.ButtonClickListener;
import com.example.androidengine.AndroidGraphics;
import com.example.gamelogic.Image;
import com.example.gamelogic.Text;
import com.example.gamelogic.figure.Figure;
import com.example.gamelogic.figure.Hexagon;
import com.example.gamelogic.figure.Square;
import com.example.gamelogic.figure.Triangle;
import com.example.gamelogic.states.GameLogic;
import com.example.gamelogic.towers.Tower;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GameHUDManager {
    // Botones de compra de torres
    //ArrayList<Button> botonesTorres = new ArrayList<>();
    // Botones de mejora de torre
    //ArrayList<Button> botonesMejoras = new ArrayList<>();
    // Figuras y Textos
    private Square franjaGris;
    private Text textoV, textoD, textoOleadas;
    private Image imagenVida, imagenDinero, imagenFondo;

    //Listas de textos e imagenes a renderizar
    ArrayList<Text> textos = new ArrayList<>();
    ArrayList<Image> imagenes = new ArrayList<>();

    // constantes para inicializar la posicion de los botones
    private final float START_BUTTONS_X = 440;
    private final float OFFSET_BUTTONS_X = 60;
    private final float START_BUTTONS_Y = 360;

    GameLogic gl;

    AndroidGraphics gr;

    //Json que contiene los estilos de cada texto, imagen y boton
    JSONObject style;

    //Json que contiene los datos del mapa (para acceder a la imagen de fondo)
    JSONObject mapObj;

    public GameHUDManager(AndroidGraphics gr, JSONObject style, JSONObject mapObj, GameLogic gl,
                          int vidas, float dinero, int oleadas,
                          int ancho, int alto, float offsetX, float offsetY) {
        this.gl = gl;
        this.gr = gr;
        this.style = style;
        this.mapObj = mapObj;

        // Fondo de la UI
        this.franjaGris = new Square(300, 370, 600, 100, true);
        this.franjaGris.setColor("#FF999999");

        //Inicializamos textos e imagenes

        initBackground(offsetX, offsetY, ancho, alto);
        initTextAndImages(this.gr, vidas, dinero, oleadas);

        // Inicialización de botones interactuables
        //initBuyTowerButtons();
        //initUpgradeButtons(gr);
    }


    private void initTextAndImages(AndroidGraphics gr,  int vida, float dinero, int oleada){
        // Textos y Stats
        float separacion = 30;
        this.textoV = new Text("Inika-Regular.ttf", String.valueOf(vida), 30, 340, 20);
        this.actualizaVidas(vida); this.textos.add(textoV);
        this.textoD = new Text("Inika-Regular.ttf", String.valueOf(dinero), 30, 340 + separacion, 20);
        this.actualizaDinero(dinero); this.textos.add(textoD);
        this.textoOleadas = new Text("Inika-Regular.ttf", "Oleada: " + String.valueOf(oleada), 60, 15, 25);
        this.actualizaNumOleadas(oleada); this.textos.add(textoOleadas);

        //Imagenes
        //this.imagenVida = new Image("Vida.png", 60, 330, 30, 30, gr); this.imagenes.add(imagenVida);
        //this.imagenDinero = new Image("Dinero.png", 60, 360, 30, 30, gr); this.imagenes.add(imagenDinero);
        try {
            this.imagenVida = new Image( style.getJSONObject("ImagenVida"), gr); this.imagenes.add(this.imagenVida);
            this.imagenDinero = new Image(style.getJSONObject("ImagenDinero"), gr); this.imagenes.add(this.imagenDinero);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void initBackground(float posX, float posY, int w, int h){
        //Inicializamos posicion y escalado del fondo
        try {
            this.imagenFondo=new Image(this.mapObj.getJSONObject("background"),this.gr); //Fondo del nivel
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        this.imagenFondo.setX((int)posX -17); this.imagenFondo.setY((int)posY -17);
        this.imagenFondo.setW(w); this.imagenFondo.setH(h);
    }

    public void renderizaFondo(){
        this.imagenFondo.RenderEscalado();
    }


    public void render(AndroidGraphics gr, GameLogic.Estado estado, Tower torreSeleccionada) {
        //renderizamos textos e imagenes

        this.franjaGris.Render(gr);
        for (Text t : this.textos) {
            t.Render(gr);
        }

        for (Image i: this.imagenes){
            i.Render();
        }
        // Elegimos qué lista de botones rendrizar según el estado del juego

        /*ArrayList<Button> listaActiva;
        if (estado == GameLogic.Estado.TORRE) { //Si hemos seleccionado una torre, vemos sus mejoras
            listaActiva = botonesMejoras;
        } else {
            listaActiva = botonesTorres;
        }
        //System.out.println("ESTADO EN HUD: " + estado.toString());
        //System.out.println(listaActiva);


        // Recorremos la lista de botones para renderizarlos
        for (Button b : listaActiva) {
            b.Render(gr);
        }

        //Dibujamos un circulo con el rango de la torre que tenga seleccionada
        if(estado == GameLogic.Estado.TORRE) {
            gr.pintarCirculo(torreSeleccionada.getX(), torreSeleccionada.getY(), torreSeleccionada.getRange());
        }*/
    }
    /**
     * Crea los botones de compra de torres usando un metodo de ayuda
     */
    /*private void initBuyTowerButtons() {
        this.createTowerButton(START_BUTTONS_X, START_BUTTONS_Y,"100",
                new Triangle(1, 1, 15, true),() -> gl.prepararRayo());
        this.createTowerButton(START_BUTTONS_X, START_BUTTONS_Y,"150",
                new Square(1, -5, 30, 30, true, 0xFFC8A2C8), () -> gl.prepararHielo());
        this.createTowerButton(START_BUTTONS_X, START_BUTTONS_Y,"200",
                new Hexagon(1, -5, 15, true, 0xFFFF0000), () -> gl.prepararFuego());
    }*/

    /**
     * Crea los botones de mejora de torre usando un metodo de ayuda
     */
    /*private void initUpgradeButtons(AndroidGraphics gr) {
        this.createUpgradeButton(START_BUTTONS_X,START_BUTTONS_Y,"75", "Espada.png", () -> gl.mejorarAtaque(),gr );
        this.createUpgradeButton(START_BUTTONS_X,START_BUTTONS_Y,"75", "Arco.png", () -> gl.mejorarRango(),gr );
        this.createUpgradeButton(START_BUTTONS_X,START_BUTTONS_Y,"100", "Reloj.png", () -> gl.mejorarVelocidad(),gr );
    }*/
    /**
     * Crea una base común de cualquier botón de la interfaz
     */
    /*private Button createTowerButton(float x, float y, String coste, Figure fig, ButtonClickListener action) {
        // Llamamos al genérico pasando la lista de torres
        Button b = createButtonForList(botonesTorres, y, coste, action);
        b.setFigura(fig); // Lo único especial de este método
        return b;
    }*/

    /**
     * Crea específicamente un botón de mejora con imagen
     */
    /*private Button createUpgradeButton(float x, float y, String coste, String imgPath, ButtonClickListener action, Graphics gr) {
        // Llamamos al genérico pasando la lista de mejoras
        Button b = createButtonForList(botonesMejoras, y, coste, action);
        b.setImagen(new Image(imgPath, -15, -20, 30, 30, gr)); // Lo único especial de este método
        return b;
    }*/

    // Metodo para detectar el input de los botones
    /*public boolean handleInput(TouchEvent event, GameLogic.Estado estado) {
        // Elegimos qué lista de botones mirar según el estado
        ArrayList<Button> listaActiva = estado == GameLogic.Estado.TORRE ? botonesMejoras: botonesTorres;

        // Recorremos la lista
        for (Button b : listaActiva) {
            if (b.handleInput(event)) return true;
        }
        return false;
    }*/

    /**
     * Metodo interno genérico que centraliza la creación de cualquier botón del HUD
     //* @param list lista a la que vamos a añadir los botones
     //* @param y posicion y de los botones
     //* @param coste texto con el coste
     //* @param action Accion del boton
     * @return
     */
    /*private Button createButtonForList(ArrayList<Button> list, float y, String coste, ButtonClickListener action) {
        //Calculamos posición automática según el tamaño de la lista que nos pasen
        float posX = START_BUTTONS_X + (list.size() * OFFSET_BUTTONS_X);

        // Creamos el boton
        Button b = new Button(posX, y, 50, 50, true, 20, action);
        b.setColor(0xFFFFFFFF);
        b.setText(new Text("Inika-Regular.ttf", coste, 0, 15, 15, true, true));

        //Lo añadimos a la lista correspondiente
        list.add(b);

        return b;
    }*/

    //Setters
    public void actualizaDinero(float d){ this.textoD.setText(String.valueOf(d)); }
    public void actualizaVidas(int v){ this.textoV.setText(String.valueOf(v)); }
    public void actualizaNumOleadas(int o){ this.textoOleadas.setText("Oleada:" + String.valueOf(o)); }

    /**
     * Metodo que pinta de color amarillo el boton que se ha pulsado
     * @param estado estado del juego actual
     */
    /*public void actualizarColoresBotones(GameLogic.Estado estado, int indiceSel) {
        // Ponemos todos en blanco
        for (Button b : botonesTorres) b.setColor(0xFFFFFFFF);

        // Resaltamos el que coincida
        if(indiceSel >= 0 && estado == GameLogic.Estado.CONSTRUCCION){
            botonesTorres.get(indiceSel).setColor(0xfffffb64);
        }
    }*/
}
