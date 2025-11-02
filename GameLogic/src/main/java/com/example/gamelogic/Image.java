package com.example.gamelogic;

import com.example.engine.Graphics;
import com.example.engine.IImage;

import java.awt.Graphics2D;

public class Image {
    int x;
    int y;
    float w;
    float h;
    String imagen;
    IImage im;
    Graphics gr;

    public Image(String imagen, int x, int y, Graphics gr){
        this.imagen = imagen;
        this.x = x;
        this.y = y;
        this.gr = gr;
        this.im = this.gr.newImage(this.imagen);
    }

    public void Render(){

        this.gr.pintarImagen(this.im,this.x,this.y);
    }

    public void RenderCentrado(int x, int y){
        this.gr.pintarImagen(this.im,x+this.x,y+this.y);
    }
}
