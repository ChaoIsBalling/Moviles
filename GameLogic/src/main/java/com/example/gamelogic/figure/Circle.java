package com.example.gamelogic.figure;
import com.example.androidengine.AndroidGraphics;
import com.example.gamelogic.UIElement;

/**
 * Clase que representa un círculo e implementa a Figura
 */
public class Circle extends UIElement implements Figure{
    //Atributos del circulo
    private float r; //radio
    private String color;
    private boolean isFill; //Si esta relleno o no
    private boolean visible;

    /**
     * Constructora con su posición, radio y si esta relleno o no
     */
    public Circle(float x, float y, float r, boolean isFill){
        this.x = x;
        this.y = y;
        this.r = r;
        this.h=r*2;
        this.isFill = isFill;
        this.visible = true;
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

    @Override
    public void setVisible(boolean c) {
        this.visible = c;
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

    @Override
    public void RenderAtPosition(AndroidGraphics gr, float x, float y) {
        gr.setColor(this.color);

        if(isFill)
            gr.rellenarCirculo(x,y,this.r);
        else
            gr.pintarCirculo(x,y,this.r);
    }
}
