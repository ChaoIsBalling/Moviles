package com.example.gamelogic.VisualElements.figure;

import com.example.androidengine.AndroidGraphics;
import com.example.gamelogic.VisualElements.VisualElement;

import org.json.JSONObject;

public abstract class Figure extends VisualElement {
    /**
     * Devuelve el color de la figura
     * @return color
     */
    public Figure(){}
    public Figure(JSONObject obj){super(obj);}
    public abstract String getColor();

    public abstract void RenderCentrado(AndroidGraphics gr, float x, float y);
    /**
     * Renderiza la figura en la posicion x,y
     */
   public abstract void RenderAtPosition(AndroidGraphics gr, float x, float y);
}
