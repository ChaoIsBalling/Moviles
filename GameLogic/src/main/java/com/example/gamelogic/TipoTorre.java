package com.example.gamelogic;

import java.util.Random;

/**
 * Enumerado que representa un tipo de torre al que es resistente un enemigo
 */
public enum TipoTorre {
    RAYO, FUEGO, HIELO, MINI;
    /**
     * Metodo que devuelve un tipo random a la hora de inicializar el tipo al que es resistente un enemigo
     */
    public static TipoTorre getRandomType() {
        Random random = new Random();
        return values()[random.nextInt(values().length)];
    }

    /**
     * Metodo que devuelve el indice del tipo en el enumerado -> rango[0, numTipos - 1]
     * @return indice
     */
    public int getIndice(){
        return this.ordinal();
    }
}
