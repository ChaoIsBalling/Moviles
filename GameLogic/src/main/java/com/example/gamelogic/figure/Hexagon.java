package com.example.gamelogic.figure;
import com.example.androidengine.AndroidGraphics;
import com.example.gamelogic.UIElement;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Clase que representa un Hexágono
 */
public class Hexagon extends UIElement implements Figure {

    //radio desde el centro
    private float r;

    //Color del hexágono
    private String color;

    //Indica si es relleno o no
    private boolean isFill;
    //Booleano que indica si es visible o no


    /**
     * Constructora de la clase Hexagono con su coordenada x,y, su radio y si esta relleno o no
     */
    public Hexagon(float x, float y, float r, boolean isFill){
        this.x = x;
        this.y = y;
        this.r = r;
        this.isFill = isFill;
        this.isVisible = true;
    }

    public Hexagon(JSONObject obj){
        super(obj);
        try {
            this.x = obj.getInt("x");
            this.y = obj.getInt("y");
            this.r = obj.getInt("r");
            this.h=r*2;
            this.isFill = obj.getBoolean("fill");
            this.color = obj.getString("color");
            setVisible(obj.getBoolean("visible"));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


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
    public void setColor(String color) {
        this.color= color;
    }


    @Override
    public void setX(float x) {
        this.x = x;
    }

    @Override
    public void setY(float y) {
        this.y = y;
    }

    /**
     * Renderiza el circulo con o sin relleno
     * @param gr Interfaz Graphics
     */
    @Override
    public void Render(AndroidGraphics gr) {
        if(this.isVisible){
            gr.setColor(this.color);
            if(isFill){
                gr.rellenarHexagono(this.x, this.y,this.r);
            }
            else {
                gr.rellenarHexagono(this.x, this.y,this.r);
            }
        }

    }

    /**
     * Renderiza de forma centrada el círculo a partir de una posicion x,y
     */
    @Override
    public void RenderCentrado(AndroidGraphics gr, float x, float y) {
        if(this.isVisible) {
            gr.setColor(this.color);
            if (isFill) {
                gr.rellenarHexagono(x + this.x, y + this.y, this.r);
            } else {
                gr.rellenarHexagono(x + this.x, y + this.y, this.r);
            }
        }
    }

    @Override
    public void RenderAtPosition(AndroidGraphics gr, float x, float y) {
        if(this.isVisible) {
            gr.setColor(this.color);
            if (isFill) {
                gr.rellenarHexagono(x,y,this.r);
            } else {
                gr.rellenarHexagono(x,y,this.r);
            }
        }
    }
}
