package com.example.gamelogic;

import com.example.engine.Scene;
import com.example.engine.Graphics;

public class GameLogic implements Scene {
    private int x;
    private int y;
    private int radious;
    private int speed;

    private Graphics graphics;

    public GameLogic(){
        this.x =100;
        this.y=0;
        this.radious=100;
        this.speed=150;
    }


    @Override
    public void update(double deltaTime) {

        this.x += this.speed *deltaTime;
        while (this.x<this.radious){
            if (this.x < this.radious){
                this.speed *= -1;
            }
        }
    }

    @Override
    public void render() {
        //graphics.pintarCirculo(this.x,this.y,this.radious*5);
    }
}