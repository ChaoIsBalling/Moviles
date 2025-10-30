package com.example.engine;

import java.awt.Font;

public interface Graphics {
    public int getWidth();

    public void pintarFondo(int color);

    public void pintarTexto(String texto, float x, float y);

    public void pintarImagen(IImage img, int x, int y);

    public void pintarTextoCentrado(String texto, float x, float y);

    public void setColor(int color);

    public IFont newFont(String f);

    public IFont newFont(String f, float size);

    public IFont newFont(String f, float size, boolean bold);

    public IFont newFont(String f, float size, boolean bold, boolean italic);

    public IImage newImage(String path);

    public void setFont(IFont font);

    public void escalar(float x, float y);

    public void trasladar(float x,float y);

    public void setLogicSize(float w, float h);
    public void pintarCirculo(float x, float y, float r);

    public void pintarCuadrado(float x, float y, float w, float h);

    public void pintarPoligono(float cx, float cy, float r, int nv);

    public void pintarLinea(float x1, float y1, float x2, float y2, float width);

    public void rellenarCirculo(float x, float y, float r);

    public void rellenarCuadrado(float x, float y, float w, float h);

    public void rellenarCuadradoRedondeado(float x, float y, float w, float h, float ar);

    public void rellenarPoligono(float cx, float cy, float r, int nv);

    public float real2LogicX(float x);

    public float real2LogicY(float y);
}
