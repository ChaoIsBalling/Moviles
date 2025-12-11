package com.example.gamelogic;

import com.example.engine.Audio;
import com.example.engine.Engine;
import com.example.engine.Graphics;
import com.example.engine.Mobile;
import com.example.engine.State;
import com.example.engine.TouchEvent;
import java.util.*;

import org.json.JSONObject;

import java.util.ArrayList;

public class Tienda implements State {

    private Button botonVolver;

    private Engine engine;

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

    //ArrayList de elementos de la tienda que pueden hacer scroll
    private ArrayList<Button> ScrollableButtons;
    private ArrayList<Text>ScrollableText;

    private Image imagenDiamante;

    private enum Estado{
        normal, botonRayo, botonFuego, botonHielo, botonMini
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
            if (c1.y == c2.y)
                return 0;
            else if (c1.y > c2.y)
                return 1;
            else
                return -1;
        }
    }

    Mobile mobile;
    public Tienda(Engine engine,Mobile mobile){
        ScrollableText=new ArrayList<Text>();
        ScrollableButtons=new ArrayList<Button>();
        this.engine=engine;
        this.mobile = mobile;
        this.datos =engine.readJsonFile("Tienda/style.json");
        this.botonVolver = new Button(this.datos.getJSONObject("BotonVolver"));
        this.cargarDatos();

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
            this.botonFuego .setColor("#ff00ff00");
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

        this.minY=ScrollableText.get(0).y;
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
    }

    //carga el progreso y comprueba que no ha sido modificado
    private void cargarDatos(){

        if(this.engine.checkFileExists("save"))
        {
            JSONObject obj=this.engine.readJsonFile2("save");
            String hash = this.engine.createHash(obj.toString());
            if(this.engine.checkHash(hash)) {
                this.rayo = obj.getBoolean("rayo");
                this.fuego = obj.getBoolean("fuego");
                this.hielo = obj.getBoolean("hielo");
                this.mini = obj.getBoolean("mini");
                this.diamantes = obj.getInt("gems");
                this.skinRayo=obj.getString("skinRayo");
                this.skinFuego=obj.getString("skinFuego");
                this.skinHielo=obj.getString("skinHielo");
                this.textoDiamantes = new Text(this.datos.getJSONObject("TextoDiamantes"));
                this.textoDiamantes.setText("" + this.diamantes);
            }
            else{
                //resetea el progreso
                this.reset();
            }

        }
        else {
            this.reset();
        }
    }

    //resetea el progreso
    private void reset(){
        JSONObject obj=new JSONObject();
        obj.put("gems",0);
        obj.put("completed",0);
        obj.put("rayo",false);
        obj.put("fuego",false);
        obj.put("hielo",false);
        obj.put("mini",false);
        obj.put("skinRayo","Figura");
        obj.put("skinFuego","Figura");
        obj.put("skinHielo","Figura");
        this.engine.writeFile("hash",this.engine.createHash(obj.toString()));
        this.engine.writeFile("save",obj.toString());
        this.rayo = false;
        this.fuego = false;
        this.hielo = false;
        this.diamantes = 0;
        this.textoDiamantes = new Text(this.datos.getJSONObject("TextoDiamantes"));
        this.textoDiamantes.setText("" + this.diamantes);
    }

    //guardar progreso
    private void guardar(){
        JSONObject obj=this.engine.readJsonFile2("save");
        obj.put("gems",this.diamantes);
        obj.put("rayo",this.rayo);
        obj.put("fuego",this.fuego);
        obj.put("hielo",this.hielo);
        obj.put("mini",this.mini);
        obj.put("skinRayo",this.skinRayo);
        obj.put("skinFuego",this.skinFuego);
        obj.put("skinHielo",this.skinHielo);
        this.engine.writeFile("hash",this.engine.createHash(obj.toString()));
        this.engine.writeFile("save",obj.toString());
    }

    @Override
    public void update(double deltatime) {
    }

    @Override
    public void render(Graphics gr) {
        this.botonVolver.Render(gr);
        this.textoDiamantes.Render(gr);
        this.botonRayo.Render(gr);
        this.botonFuego.Render(gr);
        this.botonHielo.Render(gr);
        this.botonMini.Render(gr);
        this.CTorres.Render(gr);
        this.CSkins.Render(gr);
        this.imagenDiamante.Render();
        if(this.estado!=Estado.normal){
            this.fondoDes.Render(gr);
            if(this.estado ==Estado.botonRayo&&this.rayo){
                this.rayoF.Render(gr);
                this.rayoS.Render(gr);
            }
            else if (this.estado==Estado.botonFuego&&this.fuego){
                this.fuegoF.Render(gr);
                this.fuegoS.Render(gr);
            } else if (this.estado==Estado.botonHielo&&this.hielo) {
                this.hieloF.Render(gr);
                this.hieloS.Render(gr);
            } else{
            this.coste.Render(gr);
            this.botonComprar.Render(gr);
            }
        }
    }

    @Override
    public void setGraphics(Graphics gr) {
        this.botonVolver.setImagen(new Image(datos.getJSONObject("ImagenVolver"),gr));
        this.botonRayo.setImagen(new Image(datos.getJSONObject("ImagenRayo"),gr));
        this.botonFuego.setImagen(new Image(datos.getJSONObject("ImagenFuego"),gr));
        this.botonHielo.setImagen(new Image(datos.getJSONObject("ImagenHielo"),gr));
        this.botonMini.setImagen(new Image(datos.getJSONObject("ImagenMini"),gr));
        this.imagenDiamante = new Image(datos.getJSONObject("ImagenDiamante"),gr);
        this.rayoS.setImagen(new Image(datos.getJSONObject("ImagenRayo"),gr));
        this.fuegoS.setImagen(new Image(datos.getJSONObject("ImagenFuego"),gr));
        this.hieloS.setImagen(new Image(datos.getJSONObject("ImagenHielo"),gr));
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
        if((ScrollableText.get(0).y>minY&&destY>0)||(ScrollableButtons.get(ScrollableButtons.size()-1).getY()<maxY&&destY<0))
            canScroll=false;

        if(canScroll) {
            for (int i = 0; i < ScrollableText.size(); i++) {
                float newY = ScrollableText.get(i).y + destY;
                ScrollableText.get(i).y=newY;
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
            Menu menu = new Menu(this.engine,this.mobile);
            this.engine.setState(menu);
        }
        else {
            switch (this.estado) {
                case normal://cuando ningun elemento esta seleccionado
                    if (this.botonRayo.contains(e.x, e.y)) {
                        this.cambiarEstado(Estado.botonRayo);
                    } else if (this.botonFuego.contains(e.x, e.y)) {
                        this.cambiarEstado(Estado.botonFuego);
                    } else if (this.botonHielo.contains(e.x, e.y)) {
                        this.cambiarEstado(Estado.botonHielo);
                    }else if(this.botonMini.contains(e.x, e.y) && !this.mini)
                        this.cambiarEstado(Estado.botonMini);
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
            }
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
        }else if (this.botonMini.contains(e.x, e.y)) {
            this.cambiarEstado(Estado.botonMini);
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
            case botonMini://has tocado el boton para comprar la skin de la una torre de hielo
                this.diamantes-=precio;
                this.mini = true;
                this.textoDiamantes.setText("" + this.diamantes);
                this.botonMini.setColor("#ff00ff00");
                this.guardar();
                break;
        }
    }

    /**
     * Metodo que cambia el estado del juego y el texto del precio
     * @param nuevoEstado
     */
    private void cambiarEstado(Estado nuevoEstado) {
        switch (nuevoEstado) {
            case normal:
                this.estado = nuevoEstado;
                break;
            case botonRayo:
                this.coste.setText("Coste: "+ this.datos.getString("CosteRayo")+"\nCosmetico de la torre\nde Rayo");
                this.estado = nuevoEstado;
                break;
            case botonFuego:
                this.coste.setText("Coste: "+ this.datos.getString("CosteFuego")+"\nCosmetico de la torre\nde Fuego");
                this.estado = nuevoEstado;
                break;
            case botonHielo:
                this.coste.setText("Coste: "+ this.datos.getString("CosteHielo")+"\nCosmetico de la torre\nde Hielo");
                this.estado = nuevoEstado;
                break;
            case botonMini:
                this.coste.setText("Coste: "+ this.datos.getString("CosteMini")+"\n Nueva torre como la\ntorre de rayo pero\n mas rapida");
                this.estado = nuevoEstado;
                break;

        }
    }

    @Override
    public void setAudio(Audio audio) {

    }

    @Override
    public void setMobile(Mobile mobile) {

    }
}
