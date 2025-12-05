package com.example.androidengine;

import android.graphics.Bitmap;

import com.example.engine.IImage;

public class AndroidImage implements IImage {

    private Bitmap bm;
    //constructora que inicializa el bitmap
    AndroidImage(Bitmap bm) {this.bm = bm;}
    //constructora que inicialia el bitmap con un ancho y alto
    AndroidImage(Bitmap bm,int width, int height) {
        this.bm =  Bitmap.createScaledBitmap(bm, width, height, false);
    }
    //getter del bitmap
    protected Bitmap getBitmap() {return bm;}

    //getter del alto
    @Override
    public int getHeight() {
        return this.bm.getHeight();
    }
    //getter del anho
    @Override
    public int getWidth() {
        return this.bm.getWidth();
    }
}
