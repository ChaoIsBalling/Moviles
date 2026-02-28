package com.example.androidengine;

import android.content.res.AssetManager;
import android.graphics.Typeface;

/**
 * Clase que se encarga de la gestión de fuentes para texto en Android
 */
public class AndroidFont {
    private Typeface tFont;
    boolean bold;
    private float size;

    /**
     * Contructora que inicializa una font con los valores de los parámetros de la constructora
     * @param as assetManager de Android
     * @param file Nombre de la fuente
     * @param size Tamaño de la fuente
     * @param bold Determina si esta en negrita
     * @param italic Determina si esta en italica
     */
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

    /**
     * getter de la font
     * @return font de texto actual
     */
    public Typeface getTypeface()
    {
        return this.tFont;
    }

    /**
     * getter del tamaño de la font
     * @return tamaño de la font actual
     */

    public int getSize() {
        return (int)size;
    }

    /**
     * getter de si esta en negrita o no
     * @return booleano bold
     */
    public boolean isBold() {
        return this.bold;
    }


}
