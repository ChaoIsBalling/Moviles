package com.example.gamelogic;

import com.example.engine.Graphics;
import com.example.engine.State;
import com.example.engine.TouchEvent;
import com.example.engine.Engine;
import com.example.engine.Audio;

import java.util.ArrayList;
import java.lang.Integer;
public class GameLogic implements State {

    //Botones
    private Button botonMejoraTriangulos;
    private Button botonMejoraCuadrados;
    private Button botonMejoraHexagonos;

    private Button botonMejoraAtaque;
    private Button botonMejoraRango;
    private Button botonMejoraVelocidad;
    private Square figuraBotonCuadrado;
    private Triangle figuraBotonTriangulo;
    private Hexagon figuraBotonHexagono;

    private Text costeMejoraTriangulos;

    private Text costeMejoraCuadrados;

    private Text costeMejoraHexagonos;

    private Text costeMejoraAtaque;

    private Text costeMejoraRango;

    private Text costeMejoraVelocidad;

    private Image imagenMejoraAtaque;

    private Image imagenMejoraRango;

    private Image imagenMejoraVelocidad;

    private Square franjaGris;

    int fil;
    int col;

    int vida = 0;
    float dinero = 0;

    float IniX;
    float IniY;

    float FinX;
    float FinY;

    float anchoCasilla = 35;

    float altoCasilla = 35;


    //mapa
    ArrayList<ArrayList<Casilla>> casillas;
    ArrayList<Tower> torres;
    ArrayList<Enemy> enemigos;
    ArrayList<Enemy> deadEnemies;
    ArrayList<String> leer;

    Engine engine;

    Audio audio;
    Tower torreSeleccionada;

    Text textoV;
    Text textoD;

    float damTorre = 2;
    float ranTorre = (float) 11.7;
    float velTorre = (float) -0.2;

    
    private enum Estado{
        nada,botonRayo,botonFuego,botonHielo,torre
    }
    private Estado estado = Estado.nada;

    public enum Dificultad{
        corto,largo,infinito
    }
    private Dificultad dificultad;
    public GameLogic(Engine engine, Dificultad dificultad){
        this.engine=engine;
        this.vida=10;
        this.dinero = 300;
        this.dificultad = dificultad;
        this.torres = new ArrayList<Tower>();
        this.enemigos=new ArrayList<Enemy>();
        this.deadEnemies=new ArrayList<Enemy>();
        this.casillas = new ArrayList<ArrayList<Casilla>>();
        this.textoV = new Text("Inika-Regular.ttf",String.valueOf(this.vida),30,340,20);
        this.textoD = new Text("Inika-Regular.ttf",String.valueOf(this.dinero),30,370,20);
        this.franjaGris = new Square(300,370,600,100,true);
        this.franjaGris.setColor(0xFF999999);
        this.leer = engine.readFile("mapa1.txt");
       this.fil=Integer.parseInt(leer.get(0));
       this.col=Integer.parseInt(leer.get(1));

        for (int i =0; i<this.fil;i++){
            ArrayList<Casilla> fila = new ArrayList<Casilla>();
            for(int j =0; j<this.col;j++){
                if(leer.get(2+i).charAt(j) == 'h'){
                    Casilla casilla = new Casilla((float)(j*35+30),(float)(i*35+50),this.anchoCasilla,this.altoCasilla,false,false);
                    casilla.setColor(0xff000000);
                    casilla.setCoor(new Vector2D(i,j));
                    fila.add(casilla);
                }
                else{
                    Casilla casilla = new Casilla((float)(j*35+30),(float)(i*35+50),this.anchoCasilla,this.altoCasilla,true,true);
                    casilla.setColor(0xff944d03);
                    casilla.setCoor(new Vector2D(i,j));
                    fila.add(casilla);
                    if(j == 0){
                        this.IniX = j*35+30;
                        this.IniY = i*35+50;
                    }
                    if(j == this.col - 1){
                        this.FinX = j*35+30;
                        this.FinY = i*35+50;
                    }
                }

            }
            this.casillas.add(fila);
        }

        this.inicializarUI();

        this.torres.add(new ThunderTower(this.casillas.get(2).get(6).getX(),this.casillas.get(2).get(6).getY()));
        this.torres.get(0).setListaEnemigos(this.enemigos);
        this.casillas.get(2).get(6).setTorre(this.torres.get(0));
        this.torres.add(new FireTower(this.casillas.get(0).get(6).getX(),this.casillas.get(0).get(6).getY()));
        this.torres.get(1).setListaEnemigos(this.enemigos);
        this.casillas.get(0).get(6).setTorre(this.torres.get(1));
        this.torres.add(new IceTower(this.casillas.get(2).get(8).getX(),this.casillas.get(2).get(8).getY()));
        this.torres.get(2).setListaEnemigos(this.enemigos);
        this.casillas.get(2).get(8).setTorre(this.torres.get(2));
        this.enemigos.add(new Enemy(this.IniX,this.IniY,10,40,10,10,Tipo.rayo, this));
        this.enemigos.add(new Enemy(this.IniX-15,this.IniY,10,40,10,10,Tipo.rayo, this));
    }

