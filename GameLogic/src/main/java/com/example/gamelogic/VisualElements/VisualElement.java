package com.example.gamelogic.VisualElements;
import com.example.androidengine.AndroidGraphics;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Clase base abstacta de la que heredan todos los elementos visibles
 * Imagenes, Figuras, textos y Botones
 */
public abstract class VisualElement {
    protected float x, y;
    protected float h = 0;
    protected boolean isVisible = true;
    protected boolean isEnable = true;
    protected String color = "#FF000000";

    protected float left =0;
    protected float top=0;
    protected float right=600;
    protected float botton=400;

    public VisualElement(){}
    /**
     * Constructora base que solo setea la posicion x e y
     * @param json JSONObject del que lee los parametros
     */
    public VisualElement(JSONObject json)
    {
        try {
            this.x=json.getInt("x");
            this.y=json.getInt("y");

            if(json.has("left")){
                this.left=json.getInt("left");
            }
            if(json.has("top")){
                this.top = json.getInt("top");
            }
            if(json.has("right")){
                this.right = json.getInt("right");
            }
            if(json.has("botton")){
                this.botton = json.getInt("botton");
            }

            updateEnable();

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Metodo abstracto de renderizado que implementaran
     * las clases hijas de VisualElement
     * @param gr Graphics de Android
     */
    public abstract void Render(AndroidGraphics gr);
    //Getters
    public float getX(){return this.x;}
    public float getY(){return this.y;}
    public float getHeight(){return this.h;}
    //Setters
    public void setHeight(float h){this.h=h;}
    public void setX(float x){this.x=x;}
    public void setY(float y){
        this.y=y;
        updateEnable();
    }
    public void setLimits(float left, float top, float right, float botton){
        this.left = left;
        this.top=top;
        this.right=right;
        this.botton=botton;
        updateEnable();
    }

    private void updateEnable(){
        if(this.y + this.h < this.top || this.y - this.h > this.botton){
            this.isEnable =false;
        }
        else{
            this.isEnable = true;
        }
    }

    public float getLeft(){return this.left;}
    public float getTop(){return this.top;}
    public float getRight(){return this.right;}
    public float getBotton(){return this.botton;}
    public void setVisible(boolean visible){this.isVisible = visible;}
    public void setEnabled(boolean enabled){this.isEnable = enabled;}
    public void setColor(String color){ this.color = color; }
}
