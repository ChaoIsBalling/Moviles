package com.example.gamelogic;

import com.example.engine.Circle;
import com.example.engine.Engine;
import com.example.engine.Square;
import com.example.engine.Graphics;
import com.example.engine.State;
import com.example.engine.TouchEvent;

import java.util.ArrayList;
import java.util.List;

public class Menu implements State {
    private float x;
    private float y;
    private float w;
    private float h;
    boolean firstFrame = false;

    private Button botonInicial;

    private Square figuraBoton;
    Engine engine;

    public Menu(Engine engine){
        this.x =100;
        this.y=100;
        this.w =200;
        this.h =100;
        this.engine = engine;

        botonInicial = new Button(400/2, 500/2, 200,100);
        //figuraBoton = new Circle(-60,0,20, true);
        figuraBoton = new Square(-90,-20,40,40, true);
        figuraBoton.setColor(0x0000FF00);
        botonInicial.setFigura(figuraBoton);
    }
    @Override
    public void update(double deltatime) {
        if(!this.firstFrame){
            this.firstFrame = !this.firstFrame;
        }
    }

    @Override
    public void render(Graphics gr) {
        gr.setColor(0x00000000);
        //gr.pintarCuadrado(x,y,w,h);
        botonInicial.setText(gr,"Inika-Regular.ttf","Hola", 20);
        botonInicial.Render(gr);
    }

    @Override
    public void handleInput(ArrayList<TouchEvent> list, double elapseTime) {
        for(TouchEvent e: list){
            switch (e.type){
                case TOUCH_DOWN:
                    if(botonInicial.contains(e.x,e.y)){
                        GameLogic gameLogic = new GameLogic();
                        this.engine.setState(gameLogic);
                    }
                    break;
                case TOUCH_UP:
                    System.out.println("Has soltado el raton");
                    break;
            }
        }

    }
}
