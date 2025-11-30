package com.example.gamelogic;

import com.example.engine.Graphics;

/**
 * Interfaz para cada uno de todos los tipos de figuras
 */
public interface Figure {
    float getX();

    float getY();

    void setX(float x);

    void setY(float y);

    int getColor();

    void setColor(int color);

    void Render(Graphics gr);

    void RenderCentrado(Graphics gr, float x, float y);
}
