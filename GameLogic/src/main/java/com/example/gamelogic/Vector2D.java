package com.example.gamelogic;

/**
 * Clase que representa un Vector2D en el juego, en este caso, para representar las posiciones de las
 * casillas del mapa
 */
public class Vector2D {
    //Coordenadas x,y
    private int x;
    private int y;

    /**
     * Constructora
     */
    public Vector2D(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Gettters
     */
    public int getX() { return this.x; }
    public int getY(){ return this.y; }

    /**
     * Setters
     */
    public void setX(int x){
        this.x = x;
    }
    public void setY(int y){ this.y = y; }
}
