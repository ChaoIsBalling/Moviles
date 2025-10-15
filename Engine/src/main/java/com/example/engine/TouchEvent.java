package com.example.engine;

public class TouchEvent {

    //Enumerado que representa los eventos
    public static enum TouchEventType{
        TOUCH_DOWN,
        TOUCH_UP,
        TOUCH_MOVE
    }
    //El evento que registraremos en el juego
    public TouchEventType type;

    //Coordenadas de la pantalla del evento
    public float x;
    public float y;

    //Identificador
    public int finger;
}
