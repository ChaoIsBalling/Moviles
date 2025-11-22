package com.example.engine;

import java.util.ArrayList;

/**
 * Interfaz Engine que heredaran los motores de Desktop y Android
 */
public interface Engine {
    /**
     * Este metodo inicia el motor
     */
    public void resume();

    /**
     * Este metodo pausa el motor
     */
    public void pause();

    /**
     * Este metodo indica el estado de juego que debe renderizarse/ejecutarse
     * @param state Estado al que queremos transicionar
     */
    public void setState(State state);

    /**
     * Este metodo lee un archivo .txt, en este caso lo usamos para la lectura de mapa
     * @param path Nombre del archivo
     * @return Array de strings con la lectura de mapa hecha
     */
    public ArrayList<String> readFile(String path);

    /**
     * Este metodo devuelve el gestor de Audio del motor
     * @return gestor Audio
     */
    public Audio getAudio();

    /**
     * Este metodo devuelve el estado actual del motor
     * @return Estado actual
     */
    public State getState();

    /**
     * Este metodo devuelve el gestor de Graficos del motor
     * @return gestor Graficos
     */
    public Graphics getGraphics();
}
