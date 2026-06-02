package com.example.gamelogic.states;


import com.example.androidengine.AndroidEngine;
import com.example.androidengine.AndroidGraphics;
import com.example.androidengine.State;
import com.example.androidengine.TouchEvent;
import com.example.androidengine.AndroidAudio;
import org.json.JSONException;
import org.json.JSONObject;
import com.example.androidengine.AndroidMobile;
import com.example.gamelogic.Color;
import com.example.gamelogic.VisualElements.VisualElement;
import com.example.gamelogic.VisualElements.button.Button;
import com.example.gamelogic.managers.UIManager;
import java.util.ArrayList;
public class Cambio implements State {
    private AndroidEngine engine;
    private AndroidGraphics gr;
    //El archivo de guardado del juego
    private JSONObject save;
    AndroidMobile mobile;
    JSONObject style;
    private UIManager ui;

    public Cambio (AndroidEngine engine,AndroidMobile mobile,JSONObject save){
        this.save =save;
        this.engine= engine;
        this.mobile = mobile;
        this.style =engine.readJsonFile("Cambio/style.json");
    }


    @Override
    public void update(double deltatime) {}

    @Override
    public void render(AndroidGraphics gr) { this.ui.render(gr); }

    /**
     * Seteo del graphics que se encarga der inicializar la UI
     * @param gr Graphics
     */
    @Override
    public void setGr(AndroidGraphics gr) {
        this.gr =gr;
        this.ui = new UIManager(this.style, this.engine, this.gr);
        this.ui.setAllCallbacks();
    }

    /**
     * Metodo para setear el callback de volver a la pantalla anterior
     * @param b el boton al que le pasamos el callback
     */
    public void setCallbackButtonReturnShop(Button b) {
        b.setOnClickListener( () -> {
            Tienda tienda = new Tienda(this.engine,this.mobile, this.save);
            this.engine.setState(tienda);
        });
    }

    @Override
    public void handleInput(ArrayList<TouchEvent> list, double elapseTime) {
        for(TouchEvent e: list){
            //Si es nulo no se procesa
            if (e == null || e.type == null)
                continue;
            switch (e.type){
                case TOUCH_DOWN:
                    this.ui.handleInput(e);
                    break;
                case TOUCH_UP:
                    break;
                case TOUCH_MOVE:
                    break;
            }
        }
    }

    @Override
    public void setAudio(AndroidAudio audio) { }

    @Override
    public void setMobile(AndroidMobile mobile) { }

    @Override
    public JSONObject getSave() { return this.save; }
}
