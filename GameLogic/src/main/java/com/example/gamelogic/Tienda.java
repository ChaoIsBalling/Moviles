package com.example.gamelogic;

import com.example.engine.Audio;
import com.example.engine.Engine;
import com.example.engine.Graphics;
import com.example.engine.State;
import com.example.engine.TouchEvent;

import java.util.ArrayList;

public class Tienda implements State {

    private Button botonVolver;

    private Engine engine;

    private Text textoDiamantes;
    public Tienda(Engine engine){
        this.engine=engine;

        this.botonVolver = new Button(30,30,30,30,true,15);
        this.botonVolver.setColor(0xffff0000);

        this.textoDiamantes = new Text("Inika-Regular.ttf","Tienes 0",300,50,40,true,true);
        this.textoDiamantes.setColor(0xff000000);
    }

    @Override
    public void update(double deltatime) {

    }

    @Override
    public void render(Graphics gr) {
        this.botonVolver.Render(gr);
        this.textoDiamantes.Render(gr);
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
}
