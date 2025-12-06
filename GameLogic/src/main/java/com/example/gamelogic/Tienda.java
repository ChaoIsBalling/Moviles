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

    private enum Estado{
        normal, botonRayo, botonFuego, botonHielo
    }

    JSONObject datos;
    private Estado estado;
    public Tienda(Engine engine){
        this.engine=engine;
        this.datos =engine.readJsonFile("Tienda/style.json");
        this.botonVolver = new Button(this.datos.getJSONObject("BotonVolver"));

        this.diamantes=0;
        this.textoDiamantes = new Text(this.datos.getJSONObject("TextoDiamantes"));

        this.botonRayo = new Button(this.datos.getJSONObject("BotonRayo"));
        this.botonFuego = new Button(this.datos.getJSONObject("BotonFuego"));
        this.botonHielo = new Button(this.datos.getJSONObject("BotonHielo"));
        this.fondoDes = new Square(500,300,300,400,true);
        this.fondoDes.setColor(0xff00ffff);
        this.coste = new Text(this.datos.getJSONObject("TextoCoste"));
        this.botonComprar = new Button(this.datos.getJSONObject("BotonComprar"));
        this.botonComprar.setText(new Text(this.datos.getJSONObject("TextoComprar")));
        this.estado = Estado.normal;
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
                    this.gestiónBotones(e);
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
    private void gestiónBotones(TouchEvent e) //maneja los estados del juego cuando pulsas botones o las torres
    {
        if(this.botonVolver.contains(e.x,e.y)){
            Menu menu = new Menu(this.engine);
            this.engine.setState(menu);
        }
        else {
            switch (this.estado) {
                case normal://cuando ningun elemento esta seleccionado
                    if (this.botonRayo.contains(e.x, e.y)) {
                        this.cambiarEstado(Estado.botonRayo);
                    } else if (this.botonFuego.contains(e.x, e.y)) {
                        this.cambiarEstado(Estado.botonFuego);
                    } else if (this.botonFuego.contains(e.x, e.y)) {
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

    private void comprar(float precio){
        switch (this.estado) {
            case botonRayo://has tocado el boton para comprar la skin de la torre de rayo
                this.diamantes-=precio;
                this.textoDiamantes.setText("Tienes " + this.diamantes);
                break;
            case botonFuego://has tocado el boton para comprar la skin de la una torre de fuego
                this.diamantes-=precio;
                this.textoDiamantes.setText("Tienes " + this.diamantes);
                break;
            case botonHielo://has tocado el boton para comprar la skin de la una torre de hielo
                this.diamantes-=precio;
                this.textoDiamantes.setText("Tienes " + this.diamantes);
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
