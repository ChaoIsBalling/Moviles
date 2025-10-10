package com.example.androidengine;

import android.graphics.Typeface;
import com.example.engine.IFont;

public class AndroidFont implements IFont {
    private Typeface tFont;

    private float size;

    /*public AndroidFont(AssetManager assets,String file, float size){
        this(assets,file,size,false,false);
    }

    public AndroidFont(AssetManager assets, String file, float size, boolean bold){
        this.tFont = Typeface.createFromAsset(assets,file);
        this.size = size;
    }*/
}
