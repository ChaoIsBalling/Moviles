package com.example.gamelogic.VisualElements;
import com.example.androidengine.AndroidImage;
import com.example.androidengine.AndroidGraphics;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Clase que representa la clase Imagen
 */
public class Image extends VisualElement {
    //Dimensiones
    int w;
    int h;
    //Path de la imagen
    String imagen;

    boolean escalado =true;

    //Interfaz del motor
    AndroidImage im;
    //Graphics del motor
    AndroidGraphics gr;

    /**
     * Constructora de la imagen, con sus coordenadas, dimensiones y referencia al graphics del motor
     */

    public Image(JSONObject json,AndroidGraphics gr)
    {
        super(json);
        try{
        this.imagen= json.getString("imagen");
        this.w= json.getInt("w");
        this.h=json.getInt("h");

        if(json.has("visible"))
            this.isVisible = json.getBoolean("visible");
        else this.isVisible = true;
    } catch (
    JSONException e) {
        throw new RuntimeException(e);
    }
        this.gr=gr;
        this.im = this.gr.newImage(this.imagen,this.w,this.h);
    }

    public void setW(int w){
        this.w = w;
    }
    public void setH(int h){
        this.h = h;
    }

    public void setEscalado(boolean c){this.escalado=c;}

    /**
     * Renderiza la imagen con la referencia a Graphics
     */
    @Override
    public void Render(AndroidGraphics gr){
        if(this.isVisible) {
            if(this.escalado)
                gr.pintarImagenEscalada(this.im,(int)this.x,(int)this.y,this.w,this.h);
            else
                gr.pintarImagen(this.im, (int)this.x, (int)this.y);
        }
    }

    /**
     * Reneriza la imagen de forma centrada con la referencia a Graphics
     */
    public void RenderCentrado(int x, int y){
        if(this.isVisible)
            this.gr.pintarImagen(this.im,x+(int)this.x,y+(int)this.y);
    }
}
