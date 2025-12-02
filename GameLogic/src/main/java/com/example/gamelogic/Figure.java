package com.example.gamelogic;

import com.example.engine.Graphics;

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
    int getColor();

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
    void setColor(int color);

    /**
     * Renderiza la figura
     * @param gr Interfaz Graphics
     */
    void Render(Graphics gr);

    /**
     * Renderiza la figura centrada desde una posicion x,y
     */
    void RenderCentrado(Graphics gr, float x, float y);
}
