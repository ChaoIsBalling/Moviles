package com.example.gamelogic;

import com.example.engine.Audio;
import com.example.engine.Engine;
import com.example.engine.Graphics;
import com.example.engine.Mobile;
import com.example.engine.State;
import com.example.engine.TouchEvent;

import org.json.JSONObject;

import java.util.ArrayList;

public class Tienda implements State {

    private Button botonVolver;

    private Engine engine;

    private Text textoDiamantes;
    public Tienda(Engine engine){
        this.engine=engine;
        JSONObject botones=engine.readJsonFile("Tienda/style.json");
        this.botonVolver = new Button(botones.getJSONObject("BotonVolver"));

        this.textoDiamantes = new Text(botones.getJSONObject("TextoDiamantes"));
    }

    @Override
    public void update(double deltatime) {

    }

    @Override
    public void render(Graphics gr) {
        this.botonVolver.Render(gr);
        this.textoDiamantes.Render(gr);
    }

    @Override
    public void setGraphics(Graphics gr) {

    }

    @Override
    public void handleInput(ArrayList<TouchEvent> list, double elapseTime) {
        for(TouchEvent e: list){
            //Si es nulo no se procesa
            if (e == null || e.type == null) {
                continue;
            }
            switch (e.type){
                case TOUCH_DOWN:
                    if(this.botonVolver.contains(e.x,e.y)){
                        Menu menu = new Menu(this.engine);
                        this.engine.setState(menu);
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

    }

    @Override
    public void setMobile(Mobile mobile) {

    }
}
