package com.example.gamelogic;

import com.example.engine.Audio;
import com.example.engine.Graphics;

import java.util.ArrayList;

public class IceTower implements Tower{
    Square cuadrado;
    float ataque=10;
    float rango= 105;
    float velocidad = 4;
    float enfriamiento = 0;
    float fuego =1;
    boolean disparo = false;

    Tipo tipo = Tipo.fuego;
    ArrayList<Enemy> enemigos;

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
        for (int i = 0; i < this.enemigos.size(); i++){
            float x = this.enemigos.get(i).getX();
            float y = this.enemigos.get(i).getY();
            double a = x-this.cuadrado.getX();
            double b = y-this.cuadrado.getY();
            a = Math.pow(a,2);
            b = Math.pow(b,2);
            double distancia = Math.sqrt(a+b);
            if(distancia <= this.rango){
                this.enemigos.get(i).damage(this.ataque,this.tipo);
            }
        }
    }

    @Override
    public void setAudio(Audio audio) {

    }

    @Override
    public void Render(Graphics gr) {
        this.cuadrado.Render(gr);
    }

    @Override
    public float getRange() {
        return this.rango;
    }
}
