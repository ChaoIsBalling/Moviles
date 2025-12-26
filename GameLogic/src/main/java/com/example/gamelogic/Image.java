package com.example.gamelogic;

import com.example.engine.IImage;
import com.example.androidengine.AndroidGraphics;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Clase que representa la clase Imagen
 */
public class Image {
    //Posicion y dimensiones
    int x;
    int y;
    //Dimensiones
    int w;
    int h;
    //Path de la imagen
    String imagen;
    //Interfaz del motor
    IImage im;
    //Graphics del motor
    AndroidGraphics gr;

    /**
     * Constructora de la imagen, con sus coordenadas, dimensiones y referencia al graphics del motor
     */
    public Image(String imagen, int x, int y,int w,int h, AndroidGraphics gr){
        this.imagen = imagen;
        this.x = x;
        this.y = y;
        this.w= w;
        this.h= h;
        this.gr = gr;
        this.im = this.gr.newImage(imagen,this.w,this.h);
    }
    public Image(String imagen, int x, int y, AndroidGraphics gr){
        this.imagen = imagen;
        this.x = x;
        this.y = y;
        this.gr = gr;
        this.im = this.gr.newImage(this.imagen);
    }
    public Image(JSONObject json,AndroidGraphics gr)
    {
        try{
        this.imagen= json.getString("imagen");
        this.x = json.getInt("x");
        this.y= json.getInt("y");
        this.w= json.getInt("w");
        this.h=json.getInt("h");
    } catch (
    JSONException e) {
        throw new RuntimeException(e);
    }
        this.gr=gr;
        this.im = this.gr.newImage(this.imagen,this.w,this.h);
    }

    public void setX(int x){
        this.x = x;
    }
    public void setY(int y){
        this.y = y;
    }
    public void setW(int w){
        this.w = w;
    }
    public void setH(int h){
        this.h = h;
    }

    /**
     * Renderiza la imagen con la referencia a Graphics
     */
    public void Render(){
        this.gr.pintarImagen(this.im,this.x,this.y);
    }

    public void RenderEscalado(){
        this.gr.pintarImagenEscalada(this.im,this.x,this.y,this.w,this.h);
    }
    /**
     * Reneriza la imagen de forma centrada con la referencia a Graphics
     */
    public void RenderCentrado(int x, int y){
        this.gr.pintarImagen(this.im,x+this.x,y+this.y);
    }
}
