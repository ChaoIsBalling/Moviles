package com.example.gamelogic;

//import java.awt.Font;
//import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;

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
    class Texto{
        int x;
        int y;
        int w;
        int h;
        String font;
        String texto;

        void Render(Graphics gr){

        }

    }
    interface Figura{
        int x = 0;
        int y =0;
        int color =0;
        void Render(Graphics gr);

    }
    class Rectangulo implements Figura{
        int w;
        int h;
        @Override
        public void Render(Graphics gr) {

        }
    }
    class Triangulo implements Figura{
        int x2;
        int y2;
        int x3;
        int y3;
        @Override
        public void Render(Graphics gr) {

        }
    }
    class Pentagono implements Figura{
        int x2;
        int y2;
        int x3;
        int y3;
        int x4;
        int y4;
        int x5;
        int y5;
        @Override
        public void Render(Graphics gr) {

        }
    }

    private float x;
    private float y;
    private float w;
    private float h;

    String text = "";
    private IFont f;

    int color = 0x00000000;
    Imagen imagen;
    Texto texto;
    Figura figuras;



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

    public void Render(Graphics gr) {
        gr.setColor(color);
        gr.pintarCuadrado(this.x,this.y,this.w,this.h);
        gr.setColor(color);
        gr.setFont(this.f);
        gr.pintarTextoCentrado(this.text,this.x + this.w/2,this.y + this.h/2);

    }

}
