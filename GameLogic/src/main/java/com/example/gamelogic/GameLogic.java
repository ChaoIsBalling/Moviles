package com.example.gamelogic;

import com.example.engine.Engine;
import com.example.engine.Graphics;
import com.example.engine.State;

public class GameLogic implements State {
    private int x;
    private int y;
    private int radius;
    private int speed;

    private Graphics graphics;

    public GameLogic(){
        this.x =100;
        this.y=0;
        this.radius=20;
        this.speed=150;
    }
    public void init(Graphics gr){
        this.graphics = gr;
    }

    @Override
    public void update(double deltaTime) {
//        int maxX = this.graphics.getWidth() - this.radius;
//
//        this.x += this.speed * deltaTime;
//        while (this.x < this.radius) {
//            if (this.x < this.radius) {
//                this.speed *= -1;
//            } else if (this.x > maxX) {
//                // Nos salimos por la derecha. Rebotamos
//                this.x = 2 * maxX - this.x;
//                this.speed *= -1;
//            }
//        }
    }

    @Override
    public void render(Graphics gr) {

        gr.setColor(0xff00ff00);

        gr.pintarCirculo(this.x,this.y,this.radius);
    }

}