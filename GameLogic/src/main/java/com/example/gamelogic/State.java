package com.example.gamelogic;
import com.example.androidengine.AndroidGraphics;
import com.example.engine.Audio;
import com.example.engine.Mobile;
import com.example.engine.TouchEvent;

import java.util.ArrayList;


/**
 * Interfaz que define un estado del juego, así como su actualización, render e input
 */
public interface State {

    /**
     * Actualiza la lógica del estado
     * @param deltatime Tiempo trascurrido
     */
    void update (double deltatime);

    /**
     * Dibuja el contenido del estado
     * @param gr Graphics del motor
     */
    void render(AndroidGraphics gr);

    /**
     * Inicializa el Graphics del motor
     * @param gr Graphics
     */
    void setGraphics(AndroidGraphics gr);

    /**
     * Procesa los eventos de entrada del usuario
     * @param list Lista de eventos
     * @param elapseTime Tiempo trascurrido
     */
    void handleInput(ArrayList<TouchEvent> list, double elapseTime);

    /**
     * Inicializa el sistema de audio del motor
     * @param audio Interfaz Audio
     */
    void setAudio(Audio audio);

    /**
     * Inicializa el sistema de mobile del motor de Android
     * @param mobile Interfaz Mobile
     */
    void setMobile(Mobile mobile);
}
