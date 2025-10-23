package com.example.gamelogic;

import com.example.engine.Graphics;

public class Triangle implements Figure {
    private float x;

    private float y;

    private float v1;

    private float v2;

    private float v3;

    private int color;

    private boolean isFill;

    Triangle(float x, float y, float v1, float v2, float v3){
        this.x =x;
        this.y =y;
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
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

    }

    @Override
    public void RenderCentrado(Graphics gr, float x, float y) {
        if(isFill){
            //gr
        }
    }
}
