package com.example.engine;

/**
 * Interfaz de la que heredan las Font propias de cada motor
 */
public interface IFont {
    /**
     * Devuelve el tamaño del font
     * @return tamaño
     */
    public int getSize();

    /**
     * Devuelve si el texto es en negrita o no
     * @return booleano que indica si es negrita o no
     */
    public boolean isBold();

}
