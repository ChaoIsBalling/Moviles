package com.example.gamelogic;

import com.example.engine.Graphics;
import com.example.engine.State;
import com.example.engine.TouchEvent;

import java.util.ArrayList;
import java.util.List;

public class GameLogic implements State {
    boolean firstFrame = false;

    //Botones
    private Button botonMejoraTriangulos;
    private Button botonMejoraCuadrados;
    private Button botonMejoraHexagonos;

    private Square figuraBotonCuadrado;
    private Triangle figuraBotonTriangulo;
    private Hexagon figuraBotonHexagono;

    private Text costeMejoraTriangulos;

    private Text costeMejoraCuadrados;

    private Text costeMejoraHexagonos;

    int fil = 8;
    int col = 15;

    float vida = 0;
    float dinero = 0;

    //mapa
    ArrayList<ArrayList<Casilla>> casillas;
    ArrayList<Tower> torres;
    ArrayList<Enemy> enemigos;
    ArrayList<ArrayList<String>> leer;

    public GameLogic(){
        this.torres = new ArrayList<Tower>();
        this.enemigos=new ArrayList<Enemy>();
        this.casillas = new ArrayList<ArrayList<Casilla>>();
        this.leer = new ArrayList<ArrayList<String>>();
        this.InitPrueba();
        for (int i =0; i<this.fil;i++){
            ArrayList<Casilla> fila = new ArrayList<Casilla>();
            for(int j =0; j<this.col;j++){
                if(this.leer.get(i).get(j) == "h"){
                    Casilla casilla = new Casilla((float)(j*35+30),(float)(i*35+50),35,35,false,false);
                    casilla.setColor(0xff000000);
                    fila.add(casilla);
                }
                else{
                    Casilla casilla = new Casilla((float)(j*35+30),(float)(i*35+50),35,35,true,true);
                    casilla.setColor(0xff944d03);
                    fila.add(casilla);
                }

            }
            this.casillas.add(fila);
        }

        this.inicializarUI();

        this.torres.add(new ThunderTower(this.casillas.get(2).get(6).getX(),this.casillas.get(2).get(6).getY()));
        this.torres.get(0).setListaEnemigos(this.enemigos);
        this.enemigos.add(new Enemy(this.casillas.get(1).get(6).getX(),this.casillas.get(1).get(6).getY(),10,10,10,10,Tipo.rayo));
    }

    @Override
    public void update(double deltaTime) {
        if(!this.firstFrame){
            this.firstFrame = !this.firstFrame;
        }
        else {
            for(int i = 0; i<this.torres.size(); i++){
                this.torres.get(i).Update(deltaTime);
            }
            for(int i = 0; i<this.enemigos.size(); i++){
                this.enemigos.get(i).Update(deltaTime);
            }
        }

    }

    @Override
    public void render(Graphics gr) {
        //gr.setColor(0x00000000);
        for (int i =0; i<this.fil;i++){
            for(int j =0; j<this.col;j++){
                this.casillas.get(i).get(j).Render(gr);
            }
        }
        for(int i = 0; i<this.torres.size(); i++){
            this.torres.get(i).Render(gr);
        }
        for(int i = 0; i<this.enemigos.size(); i++){
            this.enemigos.get(i).Render(gr);
        }
        this.botonMejoraCuadrados.Render(gr);
        this.botonMejoraTriangulos.Render(gr);
        this.botonMejoraHexagonos.Render(gr);
    }

    @Override
    public void handleInput(ArrayList<TouchEvent> list, double elapseTime) {

        for(TouchEvent e: list){

            switch (e.type){
                case TOUCH_DOWN:
                    
                    break;
                case TOUCH_UP:

                    break;
                case TOUCH_MOVE:
                    System.out.println("Dedooo");
                    break;
            }
        }
    }

    public void inicializarUI(){
        this.botonMejoraCuadrados = new Button(500,350,50,50,true,20);
        this.botonMejoraTriangulos = new Button(440,350,50,50,true,20);
        this.botonMejoraHexagonos = new Button(560,350,50,50,true,20);

        botonMejoraCuadrados.setColor(0xFF999999);
        botonMejoraTriangulos.setColor(0xFF999999);
        botonMejoraHexagonos.setColor(0xFF999999);

        this.figuraBotonCuadrado = new Square(1,-5,30,30,true);
        this.figuraBotonCuadrado.setColor(0xFFC8A2C8);
        this.figuraBotonTriangulo = new Triangle(1,1,15, true);
        this.figuraBotonHexagono = new Hexagon(1,-5,15,true);
        this.figuraBotonHexagono.setColor(0xFFFF0000);

        this.botonMejoraCuadrados.setFigura(this.figuraBotonCuadrado);
        this.botonMejoraTriangulos.setFigura(this.figuraBotonTriangulo);
        this.botonMejoraHexagonos.setFigura(this.figuraBotonHexagono);

        this.costeMejoraCuadrados = new Text("Inika-Regular.ttf","150",0,15,15,true,true);
        this.botonMejoraCuadrados.setText(this.costeMejoraCuadrados);

        this.costeMejoraTriangulos = new Text("Inika-Regular.ttf","100",0,15,15,true,true);
        this.botonMejoraTriangulos.setText(this.costeMejoraTriangulos);

        this.costeMejoraHexagonos = new Text("Inika-Regular.ttf","200",0,15,15,true,true);
        this.botonMejoraHexagonos.setText(this.costeMejoraHexagonos);
    }

    public void InitPrueba(){
        ArrayList<String> filaL = new ArrayList<String>();
        filaL.addAll(List.of("h","h","h","h","h","h","h","h","h","h","h","h","h","h","h"));
        this.leer.add(filaL);
        filaL = new ArrayList<String>();
        filaL.addAll(List.of("c","c","c","c","c","c","c","c","h","h","h","h","h","h","h"));
        this.leer.add(filaL);
        filaL = new ArrayList<String>();
        filaL.addAll(List.of("h","h","h","h","h","h","h","c","h","h","h","h","h","h","h"));
        this.leer.add(filaL);
        filaL = new ArrayList<String>();
        filaL.addAll(List.of("h","h","h","h","h","h","h","c","h","h","h","h","h","h","h"));
        this.leer.add(filaL);
        filaL = new ArrayList<String>();
        filaL.addAll(List.of("h","h","h","h","h","h","h","c","h","h","h","h","h","h","h"));
        this.leer.add(filaL);
        filaL = new ArrayList<String>();
        filaL.addAll(List.of("h","h","h","h","h","h","h","c","h","h","h","h","h","h","h"));
        this.leer.add(filaL);
        filaL = new ArrayList<String>();
        filaL.addAll(List.of("h","h","h","h","h","h","h","c","c","c","c","c","c","c","c"));
        this.leer.add(filaL);
        filaL = new ArrayList<String>();
        filaL.addAll(List.of("h","h","h","h","h","h","h","h","h","h","h","h","h","h","h"));
        this.leer.add(filaL);
    }


}