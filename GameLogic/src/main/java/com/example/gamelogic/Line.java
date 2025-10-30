package com.example.gamelogic;

import com.example.engine.Graphics;

public class Line implements Figure{
    private float x1;
    private float y1;
    private float x2;
    private float y2;

    private int color;

    private float w;

    Line(float x1, float y1, float x2, float y2, float w){
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.w = w;
    }
    Line(float x1, float y1, float x2, float y2){
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }


    @Override
    public float getX() {
        return this.x1;
    }

    @Override
    public float getY() {
        return this.y1;
    }

    @Override
    public int getColor() {
        return this.color;
    }

    @Override
    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public void Render(Graphics gr) {
        gr.setColor(this.color);
        gr.pintarLinea(this.x1,this.y1, this.x2, this.y2,this.w);
    }

    @Override
    public void RenderCentrado(Graphics gr, float x, float y) {
        gr.setColor(this.color);
        gr.pintarLinea(this.x1,this.y1, this.x2, this.y2,this.w);
    }
}
