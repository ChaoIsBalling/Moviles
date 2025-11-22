package com.example.androidengine;

import android.graphics.Bitmap;

import com.example.engine.IImage;

public class AndroidImage implements IImage {

    private Bitmap bm;

    AndroidImage(Bitmap bm) {this.bm = bm;}

    AndroidImage(Bitmap bm,int width, int height) {
        this.bm =  Bitmap.createScaledBitmap(bm, width, height, false);
    }

    protected Bitmap getBitmap() {return bm;}


    @Override
    public int getHeight() {
        return this.bm.getHeight();
    }

    @Override
    public int getWidth() {
        return this.bm.getWidth();
    }
}
