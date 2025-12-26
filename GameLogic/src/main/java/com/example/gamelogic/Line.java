package com.example.gamelogic;
import com.example.androidengine.AndroidGraphics;
/**
 * Clase que representa una linea recta e implementa de Figura
 */
public class Line implements Figure {
    //dos coordenadas que representan el posicion inicial y final de la línea
    private float x1;
    private float y1;
    private float x2;
    private float y2;

    //color de la linea
    private String color;

    //Grosor de la línea
    private float w;

    /**
     * Constructoras con sus dos coordenadas y grosor
     * @return
     */
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

    /**
     * Getters
     */
    @Override
    public float getX() {
        return this.x1;
    }

    @Override
    public float getY() {
        return this.y1;
    }
    @Override
    public String getColor() {
        return this.color;
    }

    /**
     * Setters
     */
    @Override
    public void setX(float x) {
    }
    @Override
    public void setY(float y) {
    }

    @Override
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * Reneriza la linea de forma normal o centrada
     * @param gr Interfaz Graphics
     */
    @Override
    public void Render(AndroidGraphics gr) {
        gr.setColor(this.color);
        gr.pintarLinea(this.x1,this.y1, this.x2, this.y2,this.w);
    }

    @Override
    public void RenderCentrado(AndroidGraphics gr, float x, float y) {
        gr.setColor(this.color);
        gr.pintarLinea(this.x1,this.y1, this.x2, this.y2,this.w);
    }
}
