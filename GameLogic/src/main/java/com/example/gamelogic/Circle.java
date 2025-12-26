package com.example.gamelogic;
import com.example.androidengine.AndroidGraphics;
/**
 * Clase que representa un círculo e implementa a Figura
 */
public class Circle implements Figure{
    //Atributos del circulo
    private float x;
    private float y;
    private float r; //radio
    private String color;
    private boolean isFill; //Si esta relleno o no

    /**
     * Constructora con su posición, radio y si esta relleno o no
     */
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

    /**
     * Getters
     */
    @Override
    public float getX() {
        return this.x;
    }

    @Override
    public float getY() {
        return this.y;
    }
    @Override
    public String getColor() {
        return this.color;
    }

    /**
     * setters
     */
    @Override
    public void setX(float x) {
        this.x = x;
    }

    @Override
    public void setY(float y) {
        this.y = y;
    }

    @Override
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * Renderiza el circulo
     */
    @Override
    public void Render(AndroidGraphics gr) {
        gr.setColor(this.color);

        if(isFill)
            gr.rellenarCirculo(this.x, this.y, this.r);
        else
            gr.pintarCirculo(this.x, this.y,this.r);
    }
    /**
     * Renderiza el circulo de forma centrada a partir de una coordenada x,y
     */
    @Override
    public void RenderCentrado(AndroidGraphics gr, float x, float y) {
        gr.setColor(this.color);

        if(isFill)
            gr.rellenarCirculo(x+this.x, y+this.y, this.r);
        else
            gr.pintarCirculo(x+this.x, y+this.y,this.r);
    }
}
