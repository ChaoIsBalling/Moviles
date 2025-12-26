package com.example.gamelogic;
import com.example.androidengine.AndroidGraphics;
/**
 * Clase que representa un Hexágono
 */
public class Hexagon implements Figure{


    //Centro de la figura
    private float cx;
    private float cy;

    //radio desde el centro
    private float r;

    //Color del hexágono
    private String color;

    //Indica si es relleno o no
    private boolean isFill;

    /**
     * Constructora de la clase Hexagono con su coordenada x,y, su radio y si esta relleno o no
     */
    public Hexagon(float x, float y, float r, boolean isFill){
        this.cx = x;
        this.cy = y;
        this.r = r;
        this.isFill = isFill;
    }
    Hexagon(float x, float y, float r){
        this.cx = x;
        this.cy = y;
        this.r = r;
    }

    /**
     * Getters
     */
    @Override
    public float getX() {
        return this.cx;
    }
    @Override
    public float getY() {
        return this.cy;
    }
    @Override
    public String getColor() {
        return this.color;
    }

    /**
     * setters
     */
    @Override
    public void setColor(String color) {
        this.color= color;
    }

    @Override
    public void setX(float x) {
        this.cx = x;
    }

    @Override
    public void setY(float y) {
        this.cy = y;
    }

    /**
     * Renderiza el circulo con o sin relleno
     * @param gr Interfaz Graphics
     */
    @Override
    public void Render(AndroidGraphics gr) {
        gr.setColor(this.color);
        if(isFill){
            gr.rellenarHexagono(this.cx, this.cy,this.r);
        }
        else {
            gr.rellenarHexagono(this.cx, this.cy,this.r);
        }
    }

    /**
     * Renderiza de forma centrada el círculo a partir de una posicion x,y
     */
    @Override
    public void RenderCentrado(AndroidGraphics gr, float x, float y) {
        gr.setColor(this.color);
        if(isFill){
            gr.rellenarHexagono(x+this.cx, y+ this.cy,this.r);
        }
        else {
            gr.rellenarHexagono(x + this.cx, y+ this.cy,this.r);
        }
    }
}
