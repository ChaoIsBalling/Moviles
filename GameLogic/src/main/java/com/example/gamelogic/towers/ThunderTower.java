package com.example.gamelogic.towers;

import com.example.androidengine.AndroidAudio;
import com.example.androidengine.AndroidSound;
import com.example.androidengine.AndroidGraphics;
import com.example.gamelogic.Enemy;
import com.example.gamelogic.Image;
import com.example.gamelogic.TipoTorre;
import com.example.gamelogic.figure.Triangle;

import java.util.ArrayList;

/**
 * Clase que representa la torre de Rayo e implementa la interfaz Tower
 */
public class ThunderTower implements Tower{
    //figura de la torre
    Triangle triangulo;
    //stats de la torre de Rayo
    float ataque=4;
    float rango= 70;
    float velocidad = (float )1.6;
    float enfriamiento = 0;
    float rayo =1;
    float x;
    float y;
    //Referencia al audio manager y el sonido de ataque
    AndroidAudio audio;
    AndroidSound attack;

    //Determina si está disparando
    boolean disparo = false;
    Image imagen=null;

    TipoTorre tipo = TipoTorre.RAYO;

    //Lista de enemigos que la torre puede atacar
    ArrayList<Enemy> enemigos;
    //Enemigo a atacar
    Enemy enemigo;

    /**
     * Constructora de la torre de rayo con sus coordenadas
     */
    public ThunderTower(float x, float y){

        this.x=x;
        this.y=y;
        this.triangulo = new Triangle(x,y,15,true);
        this.triangulo.setColor("#FF000000");
    }
    public ThunderTower(float x, float y, Image im){
        this.imagen=im;
        this.x=x;
        this.y=y;
    }

    /**
     * Mejoras de estadísticas de la torre de rayo
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
    public void setAudio(AndroidAudio audio) {
        this.audio=audio;
        this.attack=audio.newSound("laser.wav");
    }

    /**
     * Actualiza el comportamiento de la torre de rayo
     * @param deltaTime tiempo trascurrido
     */
    @Override
    public void Update(double deltaTime) {

        if (this.enfriamiento <= 0){
            int enemigo = -1;
            double distanciaC = -1;
            for (int i = 0; i < this.enemigos.size(); i++){
                float x = this.enemigos.get(i).getX();
                float y = this.enemigos.get(i).getY();
                //Distancia hasta el enemigo
                double a = x-this.x;
                double b = y-this.y;
                a = Math.pow(a,2);
                b = Math.pow(b,2);
                double distancia = Math.sqrt(a+b);
                //Si esta en el rango
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
                this.audio.playSound(this.attack);
                this.rayo = 1;
            }

        }
        else{
            this.enfriamiento -= deltaTime;
            this.rayo -= deltaTime;
        }

    }


    /**
     * Reneriza la torre y, si está disparando un rayo, también la linea
     * @param gr
     */
    @Override
    public void Render(AndroidGraphics gr) {
        if(this.imagen!=null)
            this.imagen.RenderCentrado((int)this.x,(int)this.y);
        else
            this.triangulo.Render(gr);
        if(this.disparo && this.rayo > 0){
            gr.setColor(0xff00ffff);
            gr.pintarLinea(this.x,this.y,this.enemigo.getX(),this.enemigo.getY(),5);
        }
    }

    /**
     * Getters
     */
    @Override
    public float getRange() {
        return this.rango;
    }

    @Override
    public float getX() {
        return this.x;
    }

    @Override
    public float getY() {
        return this.y;
    }

    /**
     * Deteiene el sonido de ataque
     */
    @Override
    public void stopAudio() {
        this.audio.stopSound(this.attack);
    }
}
