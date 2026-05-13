package com.example.gamelogic.figure;
import com.example.androidengine.AndroidGraphics;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Clase que representa un traingulo e implementa los métodos de Figura
 */
public class Triangle implements Figure {

    //Centro del triangulo
    private float cx;
    private float cy;

    //radio desde el que generar los vertices del triangulo
    private float r;
    //color
    private String color = "#FF000000";
    //Indica si esta relleno o no
    private boolean isFill;

    private boolean visible;


    /**
     * Constructora del triangulo con su posición, radio y booleano de rellenado
     */
    public Triangle(float x, float y, float r, boolean isFill){
        this.cx = x;
        this.cy = y;
        this.r = r;
        this.isFill = isFill;
        this.visible = true;
    }

    public Triangle(JSONObject obj){
        try {
            this.cx = obj.getInt("x");
            this.cy = obj.getInt("y");
            this.r = obj.getInt("r");
            this.isFill = obj.getBoolean("fill");
            this.color = obj.getString("color");
            this.visible = obj.getBoolean("visible");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


    }

    /**
     * Getters
     */
    @Override
    public float getX() {
        return this.cx;
    }
    @Override
    public float getY() {
        return this.cy;
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
        this.cx = x;
    }

    @Override
    public void setY(float y) {
        this.cy = y;
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
     * Renderiza el triangulo de forma centrada o no
     * @param gr Interfaz Graphics
     */
    @Override
    public void Render(AndroidGraphics gr) {
        if(this.visible){
            gr.setColor(this.color);
            if(isFill){
                gr.rellenarPoligono(this.cx,this.cy,this.r, 3);
            }
            else {
                gr.pintarPoligono(this.cx,this.cy,this.r, 3);
            }
        }
    }
    @Override
    public void RenderCentrado(AndroidGraphics gr, float x, float y) {
        if(this.visible){
            gr.setColor(this.color);
            if(isFill){
                gr.rellenarPoligono(x+ this.cx,y + this.cy,this.r, 3);
            }
            else {
                gr.pintarPoligono(x + this.cx,y + this.cy,this.r, 3);
            }
        }
    }
}
