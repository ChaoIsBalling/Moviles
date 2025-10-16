package com.example.gamelogic;

import com.example.engine.Engine;
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

    Engine engine;

    public Menu(Engine engine){
        this.x =100;
        this.y=100;
        this.w =200;
        this.h =100;
        this.engine = engine;
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
        gr.pintarCuadrado(x,y,w,h);
    }

    @Override
    public void handleInput(ArrayList<TouchEvent> list, double elapseTime) {
        for(TouchEvent e: list){

            switch (e.type){
                case TOUCH_DOWN:
                    if(e.x >= this.x && e.x <= this.x + this.w && e.y >= this.y && e.y <= this.y + this.h){
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
