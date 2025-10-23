package com.example.engine;
public class Circle implements Figure{
    private float x;
    private float y;
    private float r;
    private int color;
    private boolean isFill;

    public Circle(float x, float y, float r, boolean isFill){
        this.x = x;
        this.y = y;
        this.r = r;
        this.isFill = isFill;
    }

    public Circle(float x, float y, float r){
        this.x = x;
        this.y = y;
        this.r = r;
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

        if(isFill)
            gr.rellenarCirculo(this.x, this.y, this.r);
        else
            gr.pintarCirculo(this.x, this.y,this.r);
    }

    @Override
    public void RenderCentrado(Graphics gr, float x, float y) {
        gr.setColor(this.color);

        if(isFill)
            gr.rellenarCirculo(x, y, this.r);
        else
            gr.pintarCirculo(x, y,this.r);
    }
}
