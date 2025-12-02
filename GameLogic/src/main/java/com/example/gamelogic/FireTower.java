package com.example.gamelogic;

import com.example.engine.Graphics;

import com.example.engine.Sound;

import com.example.engine.Audio;

import java.util.ArrayList;

/**
 * Clase que representa la torre de Fuego e implementa la interfaz Tower
 */
public class FireTower implements Tower {

    //Figura que represeta la torre de fuego
    Hexagon hexagono;
    //Stats de torre de fuego
    float ataque=2;
    float rango= 70;
    float velocidad = 2;
    float enfriamiento = 0;
    float fuego =1;
    boolean disparo = false;

    //Referencia al audio manager y el sonido de ataque
    Audio audio;
    Sound attack;

    //Tipo de la torre
    Tipo tipo = Tipo.fuego;

    //Lista de enemigos que detecta la torre
    ArrayList<Enemy> enemigos;
    Enemy enemigo;

    /**
     * Constructora de la torre de fuego
     */
    public FireTower(float x, float y){
        this.hexagono = new Hexagon(x,y,15,true);
        this.hexagono.setColor(0xffff0000);
        this.attack=attack;
    }

    /**
     * Actualiza la cantidad de daño por ataque gracias a una mejora
     */
    @Override
    public void UpdateAttack(float mejora) {
        this.ataque += mejora;
    }

    /**
     * Mejora el rango de ataque
     */
    @Override
    public void UpdateRange(float mejora) {
        this.rango += mejora;
    }

    /**
     * Mejora el ratio de disparo de fuego
     */
    @Override
    public void UpdateFireRate(float mejora) {
        if(this.velocidad > 0.5){
            this.velocidad += mejora;
        }
    }

    /**
     * Setters
     */
    @Override
    public void setListaEnemigos(ArrayList<Enemy> enemigos) {
        this.enemigos = enemigos;
    }
    @Override
    public void setAudio(Audio audio)
    {
        this.audio=audio;
        this.attack=audio.newSound("fire.wav");
    }

    /**
     * Funcionamiento de la torre
     */
    @Override
    public void Update(double deltaTime) {
        //Si la torre la puede disparar...
        if (this.enfriamiento <= 0){
            int enemigo = -1;
            double distanciaC = -1;
            for (int i = 0; i < this.enemigos.size(); i++){
                float x = this.enemigos.get(i).getX();
                float y = this.enemigos.get(i).getY();
                double a = x-this.hexagono.getX();
                double b = y-this.hexagono.getY();
                a = Math.pow(a,2);
                b = Math.pow(b,2);
                double distancia = Math.sqrt(a+b);
                //Si el enemigo esta en el rango
                if(distancia <= this.rango){
                    if(distanciaC == -1){
                        enemigo = i;
                        distanciaC = distancia;
                        this.enemigo = this.enemigos.get(i);
                    }
                    else if(distancia < distanciaC){
                        enemigo = i;
                        distanciaC = distancia;
                        this.enemigo = this.enemigos.get(i);
                    }
                }
            }
            //si ha encontrado un enemigo, le dispara
            if(enemigo != -1){
                this.enemigo.damage(this.ataque,this.tipo);
                this.enfriamiento = this.velocidad;
                this.disparo = true;
                this.fuego = 1;
                this.audio.playSound(this.attack);
                for (int i = 0; i < this.enemigos.size(); i++){
                    if(this.enemigos.get(i) != this.enemigo){
                        float x = this.enemigos.get(i).getX();
                        float y = this.enemigos.get(i).getY();
                        double a = x-this.enemigo.getX();
                        double b = y-this.enemigo.getY();
                        a = Math.pow(a,2);
                        b = Math.pow(b,2);
                        double distancia = Math.sqrt(a+b);
                        if(distancia <= 15){
                            this.enemigos.get(i).damage(this.ataque,this.tipo);
                        }
                    }
                }
            }

        }
        //Tiene que esperar a que se termine el enfriamento
        else{
            this.enfriamiento -= deltaTime;
            this.fuego -= deltaTime;
        }
    }

    /**
     * Renderiza la torre
     */
    @Override
    public void Render(Graphics gr) {
        this.hexagono.Render(gr);
        if(this.disparo && this.fuego > 0){
            gr.setColor(0xffff0000);
            gr.rellenarCirculo(this.enemigo.getX(),this.enemigo.getY(),15);
        }
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
        return this.hexagono.getX();
    }

    @Override
    public float getY() {
        return this.hexagono.getY();
    }

    /**
     * Deteiene el sonido de ataque
     */
    @Override
    public void stopAudio() {
        this.audio.stopSound(this.attack);
    }
}
