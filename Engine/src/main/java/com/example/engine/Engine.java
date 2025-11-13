package com.example.engine;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import org.json.JSONObject;

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

    public JSONObject readJsonFile(String file);
    public String openAssetFile(String file);
    public OutputStream writeFile(String file);

    public void SaveValueInt(String key, int value);
    public int LoadValueInt(String key);
}
