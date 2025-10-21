package com.example.desktopengine;

import com.example.engine.IFont;

import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.Font;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class DesktopFont implements IFont {

    private Font awtFont;

    // Constructor que crea la fuente
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

    // Método para obtener el Font propio de Java
    public Font getCurrentFont() {
        return this.awtFont;
    }

    @Override
    public int getSize() {
        return this.awtFont.getSize();
    }

    @Override
    public boolean isBold() {
        return this.awtFont.isBold();
    }
}
