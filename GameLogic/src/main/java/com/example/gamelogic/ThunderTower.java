package com.example.gamelogic;

import com.example.engine.Graphics;

import java.util.ArrayList;

public class ThunderTower implements Tower{
    Triangle trianulo;
    float ataque;
    float rango;
    float velocidad;
    float enfriamiento = 0;
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
            double distancia = Math.sqrt((Math.pow((x-this.trianulo.getX()),2)-Math.pow((y-this.trianulo.getY()),2)));
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
            this.enfriamiento = this.velocidad;
        }
        else{
            this.enfriamiento -= deltaTime;
        }

    }

    @Override
    public void Render(Graphics gr) {
        this.trianulo.Render(gr);
    }
}
