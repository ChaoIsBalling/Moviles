package com.example.engine;

/**
 * Interfaz de Audio para los gestores de Audio de los motores de Android y Desktop
 */
public interface Audio {

    /**
     * Este metodo crea un nuevo sonido a partir del nombre del archivo
     * @param file nombre del archivo
     * @return Interfaz sound del motor
     */
    public Sound newSound(String file);

    /**
     * Este metodo reproduce un sonido
     * @param sound Interfaz sound del engine
     */
    public void playSound(Sound sound);

    /**
     * Este metodo habilita loop al sonido
     * @param sound Interfaz sound del engine
     */
    public void loopSound(Sound sound);

    /**
     * Este metodo detiene un sonido
     * @param sound Interfaz sound del engine
     */
    public void stopSound(Sound sound);
}
