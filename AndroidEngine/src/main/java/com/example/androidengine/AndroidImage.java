package com.example.androidengine;

import android.graphics.Bitmap;

import com.example.engine.Image;

public class AndroidImage implements Image {

    private Bitmap bm;

    AndroidImage(Bitmap bm) {this.bm = bm;}

    protected Bitmap getBitmap() {return bm;}
}
