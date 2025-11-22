package com.example.engine;

import java.util.List;


/**
 * Interfaz que define las entradas propias de cada motor
 */
public interface Input {
    /**
     * Devuelve la lista de TouchEvents
     * @return lista
     */
    public List<TouchEvent> getTouchEvents();
}
