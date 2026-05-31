package com.example.gamelogic;
import com.example.androidengine.AndroidGraphics;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class UIElement {
    protected float x;
    protected float y;
    protected boolean isVisible = true;
    public UIElement(){}
    public UIElement(JSONObject json)
    {
        try {
            this.x=json.getInt("x");
            this.y=json.getInt("y");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public abstract void Render(AndroidGraphics gr);
    public void setX(float x){this.x=x;}
    public void setY(float y){this.y=y;}
    public float getX(){return this.x;}
    public float getY(){return this.y;}
    public void setVisible(boolean visible){this.isVisible = visible;}
}
