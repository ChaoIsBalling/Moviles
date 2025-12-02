package com.example.gamelogic;

import com.example.engine.Graphics;

/**
 * Clase que representa un cuadrado e implementa los métodos de Figura
 */
public class Square implements Figure{
    //Coordenadas
    private float x;
    private float y;
    //Dimensiones
    private float w;
    private float h;

    //Radio de las esquinas (si es que son redondeadas)
    private float arcRadius;
    //color del cuadrado
    private int color;

    //Determinan si esta relleno o redondeado
    private boolean isFill = false;
    private boolean isRound = false;

    /**
     * Constructora del cuadrado con sus coordenadas, dimensiones y booleanos
     */
    public Square(float x, float y, float w, float h, boolean isFill){
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.isFill = isFill;
    }
    public Square(float x, float y, float w, float h, boolean isFill, boolean isRound, float ar){
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.isFill = isFill;
        this.isRound = isRound;
        this.arcRadius = ar;
    }
    public Square(float x, float y, float w, float h){
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
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
    public int getColor() {
        return this.color;
    }

    /**
     * Setters
     */
    @Override
    public void setX(float x) {
        this.x =x;
    }
    @Override
    public void setY(float y) {
        this.y = y;
    }
    @Override
    public void setColor(int color) {
        this.color = color;
    }

    /**
     * Renderiza el cuadrado con relleno o redondeado si asi quisieramos
     * @param gr Interfaz Graphics
     */
    @Override
    public void Render(Graphics gr) {
        gr.setColor(this.color);
        if(this.isFill && this.isRound)
            gr.rellenarCuadradoRedondeado(this.x, this.y,this.w,this.h,this.arcRadius);

        else if(this.isFill)
            gr.rellenarCuadrado(this.x, this.y, this.w, this.h);

        else
            gr.pintarCuadrado(this.x,this.y,this.w,this.h);

    }

    /**
     * Renderiza centrado
     */
    public void RenderCentrado(Graphics gr, float x, float y) {
        gr.setColor(this.color);
        if(this.isFill && this.isRound)
            gr.rellenarCuadradoRedondeado(x+this.x, y+this.y,this.w,this.h,this.arcRadius);

        else if(this.isFill)
            gr.rellenarCuadrado(x+this.x, y+this.y, this.w, this.h);

        else
            gr.pintarCuadrado(x+this.x,y+this.y,this.w,this.h);
    }
}
