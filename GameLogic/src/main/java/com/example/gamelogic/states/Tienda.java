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

public class Tienda implements State {
    private AndroidEngine engine;
    private AndroidGraphics gr;
    //El archivo de guardado del juego
    private JSONObject save;
    AndroidMobile mobile;
    JSONObject style;
    private int numGems=0;
    //El item seleccionado en la tienda
    private JSONObject currentItem=null;
    private UIManager ui;
    public Tienda(AndroidEngine engine,AndroidMobile mobile,JSONObject save){
        this.save =save;
        this.engine=engine;
        this.mobile = mobile;
        this.style =engine.readJsonFile("Tienda/styleNuevo.json");
    }
    @Override
    public void update(double deltatime) {}

    @Override
    public void render(AndroidGraphics gr) {
        this.ui.render(gr);
    }

    @Override
    public void setGr(AndroidGraphics gr) {
        this.gr =gr;
        this.ui = new UIManager(this.style, this.engine, this.gr);
        this.ui.setAllCallbacks();
        try {
            numGems = this.save.getInt("gems");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        this.ui.getTextUI("TEXT_DIAMANTES").setText(String.valueOf(numGems));
        this.ui.changeVisualElementStateOfType("compra",false);
        this.ui.changeVisualElementStateOfType("yacomprado",false);
    }
    /**
     * Metodo para setear el callback de volver a la pantalla anterior
     * @param b el boton al que le pasamos el callback
     */
    public void setCallbackButtonReturn(Button b)
    {
        b.setOnClickListener( () -> {
            Menu menu = new Menu(this.engine,this.mobile,this.save);
            this.engine.setState(menu);
        });
    }

    public void setCallbackButtonShopItem(Button b)
    {
        try {
            JSONObject callback = b.getCallback();
            JSONObject item=callback.getJSONObject("item");
            b.setOnClickListener( () -> {prepararCompra(item,b);
            });
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public void setCallbackButtonBuy(Button b)
    {
        b.setOnClickListener( () -> {
            comprobarCompra(b);
        });
    }
    public void comprobarCompra(Button b)
    {   if(currentItem!=null) {
            try {
            if(this.save.getInt("gems")>this.currentItem.getInt("precio")) {
                numGems-=this.currentItem.getInt("precio");
                this.ui.getTextUI("TEXT_DIAMANTES").setText(String.valueOf(numGems));
                this.save.getJSONObject("itemsComprados").put(currentItem.getString("ID"),currentItem);
                this.ui.changeVisualElementStateOfType("compra",false);
                this.ui.changeVisualElementStateOfType("yacomprado",true);
                this.save.put("gems",numGems);
            }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void prepararCompra(JSONObject item, Button b){

        for (VisualElement element : this.ui.getAllVisualElementsOfType("item")) {
            element.setColor(Color.BLANCO.getHex());
        }
        b.setColor(Color.AMARILLO_CLARO.getHex());
        this.currentItem=item;
        try {
            if(this.save.getJSONObject("itemsComprados").has(item.getString("ID"))) {
                this.ui.changeVisualElementStateOfType("compra",false);
                this.ui.changeVisualElementStateOfType("yacomprado",true);
            }
            else {
                this.ui.changeVisualElementStateOfType("compra",true);
                this.ui.changeVisualElementStateOfType("yacomprado",false);
                this.ui.getTextUI("TEXT_DESCRIPTION").setText(item.getString("descripcion"));
                this.ui.getTextUI("TEXT_PRECIO").setText("Coste:"+item.getInt("precio"));
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

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
    public void setAudio(AndroidAudio audio) {}
    @Override
    public void setMobile(AndroidMobile mobile) {}
    @Override
    public JSONObject getSave() {
        return this.save;
    }
}
