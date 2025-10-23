package com.example.gamelogic;

//import java.awt.Font;
//import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;

import com.example.engine.Figure;
import com.example.engine.Graphics;
import com.example.engine.IFont;
public class Button {
    class Imagen{
        int x;
        int y;
        int w;
        int h;
        String image;
        void Render(Graphics gr){

        }
    }

    //Atributos del botón
    private float x;
    private float y;
    private float w;
    private float h;

    String text = "";
    private IFont f;

    int color = 0x00000000;
    Imagen imagen;
    Figure figura;

    public Button(float x, float y, float w, float h){
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public void setText(Graphics gr, String file, String text, float size) {
        this.f = gr.newFont(file,size, true);
        this.text = text;
    }

    public boolean contains(float x, float y){
        return x >= this.x && x <= this.x + this.w &&
                y >= this.y && y <= this.y + this.h;
    }

    public void setFigura(Figure fig){
        this.figura = fig;
    }
    public void Render(Graphics gr) {
        //Renderizamos el cuadrado que representa el botón
        gr.setColor(color);
        gr.pintarCuadrado(this.x,this.y,this.w,this.h);
        gr.setColor(color);
        gr.setFont(this.f);
        gr.pintarTextoCentrado(this.text,this.x + this.w/2,this.y + this.h/2);

        //Renderizamos figura centrada
        if(this.figura != null){
            this.figura.RenderCentrado(gr,this.x + this.w/2 + this.figura.getX(),
                    this.y + this.h/2 + this.figura.getY());
        }
    }

}
