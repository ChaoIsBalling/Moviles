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
        InputStream is = new FileInputStream(fileFont);
        Font baseFont = Font.createFont(Font.TRUETYPE_FONT, is);

        // Ponemos un determinado estilo y tamaño
        this.awtFont = baseFont.deriveFont(Font.BOLD, 40);
    }

    // Método para obtener el Font propio de Java
    public Font getCurrentFont() {
        return this.awtFont;
    }
}
