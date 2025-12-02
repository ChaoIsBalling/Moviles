package com.example.gamelogic;

//import java.awt.Font;
//import java.awt.Graphics;

import com.example.engine.Graphics;
import com.example.engine.IFont;

/**
 * Clase que representa un boton en la interfaz del juego
 */
public class Button {

    //Atributos del botón
    private float x;
    private float y;
    private float w;
    private float h;

    //Determina si tiene esquinas redondeadas
    private boolean isRound = false;
    //Radio del arco bordeado
    private float arcRadius;

    //Texto del boton
    Text text;

    int color = 0xFF000000; //Color por defecto
    Image imagen; //Imagen
    Figure figura; //Figura del botón

    /**
     * Constructora del botón que inicializa su posición, dimensiones, y si es redondeado o no
     */
    public Button(float x, float y, float w, float h, boolean isRound, float ar){
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.isRound = isRound;
        this.arcRadius = ar;
    }
    public Button(float x, float y, float w, float h){
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    /**
     * Setters de atributos del botón
     */
    public void setText(Text text) {
        this.text = text;
    }

    public void setColor(int color){
        this.color = color;
    }
    public void setFigura(Figure fig){
        this.figura = fig;
    }

    public void setImagen(Image img){this.imagen = img;}

    /**
     * Comprueba si la coordenada x,y está dentro del botón
     */
    public boolean contains(float x, float y){
        return x >= this.x-this.w/2 && x <= this.x + this.w/2 &&
                y >= this.y-this.h/2 && y <= this.y + this.h/2;
    }


    /**
     * Metodo que renderiza el boton
     * @param gr
     */
    public void Render(Graphics gr) {
        //Renderizamos el cuadrado que representa el botón
        gr.setColor(color);

        //Vemos si es redondeado o no
        if(isRound)
            gr.rellenarCuadradoRedondeado(this.x,this.y,this.w,this.h,this.arcRadius);
        else
            gr.rellenarCuadrado(this.x,this.y,this.w,this.h);

        //Renderizamos imagen si la tiene
        if(this.imagen != null){
            this.imagen.RenderCentrado((int)this.x,(int)this.y);
        }

        //Renderizamos figura centrada
        if(this.figura != null){
            this.figura.RenderCentrado(gr,this.x,this.y);
        }

        //Renderizamos texto centrado
        if(this.text != null){
            this.text.RenderCentrado(gr,this.x,this.y);
        }
    }

}