    @Override
    public void update(double deltaTime) {
        for(int i = 0; i<this.torres.size(); i++){
            this.torres.get(i).Update(deltaTime);
        }
        for(int i = 0; i<this.enemigos.size(); i++){
            this.enemigos.get(i).Update(deltaTime);
            //Si el enemigo llega a la casilla final, se elimina
            if(this.enemigos.get(i).getX() >= this.FinX &&
                    this.enemigos.get(i).getY() >= this.FinY){
                this.vida--;
                this.enemigos.get(i).setWin();
            }
            if(this.enemigos.get(i).Dead()){

                deadEnemies.add(this.enemigos.get(i));
                this.dinero+=25;
            }
            if(this.enemigos.get(i).Win()){

                deadEnemies.add(this.enemigos.get(i));
            }

        }
        for(int i=0;i<deadEnemies.size();i++){
            this.enemigos.remove(this.deadEnemies.get(i));
        }
        this.deadEnemies.clear();
            
        this.textoV.setText(String.valueOf(this.vida));
        this.textoD.setText(String.valueOf(this.dinero));


    }

    //Dada una posición (x,y) se determina en que casilla está a partir del ancho y alto de la casilla
    public Vector2D determinaCasilla(float x, float y){
        int offsetX = 30;
        int offsetY = 50;

        int j = (int) ((x - offsetX) / this.anchoCasilla);
        int i = (int) ((y - offsetY) / this.altoCasilla);

        Vector2D c = new Vector2D(i,j);
        //System.out.println("("+c.getX()+","+c.getY()+")");
        return c;
    }

    public Vector2D determinaCasillaRaton(float x, float y){
        if(x < 30 - this.anchoCasilla/2 || y < 50 -this.altoCasilla/2){
            return new Vector2D(-1,-1);
        }
        int offsetX = 30;
        int offsetY = 50;

        int j = (int) (((x +(this.anchoCasilla/2)- offsetX)) / this.anchoCasilla);
        int i = (int) (((y + (this.altoCasilla/2)- offsetY)) / this.altoCasilla);

        Vector2D c = new Vector2D(i,j);
        //System.out.println("("+c.getX()+","+c.getY()+")");

        return c;
    }

    @Override
    public void render(Graphics gr) {
        //gr.setColor(0x00000000);
        for (int i =0; i<this.fil;i++){
            for(int j =0; j<this.col;j++){
                this.casillas.get(i).get(j).Render(gr);
            }
        }
        for(int i = 0; i<this.enemigos.size(); i++){
            this.enemigos.get(i).Render(gr);
        }
        for(int i = 0; i<this.torres.size(); i++){
            this.torres.get(i).Render(gr);
        }
        this.franjaGris.Render(gr);
        if(this.estado != Estado.torre){
            this.botonMejoraCuadrados.Render(gr);
            this.botonMejoraTriangulos.Render(gr);
            this.botonMejoraHexagonos.Render(gr);
        }
        else{
            this.botonMejoraAtaque.Render(gr);
            this.botonMejoraRango.Render(gr);
            this.botonMejoraVelocidad.Render(gr);
            gr.setColor(0xff000000);
            gr.pintarCirculo(this.torreSeleccionada.getX(),this.torreSeleccionada.getY(),this.torreSeleccionada.getRange());
        }
        this.textoV.Render(gr);
        this.textoD.Render(gr);
        this.imagenMejoraAtaque.Render(gr);
    }



