package com.example.engine;

public interface Engine {
    /**
     * Inicia el motor
     */
    public void resume();

    /**
     * pausa el motor
     */
    public void pause();

    /**
     * Indicar estado de juego que debe renderizarse/ejecutarse
     *
     * @param state
     */
    public void setState(State state);

    public Audio getAudio();

    /**
     *
     */
    public State getState();
    public Graphics getGraphics();
}
