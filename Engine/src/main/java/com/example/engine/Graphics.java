package com.example.engine;

import javax.imageio.IIOImage;

public interface Graphics {
    public void pintarCirculo(float x, float y, float r);

    public void pintarCuadrado(float x, float y, float w, float h);

    public void pintarFondo(int color);


    public void pintarTexto(String texto, float x, float y);

    public void pintarImagen(Image img, float x, float y);

    public void setColor(int color);
    public void setFont(Font font);
}
