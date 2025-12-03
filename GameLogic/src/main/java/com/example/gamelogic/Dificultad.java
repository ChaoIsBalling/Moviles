package com.example.gamelogic;

import com.example.engine.Audio;
import com.example.engine.Engine;
import com.example.engine.Graphics;
import com.example.engine.State;
import com.example.engine.TouchEvent;

import java.util.ArrayList;

/**
 * Clase que representa el menú de seleccción de dificultad
 */
public class Dificultad implements State {
    //Botones del menú de dificultad
    private Button botonCorto;
    private Button botonLargo;
    private Button botonInfinito;
    private Button botonVolver;

    //Referencias de modulos del motor
    private Engine engine;
    Graphics gr;

    /**
     * Constructora del menú de dificultad con los tres botones que representan los tres modos de juego
     * @param engine
     */
    public Dificultad(Engine engine){
        this.engine = engine;
        botonCorto = new Button(300,100,220,50,true,20);
        Text textC = new Text("Inika-Regular.ttf","Partida corta",0,0,30,true,true);
        botonCorto.setText(textC);
        botonCorto.setColor("#FF999999");
        botonLargo = new Button(300,200,220,50,true,20);
        Text textL = new Text("Inika-Regular.ttf","Partida larga",0,0,30,true,true);
        botonLargo.setText(textL);
        botonLargo.setColor("#FF999999");
        botonInfinito = new Button(300,300,220,50,true,20);
        Text textI = new Text("Inika-Regular.ttf","Modo Infinito",0,0,30,true,true);
        botonInfinito.setText(textI);
        botonInfinito.setColor("#FF999999");
        this.botonVolver = new Button(30,30,30,30,true,15);
        this.botonVolver.setColor("#FFFF0000");
    }
    @Override
    public void update(double deltaTime) {

    }

    /**
     * Renderiza los botones
     */
    @Override
    public void render(Graphics gr) {
        botonCorto.Render(gr);
        botonLargo.Render(gr);
        botonInfinito.Render(gr);
        botonVolver.Render(gr);
    }

    /**
     * setter del graphics
     * @param gr Graphics
     */
    @Override
    public void setGraphics(Graphics gr) {
        this.gr=gr;
    }

    /**
     * Dependiendo del modo elegido, iremos al estado GameLogic
     * @param list Lista de eventos
     * @param elapseTime Tiempo trascurrido
     */
    @Override
    public void handleInput(ArrayList<TouchEvent> list, double elapseTime) {
        for(TouchEvent e: list){
            switch (e.type){
                case TOUCH_DOWN:
                    if(this.botonCorto.contains(e.x,e.y)){
                        GameLogic gameLogic = new GameLogic(this.engine, GameLogic.Dificultad.corto);
                        this.engine.setState(gameLogic);
                    }
                    else if(this.botonLargo.contains(e.x,e.y)){
                        GameLogic gameLogic = new GameLogic(this.engine, GameLogic.Dificultad.largo);
                        this.engine.setState(gameLogic);
                    }
                    else if(this.botonInfinito.contains(e.x,e.y)){
                        GameLogic gameLogic = new GameLogic(this.engine, GameLogic.Dificultad.infinito);//el -1 es para indicar que es infinito
                        this.engine.setState(gameLogic);
                    }
                    else if(this.botonVolver.contains(e.x,e.y)){
                        Menu menu = new Menu(this.engine);
                        this.engine.setState(menu);
                    }
                    break;
                case TOUCH_UP:

                    break;
                case TOUCH_MOVE:
                    break;
            }
        }
    }

    @Override
    public void setAudio(Audio audio) {

    }

}
