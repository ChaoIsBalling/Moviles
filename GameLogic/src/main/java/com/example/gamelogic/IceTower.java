package com.example.gamelogic;

import com.example.engine.Audio;
import com.example.engine.Graphics;
import com.example.engine.Sound;

import java.util.ArrayList;

/**
 *  Clase que representa la torre de Hielo e implementa la interfaz Tower
 */
public class IceTower implements Tower{
    //Forma que representa la torre
    Square cuadrado;
    //stats de la torre de Hielo
    float ataque=10;
    float rango= 70;
    float velocidad = 4;

    //Referencia al audio manager y el sonido de ataque
    Audio audio;
    Sound attack;

    //Tipo de la torre
    Tipo tipo = Tipo.hielo;
    //Lista de enemigos que detecta la torre
    ArrayList<Enemy> enemigos;

    //Determina si hay enemigos en rango
    boolean enRango;

    /**
     * Constructora de la torre de hielo con su coordenada x,y
     */
    public IceTower(float x, float y){
        this.cuadrado = new Square(x,y,20,20,true);
        this.cuadrado.setColor(0xFFC8A2C8);
    }

    /**
     * Mejoras de estadísticas de la torre de hielo
     * @param mejora canitdad de mejora del atributo
     */
    @Override
    public void UpdateAttack(float mejora) {
        this.ataque += mejora;
    }

    @Override
    public void UpdateRange(float mejora) {
        this.rango += mejora;
    }

    @Override
    public void UpdateFireRate(float mejora) {
        this.velocidad += mejora;
    }

    /**
     * getters
     * @return
     */
    @Override
    public float getRange() {
        return this.rango;
    }

    @Override
    public float getX() {
        return this.cuadrado.getX();
    }

    @Override
    public float getY() {
        return this.cuadrado.getY();
    }

    /**
     * setters
     */
    @Override
    public void setListaEnemigos(ArrayList<Enemy> enemigos) {
        this.enemigos = enemigos;
    }
    @Override
    public void setAudio(Audio audio) {
        this.audio=audio;
        this.attack=audio.newSound("ice.wav");
    }

    /**
     * Funcionamiento de la torre
     */
    @Override
    public void Update(double deltaTime) {
        boolean encontrar = false;
        for (int i = 0; i < this.enemigos.size(); i++){
            float x = this.enemigos.get(i).getX();
            float y = this.enemigos.get(i).getY();
            double a = x-this.cuadrado.getX();
            double b = y-this.cuadrado.getY();
            a = Math.pow(a,2);
            b = Math.pow(b,2);
            double distancia = Math.sqrt(a+b);
            if(distancia <= this.rango){
                encontrar = true;
                if(!this.enRango){
                    this.audio.loopSound(this.attack);
                    this.enRango = true;
                }
                this.enemigos.get(i).damage(this.ataque,this.tipo);
            }
        }
        if(!encontrar && this.enRango){
            this.enRango = false;
            this.audio.stopSound(this.attack);
        }

    }


    /**
     * Renderizado de la figura
     * @param gr Interfaz graphics
     */
    @Override
    public void Render(Graphics gr) {
        this.cuadrado.Render(gr);
    }

    /**
     * Deteiene el sonido de ataque
     */
    @Override
    public void stopAudio() {
        this.audio.stopSound(this.attack);
    }
}
