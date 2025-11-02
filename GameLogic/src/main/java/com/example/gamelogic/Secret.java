package com.example.gamelogic;

import com.example.engine.Audio;
import com.example.engine.Graphics;
import com.example.engine.IFont;
import com.example.engine.State;
import com.example.engine.TouchEvent;

import java.util.ArrayList;

public class Secret implements State {
    private float x;
    private float y;
    private float radious;
    private float speed;
    boolean firstFrame = false;

    private Audio audio;

    public Secret(){
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

        IFont f = gr.newFont("Inika-Regular.ttf");
        gr.setFont(f);
        gr.pintarTextoCentrado("Balatrito", 300,200);

        gr.pintarImagen(gr.newImage("asgore.png",10,10),0,210);
        gr.pintarImagen(gr.newImage("Joker.png",10,10),400,200);

    }

    @Override
    public void setGraphics(Graphics gr) {

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

    @Override
    public void setAudio(Audio audio) {
        this.audio=audio;
    }

}
