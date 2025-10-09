package com.example.gamelogic;

import com.example.engine.Engine;
import com.example.engine.Graphics;
import com.example.engine.State;

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
        else{
            this.x += (float) (this.speed *deltaTime);
        }


    }

    @Override
    public void render(Graphics gr) {

        gr.setColor(0x00000000);

        gr.rellenarCirculo(this.x,this.y,this.radious);
    }

}