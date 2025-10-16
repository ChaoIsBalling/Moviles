package com.example.gamelogic;

import com.example.engine.Engine;
import com.example.engine.Graphics;
import com.example.engine.State;
import com.example.engine.TouchEvent;

import java.awt.FontFormatException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameLogic implements State {
    private float x;
    private float y;
    private float radious;
    private float speed;
    boolean firstFrame = false;

    public GameLogic(){
        this.x =100;
        this.y=100;
        this.radious=50;
        this.speed=10f;
    }

    @Override
    public void update(double deltaTime) {
        if(!this.firstFrame){
            this.firstFrame = !this.firstFrame;
        }
        else {
            this.x += (float) (this.speed * deltaTime);
        }

    }

    @Override
    public void render(Graphics gr) {

        gr.setColor(0x000000FF);

        gr.rellenarCirculo(this.x,this.y,this.radious);

        gr.newFont("Inika-Regular.ttf");
        gr.setFont();
        gr.pintarTexto("Balatrito", 200,200);

        gr.pintarImagen(gr.newImage("asgore.png"),0,210);
        gr.pintarImagen(gr.newImage("Joker.png"),400,200);
    }

    @Override
    public void handleInput(ArrayList<TouchEvent> list, double elapseTime) {

        for(TouchEvent e: list){

            switch (e.type){
                case TOUCH_DOWN:
                    System.out.println("Has pulsado el raton");
                    break;
                case TOUCH_UP:
                    System.out.println("Has soltado el raton");
                    break;
            }
        }
    }


}