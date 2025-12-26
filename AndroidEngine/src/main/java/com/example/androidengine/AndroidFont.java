package com.example.androidengine;

import android.content.res.AssetManager;
import android.graphics.Typeface;

public class AndroidFont {
    private Typeface tFont;

    boolean bold;
    private float size;
    //contructora que inicializa una font con tamaño, bold e italics
    public AndroidFont(AssetManager as,String file, float size, boolean bold, boolean italic)
    {
        this.bold=bold;
        Typeface tmp=Typeface.createFromAsset(as,file);
        this.size=size;
        int style =tmp.getStyle();
        // Ponemos un determinado estilo y tamaño
        if(bold){
            style |= Typeface.BOLD;
        }
        if(italic){
            style |= Typeface.ITALIC;
        }
        this.tFont =Typeface.create(tmp,style);
    }
    //getter de la font
    public Typeface getTypeface()
    {
        return this.tFont;
    }
    //getter del tamaño

    public int getSize() {
        return (int)size;
    }
    //getter del bold

    public boolean isBold() {
        return this.bold;
    }


}
