package com.example.gamelogic.VisualElements.figure;
import com.example.androidengine.AndroidGraphics;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Clase que representa un círculo e implementa a Figura
 */
public class Circle extends Figure {
    //Atributos del circulo
    private float r; //radio
    private boolean isFill; //Si esta relleno o no

    /**
     * Constructora con su posición, radio y si esta relleno o no (a partir de un json object)
     */
    public Circle(JSONObject obj)
    {
        super(obj);
        try {
            this.x = obj.getInt("x");
            this.y = obj.getInt("y");
            this.r = obj.getInt("r");
            this.h= r * 2;
            this.isFill = obj.getBoolean("fill");
            this.color = obj.getString("color");
            setVisible(obj.getBoolean("visible"));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
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
