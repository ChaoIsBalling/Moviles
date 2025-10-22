package com.example.engine;

import com.example.engine.Graphics;

//Interfaz para la figura
public interface Figure {
    float getX();

    float getY();

    int getColor();

    void setColor(int color);

    void Render(Graphics gr);

    void RenderCentrado(Graphics gr, float x, float y);
}
