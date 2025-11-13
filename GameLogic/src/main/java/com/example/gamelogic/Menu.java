package com.example.gamelogic;

import com.example.engine.Engine;
import com.example.engine.Graphics;
import com.example.engine.State;
import com.example.engine.TouchEvent;
import com.example.engine.Audio;
import com.example.engine.Sound;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;

public class Menu implements State {

    private Button botonInicial;
    private Text textoInicial;

    private Button botonAventura;

    private Button botonTienda;

    private Text textoDiamantes;

    private Audio audio;
    Engine engine;

    Graphics gr;


    public Menu(Engine engine){
        this.engine = engine;

        this.botonInicial = new Button(150, 250, 200,50,true,20);
        this.botonInicial.setColor(0xFF999999);
        Text textoBoton = new Text("Inika-Regular.ttf","Jugar",0,0,30,true,true);
        textoBoton.setColor(0xff00ffff);
        this.botonInicial.setText(textoBoton);
        this.textoInicial = new Text("Inika-Regular.ttf","TowerDefense",300,150,40,true,true);
        this.textoInicial.setColor(0Xff000000);

        this.botonAventura = new Button(450, 250, 200,50,true,20);
        this.botonAventura.setColor(0xFF999999);
        Text textoAventura = new Text("Inika-Regular.ttf","Aventura",0,0,30,true,true);
        textoAventura.setColor(0xff00ffff);
        this.botonAventura.setText(textoAventura);

        this.botonTienda = new Button(150, 350, 200,50,true,20);
        this.botonTienda.setColor(0xFF00ffff);
        Text textoTienda = new Text("Inika-Regular.ttf","Tienda",0,0,30,true,true);
        textoTienda.setColor(0xff000000);
        this.botonTienda.setText(textoTienda);

        this.textoDiamantes = new Text("Inika-Regular.ttf","0",450,350,40,true,true);
        this.textoDiamantes.setColor(0Xff00ffff);

    }
    @Override
    public void update(double deltatime) {

    }

    @Override
    public void render(Graphics gr) {
        botonInicial.Render(gr);
        textoInicial.Render(gr);
        botonAventura.Render(gr);
        botonTienda.Render(gr);
        textoDiamantes.Render(gr);
    }

    @Override
    public void setGraphics(Graphics gr) {
        this.gr=gr;
    }

    @Override
    public void handleInput(ArrayList<TouchEvent> list, double elapseTime) {
        for(TouchEvent e: list){
            switch (e.type){
                case TOUCH_DOWN:
                    if(this.botonInicial.contains(e.x,e.y)){
                        Dificultad dificultad = new Dificultad(this.engine);
                        this.engine.setState(dificultad);

                        OutputStream os = this.engine.writeFile("hola.txt");
                        String s = "dasdaw";
                        try {
                            os.write(s.getBytes());
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }

                        InputStream is = this.engine.readFile2("hola.txt");
                        InputStreamReader inputStreamReader= new InputStreamReader(is);
                        BufferedReader bf = new BufferedReader(inputStreamReader);

                        try{
                            String content = bf.readLine();
                            //menu.setText(this.e.getGraphics(), content,30);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                    else if(this.botonAventura.contains(e.x,e.y)){
                        Mundo mundo = new Mundo(this.engine);
                        this.engine.setState(mundo);
                    }
                    else if(this.botonTienda.contains(e.x,e.y)){
                        Tienda tienda = new Tienda(this.engine);
                        this.engine.setState(tienda);
                    }
                    break;
                case TOUCH_UP:
                    System.out.println("Has soltado el raton");
                    break;
                case TOUCH_MOVE:
                    break;
            }
        }

    }

    @Override
    public void setAudio(Audio audio) {
    this.audio=audio;
    }
}
