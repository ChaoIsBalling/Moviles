package com.example.gamelogic;

import com.example.engine.Graphics;

public class Casilla {
    Square cuadrado;
    Tower tower;
    Boolean camino;

    Coordenada coor;
    public Casilla(float x, float y, float w, float h, boolean fill, boolean camino){
        this.cuadrado = new Square(x,y,w,h,fill);
        this.camino=camino;

    }
    public void setColor(int color){
        this.cuadrado.setColor(color);
    }
    public void setTorre(Tower tower){
        if(this.tower == null && !camino){
            this.tower = tower;
        }
    }

    public boolean esCamino(){
        return this.camino;
    }
    public Tower getTorre(){
        return this.tower;
    }
    public float getX(){
        return this.cuadrado.getX();
    }
    public float getY(){
        return this.cuadrado.getY();
    }
    public Coordenada getCoor() {return this.coor;};
    public void setCoor(Coordenada coor){this.coor = coor; }

    public void Render(Graphics gr){
        this.cuadrado.Render(gr);
    }
}