    public void inicializarUI() {
        this.botonMejoraCuadrados = new Button(500, 360, 50, 50, true, 20);
        this.botonMejoraTriangulos = new Button(440, 360, 50, 50, true, 20);
        this.botonMejoraHexagonos = new Button(560, 360, 50, 50, true, 20);

        this.botonMejoraAtaque = new Button(440, 360, 50, 50, true, 20);
        this.botonMejoraRango = new Button(500, 360, 50, 50, true, 20);
        this.botonMejoraVelocidad = new Button(560, 360, 50, 50, true, 20);

        this.botonMejoraCuadrados.setColor(0xFFffffff);
        this.botonMejoraTriangulos.setColor(0xFFffffff);
        this.botonMejoraHexagonos.setColor(0xFFffffff);

        this.botonMejoraAtaque.setColor(0xFFffffff);
        this.botonMejoraRango.setColor(0xFFffffff);
        this.botonMejoraVelocidad.setColor(0xFFffffff);

        this.figuraBotonCuadrado = new Square(1, -5, 30, 30, true);
        this.figuraBotonCuadrado.setColor(0xFFC8A2C8);
        this.figuraBotonTriangulo = new Triangle(1, 1, 15, true);
        this.figuraBotonHexagono = new Hexagon(1, -5, 15, true);
        this.figuraBotonHexagono.setColor(0xFFFF0000);

        this.botonMejoraCuadrados.setFigura(this.figuraBotonCuadrado);
        this.botonMejoraTriangulos.setFigura(this.figuraBotonTriangulo);
        this.botonMejoraHexagonos.setFigura(this.figuraBotonHexagono);

        this.costeMejoraCuadrados = new Text("Inika-Regular.ttf", "150", 0, 15, 15, true, true);
        this.botonMejoraCuadrados.setText(this.costeMejoraCuadrados);

        this.costeMejoraTriangulos = new Text("Inika-Regular.ttf", "100", 0, 15, 15, true, true);
        this.botonMejoraTriangulos.setText(this.costeMejoraTriangulos);

        this.costeMejoraHexagonos = new Text("Inika-Regular.ttf", "200", 0, 15, 15, true, true);
        this.botonMejoraHexagonos.setText(this.costeMejoraHexagonos);

        this.costeMejoraAtaque = new Text("Inika-Regular.ttf", "75", 0, 15, 15, true, true);
        this.botonMejoraAtaque.setText(this.costeMejoraAtaque);

        this.costeMejoraRango = new Text("Inika-Regular.ttf", "75", 0, 15, 15, true, true);
        this.botonMejoraRango.setText(this.costeMejoraRango);

        this.costeMejoraVelocidad = new Text("Inika-Regular.ttf", "100", 0, 15, 15, true, true);
        this.botonMejoraVelocidad.setText(this.costeMejoraVelocidad);

        this.imagenMejoraAtaque = new Image("Espada.png",0,0);
        this.botonMejoraAtaque.setImagen(this.imagenMejoraAtaque);

        this.imagenMejoraRango = new Image("Arco.png",0,0);
        this.botonMejoraRango.setImagen(this.imagenMejoraRango);

        this.imagenMejoraVelocidad = new Image("Reloj.png",0,0);
        this.botonMejoraVelocidad.setImagen(this.imagenMejoraVelocidad);
    }
    @Override
    public void handleInput(ArrayList<TouchEvent> list, double elapseTime) {

        for(TouchEvent e: list){

            switch (e.type){
                case TOUCH_DOWN:
                    switch (this.estado){
                        case nada:
                            if(this.botonMejoraTriangulos.contains(e.x,e.y)){
                                this.cambiarEstado(Estado.botonRayo);
                            }
                            else if(this.botonMejoraHexagonos.contains(e.x,e.y)){
                                this.cambiarEstado(Estado.botonFuego);
                            }
                            else if(this.botonMejoraCuadrados.contains(e.x,e.y)){
                                this.cambiarEstado(Estado.botonHielo);
                            }
                            else {
                                Vector2D casilla = this.determinaCasillaRaton(e.x,e.y);
                                //System.out.println("("+casilla.getX()+","+casilla.getY()+")");
                                if(casilla.getX() < this.fil && casilla.getY() < this.col && casilla.getX()>= 0 && casilla.getY()>=0){
                                    Tower torre = this.casillas.get(casilla.getX()).get(casilla.getY()).getTorre();
                                    if(torre != null){
                                        this.torreSeleccionada = torre;
                                        this.cambiarEstado(Estado.torre);
                                    }
                                }
                            }
                            break;
                        case torre:
                            Vector2D casillaT = this.determinaCasillaRaton(e.x,e.y);
                            if(this.botonMejoraAtaque.contains(e.x,e.y) && this.dinero >= 75){
                                this.torreSeleccionada.UpdateAttack(this.damTorre);
                                this.dinero -= 75;
                            }
                            else if(this.botonMejoraRango.contains(e.x,e.y) && this.dinero >= 75){
                                this.torreSeleccionada.UpdateRange(this.ranTorre);
                                this.dinero -= 75;
                            }
                            else if(this.botonMejoraVelocidad.contains(e.x,e.y) && this.dinero >= 75){
                                this.torreSeleccionada.UpdateFireRate(this.velTorre);
                                this.dinero -= 100;
                            }
                            else if(casillaT.getX() < this.fil && casillaT.getY() < this.col && casillaT.getX()>= 0 && casillaT.getY()>=0){
                                Tower torre = this.casillas.get(casillaT.getX()).get(casillaT.getY()).getTorre();
                                if(torre != this.torreSeleccionada && torre != null){
                                    this.torreSeleccionada = torre;
                                }
                                else{
                                    this.cambiarEstado(Estado.nada);
                                }
                            }
                            else{
                                this.cambiarEstado(Estado.nada);
                            }
                            break;
                        case botonRayo:
                            Vector2D casillaR = this.determinaCasillaRaton(e.x,e.y);
                            if(this.botonMejoraHexagonos.contains(e.x,e.y)){
                                this.cambiarEstado(Estado.botonFuego);
                            }
                            else if(this.botonMejoraCuadrados.contains(e.x,e.y)){
                                this.cambiarEstado(Estado.botonHielo);
                            }
                            else if(casillaR.getX() < this.fil && casillaR.getY() < this.col && casillaR.getX()>= 0 && casillaR.getY()>=0){
                                Tower torre = this.casillas.get(casillaR.getX()).get(casillaR.getY()).getTorre();
                                if(torre != null){
                                    this.torreSeleccionada = torre;
                                    this.cambiarEstado(Estado.torre);
                                }
                                else if(this.dinero >= 100 && !this.casillas.get(casillaR.getX()).get(casillaR.getY()).esCamino()){
                                    ThunderTower torreR = new ThunderTower(this.casillas.get(casillaR.getX()).get(casillaR.getY()).getX(),this.casillas.get(casillaR.getX()).get(casillaR.getY()).getY());
                                    torreR.setListaEnemigos(this.enemigos);
                                    torreR.setAudio(this.audio);
                                    this.casillas.get(casillaR.getX()).get(casillaR.getY()).setTorre(torreR);
                                    this.torres.add(torreR);
                                    this.dinero -= 100;
                                    this.cambiarEstado(Estado.nada);
                                }
                                else{
                                    this.cambiarEstado(Estado.nada);
                                }
                            }
                            else{
                                this.cambiarEstado(Estado.nada);
                            }
                            break;
                        case botonFuego:
                            Vector2D casillaF = this.determinaCasillaRaton(e.x,e.y);
                            if(this.botonMejoraTriangulos.contains(e.x,e.y)){
                                this.cambiarEstado(Estado.botonRayo);
                            }
                            else if(this.botonMejoraCuadrados.contains(e.x,e.y)){
                                this.cambiarEstado(Estado.botonHielo);
                            }
                            else if(casillaF.getX() < this.fil && casillaF.getY() < this.col && casillaF.getX()>= 0 && casillaF.getY()>=0){
                                Tower torre = this.casillas.get(casillaF.getX()).get(casillaF.getY()).getTorre();
                                if(torre != null){
                                    this.torreSeleccionada = torre;
                                    this.cambiarEstado(Estado.torre);
                                }
                                else if(this.dinero >= 200 && !this.casillas.get(casillaF.getX()).get(casillaF.getY()).esCamino()){
                                    FireTower torreF = new FireTower(this.casillas.get(casillaF.getX()).get(casillaF.getY()).getX(),this.casillas.get(casillaF.getX()).get(casillaF.getY()).getY());
                                    torreF.setListaEnemigos(this.enemigos);
                                    torreF.setAudio(this.audio);
                                    this.casillas.get(casillaF.getX()).get(casillaF.getY()).setTorre(torreF);
                                    this.torres.add(torreF);
                                    this.dinero -= 200;
                                    this.cambiarEstado(Estado.nada);
                                }
                                else{
                                    this.cambiarEstado(Estado.nada);
                                }
                            }
                            else{
                                this.cambiarEstado(Estado.nada);
                            }
                            break;
                        case botonHielo:
                            Vector2D casillaH = this.determinaCasillaRaton(e.x,e.y);
                            if(this.botonMejoraTriangulos.contains(e.x,e.y)){
                                this.cambiarEstado(Estado.botonRayo);
                            }
                            else if(this.botonMejoraHexagonos.contains(e.x,e.y)){
                                this.cambiarEstado(Estado.botonFuego);
                            }
                            else if(casillaH.getX() < this.fil && casillaH.getY() < this.col && casillaH.getX()>= 0 && casillaH.getY()>=0){
                                Tower torre = this.casillas.get(casillaH.getX()).get(casillaH.getY()).getTorre();
                                if(torre != null){
                                    this.torreSeleccionada = torre;
                                    this.cambiarEstado(Estado.torre);
                                }
                                else if(this.dinero >= 150 && !this.casillas.get(casillaH.getX()).get(casillaH.getY()).esCamino()){
                                    IceTower torreH = new IceTower(this.casillas.get(casillaH.getX()).get(casillaH.getY()).getX(),this.casillas.get(casillaH.getX()).get(casillaH.getY()).getY());
                                    torreH.setListaEnemigos(this.enemigos);
                                    torreH.setAudio(this.audio);
                                    this.casillas.get(casillaH.getX()).get(casillaH.getY()).setTorre(torreH);
                                    this.torres.add(torreH);
                                    this.dinero -= 150;
                                    this.cambiarEstado(Estado.nada);
                                }
                                else{
                                    this.cambiarEstado(Estado.nada);
                                }
                            }
                            else{
                                this.cambiarEstado(Estado.nada);
                            }
                            break;
                    }
                    break;
                case TOUCH_UP:

                    break;
                case TOUCH_MOVE:
                    System.out.println("Dedooo");
                    break;
            }
        }
    }

