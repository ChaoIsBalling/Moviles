package com.example.gamelogic;

import com.example.engine.Audio;
import com.example.engine.Graphics;
import com.example.engine.Sound;

import java.util.ArrayList;

public class IceTower implements Tower{
    Square cuadrado;
    float ataque=10;
    float rango= 70;
    float velocidad = 4;

    Audio audio;
    Sound attack;

    Tipo tipo = Tipo.hielo;
    ArrayList<Enemy> enemigos;

    boolean enRango;

    public IceTower(float x, float y){
        this.cuadrado = new Square(x,y,20,20,true);
        this.cuadrado.setColor(0xFFC8A2C8);
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
                    this.audio.playSound(this.attack);
                    this.enRango = true;
                }
                this.enemigos.get(i).damage(this.ataque,this.tipo);
            }
        }
        if(!encontrar){
            this.enRango = false;
            this.audio.stopSound(this.attack);
        }
    }

    @Override
    public void setAudio(Audio audio) {
        this.audio=audio;
        this.attack=audio.newSound("ice.wav");
    }

    @Override
    public void Render(Graphics gr) {
        this.cuadrado.Render(gr);
    }

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
}
