package com.example.gamelogic.towers;

import com.example.gamelogic.Image;
import com.example.gamelogic.figure.Figure;

/**
 * Interfaz para añadir un callback a la factoria de torres
 */
public interface TowerCreator {
    Tower create(float x, float y, Image skin, Figure figure);
}
