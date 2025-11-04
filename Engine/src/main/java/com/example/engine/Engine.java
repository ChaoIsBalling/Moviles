package com.example.engine;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

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

    public ArrayList<String> readFile(String path);

    public Audio getAudio();

    /**
     *
     */
    public State getState();
    public Graphics getGraphics();

    public InputStream readFile2(String file);
    public String openAssetFile(String file);
    public OutputStream writeFile(String file);
}
