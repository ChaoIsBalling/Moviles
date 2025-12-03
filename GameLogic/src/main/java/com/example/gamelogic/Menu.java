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
import org.json.JSONObject;
import org.json.JSONArray;
import java.util.ArrayList;

/**
 * Clase que implementa el menú inicial
 */
public class Menu implements State {

    //Boton y titulo inicial
    private Button botonInicial;
    private Text textoInicial;

    private Button botonAventura;

    private Button botonTienda;

    private Text textoDiamantes;


    //Referencias al Audio Manager, al motor y a Graphics
    private Audio audio;
    Engine engine;
    Graphics gr;


    /**
     * Constructora del menú
     */
    public Menu(Engine engine){
        this.engine = engine;

        JSONObject botones=engine.readJsonFile("Menu/style.json");
          JSONObject no= botones.getJSONObject("BotonInicial");
        this.botonInicial = new Button(no);
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
    public void update(double deltaTime) {
    }

    /**
     * Renderiza la UI
     * @param gr Graphics del motor
     */
    @Override
    public void render(Graphics gr) {
        botonInicial.Render(gr);
        textoInicial.Render(gr);
        botonAventura.Render(gr);
        botonTienda.Render(gr);
        textoDiamantes.Render(gr);
    }

    /**
     * Inicializa Graphics
     * @param gr Graphics
     */
    @Override
    public void setGraphics(Graphics gr) {
        this.gr=gr;
    }

    /**
     * Gestiona la entrada del jugador
     * @param list Lista de eventos
     * @param elapseTime Tiempo trascurrido
     */
    @Override
    public void handleInput(ArrayList<TouchEvent> list, double elapseTime) {
        for(TouchEvent e: list){
            switch (e.type){
                case TOUCH_DOWN:
                    if(this.botonInicial.contains(e.x,e.y)){
                        Dificultad dificultad = new Dificultad(this.engine);
                        this.engine.setState(dificultad);
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

                    break;
                case TOUCH_MOVE:
                    break;
            }
        }

    }

    /**
     * Inicializa el Audio
     * @param audio Interfaz Audio
     */
    @Override
    public void setAudio(Audio audio) {
    this.audio=audio;
    }
}
