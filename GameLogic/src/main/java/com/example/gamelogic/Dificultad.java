package com.example.gamelogic;

import com.example.engine.Audio;
import com.example.engine.Engine;
import com.example.engine.Graphics;
import com.example.engine.Mobile;
import com.example.engine.State;
import com.example.engine.TouchEvent;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Clase que representa el menú de seleccción de dificultad
 */
public class Dificultad implements State {
    //Botones del menú de dificultad
    private Button botonCorto;
    private Button botonLargo;
    private Button botonInfinito;
    private Button botonVolver;

    //Referencias de modulos del motor
    private Engine engine;
    Graphics gr;

    Mobile mob;

    /**
     * Constructora del menú de dificultad con los tres botones que representan los tres modos de juego
     * @param engine
     */
    public Dificultad(Engine engine){
        this.engine = engine;
        JSONObject botones=engine.readJsonFile("Dificultad/style.json");
        botonCorto = new Button(botones.getJSONObject("BotonCorto"));
        botonCorto.setText(new Text(botones.getJSONObject("TextoC")));
        botonLargo = new Button(botones.getJSONObject("BotonLargo"));
        botonLargo.setText(new Text(botones.getJSONObject("TextoL")));
        botonInfinito = new Button(botones.getJSONObject("BotonInfinito"));
        botonInfinito.setText(new Text(botones.getJSONObject("TextoI")));
        this.botonVolver = new Button(botones.getJSONObject("BotonVolver"));
    }
    @Override
    public void update(double deltaTime) {

    }

    /**
     * Renderiza los botones
     */
    @Override
    public void render(Graphics gr) {
        botonCorto.Render(gr);
        botonLargo.Render(gr);
        botonInfinito.Render(gr);
        botonVolver.Render(gr);
    }

    /**
     * setter del graphics
     * @param gr Graphics
     */
    @Override
    public void setGraphics(Graphics gr) {
        this.gr=gr;
    }

    /**
     * Dependiendo del modo elegido, iremos al estado GameLogic
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
                    if(this.botonCorto.contains(e.x,e.y)){
                        GameLogic gameLogic = new GameLogic(this.engine, GameLogic.Dificultad.corto);
                        this.engine.setState(gameLogic);
                    }
                    else if(this.botonLargo.contains(e.x,e.y)){
                        GameLogic gameLogic = new GameLogic(this.engine, GameLogic.Dificultad.largo);
                        this.engine.setState(gameLogic);
                    }
                    else if(this.botonInfinito.contains(e.x,e.y)){
                        GameLogic gameLogic = new GameLogic(this.engine, GameLogic.Dificultad.infinito);//el -1 es para indicar que es infinito
                        this.engine.setState(gameLogic);
                    }
                    else if(this.botonVolver.contains(e.x,e.y)){
                        Menu menu = new Menu(this.engine);
                        this.engine.setState(menu);
                    }
                    break;
                case TOUCH_UP:

                    break;
                case TOUCH_MOVE:
                    break;
            }
        }
    }

    @Override
    public void setAudio(Audio audio) {

    }

    @Override
    public void setMobile(Mobile mobile) {
        this.mob = mobile;
    }

}
