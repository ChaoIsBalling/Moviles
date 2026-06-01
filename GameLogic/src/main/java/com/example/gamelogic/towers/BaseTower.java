package com.example.gamelogic.towers;

import com.example.androidengine.AndroidAudio;
import com.example.androidengine.AndroidSound;
import com.example.gamelogic.Enemy;
import com.example.gamelogic.Tipos.TipoTorre;
import com.example.gamelogic.VisualElements.figure.Figure;

import java.util.ArrayList;

/**
 * Clase abstracta de la que van a heredar todas las torres que contiene la
 * implementación de metodos basicos que hacen todas las torres y
 * asi evitar la repetición de código en todos los tipos de torres
 */
public abstract class BaseTower implements Tower {

    //Stats comunes de las torres
    protected float ataque, rango, velocidad, enfriamiento;

    //Coordenadas de posicion
    protected float posX, posY;

    //Referencia al audio manager y el sonido de ataque
    protected AndroidAudio audio;
    protected AndroidSound soundAttack;

    //Lista de los enemigos a los que puede atacar una torre
    protected ArrayList<Enemy> enemigos;

    //Tipo de ataque de la torre
    protected TipoTorre tipoTorre;

    protected Figure figura;

    public BaseTower(float x, float y, float ataque, float rango, float velocidad, TipoTorre tipo){
        this.posX = x;
        this.posY = y;
        this.ataque = ataque;
        this.rango = rango;
        this.velocidad = velocidad;
        this.tipoTorre = tipo;
        this.enfriamiento = 0;
    }

    /**
     * Mejoras de estadísticas de la torre
     * @param m canitdad de mejora del atributo deseado (ataque, rango o velocidad de disparo)
     */
    @Override public void UpdateAttack(float m) { this.ataque += m; }
    @Override public void UpdateRange(float m) { this.rango += m; }
    @Override
    public void UpdateFireRate(float m) {if(this.velocidad > 0.5) this.velocidad += m;}

    /**
     * Settea la lista de los enemigos disponibles para atacar
     * @param e lista
     */
    @Override public void setListaEnemigos(ArrayList<Enemy> e) { this.enemigos = e; }
    @Override public void setFigura(Figure figura){this.figura =figura;}
    /**
     * Getter del rango de la torre
     * @return
     */
    @Override public float getRange() { return this.rango; }
    /**
     * getters
     * @return
     */
    @Override
    public float getPosX() { return this.posX; }
    @Override
    public float getPosY() { return this.posY; }
    /**
     * Deteiene el sonido de ataque asociado a la torre
     */
    @Override public void stopAudio() { if(audio != null) audio.stopSound(soundAttack); }


    /**
     * Calcula la distancia entre dos entidades
     * @param x1 coordenada x origen
     * @param y1  coordenada y origen
     * @param x2  coordenada x destino
     * @param y2 coordenada y destino
     * @return distancia
     */
    protected double distancia(float x1, float y1, float x2, float y2) {
        float dx = x2 - x1;
        float dy = y2 - y1;
        return Math.sqrt((dx * dx) + (dy * dy));
    }

    /**
     * Metodo para encontrar el enemigo más cercano a la torre
     */
    protected Enemy buscarEnemigoMasCercano() {
        Enemy masCercano = null;
        double mejorDist = Double.MAX_VALUE;

        for (Enemy e : enemigos) {
            double dist = distancia(getPosX(), getPosY(), e.getX(), e.getY());
            if (dist <= rango && dist < mejorDist) {
                mejorDist = dist;
                masCercano = e;
            }
        }
        return masCercano;
    }
}
