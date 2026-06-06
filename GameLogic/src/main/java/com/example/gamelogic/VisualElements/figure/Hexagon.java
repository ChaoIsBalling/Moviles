package com.example.gamelogic.VisualElements.figure;
import com.example.androidengine.AndroidGraphics;
import com.example.gamelogic.VisualElements.VisualElement;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Clase que representa un Hexágono
 */
public class Hexagon extends Figure {

    //radio desde el centro
    private float r;

    //Indica si es relleno o no
    private boolean isFill;
    //Booleano que indica si es visible o no


    /**
     * Constructora de la clase Hexagono con su coordenada x,y, su radio y si esta relleno o no
     * (a partir de un jsonObject)
     */
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
