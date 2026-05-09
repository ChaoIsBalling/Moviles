package com.example.gamelogic.states;

import com.example.androidengine.AndroidEngine;
import com.example.androidengine.AndroidGraphics;
import com.example.androidengine.State;
import com.example.androidengine.TouchEvent;
import java.util.*;
import com.example.androidengine.AndroidAudio;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.example.androidengine.AndroidMobile;
import com.example.gamelogic.button.Button;
import com.example.gamelogic.button.ButtonCambio;
import com.example.gamelogic.button.ButtonComprar;
import com.example.gamelogic.figure.Circle;
import com.example.gamelogic.figure.Hexagon;
import com.example.gamelogic.Image;
import com.example.gamelogic.figure.Square;
import com.example.gamelogic.Text;
import com.example.gamelogic.figure.Triangle;

import java.util.ArrayList;

public class Tienda implements State {

    private Button botonVolver;

    private AndroidEngine engine;

    private AndroidGraphics graphics;

    private Text textoDiamantes;
    private int diamantes;

    private Button botonRayo;
    private Button botonFuego;
    private Button botonHielo;

    private Button botonMini;

    private Button rayoF;
    private Button fuegoF;
    private Button hieloF;

    private Button rayoS;
    private Button fuegoS;
    private Button hieloS;

    private Text coste;
    private Button botonComprar;

    private Square fondoDes;

    private boolean rayo;
    private boolean fuego;
    private boolean hielo;

    private boolean mini;

    private String skinRayo;
    private String skinFuego;
    private String skinHielo;

    private Text CTorres;
    private Text CSkins;

    private Button botonRojo;
    private Button botonAzul;
    private Button botonBlancoF;
    private Button botonRojoF;
    private Button botonAzulF;

    private boolean rojo;
    private boolean azul;

    private Text CFondo;

    private String fondo;

    //El archivo de guardado del juego
    private JSONObject save;
    //ArrayList de elementos de la tienda que pueden hacer scroll
    private ArrayList<ButtonComprar> ScrollableButtons;
    private ArrayList<Text>ScrollableText;

    private Image imagenDiamante;

    private enum Estado{
        normal, botonRayo, botonFuego, botonHielo, botonMini, botonRojo, botonAzul
    }
    //Ultima cordenada Y tocada
    float lastTouchedY;

    //bool que nos dice si estamos haciendo scroll de pantalla
    boolean scroll;

    //La posición minima que puede tener el promer boton de la lista en la posicion y
    float minY;
    //La posición minima que puede tener el promer boton de la lista en la posicion y
    float maxY;
    JSONObject datos;
    private Estado estado;
    //Comparadores que ordenan los elementos de la lista de mas altos a mas bajos
    //en la representacion
    class ButtonComparator implements Comparator<Button> {
        // Function to compare
        public int compare(Button c1, Button c2)
        {
            if (c1.getY() == c2.getY())
                return 0;
            else if (c1.getY() > c2.getY())
                return 1;
            else
                return -1;
        }
    }
    class TextComparator implements Comparator<Text> {
        // Function to compare
        public int compare(Text c1, Text c2)
        {
            if (c1.getY() == c2.getY())
                return 0;
            else if (c1.getY() > c2.getY())
                return 1;
            else
                return -1;
        }
    }

    AndroidMobile mobile;

    JSONObject datos2;

    int x;
    int initx;
    int y;
    int inity;

    ButtonComprar comprando;

    public Tienda(AndroidEngine engine,AndroidMobile mobile,JSONObject save){
        this.save =save;
        ScrollableText=new ArrayList<Text>();
        ScrollableButtons=new ArrayList<ButtonComprar>();
        this.engine=engine;
        this.mobile = mobile;
        this.datos =engine.readJsonFile("Tienda/style.json");
        this.datos2 =engine.readJsonFile("Tienda/style2.json");
        try{
        this.botonVolver = new Button(this.datos2.getJSONObject("BotonVolver"));

        this.textoDiamantes = new Text(this.datos2.getJSONObject("TextoDiamantes"));
        this.textoDiamantes.setText("" + this.save.getInt("gems"));

        this.fondoDes = new Square(500,300,300,400,true);
        this.fondoDes.setColor("#ff00ffff");
        this.coste = new Text(this.datos2.getJSONObject("TextoCoste"));
        this.botonComprar = new Button(this.datos2.getJSONObject("BotonComprar"));
        this.botonComprar.setText(new Text(this.datos2.getJSONObject("TextoComprar")));

        this.comprando = null;
    } catch (
    JSONException e) {
        throw new RuntimeException(e);
    }
    }


