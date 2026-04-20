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
import com.example.gamelogic.Button;
import com.example.gamelogic.ButtonCambio;
import com.example.gamelogic.ButtonComprar;
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
        /*this.cargarDatos();

        this.botonMini = new Button(this.datos.getJSONObject("BotonMini"));
        this.botonRayo = new Button(this.datos.getJSONObject("BotonRayo"));
        this.botonFuego = new Button(this.datos.getJSONObject("BotonFuego"));
        this.botonHielo = new Button(this.datos.getJSONObject("BotonHielo"));

        ScrollableButtons.add(botonMini);
        ScrollableButtons.add(botonRayo);
        ScrollableButtons.add(botonFuego);
        ScrollableButtons.add(botonHielo);

        Collections.sort(ScrollableButtons,new ButtonComparator());

        if(this.rayo){
            this.botonRayo.setColor("#ff00ff00");
        }
        if(this.fuego){
            this.botonFuego.setColor("#ff00ff00");
        }
        if(this.hielo){
            this.botonHielo.setColor("#ff00ff00");
        }
        if(this.mini){
            this.botonMini.setColor("#ff00ff00");
        }

        this.fondoDes = new Square(500,300,300,400,true);
        this.fondoDes.setColor("#ff00ffff");
        this.coste = new Text(this.datos.getJSONObject("TextoCoste"));
        this.botonComprar = new Button(this.datos.getJSONObject("BotonComprar"));
        this.botonComprar.setText(new Text(this.datos.getJSONObject("TextoComprar")));
        this.estado = Estado.normal;
        this.CTorres = new Text(this.datos.getJSONObject("TextoCTorres"));
        this.CSkins = new Text(this.datos.getJSONObject("TextoCSkins"));
        ScrollableText.add(this.CTorres);
        ScrollableText.add(this.CSkins);

        Collections.sort(ScrollableText,new TextComparator());

        this.minY=ScrollableText.get(0).getY();
        this.maxY=400-ScrollableButtons.get(ScrollableButtons.size()-1).getHeight();

        this.rayoF = new Button(this.datos.getJSONObject("BotonRayoF"));
        this.fuegoF = new Button(this.datos.getJSONObject("BotonFuegoF"));
        this.hieloF = new Button(this.datos.getJSONObject("BotonHieloF"));
        this.rayoS = new Button(this.datos.getJSONObject("BotonRayoS"));
        this.fuegoS = new Button(this.datos.getJSONObject("BotonFuegoS"));
        this.hieloS = new Button(this.datos.getJSONObject("BotonHieloS"));
        Triangle tri = new Triangle(0,0,40,true);
        tri.setColor("#FF000000");
        this.rayoF.setFigura(tri);
        Hexagon hex = new Hexagon(0,0,40,true);
        hex.setColor("#FFFF0000");
        this.fuegoF.setFigura(hex);
        Square sq = new Square(0,0,80,80,true);
        sq.setColor("#FFC8A2C8");
        this.hieloF.setFigura(sq);
        if(Objects.equals(this.skinRayo, "Figura")){
            this.rayoF.setColor("#FF00FF00");
        } else if (Objects.equals(this.skinRayo, "TorreRayoCosmetico")) {
            this.rayoS.setColor("#FF00FF00");
        }
        if(Objects.equals(this.skinFuego, "Figura")){
            this.fuegoF.setColor("#FF00FF00");
        } else if (Objects.equals(this.skinFuego, "TorreFuegoCosmetico")) {
            this.fuegoS.setColor("#FF00FF00");
        }
        if(Objects.equals(this.skinHielo, "Figura")){
            this.hieloF.setColor("#FF00FF00");
        } else if (Objects.equals(this.skinHielo, "TorreHieloCosmetico")) {
            this.hieloS.setColor("#FF00FF00");
        }

        this.botonRojo = new Button(this.datos.getJSONObject("BotonRojo"));
        Square br = new Square(0,0,80,80,true);
        br.setColor("#FFFF0000");
        this.botonRojo.setFigura(br);
        this.botonAzul = new Button(this.datos.getJSONObject("BotonAzul"));
        Square ba = new Square(0,0,80,80,true);
        ba.setColor("#FF0000FF");
        this.botonAzul.setFigura(ba);
        this.botonBlancoF = new Button(this.datos.getJSONObject("BotonBlancoF"));
        this.botonRojoF = new Button(this.datos.getJSONObject("BotonRojoF"));
        this.botonAzulF = new Button(this.datos.getJSONObject("BotonAzulF"));
        ScrollableButtons.add(botonRojo);
        ScrollableButtons.add(botonAzul);
        this.CFondo = new Text(this.datos.getJSONObject("TextoColores"));
        ScrollableText.add(CFondo);
        if(this.rojo){
            this.botonRojo.setColor("#ff00ff00");
        }
        if(this.azul){
            this.botonAzul.setColor("#ff00ff00");
        }*/
    } catch (
    JSONException e) {
        throw new RuntimeException(e);
    }
    }

    //carga el progreso y comprueba que no ha sido modificado
    private void cargarDatos(){
        try{
            this.rayo = this.save.getBoolean("rayo");
            this.fuego = this.save.getBoolean("fuego");
            this.hielo = this.save.getBoolean("hielo");
            this.mini = this.save.getBoolean("mini");
            this.diamantes = this.save.getInt("gems");
            this.skinRayo=this.save.getString("skinRayo");
            this.skinFuego=this.save.getString("skinFuego");
            this.skinHielo=this.save.getString("skinHielo");
            this.rojo=this.save.getBoolean("rojo");
            this.azul=this.save.getBoolean("azul");
            this.fondo=this.save.getString("fondo");
            this.textoDiamantes = new Text(this.datos.getJSONObject("TextoDiamantes"));
        } catch (JSONException e) {
            throw new RuntimeException(e);
                }
            this.textoDiamantes.setText("" + this.diamantes);
            }



        //guardar progreso
    private void guardar(){
        try{
        this.save.put("gems",this.diamantes);
        this.save.put("rayo",this.rayo);
        this.save.put("fuego",this.fuego);
        this.save.put("hielo",this.hielo);
        this.save.put("mini",this.mini);
        this.save.put("skinRayo",this.skinRayo);
        this.save.put("skinFuego",this.skinFuego);
        this.save.put("skinHielo",this.skinHielo);
        this.save.put("rojo",this.rojo);
        this.save.put("azul",this.azul);
        this.save.put("fondo",this.fondo);
        } catch (JSONException e) {
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

        /*this.botonRayo.Render(gr);
        this.botonFuego.Render(gr);
        this.botonHielo.Render(gr);
        this.botonMini.Render(gr);
        this.CTorres.Render(gr);
        this.CSkins.Render(gr);*/
        gr.TerminarLimiteDibujado();
        this.botonVolver.Render(gr);
        this.textoDiamantes.Render(gr);
        this.imagenDiamante.Render();
        /*this.botonRojo.Render(gr);
        this.botonAzul.Render(gr);
        this.CFondo.Render(gr);*/
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



            /*if(this.estado ==Estado.botonRayo&&this.rayo){
                this.rayoF.Render(gr);
                this.rayoS.Render(gr);
            }
            else if (this.estado==Estado.botonFuego&&this.fuego){
                this.fuegoF.Render(gr);
                this.fuegoS.Render(gr);
            } else if (this.estado==Estado.botonHielo&&this.hielo) {
                this.hieloF.Render(gr);
                this.hieloS.Render(gr);
            } else if (this.estado==Estado.botonRojo&&this.rojo) {
                this.botonBlancoF.Render(gr);
                this.botonRojoF.Render(gr);
            } else if (this.estado==Estado.botonAzul&&this.azul) {
                this.botonBlancoF.Render(gr);
                this.botonAzulF.Render(gr);
            } else {
            this.coste.Render(gr);
            this.botonComprar.Render(gr);
            }*/
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

//                boolean bct = false;
//                int i =0;
//                while (i<this.botonesCambio.size() && !bct){
//                    if(Objects.equals(this.botonesCambio.get(i).getDesbloqueo(), this.comprando.getDesbloqueo()) && this.botonesCambio.get(i).contains(e.x,e.y)){
//                        try {
//                            this.save.put(this.botonesCambio.get(i).getGuardado(),this.botonesCambio.get(i).getDatoGuardado());
//                            this.graphics.setColorClear(this.save.getString("fondo"));
//                        }catch (JSONException ex) {
//                            throw new RuntimeException(ex);
//                        }
//
//                        bct = true;
//                    }
//                    i++;
//                }
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


            /*switch (this.estado) {
                case normal://cuando ningun elemento esta seleccionado
                    if (this.botonRayo.contains(e.x, e.y)) {
                        this.cambiarEstado(Estado.botonRayo);
                    } else if (this.botonFuego.contains(e.x, e.y)) {
                        this.cambiarEstado(Estado.botonFuego);
                    } else if (this.botonHielo.contains(e.x, e.y)) {
                        this.cambiarEstado(Estado.botonHielo);
                    } else if(this.botonMini.contains(e.x, e.y) && !this.mini) {
                        this.cambiarEstado(Estado.botonMini);
                    } else if(this.botonRojo.contains(e.x, e.y)) {
                        this.cambiarEstado(Estado.botonRojo);
                    }else if(this.botonAzul.contains(e.x, e.y)) {
                        this.cambiarEstado(Estado.botonAzul);
                    }
                    break;
                case botonRayo://has tocado el boton para comprar la skin de la torre de rayo o cambiarla
                    if(!this.rayo){
                        this.gestionCompra(e,Integer.parseInt(this.datos.getString("CosteRayo")));
                    }
                    else{
                        this.gestionarSkin(e);
                    }
                    break;
                case botonFuego://has tocado el boton para comprar la skin de la una torre de fuego o cambiarla
                    if(!this.fuego){
                        this.gestionCompra(e,Integer.parseInt(this.datos.getString("CosteFuego")));
                    }
                    else{
                        this.gestionarSkin(e);
                    }
                    break;
                case botonHielo://has tocado el boton para comprar la skin de la una torre de hielo o cambiarla
                    if(!this.hielo){
                        this.gestionCompra(e,Integer.parseInt(this.datos.getString("CosteHielo")));
                    }
                    else{
                        this.gestionarSkin(e);
                    }
                    break;
                case botonMini://has tocado el boton para comprar la torre de mini rayo
                    this.gestionCompra(e,Integer.parseInt(this.datos.getString("CosteMini")));
                    break;
                case botonRojo://has tocado el boton para comprar el fondo rojo o cambiarlo
                    if(!this.rojo){
                        this.gestionCompra(e,Integer.parseInt(this.datos.getString("CosteRojo")));
                    }
                    else{
                        this.gestionarSkin(e);
                    }
                    break;
                case botonAzul://has tocado el boton para comprar el fondo azul o cambiarlo
                    if(!this.azul){
                        this.gestionCompra(e,Integer.parseInt(this.datos.getString("CosteAzul")));
                    }
                    else{
                        this.gestionarSkin(e);
                    }
                    break;
            }*/
        }

    }

    //gestiona el cambio de skin
    private void gestionarSkin(TouchEvent e){
        if (this.botonRayo.contains(e.x, e.y)) {
            this.cambiarEstado(Estado.botonRayo);
        } else if (this.botonFuego.contains(e.x, e.y)) {
            this.cambiarEstado(Estado.botonFuego);
        } else if (this.botonHielo.contains(e.x, e.y)) {
            this.cambiarEstado(Estado.botonHielo);
        } else if (this.botonMini.contains(e.x, e.y)) {
            this.cambiarEstado(Estado.botonMini);
        } else if (this.botonRojo.contains(e.x, e.y)) {
            this.cambiarEstado(Estado.botonRojo);
        } else if (this.botonAzul.contains(e.x, e.y)) {
            this.cambiarEstado(Estado.botonAzul);
        } else {
            switch (this.estado){
                case botonRayo:
                    if(this.rayoF.contains(e.x,e.y)){
                        this.rayoF.setColor("#ff00ff00");
                        this.rayoS.setColor("#ffffffff");
                        this.skinRayo="Figura";
                        this.guardar();
                    } else if (this.rayoS.contains(e.x,e.y)) {
                        this.rayoF.setColor("#ffffffff");
                        this.rayoS.setColor("#ff00ff00");
                        this.skinRayo="TorreRayoCosmetico";
                        this.guardar();
                    }
                    break;
                case botonFuego:
                    if(this.fuegoF.contains(e.x,e.y)){
                        this.fuegoF.setColor("#ff00ff00");
                        this.fuegoS.setColor("#ffffffff");
                        this.skinFuego="Figura";
                        this.guardar();
                    } else if (this.fuegoS.contains(e.x,e.y)) {
                        this.fuegoF.setColor("#ffffffff");
                        this.fuegoS.setColor("#ff00ff00");
                        this.skinFuego="TorreFuegoCosmetico";
                        this.guardar();
                    }
                    break;
                case botonHielo:
                    if(this.hieloF.contains(e.x,e.y)){
                        this.hieloF.setColor("#ff00ff00");
                        this.hieloS.setColor("#ffffffff");
                        this.skinHielo="Figura";
                        this.guardar();
                    } else if (this.hieloS.contains(e.x,e.y)) {
                        this.hieloF.setColor("#ffffffff");
                        this.hieloS.setColor("#ff00ff00");
                        this.skinHielo="TorreHieloCosmetico";
                        this.guardar();
                    }
                    break;
                case botonRojo:
                    if(this.botonBlancoF.contains(e.x,e.y)){
                        this.fondo="#FFFFFFFF";
                        this.graphics.setColorClear(this.fondo);
                        this.guardar();
                    } else if (this.botonRojoF.contains(e.x,e.y)) {
                        this.fondo="#FFFF0000";
                        this.graphics.setColorClear(this.fondo);
                        this.guardar();
                    }
                    break;
                case botonAzul:
                    if(this.botonBlancoF.contains(e.x,e.y)){
                        this.fondo="#FFFFFFFF";
                        this.graphics.setColorClear(this.fondo);
                        this.guardar();
                    } else if (this.botonAzulF.contains(e.x,e.y)) {
                        this.fondo="#FF0000FF";
                        this.graphics.setColorClear(this.fondo);
                        this.guardar();
                    }
                    break;
            }
            this.cambiarEstado(Estado.normal);
        }
    }

    /**
     * Metodo que gestiona la pulsación del boton para comprar
     */
    private void gestionCompra(TouchEvent e, float precio) {
        if (this.botonRayo.contains(e.x, e.y)) {
            this.cambiarEstado(Estado.botonRayo);
        } else if (this.botonFuego.contains(e.x, e.y)) {
            this.cambiarEstado(Estado.botonFuego);
        } else if (this.botonHielo.contains(e.x, e.y)) {
            this.cambiarEstado(Estado.botonHielo);
        } else if (this.botonMini.contains(e.x, e.y)) {
            this.cambiarEstado(Estado.botonMini);
        } else if (this.botonRojo.contains(e.x, e.y)) {
            this.cambiarEstado(Estado.botonRojo);
        } else if (this.botonAzul.contains(e.x, e.y)) {
            this.cambiarEstado(Estado.botonAzul);
        } else if (this.botonComprar.contains(e.x, e.y) && this.diamantes >= precio) {
            this.comprar(precio);
            this.cambiarEstado(Estado.normal);
        } else {
            this.cambiarEstado(Estado.normal);
        }
    }

    //metodo que gestiona la compra
    private void comprar(float precio){
        switch (this.estado) {
            case botonRayo://has tocado el boton para comprar la skin de la torre de rayo
                this.diamantes-=precio;
                this.rayo = true;
                this.skinRayo="TorreRayoCosmetico";
                this.textoDiamantes.setText("" + this.diamantes);
                this.botonRayo.setColor("#ff00ff00");
                this.rayoS.setColor("#ff00ff00");
                this.guardar();
                break;
            case botonFuego://has tocado el boton para comprar la skin de la una torre de fuego
                this.diamantes-=precio;
                this.fuego = true;
                this.skinFuego="TorreFuegoCosmetico";
                this.textoDiamantes.setText("" + this.diamantes);
                this.botonFuego.setColor("#ff00ff00");
                this.fuegoS.setColor("#ff00ff00");
                this.guardar();
                break;
            case botonHielo://has tocado el boton para comprar la skin de la una torre de hielo
                this.diamantes-=precio;
                this.hielo = true;
                this.skinHielo="TorreHieloCosmetico";
                this.textoDiamantes.setText("" + this.diamantes);
                this.botonHielo.setColor("#ff00ff00");
                this.fuegoS.setColor("#ff00ff00");
                this.guardar();
                break;
            case botonMini://has tocado el boton para comprar la torre de rayo mini
                this.diamantes-=precio;
                this.mini = true;
                this.textoDiamantes.setText("" + this.diamantes);
                this.botonMini.setColor("#ff00ff00");
                this.guardar();
                break;
            case botonRojo://has tocado el boton para comprar el color de fondo rojo
                this.diamantes-=precio;
                this.botonRojo.setColor("#ff00ff00");
                this.rojo = true;
                this.fondo = "#FFFF0000";
                this.graphics.setColorClear(this.fondo);
                this.textoDiamantes.setText("" + this.diamantes);
                this.guardar();
                break;
            case botonAzul://has tocado el boton para comprar el color de fondo azul
                this.diamantes-=precio;
                this.botonAzul.setColor("#ff00ff00");
                this.azul = true;
                this.fondo = "#FF0000FF";
                this.graphics.setColorClear(this.fondo);
                this.textoDiamantes.setText("" + this.diamantes);
                this.guardar();
                break;
        }
    }

    /**
     * Metodo que cambia el estado del juego y el texto del precio
     * @param nuevoEstado
     */
    private void cambiarEstado(Estado nuevoEstado) {
        try{
        switch (nuevoEstado) {
            case normal:
                this.estado = nuevoEstado;
                break;
            case botonRayo:
                this.coste.setText("Coste: "+ this.datos.getString("CosteRayo")+"\nCosmetico de la\n torre de Rayo");
                this.estado = nuevoEstado;
                break;
            case botonFuego:
                this.coste.setText("Coste: "+ this.datos.getString("CosteFuego")+"\nCosmetico de la\ntorre de Fuego");
                this.estado = nuevoEstado;
                break;
            case botonHielo:
                this.coste.setText("Coste: "+ this.datos.getString("CosteHielo")+"\nCosmetico de la\n torre de Hielo");
                this.estado = nuevoEstado;
                break;
            case botonMini:
                this.coste.setText("Coste: "+ this.datos.getString("CosteMini")+"\n Nueva torre como\n la torre de rayo\n pero mas rapida");
                this.estado = nuevoEstado;
                break;
            case botonRojo:
                this.coste.setText("Coste: "+ this.datos.getString("CosteRojo")+"\n Nuevo color\nde fondo rojo");
                this.estado = nuevoEstado;
                break;
            case botonAzul:
                this.coste.setText("Coste: "+ this.datos.getString("CosteAzul")+"\n Nuevo color\nde fondo azul");
                this.estado = nuevoEstado;
                break;

        }
        } catch (JSONException e) {
            throw new RuntimeException(e);
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
