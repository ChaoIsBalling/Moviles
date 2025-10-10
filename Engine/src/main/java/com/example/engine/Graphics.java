package com.example.engine;

import com.example.engine.IFont;

import java.awt.FontFormatException;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface Graphics {
    public int getWidth();

    public void pintarCirculo(float x, float y, float r);

    public void pintarCuadrado(float x, float y, float w, float h);

    public void pintarFondo(int color);


    public void pintarTexto(String texto, float x, float y);

    public void pintarImagen(Image img, float x, float y);

    public void setColor(int color);

    public IFont newFont(String f);

    public void setFont();

    public void escalar(float x, float y);

    public void trasladar(float x,float y);

    public void setLogicSize(float w, float h);

    public void rellenarCirculo(float x, float y, float r);
}
