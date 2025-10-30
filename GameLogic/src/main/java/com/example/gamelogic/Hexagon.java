package com.example.gamelogic;

import com.example.engine.Graphics;

public class Hexagon implements Figure{

    private float cx;

    private float cy;

    private float r;

    private int color;

    private boolean isFill;


    public Hexagon(float x, float y, float r, boolean isFill){
        this.cx = x;
        this.cy = y;
        this.r = r;
        this.isFill = isFill;
    }
    Hexagon(float x, float y, float r){
        this.cx = x;
        this.cy = y;
        this.r = r;
    }
    @Override
    public float getX() {
        return this.cx;
    }

    @Override
    public float getY() {
        return this.cy;
    }

    @Override
    public void setX(float x) {
        this.cx = x;
    }

    @Override
    public void setY(float y) {
        this.cy = y;
    }

    @Override
    public int getColor() {
        return this.color;
    }

    @Override
    public void setColor(int color) {
        this.color= color;
    }

    @Override
    public void Render(Graphics gr) {
        gr.setColor(this.color);
        if(isFill){
            gr.rellenarPoligono(this.cx,this.cy,this.r, 6);
        }
        else {
            gr.pintarPoligono(this.cx,this.cy,this.r, 6);
        }
    }

    @Override
    public void RenderCentrado(Graphics gr, float x, float y) {
        gr.setColor(this.color);
        if(isFill){
            gr.rellenarPoligono(x+this.cx,y+this.cy,this.r, 6);
        }
        else {
            gr.pintarPoligono(x+this.cx,x+this.cy,this.r, 6);
        }
    }
}