    @Override
    public void setAudio(Audio audio) {
        this.audio=audio;

        for(int i=0;i<this.torres.size();i++)
        {
            this.torres.get(i).setAudio(this.audio);
        }

    }

    private void cambiarEstado(Estado nuevoEstado) {
        switch (nuevoEstado) {
            case nada:
                this.botonMejoraTriangulos.setColor(0xFFffffff);
                this.botonMejoraHexagonos.setColor(0xFFffffff);
                this.botonMejoraCuadrados.setColor(0xFFffffff);
                this.estado = nuevoEstado;
                break;
            case torre:
                this.estado = nuevoEstado;
                break;
            case botonRayo:
                this.botonMejoraTriangulos.setColor(0xfffffb64);
                this.botonMejoraHexagonos.setColor(0xFFffffff);
                this.botonMejoraCuadrados.setColor(0xFFffffff);
                this.estado = nuevoEstado;
                break;
            case botonFuego:
                this.botonMejoraTriangulos.setColor(0xFFffffff);
                this.botonMejoraHexagonos.setColor(0xfffffb64);
                this.botonMejoraCuadrados.setColor(0xFFffffff);
                this.estado = nuevoEstado;
                break;
            case botonHielo:
                this.botonMejoraTriangulos.setColor(0xFFffffff);
                this.botonMejoraHexagonos.setColor(0xFFffffff);
                this.botonMejoraCuadrados.setColor(0xfffffb64);
                this.estado = nuevoEstado;
                break;

        }
    }


}