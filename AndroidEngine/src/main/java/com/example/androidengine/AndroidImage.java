package com.example.androidengine;

import android.graphics.Bitmap;

/**
 * Metodo que implementa la imagen de Android en el motor
 */
public class AndroidImage {
    private Bitmap bm;

    /**
     * Constructora que inicializa el bitmap que contiene la imagen
     * @param bm bitMap de la imagen
     */
    AndroidImage(Bitmap bm) {this.bm = bm;}

    /**
     * Constructora que inicializa el bitmap con un ancho y alto
     * @param bm bitmap de la imagen
     * @param width ancho de la imagen
     * @param height alto de la imagen
     */
    AndroidImage(Bitmap bm,int width, int height) {
        this.bm =  Bitmap.createScaledBitmap(bm, width, height, false);
    }

    /**
     * Getter del bitmap de la imagen
     * @return bitmap de la imagen
     */
    protected Bitmap getBitmap() {return bm;}

    /**
     * getter del alto de la imagen
     * @return alto del bitmap que almacena la imagen
     */
    public int getHeight() {
        return this.bm.getHeight();
    }

    /**
     * getter del ancho de la imagen
     * @return ancho del bitmap que almacena la imagen
     */
    public int getWidth() {
        return this.bm.getWidth();
    }
}
