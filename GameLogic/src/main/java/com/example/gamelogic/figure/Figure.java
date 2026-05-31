package com.example.gamelogic.figure;
import com.example.androidengine.AndroidGraphics;
/**
 * Interfaz para cada uno de todos los tipos de figuras
 */
public interface Figure {
    /**
     * Devuelve la coordenada x
     * @return x
     */
    float getX();
    /**
     * Devuelve la coordenada y
     * @return x
     */
    float getY();

    /**
     * Devuelve el color de la figura
     * @return color
     */
    String getColor();

    /**
     * Da valor a la coordenada x
     * @param x coordenada x
     */
    void setX(float x);
    /**
     * Da valor a la coordenada y
     * @param y coordenada x
     */
    void setY(float y);

    /**
     * Da un color a la figura
     * @param color color deseado
     */
    void setColor(String color);

    /**
     * Lo vuelve invisble o visble segun el parametro
     * @param c booleano
     */
    void setVisible(boolean c);

    /**
     * Renderiza la figura
     * @param gr Interfaz Graphics
     */
    void Render(AndroidGraphics gr);

    /**
     * Renderiza la figura centrada desde una posicion x,y
     */
    void RenderCentrado(AndroidGraphics gr, float x, float y);
    /**
     * Renderiza la figura en la posicion x,y
     */
    void RenderAtPosition(AndroidGraphics gr, float x, float y);
}
