package com.example.gamelogic;

import com.example.engine.Engine;
import com.example.engine.Graphics;
import com.example.engine.Mobile;
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


    //Referencias al Audio Manager, al motor y a Graphics y a Mobile
    private Audio audio;
    Engine engine;
    Graphics gr;
    Mobile mob;

    /**
     * Constructora del menú
     */
    public Menu(Engine engine){
        this.engine = engine;
        JSONObject botones=engine.readJsonFile("Menu/style.json");

        this.botonInicial = new Button(botones.getJSONObject("BotonInicial"));
        this.botonInicial.setText(new Text(botones.getJSONObject("TextoBoton")));

        this.textoInicial = new Text(botones.getJSONObject("TextoInicial"));

        this.botonAventura = new Button(botones.getJSONObject("BotonAventura"));
        this.botonAventura.setText( new Text(botones.getJSONObject("TextoAventura")));

        this.botonTienda = new Button(botones.getJSONObject("BotonTienda"));
        this.botonTienda.setText( new Text(botones.getJSONObject("TextoTienda")));

        this.textoDiamantes = new Text(botones.getJSONObject("TextoDiamantes"));
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
            if(e == null){
                return;
            }
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
                            //this.engine.showNotificacion("Hola","TOnto");
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

    /**
     * Inicializa Mobile
     * @param mobile Interfaz Mobile
     */
    @Override
    public void setMobile(Mobile mobile) {
        this.mob = mobile;
    }
}
