package com.example.gamelogic.towers;

import com.example.gamelogic.VisualElements.Image;
import com.example.gamelogic.VisualElements.figure.Figure;

/**
 * Interfaz para añadir un callback a la factoria de torres
 */
public interface TowerCreator {
    Tower create(float x, float y, Image skin, Figure figure);
}
