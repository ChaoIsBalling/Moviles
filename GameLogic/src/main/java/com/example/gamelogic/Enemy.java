package com.example.gamelogic;

import com.example.engine.Graphics;

public class Enemy {
    Circle circulo;
    float vida;
    Vector2D direccion;

    float velocidad = 100;
    float defensa;
    float resistencia;
    Tipo tipo;
    GameLogic gl;
    Vector2D coor;

    Casilla casillaSig;

    Casilla casillaActual;
    public Enemy(float x, float y, float vida, float velocidad, float defensa, float resistencia, Tipo tipoRes, GameLogic gl){
        this.circulo = new Circle(x,y,5,true);
        this.circulo.setColor(0xff00ff00);
        this.vida=vida;
        this.direccion = new Vector2D(1,0);
        this.velocidad = velocidad;
        this.defensa = defensa;
        this.resistencia = resistencia;
        this.tipo = tipoRes;
        this.gl = gl;
        this.coor = gl.determinaCasilla(this.circulo.getY(), this.circulo.getX());
    }
    public float getX(){
        return this.circulo.getX();
    }
    public float getY(){
        return this.circulo.getY();
    }
    public void damage(float damage, Tipo tipo){

    }

    private boolean boundsPath(Vector2D c){
        return ((c.getX() < this.gl.fil && c.getX() >= 0) &&
                (c.getY() >= 0 && c.getY() < this.gl.col));
    }
    public void Update(double deltaTime){

        this.coor = this.gl.determinaCasilla(this.circulo.getX(), this.circulo.getY());
        this.casillaActual = this.gl.casillas.get(this.coor.getX()).get(this.coor.getY());

        //Si llega a la casilla final
        if(this.casillaActual.coor.getX() == 6 && this.casillaActual.coor.getY() == 13){
            this.direccion.setX(0);
            this.direccion.setY(0);
        }

        this.casillaSig = this.gl.casillas.get(this.coor.getX() + this.direccion.getY()).get(this.coor.getY() + this.direccion.getX());
        boolean encontrado = false;
        //Si la casilla siguiente no es un camino
        if(!this.casillaSig.esCamino()){
            //Mirar arriba y abajo
            Casilla arriba = this.gl.casillas.get(this.coor.getX() + this.direccion.getY()).get(this.coor.getY() + this.direccion.getX() -1);
            Casilla abajo = this.gl.casillas.get(this.coor.getX() + this.direccion.getY()).get(this.coor.getY() + this.direccion.getX() + 1);
            if(arriba.esCamino()){
                this.direccion.setX(0);
                this.direccion.setY(1);
                encontrado = true;
            }
            else if(abajo.esCamino()){
                this.direccion.setX(0);
                this.direccion.setY(-1);
                encontrado = true;
            }

            if(!encontrado){
                //Mirar derecha izquierda
                if(boundsPath(casillaSig.coor)){
                    Casilla dcha = this.gl.casillas.get(this.casillaSig.getCoor().getX() -1).get(this.casillaSig.getCoor().getY() + 1);
                    Casilla izq = this.gl.casillas.get(this.casillaSig.getCoor().getX() -1).get(this.casillaSig.getCoor().getY() -1);

                    if(dcha.esCamino()){
                        this.direccion.setX(1);
                        this.direccion.setY(0);
                        encontrado = true;
                    }
                    else if(izq.esCamino()){
                        this.direccion.setX(-1);
                        this.direccion.setY(0);
                        encontrado = true;
                    }

                }

            }

        }


        this.circulo.setX((float)(this.circulo.getX() + (this.direccion.getX() * velocidad * deltaTime)));
        this.circulo.setY((float)(this.circulo.getY() + (this.direccion.getY() * velocidad * deltaTime)));

    }
    public void Render(Graphics gr){
        this.circulo.Render(gr);
    }

    public void setDead()
    {
        vida=0;
    }
    public boolean Dead()
    {
        return vida<=0;
    }
}
