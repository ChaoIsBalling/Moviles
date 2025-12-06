package com.example.engine;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.json.JSONObject;

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

    /**
     * Este metodo lanza un Intent implicito simple
     */
    public void launchIntent(String application);
    /**
     * Este metodo lanza un Intent implicito pero con mas parametros a la hora de crearlo
     */
    public void launchIntent(String application, String text, String parameter);
    /**
     * Este metodo lanza un Intent implicito de tipo share
     */
    public void luanchShareIntent(String message);
    /**
     * Este metodo devuelve el gestor de las funciones propias de Android
     * @return gestor Mobile
     */
    public Mobile getMobile();

    public JSONObject readJsonFile2(String file);

    public JSONObject readJsonFile(String file);
    public String openAssetFile(String file);
    public void writeFile(String file,String output);

    public void programNotificacion(int time, TimeUnit timeunit, int icon, String title, String firstText);
    public void showNotificacion(String title, String firstText);
    public void setNotificationIcon(int icono);


    public boolean checkFileExists(String file);

    public String hashSHA256(String string);

}
