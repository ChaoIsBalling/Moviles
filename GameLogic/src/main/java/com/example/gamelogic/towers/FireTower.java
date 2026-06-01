package com.example.gamelogic.towers;

import com.example.androidengine.AndroidGraphics;
import com.example.androidengine.AndroidAudio;
import com.example.gamelogic.Enemy;
import com.example.gamelogic.VisualElements.Image;
import com.example.gamelogic.Tipos.TipoTorre;
import com.example.gamelogic.VisualElements.figure.Figure;

/**
 * Clase que representa la torre de Fuego e implementa la interfaz Tower
 */
public class FireTower extends BaseTower {

    float fuego = 1;
    boolean disparo = false;
    float radioBolaFuego = 15.0f;
    //Enemigo a atacar
    Enemy enemigo;
    Image image =null;

    /**
     * Constructora de la torre de fuego
     */
    public FireTower(float x, float y, Figure figure) {
        super(x,y,3,70,2, TipoTorre.FUEGO);
        this.figura=figure;
    }
    public FireTower(float x, float y, Image im){
        super(x,y,3,70,2, TipoTorre.FUEGO);
        this.image=im;
    }

    /**
     * Implementa la logica de disparo de la torre de fuego
     * Una vez haya encontrado un enemigo, busca si alrededor de ese enemigo hay otros para dañarlos tambien
     */
    @Override
    public void Shoot(){
        //Dañamos al enemigo actual
        this.enemigo.damage(this.ataque,this.tipoTorre);
        this.enfriamiento = this.velocidad;
        this.disparo = true;
        this.fuego = 1;
        this.audio.playSound(this.soundAttack);

        //Determinamos a que enemigo hay que atacar que este en el radio de ataque de la bola de fuego desde el enemigo objetivo
        for(Enemy e: enemigos){
            double dis = distancia(enemigo.getX(), enemigo.getY(), e.getX(), e.getY());
            //Si el enemigo esta dentro del radio de ataque Y no es el enemigo que ha sido asignado como enemigo
            if(dis <= this.radioBolaFuego && enemigo != e){
                //Simplemente lo dañamos
                e.damage(this.ataque,this.tipoTorre);
            }
        }
    }

    @Override
    public void setAudio(AndroidAudio audio)
    {
        this.audio=audio;
        this.soundAttack=audio.newSound("fire.wav");
    }

    /**
     * Funcionamiento de la torre de fuego en cada frame
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
            this.fuego -= deltaTime;
        }
    }

    /**
     * Renderiza la torre
     */
    @Override
    public void Render(AndroidGraphics gr) {
        if(this.image==null)
            this.figura.RenderAtPosition(gr,this.getPosX(),this.getPosY());
        else
            this.image.RenderCentrado((int)this.getPosX(),(int)this.getPosY());
        if(this.disparo && this.fuego > 0){
            gr.setColor(0xffff0000);
            gr.rellenarCirculo(this.enemigo.getX(),this.enemigo.getY(),15);
        }
    }
}
