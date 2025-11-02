package com.example.gamelogic;

import com.example.engine.Graphics;

import com.example.engine.Sound;

import com.example.engine.Audio;

import java.util.ArrayList;

public class FireTower implements Tower {
    Hexagon hexagono;
    float ataque=2;
    float rango= 70;
    float velocidad = 4;
    float enfriamiento = 0;
    float fuego =1;
    boolean disparo = false;

    Audio audio;
    Sound attack;

    Tipo tipo = Tipo.fuego;
    ArrayList<Enemy> enemigos;
    Enemy enemigo;

    public FireTower(float x, float y){
        this.hexagono = new Hexagon(x,y,15,true);
        this.hexagono.setColor(0xffff0000);
        this.attack=attack;
    }
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
        if(this.velocidad > 0.5){
            this.velocidad += mejora;
        }
    }

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
    @Override
    public void Update(double deltaTime) {

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
        else{
            this.enfriamiento -= deltaTime;
            this.fuego -= deltaTime;
        }
    }

    @Override
    public void Render(Graphics gr) {
        this.hexagono.Render(gr);
        if(this.disparo && this.fuego > 0){
            gr.setColor(0xffff0000);
            gr.rellenarCirculo(this.enemigo.getX(),this.enemigo.getY(),15);
        }
    }

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
}
