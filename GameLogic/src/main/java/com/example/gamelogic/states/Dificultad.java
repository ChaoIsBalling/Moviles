package com.example.gamelogic.states;

import com.example.androidengine.AndroidEngine;
import com.example.androidengine.State;
import com.example.androidengine.TouchEvent;
import org.json.JSONObject;
import java.util.ArrayList;
import com.example.androidengine.AndroidAudio;
import com.example.androidengine.AndroidGraphics;
import com.example.androidengine.AndroidMobile;
import com.example.gamelogic.managers.UIManager;

/**
 * Clase que representa el menú de seleccción de dificultad
 */
public class Dificultad implements State {
    private UIManager ui;
    //El archivo de guardado del juego
    private JSONObject save;
    //Referencias de modulos del motor
    private AndroidEngine engine;
    AndroidGraphics gr;
    AndroidMobile mobile;
    //Json del que vamos a leer
    JSONObject style;

    /**
     * Constructora del menú de dificultad con los tres botones que representan los tres modos de juego
     * @param engine
     */
    public Dificultad(AndroidEngine engine, AndroidMobile mobile,JSONObject save){
        this.save=save;
        this.engine = engine;
        this.mobile = mobile;
        this.style = engine.readJsonFile("Dificultad/style.json");//Archivo a leer
        //escondemos el banner
        this.mobile.setVisibleAdBanner(false);
    }
    @Override
    public void update(double deltaTime) { }

    /**
     * Renderiza la UI
     */
    @Override
    public void render(AndroidGraphics gr) {
        this.ui.render(gr);
    }

    /**
     * setter del graphics
     * @param gr Graphics
     */
    @Override
    public void setGraphics(AndroidGraphics gr) {
        this.gr=gr;
        this.ui = new UIManager(this.style,this.engine,gr);

        //Seteamos los callback de los botones una vez leidos
        this.ui.getButtonUI("BUT_CORTA").setOnClickListener( () -> play(GameLogic.Dificultad.corto));
        this.ui.getButtonUI("BUT_LARGA").setOnClickListener( () -> play(GameLogic.Dificultad.largo));
        this.ui.getButtonUI("BUT_INFINITO").setOnClickListener( () -> play(GameLogic.Dificultad.infinito));
        this.ui.getButtonUI("BUT_VOLVER").setOnClickListener(() -> returnMenu());
    }

    /**
     * Metodo para ir a la pantalla de juego con la dificultad correspondiente
     * @param dif la dificultad de la proxima partida
     */
    void play(GameLogic.Dificultad dif){
        GameLogic gameLogic = new GameLogic(this.engine,this.mobile, dif,this.save);
        this.engine.setState(gameLogic);
    }

    /**
     * Metodo para volver al menu inicial
     */
    void returnMenu(){
        Menu menu = new Menu(this.engine,this.mobile,this.save);
        this.engine.setState(menu);
    }

    /**
     * Dependiendo del modo elegido, iremos al estado GameLogic
     * @param list Lista de eventos
     * @param elapseTime Tiempo trascurrido
     */
    @Override
    public void handleInput(ArrayList<TouchEvent> list, double elapseTime) {
        for (TouchEvent e : list) {
            //Si es nulo no se procesa
            if (e == null || e.type == null) {
                continue;
            }
            switch (e.type) {
                case TOUCH_DOWN:
                    this.ui.handleInput(e);
            }
        }
    }
    @Override
    public void setAudio(AndroidAudio audio) {}
    @Override
    public void setMobile(AndroidMobile mobile) {}
    @Override
    public JSONObject getSave() { return this.save;}
}
