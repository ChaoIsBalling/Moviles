package com.example.gamelogic.states;

import com.example.androidengine.AndroidEngine;
import com.example.androidengine.State;
import com.example.androidengine.TouchEvent;
import com.example.androidengine.AndroidGraphics;
import com.example.androidengine.AndroidAudio;
import com.example.androidengine.AndroidMobile;
import com.example.gamelogic.VisualElements.button.Button;
import com.example.gamelogic.VisualElements.button.ButtonClickListener;
import com.example.gamelogic.managers.UIManager;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Clase que implementa el menú inicial
 */
public class Menu implements State {
    private String fondo;
    //El archivo de guardado del juego
    private JSONObject save;

    //Referencias al Audio Manager, al motor y a Graphics y a Mobile
    private AndroidAudio audio;
    AndroidEngine engine;
    AndroidGraphics gr;
    AndroidMobile mobile;

    //JSONObject botones;
    JSONObject style;

    UIManager ui;

    /**
     * Constructora del menú
     */
    public Menu(AndroidEngine engine, AndroidMobile mobile, JSONObject save){
        this.engine = engine;
        this.mobile = mobile;
        this.save=save;


        this.mobile.setVisibleAdBanner(true);
    }

    @Override
    public void update(double deltaTime) {}

    /**
     * Renderiza la UI
     * @param gr Graphics del motor
     */
    @Override
    public void render(AndroidGraphics gr) {
        this.ui.render(gr);
    }

    /**
     * Inicializa Graphics
     * @param gr Graphics
     */
    @Override
    public void setGr(AndroidGraphics gr) {
        this.gr=gr;
        this.style = engine.readJsonFile("Menu/style.json");

        this.ui = new UIManager(this.style , this.engine, this.gr);
        this.ui.setAllCallbacks();
        //this.ui.createPrefabs(engine.readJsonFile("Menu/prefabs.json"),3);
        //Leemos valores guardados
        this.fondo="#FFFFFFFF"; //Fondo por defecto
        try {
            int numGemasGuardadas = this.save.getInt("gems");
            this.ui.getTextUI("TEXT_DIAMANTES").setText(String.valueOf(numGemasGuardadas));
            this.fondo = this.save.getString("background");
            this.gr.setColorClear(this.fondo);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Metodo para setear el callback de los botones de menu
     * @param b el boton al que le pasamos el callback
     */
    public void setCallbackButtonMenu(Button b) {
        String state= null;
        try {
            state = b.getCallback().getString("state");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        ButtonClickListener listener=null;
        switch (state) {
            case "Dificultad":
                listener=()-> {this.engine.setState( new Dificultad(this.engine, this.mobile, this.save));};
                break;
            case "Aventura":
                listener=() -> {this.engine.setState(new Mundo(this.engine, this.mobile, 1, this.save));};
                break;
            case "Tienda":
                listener=() -> {this.engine.setState(new Tienda(this.engine,this.mobile,this.save));};
                break;
        }
        b.setOnClickListener(listener);
    }

    /**
     * Gestiona la entrada del jugador
     * @param list Lista de eventos
     * @param elapseTime Tiempo trascurrido
     */
    @Override
    public void handleInput(ArrayList<TouchEvent> list, double elapseTime) {
        for(TouchEvent e: list){
            //Si es nulo no se procesa
            if (e == null || e.type == null) {
                continue;
            }
            switch (e.type){
                    case TOUCH_DOWN:
                        this.ui.handleInput(e);
                        break;
            }
        }
    }

    /**
     * Inicializa el Audio
     * @param audio Interfaz Audio
     */
    @Override
    public void setAudio(AndroidAudio audio) { this.audio=audio; }

    /**
     * Inicializa Mobile
     * @param mobile Interfaz Mobile
     */
    @Override
    public void setMobile(AndroidMobile mobile) { this.mobile = mobile; }

    @Override
    public JSONObject getSave() { return this.save; }
}
