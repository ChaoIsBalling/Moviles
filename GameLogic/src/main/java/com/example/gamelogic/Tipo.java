package com.example.gamelogic;

import java.util.Random;
public enum Tipo {
    rayo,fuego,hielo;

    //Devuelve tipo random
    public static Tipo getRandomType() {
        Random random = new Random();
        return values()[random.nextInt(values().length)];
    }
}
