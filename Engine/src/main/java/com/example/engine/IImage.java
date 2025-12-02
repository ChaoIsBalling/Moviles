package com.example.engine;

/**
 * Interfaz de las que heredan todas las imagenes dentro del motor
 */
public interface IImage {
    /**
     * Devuelve el ancho de la imagen
     * @return ancho
     */
    public int getWidth();

    /**
     * Devuelve el alto de la imagen
     * @return alto
     */
    public int getHeight();
}
