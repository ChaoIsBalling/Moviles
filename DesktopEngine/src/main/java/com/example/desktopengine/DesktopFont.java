package com.example.desktopengine;

import com.example.engine.IFont;

import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.Font;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Clase que implementa la interfaz IFont de Engine
 * Se encarga de la lectura de nuevas fuentes Font.
 */
public class DesktopFont implements IFont {

    /**
     * Font propia de Java
     */
    private Font awtFont;

    /**
     * Varias constructoras que inicializan un Font de escritorio a partir de varios parametros.
     * Se lanzarán excepciones en caso de que ocurra algún error
     * @param fileFont Nombre del archivo que contiene el font
     */
    public DesktopFont(String fileFont) throws FileNotFoundException, FontFormatException, IOException {

        //Leemos el archivo fileFont y creamos la fuente
        FileInputStream is = new FileInputStream(fileFont);
        Font baseFont = Font.createFont(Font.TRUETYPE_FONT, is);

        // Ponemos un determinado estilo y tamaño
        this.awtFont = baseFont.deriveFont(Font.BOLD, 40);
    }
    public DesktopFont(String fileFont, float size) throws FileNotFoundException, FontFormatException, IOException {

        //Leemos el archivo fileFont y creamos la fuente
        FileInputStream is = new FileInputStream(fileFont);
        Font baseFont = Font.createFont(Font.TRUETYPE_FONT, is);

        // Ponemos un determinado estilo y tamaño
        this.awtFont = baseFont.deriveFont(Font.TRUETYPE_FONT, size);
    }
    public DesktopFont(String fileFont, float size, boolean bold) throws FileNotFoundException, FontFormatException, IOException {

        //Leemos el archivo fileFont y creamos la fuente
        FileInputStream is = new FileInputStream(fileFont);
        Font baseFont = Font.createFont(Font.TRUETYPE_FONT, is);

        // Ponemos un determinado estilo y tamaño
        if(bold){
            this.awtFont = baseFont.deriveFont(Font.BOLD, size);
        }
    }
    public DesktopFont(String fileFont, float size, boolean bold, boolean italic) throws FileNotFoundException, FontFormatException, IOException {

        //Leemos el archivo fileFont y creamos la fuente
        FileInputStream is = new FileInputStream(fileFont);
        Font baseFont = Font.createFont(Font.TRUETYPE_FONT, is);
        int style =Font.TRUETYPE_FONT;
        // Ponemos un determinado estilo y tamaño
        if(bold){
            style |= Font.BOLD;
        }
        if(italic){
            style |= Font.ITALIC;
        }
        this.awtFont = baseFont.deriveFont(style, size);
    }

    /**
     * Metodo para obtener el Font propio de Java
     * @return Font
     */

    public Font getCurrentFont() {
        return this.awtFont;
    }

    /**
     * Metodo para obtener el tamaño del font (el de Java)
     * @return
     */
    @Override
    public int getSize() {
        return this.awtFont.getSize();
    }

    /**
     * Devuelve si el font es en negrita o no (el de Java)
     * @return booleano que indica si es negrita o no
     */
    @Override
    public boolean isBold() {
        return this.awtFont.isBold();
    }
}
