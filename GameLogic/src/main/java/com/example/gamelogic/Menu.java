package com.example.gamelogic;

import com.example.engine.Engine;
import com.example.engine.Graphics;
import com.example.engine.State;
import com.example.engine.TouchEvent;
import com.example.engine.Audio;
import com.example.engine.Sound;

import java.awt.Color;
import java.util.ArrayList;

public class Menu implements State {
    private float x;
    private float y;
    private float w;
    private float h;
    boolean firstFrame = false;

    private Button botonInicial;

    private Square figuraBoton;

    private Triangle tri;

    private Hexagon hex;

    private Line lin;

    private Text textoBoton;
    private Text textoInicial;
    private Audio audio;
    Engine engine;

    public Menu(Engine engine){
        this.x =100;
        this.y=100;
        this.w =200;
        this.h =100;
        this.engine = engine;

        botonInicial = new Button(300, 250, 200,50,true,20);
        botonInicial.setColor(0xFF999999);
        //figuraBoton = new Circle(-60,0,20, true);
        //figuraBoton = new Square(-60,0,40,40, true);
        //figuraBoton.setColor(0x0000FF00);
        //tri = new Triangle(-60,0,40,true);
        //tri.setColor(0x00000000);
        //hex = new Hexagon(-60,0,40, true);
        //hex.setColor(0x000000FF);
        //botonInicial.setFigura(figuraBoton);
        textoBoton = new Text("Inika-Regular.ttf","Jugar",0,0,30,true,true);
        textoBoton.setColor(0xff00ffff);
        botonInicial.setText(textoBoton);
        textoInicial = new Text("Inika-Regular.ttf","TowerDefense",300,150,40,true,true);
        textoInicial.setColor(0Xff000000);
        //this.lin = new Line(100,100,200,200,10);
        //this.lin.setColor(0xFF0000FF);
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
        botonInicial.Render(gr);
        textoInicial.Render(gr);
    }
    @Override
    public void playAudio(Audio audio)
    {
    }
    @Override
    public void handleInput(ArrayList<TouchEvent> list, double elapseTime) {
        for(TouchEvent e: list){
            switch (e.type){
                case TOUCH_DOWN:
                    if(this.botonInicial.contains(e.x,e.y)){
                        GameLogic gameLogic = new GameLogic(this.engine);
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
}
