package com.example.gamelogic;
import com.example.androidengine.AndroidGraphics;
import com.example.gamelogic.states.GameLogic;

import java.util.ArrayList;

/**
 * Clase que representa un enemigo en el juego
 */
public class Enemy {
    //vida del enemigo
    float vida;
    //Determina si ha llegado al final del camino sin morir
    boolean win;
    //Direccion a la que se mueve
    Vector2D direccion;
    //stats
    float velocidad;
    float defensa;
    float resistencia;
    float x, y;


    //Minimo a lo que se puede ajustar los daños al enemigo
    float damMinimo = 2;
    float ralentizacionMinima = 7;
    Image imagen;
    //Tipo al que es resistente
    TipoTorre tipoTorreResistente;

    //Referencia a la escena de juego
    GameLogic gl;

    //Coordenada de casilla en la que se encuentra
    Vector2D coor;

    //factor ralentizar
    float ralentizar =0;

    //Casillas para calcular el movimiento
    Casilla casillaInicial;

    //Vector de puntos del camino a seguir por el enemigo en forma de coordenadas del tablero
    ArrayList<Vector2D> puntosCamino;

    //Punto i-esimo al que el enemigo debe ir
    int puntoIndex;

    /**
     * Constructora de la clase Enemy con todos sus parámetros a inicializar
     */
    public Enemy(float vida, float velocidad, float defensa, float resistencia, TipoTorre tipoRes, ArrayList<Vector2D>camino, GameLogic gl){
        //Posicion inicial sacada del inicio del camino
        this.x=gl.getRealX(camino.get(0).getY());
        this.y=gl.getRealY(camino.get(0).getX());

        this.vida=vida; //Vida
        this.direccion = new Vector2D(1,0); //Direccion representado por un Vector2D
        this.velocidad = velocidad; //Velocidad con la que se mueve
        this.defensa = defensa; //Defensa
        this.resistencia = resistencia; //Daño infligido con resistencia activada
        this.tipoTorreResistente = tipoRes; //El tipo del ataque al que resiste

        this.gl = gl; //Instancia del gameLogic

        //Obtenemos casilla actual a partir de sus coordenadas
        this.coor = gl.determinaCasilla(this.y, this.x);
        this.casillaInicial = this.gl.getCasillas().get(this.coor.getX()).get(this.coor.getY());
        this.puntosCamino = camino;
    }

    /**
     * Getter de las coordenadas x e y
     */
    public float getX(){ return this.x; }
    public float getY(){
        return this.y;
    }

    /**
     * Metodo que se llama cada vez que el enemigo sufre daño
     */
    public void damage(float damage, TipoTorre tipoTorre){
        //Cuanto valdra la reduccion de daño (si la hay)
        float reduccion = 0f;

        //Aplicamos la defensa al enemigo si es resistente al tipo de la torre
        if(this.tipoTorreResistente == tipoTorre)
            reduccion = this.defensa;

        //daño total al enemigo
        float damEfectivo = damage - reduccion;

        if(tipoTorre == TipoTorre.HIELO) //solo ralentizamos si la torre es de hielo
            this.ralentizar = Math.max(ralentizacionMinima, damEfectivo);
        else // quitamos vida
            this.vida -= Math.max(damMinimo, damEfectivo);

        //System.out.println("("+damage+","+tipo+")");
        //System.out.println(this.vida);
    }

    public void setImagen(Image img){this.imagen = img;}

    /**
     * Se actualiza la lógica de movimiento del enemigo
     * @param deltaTime tiempo trascurrido
     */
    public void Update(double deltaTime){
        //vamos recorriendo mientras haya puntos por los que el enemigo pueda ir
        if(puntoIndex >= puntosCamino.size()){
            this.win = true;
            return;
        }

        //Proximo punto al que irá el enemigo
        Vector2D objetivo = puntosCamino.get(puntoIndex);

        //Posicion a la que queremos llegar
        float targetX = gl.getRealX(objetivo.getY());
        float targetY = gl.getRealY(objetivo.getX());
        //System.out.println(targetX + ","+ targetY);

        //Direccion a la que debemos ir
        float dirX = targetX - this.x;
        float dirY =  targetY - this.y;
        //Distancia que queda para que llegue al objetivo
        float distancia = (float) Math.sqrt((dirX * dirX) + (dirY * dirY));

        //Direccion normalizada
        float nx = dirX/distancia;
        float ny = dirY/distancia;

        //Velocidad resultante
        float velRes = velocidad - ralentizar;

        // Cálculo del movimiento deseado
        float movimiento = (float)(velRes * deltaTime);

        //Si el movimiento es mayor que la distancia, nos quedamos en el punto exacto
        //Esto evita oscilaciones a la hora de pasar a otro punto
        if (movimiento >= distancia) {
            this.x = targetX;
            this.y = targetY;
            puntoIndex++; // Saltamos al siguiente punto
        } else {
            //movimiento normal (suave)
            this.x += nx * movimiento;
            this.y += ny * movimiento;
        }

        this.ralentizar = 0;
    }

    /**
     * Metodo para renderizar al enemigo
     * @param gr Graphics
     */
    public void Render(AndroidGraphics gr){
        this.imagen.RenderCentrado((int)this.x,(int)this.y);
    }

    /**
     * @return si el enemigo ha ganado o no
     */
    public boolean Win(){return this.win;}

    /**
     * Determina si esta muerto
     */
    public boolean Dead()
    {
        return vida<=0;
    }
}
