package com.example.gamelogic;

import com.example.engine.Graphics;

import java.util.ArrayList;

public class ThunderTower implements Tower{
    Triangle trianulo;
    float ataque=10;
    float rango= 105;
    float velocidad = 4;
    float enfriamiento = 0;
    float rayo =1;
    boolean disparo = false;
    float enemyX;
    float enemyY;

    Tipo tipo = Tipo.rayo;
    ArrayList<Enemy> enemigos;
    public ThunderTower(float x, float y){
        this.trianulo = new Triangle(x,y,15,true);
        this.trianulo.setColor(0xFF000000);
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
        Enemy cercano = new Enemy(-100,-100,0,0,0,0,Tipo.rayo);
        double distanciaC = -1;
        for (int i = 0; i < this.enemigos.size(); i++){
            float x = this.enemigos.get(i).getX();
            float y = this.enemigos.get(i).getY();
            double a = x-this.trianulo.getX();
            double b = y-this.trianulo.getY();
            a = Math.pow(a,2);
            b = Math.pow(b,2);
            double distancia = Math.sqrt(a+b);
            if(distancia <= this.rango){
                if(distanciaC == -1){
                    cercano = this.enemigos.get(i);
                    distanciaC = distancia;
                }
                else if(distancia < distanciaC){
                    cercano = this.enemigos.get(i);
                    distanciaC = distancia;
                }
            }
        }
        if (this.enfriamiento <= 0 && this.enemigos.contains(cercano)){
            cercano.damage(this.ataque,this.tipo);
            this.enemyX = cercano.getX();
            this.enemyY = cercano.getY();
            this.enfriamiento = this.velocidad;
            this.disparo = true;
            this.rayo = 1;
        }
        else{
            this.enfriamiento -= deltaTime;
            this.rayo -= deltaTime;
        }

    }

    @Override
    public void Render(Graphics gr) {
        this.trianulo.Render(gr);
        if(this.disparo && this.rayo > 0){
            gr.setColor(0x0000ffff);
            gr.pintarLinea(this.trianulo.getX(),this.trianulo.getY(),this.enemyX,this.enemyY);
        }
    }
}
