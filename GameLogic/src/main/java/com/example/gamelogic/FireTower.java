package com.example.gamelogic;

import com.example.engine.Graphics;

import com.example.engine.Sound;

import com.example.engine.Audio;

import java.util.ArrayList;

public class FireTower implements Tower {
    Hexagon hexagono;
    float ataque=10;
    float rango= 105;
    float velocidad = 4;
    float enfriamiento = 0;
    float fuego =1;
    boolean disparo = false;
    float enemyX;
    float enemyY;

    Audio audio;
    Sound attack;

    Tipo tipo = Tipo.fuego;
    ArrayList<Enemy> enemigos;

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
        this.velocidad += mejora;
    }

    @Override
    public void setListaEnemigos(ArrayList<Enemy> enemigos) {
        this.enemigos = enemigos;
    }

    @Override
    public void setAudio(Audio audio)
    {
        this.audio=audio;
        this.attack=audio.newSound("laser.wav");
    }
    @Override
    public void Update(double deltaTime) {
        Enemy cercano = new Enemy(-100,-100,0,0,0,0,Tipo.rayo);
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
                    cercano = this.enemigos.get(i);
                    distanciaC = distancia;
                    this.enemyX = cercano.getX();
                    this.enemyY = cercano.getY();
                }
                else if(distancia < distanciaC){
                    cercano = this.enemigos.get(i);
                    distanciaC = distancia;
                    this.enemyX = cercano.getX();
                    this.enemyY = cercano.getY();
                }
            }
        }
        if (this.enfriamiento <= 0 && this.enemigos.contains(cercano)){
            cercano.damage(this.ataque,this.tipo);
            this.enemyX = cercano.getX();
            this.enemyY = cercano.getY();
            this.enfriamiento = this.velocidad;
            this.disparo = true;
            this.fuego = 1;
            this.audio.playSound(this.attack);
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
            gr.rellenarCirculo(this.enemyX,this.enemyY,10);
        }
    }
}
