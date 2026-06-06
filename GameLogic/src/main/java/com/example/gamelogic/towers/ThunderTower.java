package com.example.gamelogic.towers;

import com.example.androidengine.AndroidAudio;
import com.example.androidengine.AndroidGraphics;
import com.example.gamelogic.Enemy;
import com.example.gamelogic.VisualElements.Image;
import com.example.gamelogic.Tipos.TipoTorre;
import com.example.gamelogic.VisualElements.figure.Figure;

/**
 * Clase que representa la torre de Rayo e implementa la interfaz Tower
 */
public class ThunderTower extends BaseTower{
    //Timer de renderizado del rayo
    float timerFeedBackRayo;
    //Cada cuanto se renderiza el rayo
    float frecuenciaRayo = 1;
    //Enemigo a atacar
    Enemy enemigo;
    //Color del rayo
    String colorRayo = "#FF00FFFF";

    /**
     * Constructora de la torre de rayo con sus coordenadas
     */
    public ThunderTower(float x, float y, Figure figure){
        super(x,y,4,70,1.6f, TipoTorre.RAYO);
        this.timerFeedBackRayo = this.frecuenciaRayo;
        this.figura=figure;

    }
    public ThunderTower(float x, float y, Image im){
        super(x,y,4,70,1.6f, TipoTorre.RAYO);
        this.image=im;
        this.timerFeedBackRayo = this.frecuenciaRayo;
    }
    @Override
    public void setAudio(AndroidAudio audio) {
        this.audio=audio;
        this.soundAttack=audio.newSound("laser.wav");
    }

    /**
     * Metodo con la logica de disparo de la torre de rayo. Se ejecuta solo si ha encontrado un enemigo
     */
    @Override
    public void Shoot(){
        this.enemigo.damage(this.ataque,this.tipoTorre);
        this.enfriamiento = this.velocidad;
        this.disparo = true;
        this.audio.playSound(this.soundAttack);
        this.timerFeedBackRayo = this.frecuenciaRayo;
    }

    /**
     * Actualiza el comportamiento de la torre de rayo
     * @param deltaTime tiempo trascurrido
     */
    @Override
    public void Update(double deltaTime) {
        if(this.enfriamiento <= 0){
            this.enemigo = buscarEnemigoMasCercano();
            if(enemigo != null)
                Shoot();
        }
        else{
            //actualizamos cooldown
            this.enfriamiento -= deltaTime;
            this.timerFeedBackRayo -= deltaTime;
        }
    }

    /**
     * Reneriza la torre y, si está disparando un rayo, también la linea
     * @param gr
     */
    @Override
    public void Render(AndroidGraphics gr) {
        if(this.image!=null)
            this.image.RenderCentrado((int)this.getPosX(),(int)this.getPosY());
        else
            this.figura.RenderAtPosition(gr,this.getPosX(),this.getPosY());

        if(this.disparo && this.timerFeedBackRayo > 0 && this.enemigo != null){
            gr.setColor(colorRayo);
            gr.pintarLinea(this.getPosX(),this.getPosY(),this.enemigo.getX(),this.enemigo.getY(),5);
        }
    }
}
