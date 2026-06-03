package com.example.gamelogic;
import com.example.androidengine.AndroidGraphics;
import com.example.androidengine.TouchEvent;
import com.example.gamelogic.VisualElements.figure.Square;
import com.example.gamelogic.towers.Tower;

/**
 * Clase que representa una casilla del mapa en el juego
 */
public class Casilla {
    //Atributos de una casilla
    Square cuadrado; //Figura que representa la casilla
    Tower tower; //Torre que está en la casilla
    Boolean camino; //Determinar si es un camino o no
    Vector2D coor; //Coordenadas reales de la casilla
    float x,y,w,h;

    /**
     * Constructora de una casilla con su posición, dimensiones y si es un camino o no
     */
    public Casilla(float x, float y, float w, float h, boolean fill, boolean camino){
        this.cuadrado = new Square(x,y,w,h,fill);
        this.x=x;
        this.y=y;
        this.w=w;
        this.h=h;
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
        return this.x;
    }
    public float getY(){
        return this.y;
    }
    public Vector2D getCoor() {return this.coor;};

    /**
     * Comprueba si la coordenada x,y está dentro de la Casilla
     */
    public boolean contains(float x, float y){
        return x >= this.x-this.w/2 && x <= this.x + this.w/2 &&
                y >= this.y-this.h/2 && y <= this.y + this.h/2;
    }

    public Boolean handleInput(TouchEvent event){
        if(event.type == TouchEvent.TouchEventType.TOUCH_DOWN){
            if(contains(event.x, event.y)){
                return true;
            }
        }
        return false;
    }

    /**
     * Renderiza el cuadrado de la casilla
     */
    public void Render(AndroidGraphics gr){
        this.cuadrado.Render(gr);
    }
}
