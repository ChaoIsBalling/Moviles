package com.example.engine;

public interface Graphics {
    public int getWidth();

    public void pintarCirculo(float x, float y, float r);

    public void pintarCuadrado(float x, float y, float w, float h);

    public void pintarFondo(int color);


    public void pintarTexto(String texto, float x, float y);

    public void pintarImagen(IImage img, int x, int y);

    public void setColor(int color);

    public IFont newFont(String f);

    public IImage newImage(String path);

    public void setFont();

    public void escalar(float x, float y);

    public void trasladar(float x,float y);

    public void setLogicSize(float w, float h);

    public void rellenarCirculo(float x, float y, float r);

    public float real2LogicX(float x);

    public float real2LogicY(float y);
}
