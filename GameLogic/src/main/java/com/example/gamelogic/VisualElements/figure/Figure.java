package com.example.gamelogic.VisualElements.figure;

import com.example.androidengine.AndroidGraphics;
import com.example.gamelogic.VisualElements.VisualElement;

import org.json.JSONObject;

public abstract class Figure extends VisualElement {
    public Figure(){}
    public Figure(JSONObject obj){super(obj);}

    /**
     * Devuelve el color de la figura
     * @return color
     */
    public String getColor(){ return this.color; };

    public abstract void RenderCentrado(AndroidGraphics gr, float x, float y);
    /**
     * Renderiza la figura en la posicion x,y
     */
   public abstract void RenderAtPosition(AndroidGraphics gr, float x, float y);
}
