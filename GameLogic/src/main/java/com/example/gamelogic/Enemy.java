package com.example.gamelogic;

import com.example.engine.Graphics;

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
    float velocidad = 100;
    float defensa;
    float resistencia;
    float x;
    float y;
    Image imagen;
    //Tipo al que es resistente
    Tipo tipo;

    //Referencia a la escena de juego
    GameLogic gl;

    //Coordenada de casilla en la que se encuentra
    Vector2D coor;

    //factor ralentizar
    float ralentizar =0;

    //Casillas para calcular el movimiento
    Casilla casillaSig;
    Casilla casillaActual;
    Casilla casillaInicial;

    //Vector de puntos del camino a seguir por el enemigo en forma de coordenadas del tablero
    ArrayList<Vector2D> puntosCamino;

    //Punto i-esimo al que el enemigo debe ir
    int puntoIndex;

    /**
     * Constructora de la clase Enemy con todos sus parámetros a inicializar
     */
    public Enemy(float vida, float velocidad, float defensa, float resistencia, Tipo tipoRes, ArrayList<Vector2D>camino, GameLogic gl){
        //El enmeigo se representa mediante un círculo
        this.x=gl.getRealX(camino.get(0).getY());
        this.y=gl.getRealY(camino.get(0).getX());
        this.vida=vida; //Vida
        this.direccion = new Vector2D(1,0); //Direccion representado por un Vector2D
        this.velocidad = velocidad; //Velocidad con la que se mueve
        this.defensa = defensa; //Defensa
        this.resistencia = resistencia; //Daño infligido con resistencia activada
        this.tipo = tipoRes; //El tipo del ataque al que resiste
        this.gl = gl; //Instancia del gameLogic
        //Obtenemos casilla actual a partir de sus coordenadas
        this.coor = gl.determinaCasilla(this.y, this.x);
        this.casillaInicial = this.gl.casillas.get(this.coor.getX()).get(this.coor.getY());
        this.puntosCamino = camino;
    }

    /**
     * Getter de las coordenadas x e y
     */
    public float getX(){
        return this.x;
    }
    public float getY(){
        return this.y;
    }

    /**
     * Metodo que se llama cada vez que el enemigo sufre daño
     */
    public void damage(float damage, Tipo tipo){
        if(tipo == Tipo.hielo){
            float dam = damage;
            if(this.tipo == tipo){
                dam -= this.resistencia;
            }
            if(dam < 7){
                dam = 7;
            }
            if(this.ralentizar < dam){
                this.ralentizar = dam;
            }
        }
        else{
            float dam = damage - this.defensa;
            if(this.tipo == tipo){
                dam -= this.resistencia;
            }
            if(dam <2){
                dam = 2;
            }
            this.vida -= dam;
        }
        //System.out.println("("+damage+","+tipo+")");
        //System.out.println(this.vida);
    }

    public void setImagen(Image img){this.imagen = img;}
    /**
     * Para comprobar si no se sale del tablero
     */
    private boolean boundsPath(Vector2D c){
        return ((c.getX() < this.gl.fil && c.getX() >= 0) &&
                (c.getY() >= 0 && c.getY() < this.gl.col));
    }

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

        //calculamos direccion
        float dirX = gl.getRealX(objetivo.getY());
        float dirY = gl.getRealY(objetivo.getX());
        //System.out.println(dirX + ","+dirY);

        dirX -= this.x;
        dirY -=this.y;
        //Distancia que queda para que llegue al objetivo
        float distancia = (float) Math.sqrt((dirX*dirX) +(dirY*dirY));

        //Si llegamos al punto, pasamos al siguiente
        if(distancia < 1.0f){
            puntoIndex++;
            return;
        }

        float nx = dirX/distancia;
        float ny = dirY/distancia;

        float mov = velocidad - ralentizar;

        // Mover suavemente
        this.x += nx * mov * deltaTime;
        this.y += ny * mov * deltaTime;

        ralentizar = 0;

        //buscamos el primer punto



//        //A partir de la coordenada del enemigo, determinamos la casilla en la que está
//        this.coor = this.gl.determinaCasilla(this.x, this.y);
//        this.casillaActual = this.gl.casillas.get(this.coor.getX()).get(this.coor.getY());
//
//        //Determinamos la casilla siguiente
//        this.casillaSig = this.gl.casillas.get(this.coor.getX() + this.direccion.getY()).get(this.coor.getY() + this.direccion.getX());
//
//        boolean encontrado = false;
//        //Si la casilla siguiente no es un camino
//        if(!this.casillaSig.esCamino()){
//            //Mirar arriba y abajo
//            Casilla arriba = this.gl.casillas.get(this.coor.getX() + this.direccion.getY()).get(this.coor.getY() + this.direccion.getX() -1);
//            Casilla abajo = this.gl.casillas.get(this.coor.getX() + this.direccion.getY()).get(this.coor.getY() + this.direccion.getX() + 1);
//            if(arriba.esCamino()){
//                this.direccion.setX(0);
//                this.direccion.setY(1);
//                encontrado = true;
//            }
//            else if(abajo.esCamino()){
//                this.direccion.setX(0);
//                this.direccion.setY(-1);
//                encontrado = true;
//            }
//
//            if(!encontrado){
//                //Mirar derecha izquierda
//                if(boundsPath(casillaSig.coor)){
//                    Casilla dcha = this.gl.casillas.get(this.casillaSig.getCoor().getX() -1).get(this.casillaSig.getCoor().getY() + 1);
//                    Casilla izq = this.gl.casillas.get(this.casillaSig.getCoor().getX() -1).get(this.casillaSig.getCoor().getY() -1);
//                    if(dcha.esCamino()){
//                        this.direccion.setX(1);
//                        this.direccion.setY(0);
//                        encontrado = true;
//                    }
//                    else if(izq.esCamino()){
//                        this.direccion.setX(-1);
//                        this.direccion.setY(0);
//                        encontrado = true;
//                    }
//                }
//            }
//        }
//
//
//        float movimiento = this.velocidad-this.ralentizar;
//
//
//        //Posicion con deltatime aplicado
//        float compX = (float)(this.x + (this.direccion.getX() * movimiento * deltaTime));
//        float compY = (float)(this.y + (this.direccion.getY() * movimiento * deltaTime));
//
//        //Casilla si le aplico el deltatime
//        Vector2D compCoor = this.gl.determinaCasilla(compX,compY);
//
//        //Si esa casilla esta en el tablero, la guardo
//        Casilla casillaComp;
//        if(boundsPath(compCoor)){
//             casillaComp = this.gl.casillas.get(compCoor.getX()).get(compCoor.getY());
//        }else
//            casillaComp =  this.casillaActual;
//
//
//        //Si por alguna razón (por un deltatime elevado al principio) el enemigo se sale del camino,
//        //lo devolvemos a la casilla valida
//        if(casillaComp.esCamino()){
//            this.x=compX;
//            this.y=compY;
//
//        }
//        this.ralentizar = 0;
    }

    /**
     * Metodo para renderizar al enemigo
     * @param gr Graphics
     */
    public void Render(Graphics gr){
        this.imagen.RenderCentrado((int)this.x,(int)this.y);
    }

    /**
     * Metodos para añadir el enemigo a la lista de ganadores
     */
    public void setWin() {this.win=true;}
    public boolean Win(){return this.win;}

    /**
     * Determina si esta muerto
     */
    public boolean Dead()
    {
        return vida<=0;
    }
}
