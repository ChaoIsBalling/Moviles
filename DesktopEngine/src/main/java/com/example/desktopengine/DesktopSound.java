package com.example.desktopengine;
import com.example.engine.Sound;

import javax.sound.sampled.Clip;

/**
 * Clase que implementa la interfaz Sonido de Engine
 * Crea y guarda los atributos de un sonido de escritorio
 */
public class DesktopSound implements Sound{
    /**
     * Nombre del sonido
     */
    private String file;
    /**
     * Objeto Clip que tiene el sonido cargado
     */
    private Clip clip;

    /**
     * Guarda el nombre del sonido
     * @param file nombre
     */
    DesktopSound(String file) {
        this.file=file;
    }

    /**
     * Devuelve el nombre del sonido
     * @return nombre
     */
    public String getName()
    {
        return this.file;
    }

    /**
     * Asigna el objeto clip al audio y lo devuelve
     * @param c
     * @return
     */
    public Object setClip(Clip c) {
        return this.clip = c;
    }

    /**
     * Devuelve el objeto clip
     * @return Clip
     */

    protected Clip getClip(){
        return this.clip;
    }

}
