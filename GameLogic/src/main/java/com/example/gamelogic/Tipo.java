package com.example.gamelogic;

import java.util.Random;

/**
 * Enumerado que representa un tipo de torre al que es resistente un enemigo
 */
public enum Tipo {
    rayo,fuego,hielo;
    /**
     * Metodo que devuelve un tipo random a la hora de inicializar el tipo al que es resistente un enemigo
     */
    public static Tipo getRandomType() {
        Random random = new Random();
        return values()[random.nextInt(values().length)];
    }
}
