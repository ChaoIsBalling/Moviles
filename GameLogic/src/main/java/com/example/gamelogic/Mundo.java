package com.example.gamelogic;

import com.example.engine.Audio;
import com.example.engine.Engine;
import com.example.engine.Graphics;
import com.example.engine.Mobile;
import com.example.engine.State;
import com.example.engine.TouchEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;

public class Mundo implements State {

    private Button nivelMundo;

    private Text textoMundo;
    private Square fondoTexto;

    private Button siguienteMundo;
    private Button anteriorMundo;
    private Button botonVolver;

    private Engine engine;

    public Mundo(Engine engine){
        this.engine=engine;

        this.nivelMundo = new Button(75,150,100,100);
        this.nivelMundo.setColor("#FF88FF00");
        Text nivel1 = new Text("Inika-Regular.ttf","1",0,0,40,true,true);
        nivel1.setColor("#FF000000");
        this.nivelMundo.setText(nivel1);

        this.textoMundo = new Text("Inika-Regular.ttf","Mundo1",300,50,40,true,true);
        this.textoMundo.setColor("#FF000000");
        this.fondoTexto = new Square(300,50,300,70,true);
        this.fondoTexto.setColor(0xff009900);

        this.siguienteMundo = new Button(110,50,50,50,true,30);
        this.siguienteMundo.setColor("#FF0000FF");
        this.anteriorMundo = new Button(490,50,50,50,true,30);
        this.anteriorMundo.setColor("#FF0000FF");
        this.botonVolver = new Button(30,30,30,30,true,15);
        this.botonVolver.setColor("#FFFF0000");
    }

    @Override
    public void update(double deltatime) {

    }

    @Override
    public void render(Graphics gr) {
        this.nivelMundo.Render(gr);
        this.fondoTexto.Render(gr);
        this.textoMundo.Render(gr);
        this.siguienteMundo.Render(gr);
        this.anteriorMundo.Render(gr);
        this.botonVolver.Render(gr);
    }

    @Override
    public void setGraphics(Graphics gr) {

    }

    @Override
    public void handleInput(ArrayList<TouchEvent> list, double elapseTime) {
        for(TouchEvent e: list){
            switch (e.type){
                case TOUCH_DOWN:
                    if(this.botonVolver.contains(e.x,e.y)){
                        Menu menu = new Menu(this.engine);
                        this.engine.setState(menu);
                    }
                    else if (this.nivelMundo.contains(e.x,e.y)){
                        GameLogic gameLogic = new GameLogic(this.engine,"mapa1.json");
                        this.engine.setState(gameLogic);
                    }
                    break;
                case TOUCH_UP:
                    System.out.println("Has soltado el raton");
                    break;
                case TOUCH_MOVE:
                    break;
            }
        }
    }

    @Override
    public void setAudio(Audio audio) {

    }

    @Override
    public void setMobile(Mobile mobile) {

    }
}
