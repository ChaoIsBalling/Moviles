package com.example.gamelogic.VisualElements.figure;
import com.example.androidengine.AndroidGraphics;
import com.example.gamelogic.VisualElements.VisualElement;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Clase que representa un traingulo e implementa los métodos de Figura
 */
public class Triangle extends Figure {


    //radio desde el que generar los vertices del triangulo
    private float r;

    //Indica si esta relleno o no
    private boolean isFill;
    private boolean visible;


    /**
     * Constructora del triangulo con su posición, radio y booleano de rellenado
     */
    public Triangle(float x, float y, float r, boolean isFill){
        this.x = x;
        this.y = y;
        this.r = r;
        this.isFill = isFill;
        this.visible = true;
    }

    public Triangle(JSONObject obj){
        super(obj);
        try {
            this.r = obj.getInt("r");
            this.h=r*2;
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
    public String getColor() {
        return this.color;
    }

    /**
     * Setters
     */

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
                gr.rellenarPoligono(this.x,this.y,this.r, 3);
            }
            else {
                gr.pintarPoligono(this.x,this.y,this.r, 3);
            }
        }
    }
    @Override
    public void RenderCentrado(AndroidGraphics gr, float x, float y) {
        if(this.visible){
            gr.setColor(this.color);
            if(isFill){
                gr.rellenarPoligono(x+ this.x,y + this.y,this.r, 3);
            }
            else {
                gr.pintarPoligono(x + this.x,y + this.y,this.r, 3);
            }
        }
    }

    @Override
    public void RenderAtPosition(AndroidGraphics gr, float x, float y) {
        if(this.visible){
            gr.setColor(this.color);
            if(isFill){
                gr.rellenarPoligono(x,y,this.r, 3);
            }
            else {
                gr.pintarPoligono(x ,y,this.r, 3);
            }
        }
    }
}
