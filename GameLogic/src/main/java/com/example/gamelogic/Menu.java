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

    private Button botonInicial;

    private Text textoInicial;
    private Audio audio;
    Engine engine;

    Graphics gr;

    public Menu(Engine engine){
        this.engine = engine;

        this.botonInicial = new Button(300, 250, 200,50,true,20);
        this.botonInicial.setColor(0xFF999999);
        Text textoBoton = new Text("Inika-Regular.ttf","Jugar",0,0,30,true,true);
        textoBoton.setColor(0xff00ffff);
        this.botonInicial.setText(textoBoton);
        this.textoInicial = new Text("Inika-Regular.ttf","TowerDefense",300,150,40,true,true);
        this.textoInicial.setColor(0Xff000000);
    }
    @Override
    public void update(double deltaTime) {

    }

    @Override
    public void render(Graphics gr) {
        botonInicial.Render(gr);
        textoInicial.Render(gr);
    }

    @Override
    public void setGraphics(Graphics gr) {
        this.gr=gr;
    }

    @Override
    public void handleInput(ArrayList<TouchEvent> list, double elapseTime) {
        for(TouchEvent e: list){
            switch (e.type){
                case TOUCH_DOWN:
                    if(this.botonInicial.contains(e.x,e.y)){
                        Dificultad dificultad = new Dificultad(this.engine);
                        this.engine.setState(dificultad);
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
    this.audio=audio;
    }
}
