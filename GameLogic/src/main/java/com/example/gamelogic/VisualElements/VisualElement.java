package com.example.gamelogic.VisualElements;
import com.example.androidengine.AndroidGraphics;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class VisualElement {
    protected float x;
    protected float y;
    protected float h=0;
    protected boolean isVisible = true;
    protected boolean isEnable = true;
    protected String color="#FF000000";;
    public VisualElement(){}
    public VisualElement(JSONObject json)
    {
        try {
            this.x=json.getInt("x");
            this.y=json.getInt("y");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public abstract void Render(AndroidGraphics gr);
    public void setHeight(float h){this.h=h;}
    public void setX(float x){this.x=x;}
    public void setY(float y){this.y=y;}
    public float getX(){return this.x;}
    public float getY(){return this.y;}
    public float getHeight(){return this.h;}
    public void setVisible(boolean visible){this.isVisible = visible;}
    public void setEnabled(boolean enabled){this.isEnable = enabled;}
    public void setColor(String color){
        this.color = color;
    }
}
