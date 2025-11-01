package com.example.gamelogic;

import com.example.engine.Graphics;
import com.example.engine.IFont;

public class Text {
    String text;
    String font;
    float x;
    float y;
    float size;
    boolean bold;
    boolean italic;
    int color = 0xFF000000;
    public Text(String font, String text, float x, float y){
        this.font=font;
        this.text=text;
        this.x=x;
        this.y=y;
        this.size=12;
        this.bold=false;
        this.italic=false;
    }
    public Text(String font, String text, float x, float y,float size){
        this.font=font;
        this.text=text;
        this.x=x;
        this.y=y;
        this.size=size;
        this.bold=false;
        this.italic=false;
    }
    public Text(String font, String text, float x, float y,float size,boolean bold){
        this.font=font;
        this.text=text;
        this.x=x;
        this.y=y;
        this.size=size;
        this.bold=bold;
        this.italic=false;
    }
    public Text(String font, String text, float x, float y,float size,boolean bold, boolean italic){
        this.font=font;
        this.text=text;
        this.x=x;
        this.y=y;
        this.size=size;
        this.bold=bold;
        this.italic=italic;
    }

    public void setColor(int color){
        this.color = color;
    }

    public void Render(Graphics gr){
        IFont fuente = gr.newFont(this.font,this.size,this.bold,this.italic);
        gr.setColor(this.color);
        gr.setFont(fuente);
        gr.pintarTextoCentrado(this.text, this.x,this.y);
    }
    public void RenderCentrado(Graphics gr,float x, float y){
        IFont fuente = gr.newFont(this.font,this.size,this.bold,this.italic);
        gr.setColor(this.color);
        gr.setFont(fuente);
        gr.pintarTextoCentrado(this.text, x+this.x,y+this.y);
    }

    public void setText(String text){
        this.text = text;
    }
}
