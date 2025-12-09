package com.example.gamelogic;

import com.example.engine.Audio;
import com.example.engine.Engine;
import com.example.engine.Graphics;
import com.example.engine.Mobile;
import com.example.engine.State;
import com.example.engine.TouchEvent;

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

    private Text coste;
    private Button botonComprar;

    private Square fondoDes;

    private boolean rayo;
    private boolean fuego;
    private boolean hielo;

    private enum Estado{
        normal, botonRayo, botonFuego, botonHielo
    }

    JSONObject datos;
    private Estado estado;

    Mobile mobile;
    public Tienda(Engine engine,Mobile mobile){
        this.engine=engine;
        this.mobile = mobile;
        this.datos =engine.readJsonFile("Tienda/style.json");
        this.botonVolver = new Button(this.datos.getJSONObject("BotonVolver"));
        this.cargarDatos();

        this.botonRayo = new Button(this.datos.getJSONObject("BotonRayo"));
        this.botonFuego = new Button(this.datos.getJSONObject("BotonFuego"));
        this.botonHielo = new Button(this.datos.getJSONObject("BotonHielo"));

        if(this.rayo){
            this.botonRayo.setColor("#ff00ff00");
        }
        if(this.fuego){
            this.botonFuego .setColor("#ff00ff00");
        }
        if(this.hielo){
            this.botonHielo.setColor("#ff00ff00");
        }

        this.fondoDes = new Square(500,300,300,400,true);
        this.fondoDes.setColor("#ff00ffff");
        this.coste = new Text(this.datos.getJSONObject("TextoCoste"));
        this.botonComprar = new Button(this.datos.getJSONObject("BotonComprar"));
        this.botonComprar.setText(new Text(this.datos.getJSONObject("TextoComprar")));
        this.estado = Estado.normal;


    }

    private void cargarDatos(){

        if(this.engine.checkFileExists("save"))
        {
            JSONObject obj=this.engine.readJsonFile2("save");
            String hash = this.engine.createHash(obj.toString());
            if(this.engine.checkHash(hash)) {
                this.rayo = obj.getBoolean("rayo");
                this.fuego = obj.getBoolean("fuego");
                this.hielo = obj.getBoolean("hielo");
                this.diamantes = obj.getInt("gems");
                this.textoDiamantes = new Text(this.datos.getJSONObject("TextoDiamantes"));
                this.textoDiamantes.setText("Tienes " + this.diamantes);
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
        this.engine.writeFile("hash",this.engine.createHash(obj.toString()));
        this.engine.writeFile("save",obj.toString());
        this.rayo = false;
        this.fuego = false;
        this.hielo = false;
        this.diamantes = 0;
        this.textoDiamantes = new Text(this.datos.getJSONObject("TextoDiamantes"));
        this.textoDiamantes.setText("Tienes " + this.diamantes);
    }

    //guardar progreso
    private void guardar(){
        JSONObject obj=this.engine.readJsonFile2("save");
        obj.put("gems",this.diamantes);
        obj.put("rayo",this.rayo);
        obj.put("fuego",this.fuego);
        obj.put("hielo",this.hielo);
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
        if(this.estado!=Estado.normal){
            this.fondoDes.Render(gr);
            this.coste.Render(gr);
            this.botonComprar.Render(gr);
        }
    }

    @Override
    public void setGraphics(Graphics gr) {
        this.botonVolver.setImagen(new Image(datos.getJSONObject("ImagenVolver"),gr));
        this.botonRayo.setImagen(new Image(datos.getJSONObject("ImagenRayo"),gr));
        this.botonFuego.setImagen(new Image(datos.getJSONObject("ImagenFuego"),gr));
        this.botonHielo.setImagen(new Image(datos.getJSONObject("ImagenHielo"),gr));
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
                    this.gestionBotones(e);
                    break;
                case TOUCH_UP:
                    break;
                case TOUCH_MOVE:
                    break;
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
                    if (this.botonRayo.contains(e.x, e.y) && !this.rayo) {
                        this.cambiarEstado(Estado.botonRayo);
                    } else if (this.botonFuego.contains(e.x, e.y) && !this.fuego) {
                        this.cambiarEstado(Estado.botonFuego);
                    } else if (this.botonHielo.contains(e.x, e.y) && !this.hielo) {
                        this.cambiarEstado(Estado.botonHielo);
                    }
                    break;
                case botonRayo://has tocado el boton para comprar la skin de la torre de rayo
                    this.gestionCompra(e,Integer.parseInt(this.datos.getString("CosteRayo")));
                    break;
                case botonFuego://has tocado el boton para comprar la skin de la una torre de fuego
                    this.gestionCompra(e,Integer.parseInt(this.datos.getString("CosteFuego")));
                    break;
                case botonHielo://has tocado el boton para comprar la skin de la una torre de hielo
                    this.gestionCompra(e,Integer.parseInt(this.datos.getString("CosteHielo")));
                    break;
            }
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
                this.textoDiamantes.setText("Tienes " + this.diamantes);
                this.guardar();
                break;
            case botonFuego://has tocado el boton para comprar la skin de la una torre de fuego
                this.diamantes-=precio;
                this.fuego=true;
                this.textoDiamantes.setText("Tienes " + this.diamantes);
                this.guardar();
                break;
            case botonHielo://has tocado el boton para comprar la skin de la una torre de hielo
                this.diamantes-=precio;
                this.hielo = true;
                this.textoDiamantes.setText("Tienes " + this.diamantes);
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
                this.coste.setText("Coste: "+ this.datos.getString("CosteRayo"));
                this.estado = nuevoEstado;
                break;
            case botonFuego:
                this.coste.setText("Coste: "+ this.datos.getString("CosteFuego"));
                this.estado = nuevoEstado;
                break;
            case botonHielo:
                this.coste.setText("Coste: "+ this.datos.getString("CosteHielo"));
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
