package com.example.gamelogic.towers;

import com.example.androidengine.AndroidGraphics;
import com.example.androidengine.AndroidAudio;
import com.example.gamelogic.Enemy;
import com.example.gamelogic.VisualElements.Image;
import com.example.gamelogic.Tipos.TipoTorre;
import com.example.gamelogic.VisualElements.figure.Figure;

/**
 *  Clase que representa la torre de Hielo e implementa la interfaz Tower
 */
public class IceTower extends BaseTower {

    //Determina si el audio ya ha sido loopeado o no
    boolean loopAudio;
    //Determina si aun hay enemigos en el area de ataque
    boolean hayEnemigos;
    Image image;

    /**
     * Constructora de la torre de hielo con su coordenada x,y
     */
    public IceTower(float x, float y, Figure figure){
        super(x,y,10,70,4, TipoTorre.HIELO);
        this.figura=figure;
    }
    public IceTower(float x, float y,Image im){
        super(x,y,10,70,4, TipoTorre.HIELO);
        this.image=im;

    }
    @Override
    public void Shoot() {
        hayEnemigos = false;
        for(Enemy e: enemigos){
            double dis = distancia(this.getPosX(),this.getPosY(), e.getX(), e.getY());
            if(dis <= rango){
                hayEnemigos = true;
                if (!this.loopAudio) {
                    this.audio.loopSound(this.soundAttack);
                    this.loopAudio = true;
                }
                e.damage(this.ataque, this.tipoTorre);
            }
        }
    }

    @Override
    public void setAudio(AndroidAudio audio) {
        this.audio=audio;
        this.soundAttack=audio.newSound("ice.wav");
    }

    /**
     * Funcionamiento de la torre
     */
    @Override
    public void Update(double deltaTime) {
        //Intentamos hacer el disparo
        Shoot();

        //Manejamos el audio de la torre en caso de que ya no haya enemigos
        if (!hayEnemigos && this.loopAudio) {
            this.loopAudio = false;
            this.stopAudio(); //Se para el audio
        }
    }

    /**
     * Renderizado de la figura
     * @param gr Interfaz graphics
     */
    @Override
    public void Render(AndroidGraphics gr) {
        if(this.image!=null)
            this.image.RenderCentrado((int)this.getPosX(),(int)this.getPosY());
        else
            this.figura.RenderAtPosition(gr,this.getPosX(),this.getPosY());
    }
}
