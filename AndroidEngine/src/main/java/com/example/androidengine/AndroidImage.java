package com.example.androidengine;

import android.graphics.Bitmap;

import com.example.engine.IImage;

public class AndroidImage implements IImage {

    private Bitmap bm;

    AndroidImage(Bitmap bm) {this.bm = bm;}

    protected Bitmap getBitmap() {return bm;}


    @Override
    public int getHeight() {
        return 0;
    }

    @Override
    public int getWidth() {
        return 0;
    }
}
