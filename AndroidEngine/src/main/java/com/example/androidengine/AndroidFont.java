package com.example.androidengine;

import android.content.res.AssetManager;
import android.graphics.Typeface;
import com.example.engine.IFont;

public class AndroidFont implements IFont {
    private Typeface tFont;
    private float size;

    public AndroidFont(AssetManager as,String file, float size, boolean bold, boolean italic)
    {
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

    public Typeface getTypeface()
    {
        return this.tFont;
    }

    @Override
    public int getSize() {
        return (int)size;
    }
    public Typeface getFont()
{
    return tFont;
}
    @Override
    public boolean isBold() {
        return false;
    }


}
