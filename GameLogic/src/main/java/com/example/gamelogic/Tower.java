package com.example.gamelogic;

import com.example.engine.Graphics;
import com.example.engine.Audio;

import java.util.ArrayList;

/**
 * Interfaz que van a implementar todos los tipos de torres
 */
public interface Tower {
    /**
     * Mejora el ataque de la torre
     * @param mejora canitdad de mejora del atributo
     */
    public void UpdateAttack(float mejora);

    /**
     * Mejora el rango de ataque de la torre
     * @param mejora cuanto aumenta el rango
     */
    public void UpdateRange(float mejora);

    /**
     * Mejora el ratio de disparo
     */
    public void UpdateFireRate(float mejora);

    /**
     * para obtener la lista de enemigos cercanos a la torre
     * @param enemigos lista
     */
    public void setListaEnemigos(ArrayList<Enemy> enemigos);

    /**
     * Actualiza el comportamiento de la torre
     * @param deltaTime tiempo trascurrido
     */
    public void Update(double deltaTime);

    /**
     * Setea el audio asociado a la torre
     * @param audio interfaz de audio
     */
    public void setAudio(Audio audio);

    /**
     * Renderiza el enemigo
     * @param gr
     */
    public void Render(Graphics gr);

    /**
     * Getters de atributos de la torre
     * @return
     */
    public float getRange();
    public float getX();
    public float getY();

    /**
     * Para detener el audio asociado a la torre
     */
    public void stopAudio();
}
