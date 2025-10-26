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

    //mapa
    ArrayList<ArrayList<Square>> casillas;
    ArrayList<ArrayList<String>> leer;

    public GameLogic(){
        this.casillas = new ArrayList<ArrayList<Square>>();
        this.leer = new ArrayList<ArrayList<String>>();
        this.InitPrueba();
        for (int i =0; i<this.fil;i++){
            ArrayList<Square> fila = new ArrayList<Square>();
            for(int j =0; j<this.col;j++){
                if(this.leer.get(i).get(j) == "h"){
                    Square cuad = new Square((float)(j*35+30),(float)(i*35+50),35,35,false);
                    cuad.setColor(0xff000000);
                    fila.add(cuad);
                }
                else{
                    Square cuad = new Square((float)(j*35+30),(float)(i*35+50),35,35,true);
                    cuad.setColor(0xff944d03);
                    fila.add(cuad);
                }

            }
            this.casillas.add(fila);
        }

        inicializarUI();
    }

    @Override
    public void update(double deltaTime) {
        if(!this.firstFrame){
            this.firstFrame = !this.firstFrame;
        }
        else {

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