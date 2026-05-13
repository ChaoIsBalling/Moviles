package com.example.gamelogic.towers;

import com.example.gamelogic.Image;

/**
 * Interfaz para añadir un callback a la factoria de torres
 */
public interface TowerCreator {
    Tower create(float x, float y, Image skin);
}
