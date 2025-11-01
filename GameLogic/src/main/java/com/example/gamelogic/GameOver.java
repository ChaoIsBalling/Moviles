package com.example.gamelogic;

import com.example.engine.Engine;
import com.example.engine.Graphics;
import com.example.engine.State;
import com.example.engine.TouchEvent;
import com.example.engine.Audio;
import com.example.engine.Sound;

import java.awt.Color;
import java.util.ArrayList;

public class GameOver implements State {
    private float x;
    private float y;
    private float w;
    private float h;

    private Button botonMenu;
    private Button botonReintentar;


    private Text textoBoton;
    private Text textoReintentar;
    private Text textoInicial;
    private Audio audio;
    Engine engine;

    boolean win;
    public GameOver(Engine engine,boolean win)
    {
        this.x =100;
        this.y=100;
        this.w =200;
        this.h =100;
        this.engine = engine;
        this.win=win;

        botonMenu = new Button(120, 250, 200,50,true,20);
        botonMenu.setColor(0xFF999999);

        botonReintentar = new Button(480, 250, 200,50,true,20);
        botonReintentar.setColor(0xFF999999);

        textoBoton = new Text("Inika-Regular.ttf","Menu",0,0,30,true,true);
        textoBoton.setColor(0xff00ffff);
        textoReintentar = new Text("Inika-Regular.ttf","Reintentar",0,0,30,true,true);
        textoReintentar.setColor(0xff00ffff);
        botonMenu.setText(textoBoton);
        botonReintentar.setText(textoReintentar);
        if(win) {
            textoInicial = new Text("Inika-Regular.ttf", "VICTORIA", 300, 150, 40, true, true);
        }
        else {
            textoInicial = new Text("Inika-Regular.ttf", "DERROTA", 300, 150, 40, true, true);
        }
        textoInicial.setColor(0Xff000000);
    }
    @Override
    public void update(double deltatime) {

    }

    @Override
    public void render(Graphics gr) {
        gr.setColor(0x00000000);
        botonMenu.Render(gr);
        textoInicial.Render(gr);
        botonReintentar.Render(gr);
    }

    @Override
    public void handleInput(ArrayList<TouchEvent> list, double elapseTime) {
        for(TouchEvent e: list){
            switch (e.type){
                case TOUCH_DOWN:
                    if(this.botonMenu.contains(e.x,e.y)){
                        Menu menu= new Menu(this.engine);
                        this.engine.setState(menu);
                    }
                    if(this.botonReintentar.contains(e.x,e.y)){
                        Dificultad dificultad = new Dificultad(this.engine);
                        this.engine.setState(dificultad);
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
        this.audio=audio;
    }

}
