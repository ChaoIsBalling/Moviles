package com.example.gamelogic.towers;

import com.example.androidengine.AndroidGraphics;
import com.example.androidengine.AndroidAudio;
import com.example.gamelogic.VisualElements.Image;

/**
 * Clase que implementa una nueva torre: La torre de rayo mini
 * Es una torre de rayo normal pero con color de Rayo y frecuencia de disparo distinta
 * Por ello hereda de ThunderTower
 */
public class MiniThunderTower extends ThunderTower{
    /**
     * Constructora de la torre de rayo con sus coordenadas
     */
    public MiniThunderTower(float x, float y, Image im){
        super(x,y,im);
        this.ataque = 1;
        this.rango = 70;
        this.velocidad = 0.3f;
        this.colorRayo = "#FFFF0000";
        this.frecuenciaRayo = 0.3f;
        this.rayo = this.frecuenciaRayo;
    }

    /**
     * Logica de disparo de MiniThunderTower
     */
    @Override
    public void Shoot() {
        super.Shoot();
    }

    /**
     * Es el mismo sonido que el ThunderTower padre
     * @param audio interfaz de audio
     */
    @Override
    public void setAudio(AndroidAudio audio) {
        super.setAudio(audio);
    }

    /**
     * Actualiza el comportamiento de la torre de rayo
     * @param deltaTime tiempo trascurrido
     */
    @Override
    public void Update(double deltaTime) {
        super.Update(deltaTime);
    }

    /**
     * Reneriza la torre y, si está disparando un rayo, también la linea
     * @param gr
     */
    @Override
    public void Render(AndroidGraphics gr) {
        super.Render(gr);
    }
}
