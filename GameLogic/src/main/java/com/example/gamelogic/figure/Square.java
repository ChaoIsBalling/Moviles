package com.example.gamelogic.figure;
import com.example.androidengine.AndroidGraphics;

import org.json.JSONException;
import org.json.JSONObject;

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
    private String color;

    //Determinan si esta relleno o redondeado
    private boolean isFill = false;
    private boolean isRound = false;
    //Booleano que indica si es visible o no
    private boolean visible;

    /**
     * Constructora del cuadrado con sus coordenadas, dimensiones y booleanos
     */
    public Square(float x, float y, float w, float h, boolean isFill){
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.isFill = isFill;
        this.visible = true;
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

    public Square(JSONObject obj){
        try {
            this.x = obj.getInt("x");
            this.y = obj.getInt("y");
            this.w = obj.getInt("w");
            this.h = obj.getInt("h");
            this.isFill = obj.getBoolean("fill");
            this.isRound = obj.getBoolean("round");
            this.arcRadius = obj.getInt("r");
            this.color = obj.getString("color");
            this.visible = obj.getBoolean("visible");
         } catch (JSONException e) {
            throw new RuntimeException(e);
        }
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
    public String getColor() {
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
    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public void setVisible(boolean c) {
        this.visible = c;
    }

    /**
     * Renderiza el cuadrado con relleno o redondeado si asi quisieramos
     * @param gr Interfaz Graphics
     */
    @Override
    public void Render(AndroidGraphics gr) {
        if(this.visible){
            gr.setColor(this.color);
            if(this.isFill && this.isRound)
                gr.rellenarCuadradoRedondeado(this.x, this.y,this.w,this.h,this.arcRadius);

            else if(this.isFill)
                gr.rellenarCuadrado(this.x, this.y, this.w, this.h);

            else
                gr.pintarCuadrado(this.x,this.y,this.w,this.h);

        }
    }

    /**
     * Renderiza centrado
     */
    public void RenderCentrado(AndroidGraphics gr, float x, float y) {
        if(this.visible){
            gr.setColor(this.color);
            if(this.isFill && this.isRound)
                gr.rellenarCuadradoRedondeado(x+this.x, y+this.y,this.w,this.h,this.arcRadius);

            else if(this.isFill)
                gr.rellenarCuadrado(x+this.x, y+this.y, this.w, this.h);

            else
                gr.pintarCuadrado(x+this.x,y+this.y,this.w,this.h);
        }
    }
}
