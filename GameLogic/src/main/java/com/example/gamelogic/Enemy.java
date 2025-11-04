package com.example.gamelogic;

import com.example.engine.Graphics;

public class Enemy {
    Circle circulo;
    float vida;
    boolean win;
    Vector2D direccion;

    float velocidad = 100;
    float defensa;
    float resistencia;
    Tipo tipo;
    GameLogic gl;
    Vector2D coor;

    float ralentizar =0;

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
        if(tipo == Tipo.hielo && damage > this.ralentizar){
            float dam = damage;
            if(this.tipo == tipo){
                dam -= this.resistencia;
            }
            if(dam < 7){
                dam = 7;
            }
            this.ralentizar = dam;
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
    }

    private boolean boundsPath(Vector2D c){
        return ((c.getX() < this.gl.fil && c.getX() >= 0) &&
                (c.getY() >= 0 && c.getY() < this.gl.col));
    }
    public void Update(double deltaTime){

        this.coor = this.gl.determinaCasilla(this.circulo.getX(), this.circulo.getY());
        this.casillaActual = this.gl.casillas.get(this.coor.getX()).get(this.coor.getY());

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

        float movimiento = this.velocidad-this.ralentizar;

        this.circulo.setX((float)(this.circulo.getX() + (this.direccion.getX() * movimiento * deltaTime)));
        this.circulo.setY((float)(this.circulo.getY() + (this.direccion.getY() * movimiento * deltaTime)));

        this.ralentizar =0;
    }
    public void Render(Graphics gr){
        this.circulo.Render(gr);
    }

    public void setWin() {this.win=true;}
    public boolean Win(){return this.win;}
    public boolean Dead()
    {
        return vida<=0;
    }
}
