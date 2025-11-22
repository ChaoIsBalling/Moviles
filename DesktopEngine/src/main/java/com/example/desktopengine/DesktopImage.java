package com.example.desktopengine;

import com.example.engine.IImage;
import java.awt.Image;
import java.awt.image.ImageObserver;

/**
 * Clase que implementa la Interfaz Image de Engine.
 * Se encarga de guardar la imagen y sus atributos
 */
public class DesktopImage  implements IImage{
    /**
     * Guarda la imagen real (propia de Java)
     */
    private Image img;
    /**
     * Observer de la imagen para recibir actualizaciones de ella (no usado)
     */
    private ImageObserver observer;

    /**
     * Constructor de la imagen para escritorio
     * @param im Imagen de Java
     * @param width ancho de imagen
     * @param height alto de imagen
     */
    DesktopImage(Image im,int width, int height){

        this.img = im.getScaledInstance(width, height, Image.SCALE_DEFAULT);

    }
    DesktopImage(Image im){

        this.img = im;

    }

    /**
     * Devuelve la imagen de Java
     * @return Image
     */
    public Image getCurrentImage(){
        return this.img;
    }

    /**
     * Devuelven ancho y alto de la imagen
     * @return ancho, alto
     */
    @Override
    public int getWidth() {
        return this.img.getWidth(null);
    }

    @Override
    public int getHeight() {
        return this.img.getHeight(null);
    }
}
