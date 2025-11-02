package com.example.gamelogic;

import com.example.engine.Graphics;
import com.example.engine.IImage;

public class Image {
    int x;
    int y;
    float w;
    float h;
    String imagen;

    public Image(String imagen, int x, int y){
        this.imagen = imagen;
        this.x = x;
        this.y = y;
    }

    public void Render(Graphics gr){
        IImage iImage = gr.newImage(this.imagen);
        gr.pintarImagen(iImage,this.x,this.y);
    }

    public void RenderCentrado(Graphics gr,int x, int y){
        IImage iImage = gr.newImage(this.imagen);
        gr.pintarImagen(iImage,x+this.x,y+this.y);
    }
}
