package com.example.gamelogic.states;

import com.example.androidengine.AndroidEngine;
import com.example.androidengine.AndroidGraphics;
import com.example.androidengine.State;
import com.example.androidengine.TouchEvent;
import com.example.androidengine.AndroidAudio;
import org.json.JSONException;
import org.json.JSONObject;
import com.example.androidengine.AndroidMobile;
import com.example.gamelogic.VisualElements.button.Button;
import com.example.gamelogic.managers.UIManager;
import java.util.ArrayList;
import java.util.HashMap;

public class Cambio implements State {
    private AndroidEngine engine;
    private AndroidGraphics gr;
    //El archivo de guardado del juego
    private JSONObject save;
    AndroidMobile mobile;
    JSONObject style, prefabs, items;
    private UIManager ui;
    private String tipoItem;

    //Contiene los aspectos que se pueden elegir
    ArrayList<Integer>res;

    int indexSkin;

    public Cambio (AndroidEngine engine, AndroidMobile mobile, JSONObject save, ArrayList<Integer>res, String tipoItem){
        this.save =save;
        this.engine= engine;
        this.mobile = mobile;
        this.style =engine.readJsonFile("Cambio/style.json");
        this.prefabs = engine.readJsonFile("Cambio/prefabs.json");
        this.items = engine.readJsonFile("items.json");
        this.res = res;
        this.tipoItem = tipoItem;
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
        inicializarAspectos();
    }

    /**
     * Inicializa el UI Manager con los aspectos que se leen del unordered_Map
     */
    public void inicializarAspectos(){
        int amount = res.size();
        try {
            indexSkin = 0;
            JSONObject prefabBoton = prefabs.getJSONObject("BotonAspecto");
            this.ui.createPrefabs(prefabBoton, amount);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * @param b
     */
    public void initAppeareanceButton(Button b) {
        try {
            //procesamos cada tipo de compra
            switch (tipoItem){
                case "skins":
                    b.setAppeareance(this.items.getJSONObject("Skins")
                            .getJSONObject(String.valueOf(res.get(indexSkin))));
                    break;
                case "towers":
                    //Accede al campo skin del jsonArray de shop.json
                    b.setAppeareance(this.items.getJSONObject("Skins")
                            .getJSONObject(String.valueOf(res.get(indexSkin))));
                    break;
                case "bg":
                    b.setFigure(this.prefabs.getJSONObject("FiguraBoton"));
                    b.getFigButton().setColor(this.items.getJSONObject("Fondos")
                            .getJSONObject(String.valueOf(res.get(indexSkin))).getString("color"));
                    break;
                case "but":
                    b.setFigure(this.prefabs.getJSONObject("FiguraBoton"));
                    b.getFigButton().setColor(this.items.getJSONObject("Botones")
                            .getJSONObject(String.valueOf(res.get(indexSkin))).getString("color"));
                    break;
            }

            indexSkin++;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public void setCallbackButtonAspect(Button b) {
        initAppeareanceButton(b);

        b.setOnClickListener( () -> {
            changeSkinEquiped(b);
        });
    }

    public void changeSkinEquiped(Button b){
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
