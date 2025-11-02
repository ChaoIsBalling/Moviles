package com.example.gamelogic;

//import java.awt.Font;
//import java.awt.Graphics;

import com.example.engine.Graphics;
import com.example.engine.IFont;
public class Button {

    //Atributos del botón
    private float x;
    private float y;
    private float w;
    private float h;

    private boolean isRound = false;

    private float arcRadius;

    Text text;

    int color = 0xFF000000;
    Image imagen;
    Figure figura;

    public Button(float x, float y, float w, float h, boolean isRound, float ar){
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.isRound = isRound;
        this.arcRadius = ar;
    }
    public Button(float x, float y, float w, float h){
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public void setText(Text text) {
        this.text = text;
    }

    public void setColor(int color){
        this.color = color;
    }

    public boolean contains(float x, float y){
        return x >= this.x-this.w/2 && x <= this.x + this.w/2 &&
                y >= this.y-this.h/2 && y <= this.y + this.h/2;
    }

    public void setFigura(Figure fig){
        this.figura = fig;
    }

    public void setImagen(Image img){this.imagen = img;}

    public void Render(Graphics gr) {
        //Renderizamos el cuadrado que representa el botón
        gr.setColor(color);

        if(isRound)
            gr.rellenarCuadradoRedondeado(this.x,this.y,this.w,this.h,this.arcRadius);
        else
            gr.rellenarCuadrado(this.x,this.y,this.w,this.h);

        if(this.imagen != null){
            this.imagen.RenderCentrado((int)this.x,(int)this.y);
        }

        if(this.text != null){
            this.text.RenderCentrado(gr,this.x,this.y);
        }

        //Renderizamos figura centrada
        if(this.figura != null){
            this.figura.RenderCentrado(gr,this.x,this.y);
        }
    }

}
