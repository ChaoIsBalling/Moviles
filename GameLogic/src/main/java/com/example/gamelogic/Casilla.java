package com.example.gamelogic;

import com.example.engine.Graphics;

/**
 * Clase que representa una casilla del mapa en el juego
 */
public class Casilla {
    //Atributos de una casilla
    Square cuadrado; //Figura que representa la casilla
    Tower tower; //Torre que está en la casilla
    Boolean camino; //Determinar si es un camino o no
    Vector2D coor; //Coordenadas reales de la casilla

    /**
     * Constructora de una casilla con su posición, dimensiones y si es un camino o no
     */
    public Casilla(float x, float y, float w, float h, boolean fill, boolean camino){
        this.cuadrado = new Square(x,y,w,h,fill);
        this.camino=camino;

    }

    /**
     * Setters
     */
    public void setColor(String color){
        this.cuadrado.setColor(color);
    }
    public void setTorre(Tower tower){
        if(this.tower == null && !camino){
            this.tower = tower;
        }
    }
    public void setCoor(Vector2D coor){this.coor = coor; }

    /**
     * getters
     */
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
    public Vector2D getCoor() {return this.coor;};

    /**
     * Renderiza el cuadrado de la casilla
     */
    public void Render(Graphics gr){
        this.cuadrado.Render(gr);
    }
}
