package com.example.gamelogic;

import com.example.engine.Graphics;

public class Square implements Figure{

    private float x;
    private float y;
    private float w;
    private float h;

    private int color;

    private boolean isFill;

    public Square(float x, float y, float w, float h, boolean isFill){
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.isFill = isFill;
    }
    public Square(float x, float y, float w, float h){
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }


    @Override
    public float getX() {
        return this.x;
    }

    @Override
    public float getY() {
        return this.y;
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
        if(this.isFill){
            gr.rellenarCuadrado(this.x, this.y, this.w, this.h);
        }
        gr.pintarCuadrado(this.x,this.y,this.w,this.h);
    }

    public void RenderCentrado(Graphics gr, float x, float y) {
        gr.setColor(this.color);
        if(this.isFill){
            gr.rellenarCuadrado(x, y, this.w, this.h);
        }
        gr.pintarCuadrado(x,y,this.w,this.h);
    }
}