    @Override
    public void update(double deltatime) {
    }

    @Override
    public void render(AndroidGraphics gr) {

        gr.EmpezarLimiteDibujado(0,0,600,400);
        gr.EmpezarLimiteDibujado(0,80,600,400);

        for (int i =0; i<this.ScrollableButtons.size();i++){
            this.ScrollableButtons.get(i).Render(gr);
        }
        for (int i =0; i<this.ScrollableText.size();i++){
            this.ScrollableText.get(i).Render(gr);
        }
        gr.TerminarLimiteDibujado();
        this.botonVolver.Render(gr);
        this.textoDiamantes.Render(gr);
        this.imagenDiamante.Render();
        if(this.comprando!=null){
            this.fondoDes.Render(gr);

            try {
                if(!this.save.getBoolean(this.comprando.getDesbloqueo())){
                    this.coste.Render(gr);
                    this.botonComprar.Render(gr);
                }
                else{
                    this.comprando.RenderCambio(gr);
                }
            }catch (JSONException e) {
                throw new RuntimeException(e);
            }

        }
        gr.TerminarLimiteDibujado();
    }

    @Override
    public void setGraphics(AndroidGraphics gr) {
        try{
            this.graphics=gr;
            this.botonVolver.setImagen(new Image(datos2.getJSONObject("ImagenVolver"),gr));
            this.imagenDiamante = new Image(datos.getJSONObject("ImagenDiamante"),gr);
            this.CTorres = new Text(this.datos.getJSONObject("TextoCTorres"));
            ScrollableText.add(this.CTorres);

            JSONArray botonesDT = datos2.getJSONArray("BotonesDesbloqueoTorre");
            this.x = datos2.getJSONObject("EstandarBoton").getInt("x");
            this.initx = datos2.getJSONObject("EstandarBoton").getInt("x");
            this.y = datos2.getJSONObject("EstandarBoton").getInt("y");
            generarBotonesComprar(botonesDT,gr);

            this.CSkins = new Text(this.datos.getJSONObject("TextoCSkins"));
            y +=80;
            this.CSkins.setY(y);
            ScrollableText.add(this.CSkins);
            y+=100;

            x = initx;
            JSONArray botonesDS = datos2.getJSONArray("BotonesDesbloqueoSkin");
            generarBotonesComprar(botonesDS,gr);

            this.CFondo = new Text(this.datos.getJSONObject("TextoColores"));
            y+=80;
            this.CFondo.setY(y);
            ScrollableText.add(CFondo);
            y+=100;

            x = initx;
            JSONArray botonesDF = datos2.getJSONArray("BotonesDesbloqueoFondo");
            generarBotonesComprar(botonesDF,gr);

            this.minY=ScrollableText.get(0).getY();
            this.maxY=400-ScrollableButtons.get(ScrollableButtons.size()-1).getHeight();

            x = datos2.getJSONObject("InitBotonCambio").getInt("x");
            initx = datos2.getJSONObject("InitBotonCambio").getInt("x");
            y = datos2.getJSONObject("InitBotonCambio").getInt("y");
            inity = datos2.getJSONObject("InitBotonCambio").getInt("y");
            JSONArray botonesC = datos2.getJSONArray("BotonesCambio");
            generarBotonesCambio(botonesC,gr);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void generarBotonesComprar(JSONArray array, AndroidGraphics gr){
        try{
            for(int i =0; i<array.length();i++){
                JSONObject db = this.datos2.getJSONObject("EstandarBoton");
                db.put("coste",array.getJSONObject(i).getInt("coste"));
                db.put("descripcion",array.getJSONObject(i).getString("descripcion"));
                db.put("desbloqueo",array.getJSONObject(i).getString("desbloqueo"));
                ButtonComprar boton = new ButtonComprar(db);
                boton.setX(x);
                boton.setY(y);
                if(this.save.getBoolean(boton.getDesbloqueo())){
                    boton.setColor("#ff00ff00");
                }
                if(array.getJSONObject(i).getBoolean("tieneImagen")){
                    JSONObject image = datos2.getJSONObject("EstandarImagen");
                    image.put("imagen",array.getJSONObject(i).getString("imagen"));
                    boton.setImagen(new Image(image,gr));
                }
                if(array.getJSONObject(i).getBoolean("tieneForma")){
                    if(array.getJSONObject(i).getString("tipoForma").equals("cuadrado")){
                        Square br = new Square(0,0,datos2.getJSONObject("EstandarCuadrado").getInt("w"),datos2.getJSONObject("EstandarCuadrado").getInt("h"),datos2.getJSONObject("EstandarCuadrado").getBoolean("isFill"));
                        br.setColor(array.getJSONObject(i).getString("colorForma"));
                        boton.setFigura(br);
                    }
                    else if(array.getJSONObject(i).getString("tipoForma").equals("triangulo")){
                        Triangle tr = new Triangle(0,0,datos2.getJSONObject("EstandarTriangulo").getInt("r"),datos2.getJSONObject("EstandarTriangulo").getBoolean("isFill"));
                        tr.setColor(array.getJSONObject(i).getString("colorForma"));
                        boton.setFigura(tr);
                    }
                    else if(array.getJSONObject(i).getString("tipoForma").equals("hexagono")){
                        Hexagon hx = new Hexagon(0,0,datos2.getJSONObject("EstandarHexagono").getInt("r"),datos2.getJSONObject("EstandarHexagono").getBoolean("isFill"));
                        hx.setColor(array.getJSONObject(i).getString("colorForma"));
                        boton.setFigura(hx);
                    }
                    else if(array.getJSONObject(i).getString("tipoForma").equals("circulo")){
                        Circle cr = new Circle(0,0,datos2.getJSONObject("EstandarCirculo").getInt("r"),datos2.getJSONObject("EstandarCirculo").getBoolean("isFill"));
                        cr.setColor(array.getJSONObject(i).getString("colorForma"));
                        boton.setFigura(cr);
                    }
                }
                if(x == initx){
                    x+=150;
                }
                else{
                    x = initx;
                    y+=150;
                }
                ScrollableButtons.add(boton);
            }

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void generarBotonesCambio(JSONArray array, AndroidGraphics gr){
        ButtonCambio ultimo = null;
        try{
            for(int i =0; i<array.length();i++){
                JSONObject db = this.datos2.getJSONObject("EstandarBoton");
                db.put("desbloqueo",array.getJSONObject(i).getString("desbloqueo"));
                db.put("guardado",array.getJSONObject(i).getString("guardado"));
                db.put("datoGuardado",array.getJSONObject(i).getString("datoGuardado"));
                ButtonCambio boton = new ButtonCambio(db);
                if(ultimo != null && !Objects.equals(ultimo.getDesbloqueo(), boton.getDesbloqueo())){
                    x = initx;
                    y = inity;
                }
                ultimo = boton;
                boton.setX(x);
                boton.setY(y);
                if(this.save.get(boton.getGuardado()) == boton.getDatoGuardado()){
                    boton.setColor("#ff00ff00");
                }
                if(array.getJSONObject(i).getBoolean("tieneImagen")){
                    JSONObject image = datos2.getJSONObject("EstandarImagen");
                    image.put("imagen",array.getJSONObject(i).getString("imagen"));
                    boton.setImagen(new Image(image,gr));
                }
                if(array.getJSONObject(i).getBoolean("tieneForma")){
                    if(array.getJSONObject(i).getString("tipoForma").equals("cuadrado")){
                        Square br = new Square(0,0,datos2.getJSONObject("EstandarCuadrado").getInt("w"),datos2.getJSONObject("EstandarCuadrado").getInt("h"),datos2.getJSONObject("EstandarCuadrado").getBoolean("isFill"));
                        br.setColor(array.getJSONObject(i).getString("colorForma"));
                        boton.setFigura(br);
                    }
                    else if(array.getJSONObject(i).getString("tipoForma").equals("triangulo")){
                        Triangle tr = new Triangle(0,0,datos2.getJSONObject("EstandarTriangulo").getInt("r"),datos2.getJSONObject("EstandarTriangulo").getBoolean("isFill"));
                        tr.setColor(array.getJSONObject(i).getString("colorForma"));
                        boton.setFigura(tr);
                    }
                    else if(array.getJSONObject(i).getString("tipoForma").equals("hexagono")){
                        Hexagon hx = new Hexagon(0,0,datos2.getJSONObject("EstandarHexagono").getInt("r"),datos2.getJSONObject("EstandarHexagono").getBoolean("isFill"));
                        hx.setColor(array.getJSONObject(i).getString("colorForma"));
                        boton.setFigura(hx);
                    }
                    else if(array.getJSONObject(i).getString("tipoForma").equals("circulo")){
                        Circle cr = new Circle(0,0,datos2.getJSONObject("EstandarCirculo").getInt("r"),datos2.getJSONObject("EstandarCirculo").getBoolean("isFill"));
                        cr.setColor(array.getJSONObject(i).getString("colorForma"));
                        boton.setFigura(cr);
                    }
                }
                if(x == initx){
                    x+=110;
                }
                else{
                    x = initx;
                    y+=110;
                }
                boolean encontrado = false;
                int j =0;
                while (j<this.ScrollableButtons.size() && !encontrado){
                    if(Objects.equals(this.ScrollableButtons.get(j).getDesbloqueo(), boton.getDesbloqueo())){
                        this.ScrollableButtons.get(j).addBotonCambio(boton);
                        encontrado=true;
                    }
                    j++;
                }
            }

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void handleInput(ArrayList<TouchEvent> list, double elapseTime) {
        for(TouchEvent e: list){
            //Si es nulo no se procesa
            if (e == null || e.type == null) {
                continue;
            }
            switch (e.type){
                case TOUCH_DOWN:
                    onTouchDown(e);
                    this.gestionBotones(e);
                    break;
                case TOUCH_UP:
                    onTouchUp();
                    break;
                case TOUCH_MOVE:
                    onTouchMove(e);
                    break;
            }
        }
    }

    //si el evento es de tipo TouchDown guardamos el ultimo valor de la Y y ponemo a true el scroll
    private void onTouchDown(TouchEvent e){
        lastTouchedY=e.y;
        scroll=true;
    }
    //si es de tipo touch up el scroll se pone a false
    private void onTouchUp(){
        scroll = false;
    }
    //si es de tipo touchmove manejamos el scroll del juego
    private void onTouchMove(TouchEvent e)
    {
        if(!scroll)
            return;

        float destY=e.y-lastTouchedY;
        lastTouchedY=e.y;
        boolean canScroll=true;
        //checkeamos si los extremeos de los objetos scrolleables (el mas alto y el mas bajo)
        //estan entre el minimo y maximo Y que hemos definido
        if((ScrollableText.get(0).getY()>minY&&destY>0)||(ScrollableButtons.get(ScrollableButtons.size()-1).getY()<maxY&&destY<0))
            canScroll=false;

        if(canScroll) {
            for (int i = 0; i < ScrollableText.size(); i++) {
                float newY = ScrollableText.get(i).getY() + destY;
                ScrollableText.get(i).setY(newY);
            }
            for (int i = 0; i < ScrollableButtons.size(); i++) {
                float newY = ScrollableButtons.get(i).getY() + destY;
                ScrollableButtons.get(i).setY(newY);
            }
        }
    }

    /**
     * Metodo que gestiona los estados del juego
     */
    private void gestionBotones(TouchEvent e) //maneja los estados del juego cuando pulsas botones o las torres
    {
        if(this.botonVolver.contains(e.x,e.y)){
            Menu menu = new Menu(this.engine,this.mobile,this.save);
            this.engine.setState(menu);
        }
        else {
            try {
                if(this.comprando != null && !this.save.getBoolean(this.comprando.getDesbloqueo()) && this.botonComprar.contains(e.x, e.y) && this.comprando.getCoste() <= this.save.getInt("gems")){
                    this.save.put("gems",this.save.getInt("gems")-this.comprando.getCoste());
                    this.textoDiamantes.setText("" + this.save.getInt("gems"));
                    this.save.put(this.comprando.getDesbloqueo(),true);
                    this.comprando.setColor("#ff00ff00");
                }
            }catch (JSONException ex) {
                throw new RuntimeException(ex);
            }

            if(this.comprando != null){
                try {
                    this.comprando.pulsarCambio(e.x,e.y,this.save);
                    this.graphics.setColorClear(this.save.getString("fondo"));
                }catch (JSONException ex) {
                    throw new RuntimeException(ex);
                }

            }

            boolean bct = false;
            int i =0;
            while (i<this.ScrollableButtons.size() && !bct){
                if(this.ScrollableButtons.get(i).contains(e.x,e.y)){
                    this.comprando = this.ScrollableButtons.get(i);
                    this.coste.setText("Coste: "+ this.comprando.getCoste()+this.comprando.getDescripcion());
                    bct = true;
                }
                i++;
            }
            if(!bct){
                this.comprando = null;
            }

        }

    }
    @Override
    public void setAudio(AndroidAudio audio) {

    }

    @Override
    public void setMobile(AndroidMobile mobile) {

    }

    @Override
    public JSONObject getSave() {
        return this.save;
    }
}
