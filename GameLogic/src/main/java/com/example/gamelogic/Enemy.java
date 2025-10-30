package com.example.gamelogic;

import com.example.engine.Graphics;

public class Enemy {
    Circle circulo;
    float vida;
    float velocidad;
    float defensa;
    float resistencia;
    Tipo tipo;
    public Enemy(float x, float y, float vida, float velocidad, float defensa, float resistencia, Tipo tipoRes){
        this.circulo = new Circle(x,y,15,true);
        this.circulo.setColor(0xff00ff00);
        this.vida=vida;
        this.velocidad = velocidad;
        this.defensa = defensa;
        this.resistencia = resistencia;
        this.tipo = tipoRes;
    }
    public float getX(){
        return this.circulo.getX();
    }
    public float getY(){
        return this.circulo.getY();
    }
    public void damage(float damage, Tipo tipo){

    }
    public void Update(double deltaTime){

    }
    public void Render(Graphics gr){
        this.circulo.Render(gr);
    }
}
