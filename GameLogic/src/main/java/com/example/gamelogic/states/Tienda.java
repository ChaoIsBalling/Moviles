package com.example.gamelogic.states;

import com.example.androidengine.AndroidEngine;
import com.example.androidengine.AndroidGraphics;
import com.example.androidengine.State;
import com.example.androidengine.TouchEvent;
import com.example.androidengine.AndroidAudio;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.example.androidengine.AndroidMobile;
import com.example.gamelogic.Color;
import com.example.gamelogic.VisualElements.Text;
import com.example.gamelogic.VisualElements.VisualElement;
import com.example.gamelogic.VisualElements.button.Button;
import com.example.gamelogic.managers.UIManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Clase que se encarga de representar la tienda y la gestión de compras
 */
public class Tienda implements State {
    private AndroidEngine engine;
    private AndroidGraphics gr;
    //El archivo de guardado del juego mas los archivos de estilo, prefabs y la lista de items
    private JSONObject save,style,prefabs, shop;
    AndroidMobile mobile;
    private int numGems = 0;
    //El item seleccionado en la tienda
    private JSONObject currentItem=null;
    private UIManager ui;

    JSONObject shopSection=null;
    Iterator<String> shopItem=null;

    //Estructras que almacenan los items que se han obtenido
    HashMap<String, Boolean> torres=new HashMap<>();
    HashMap<String, HashMap<String, Boolean>> skins=new HashMap<>();
    HashMap<String, Boolean> fondos=new HashMap<>();



    public Tienda(AndroidEngine engine,AndroidMobile mobile,JSONObject save){
        this.save =save;
        this.engine=engine;
        this.mobile = mobile;
        this.style =engine.readJsonFile("Tienda/style.json");
        this.shop =engine.readJsonFile("Tienda/shop.json");
        readShop();
        this.prefabs=engine.readJsonFile("Tienda/prefabs.json");
    }
    @Override
    public void update(double deltatime) {}

    @Override
    public void render(AndroidGraphics gr) {
        this.ui.render(gr);
    }


    public void readShop(){
        try {
            JSONArray t = this.save.getJSONArray("towers_unlocked");
            //Iterador que apunta a la primera seccion de la tienda

            String name=null;
            for (int i = 0; i< t.length();i++){
                torres.put(t.getString(i), true);
            }
            JSONObject tor = this.shop.getJSONObject("Torres");
            Iterator<String> it = tor.keys();
            for(int i=0;i<tor.length();i++)
            {
                name=it.next();
                if(!torres.containsKey(tor.getJSONObject(name).getString("id")))
                    torres.put(tor.getJSONObject(name).getString("id"),false);
            }

            JSONObject sk =  this.save.getJSONObject("skins_available");
            //Iterador que apunta a la primera seccion de la tienda
                it = sk.keys();

            for (int i = 0; i< sk.length();i++){
                name=it.next();
                JSONArray skinTorre=sk.getJSONArray(name);
                skins.put(name, new HashMap<>());
                for (int j=0;j<skinTorre.length();j++)
                {
                    skins.get(name).put(skinTorre.getString(j),true);
                }
            }

            sk =  this.shop.getJSONObject("Skins");
            it = sk.keys();

            for (int i = 0; i< sk.length();i++){
                name=it.next();
                JSONObject currSkin=sk.getJSONObject(name);
                if(!skins.get(currSkin.getString("Torre")).containsKey(currSkin.getString("id")))
                {
                    skins.get(currSkin.getString("Torre")).put(currSkin.getString("id"),false);
                }
            }


            JSONArray bg = this.save.getJSONArray("bg_available");
            //Iterador que apunta a la primera seccion de la tienda
            name=null;

            for (int i = 0; i< bg.length();i++){
                fondos.put(bg.getString(i), true);
            }

            //Leer la tienda
            JSONObject bgFondo = this.shop.getJSONObject("Fondos");

            it= bgFondo.keys();
            for(int i=0;i<bgFondo.length();i++)
            {
                name=it.next();
                if(!fondos.containsKey(bgFondo.getJSONObject(name).getString("id")))
                    fondos.put(bgFondo.getJSONObject(name).getString("id"),false);
            }

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


    }

    /**
     * Seteo del graphics que se encarga der inicializar la UI
     * @param gr Graphics
     */
    @Override
    public void setGr(AndroidGraphics gr) {
        this.gr =gr;
        this.ui = new UIManager(this.style, this.engine, this.gr);
        this.ui.setAllCallbacks();

        inicializarScroll();
        this.ui.configurarLimitesScroll();

        try {
            numGems = this.save.getInt("gems");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        this.ui.getTextUI("TEXT_DIAMANTES").setText(String.valueOf(numGems));
        this.ui.changeVisualElementStateOfType("compra",false);
        this.ui.changeVisualElementStateOfType("yacomprado",false);

    }

    private void inicializarScroll(){
        float initXText = 100;
        float initYText = 200, offsetTextY = 1;
        //Iterador que apunta a la primera seccion de la tienda
        Iterator<String> it = shop.keys();
        try {
            for(int i = 0; i < shop.length(); i++){
                //Creamos el text con el nombre de la seccion definida en el json
                Text textoSeccion = new Text(prefabs.getJSONObject("textoTienda"), this.gr);
                String nombreSeccion = it.next();
                shopSection=shop.getJSONObject(nombreSeccion);
                shopItem=shopSection.keys();
                textoSeccion.setText(nombreSeccion);
                textoSeccion.setId(textoSeccion.getId() + i);
                textoSeccion.setX(initXText);
                textoSeccion.setY(initYText + (offsetTextY * i));
                this.ui.addVisualElementToArray(textoSeccion, prefabs.getJSONObject("textoTienda"));

                float ny = (textoSeccion.getY() + textoSeccion.getHeight()) * 1.1f;
                this.prefabs.getJSONObject("BotonItem").put("y", ny);
                this.prefabs.getJSONObject("BotonItem").put("id",
                        this.prefabs.getJSONObject("BotonItem").getString("id")+i);

                //Creamos n numero de botones
                this.ui.createPrefabs(prefabs.getJSONObject("BotonItem"), shop.getJSONObject(nombreSeccion).length());

                initYText = this.ui.getLastScrollable().getY() + this.ui.getLastScrollable().getHeight() * 1.2f;
                //textoSkins.setY(ultimaPos);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Metodo para setear el callback de volver a la pantalla anterior
     * @param b el boton al que le pasamos el callback
     */
    public void setCallbackButtonReturn(Button b) {
        b.setOnClickListener( () -> {
            Menu menu = new Menu(this.engine,this.mobile,this.save);
            this.engine.setState(menu);
        });
    }

    /**
     * Metodo para setear el callback al boton que represtenta un item a comprar
     * @param b boton al que le queremos setear el callback
     */
    public void setCallbackButtonShopItem(Button b) {
        try {
            final JSONObject item=shopSection.getJSONObject(shopItem.next());
            b.setOnClickListener( () -> {prepararCompra(item,b);
            });
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Metodo que setea el callback al boton de compra
     * @param b Boton de compra
     */
    public void setCallbackButtonBuy(Button b) {
        b.setOnClickListener( () -> {
            comprobarCompra(b);
        });
    }

    /**
     * Metodo que sirve para procesar la compra de un item en la tienda
     * @param b boton del item que se ha pulsado
     */
    public void comprobarCompra(Button b)
    {   if(currentItem!=null) {
            try {
                if(this.save.getInt("gems")>this.currentItem.getInt("precio")) {
                    numGems-=this.currentItem.getInt("precio");
                    this.ui.getTextUI("TEXT_DIAMANTES").setText(String.valueOf(numGems));
                    this.save.getJSONObject("itemsComprados").put(currentItem.getString("ID"),currentItem);
                    this.ui.changeVisualElementStateOfType("compra",false);
                    this.ui.changeVisualElementStateOfType("yacomprado",true);
                    this.ui.changeVisualElementStateOfType("cambio", true);
                    this.save.put("gems",numGems);
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Metodo que modifica la interfaz a la hora de pulsar un item de compra
     * Utiliza el UIManager para escribir el precio, descripcion y coste del producto y marcar botones con colores
     * @param item Item que se quiere comprar
     * @param b Boton que contiene el item seleccionado
     */
    public void prepararCompra(JSONObject item, Button b){

        for (VisualElement element : this.ui.getAllVisualElementsOfType("item")) {
            element.setColor(Color.BLANCO.getHex());
        }
        b.setColor(Color.AMARILLO_CLARO.getHex());
        this.currentItem=item;
        try {
            //Caso en que el item ya esté comprado
            if(itemBought()) {
                this.ui.changeVisualElementStateOfType("compra",false);
                this.ui.changeVisualElementStateOfType("yacomprado",true);
            }
            //Caso en el que el item este sin comprar
            else {
                this.ui.changeVisualElementStateOfType("compra",true);
                this.ui.changeVisualElementStateOfType("yacomprado",false);
                this.ui.getTextUI("TEXT_DESCRIPTION").setText(item.getString("Descripcion"));
                this.ui.getTextUI("TEXT_PRECIO").setText("Coste:"+item.getInt("Precio"));
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Metodo que determina si el articulo se ha comprado ya
     * @return booleano que indica el resultado
     */
    public boolean itemBought(){
        try {
            //Tipo de la compra definido en el json
            String tipoCompra = currentItem.getString("TipoCompra");
            //procesamos cada tipo de compra
            switch (tipoCompra){
                case "skin":
                    return skins.get(currentItem.getString("Torre")).get(currentItem.getString("id"));
                case "torre":
                    return torres.get(currentItem.getString("id"));
                case "fondo":
                    return fondos.get(currentItem.getString("id"));
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    /**
     *
     * @param list Lista de eventos
     * @param elapseTime Tiempo trascurrido
     */
    @Override
    public void handleInput(ArrayList<TouchEvent> list, double elapseTime) {
        for(TouchEvent e: list){
            //Si es nulo no se procesa
            if (e == null || e.type == null)
                continue;
            switch (e.type){
                case TOUCH_DOWN:
                    this.ui.handleInput(e);
                    this.ui.onTouchDown(e);
                    break;
                case TOUCH_UP:
                    this.ui.onTouchUp();
                    break;
                case TOUCH_MOVE:
                    this.ui.onTouchMove(e);
                    break;
            }
        }
    }

    @Override
    public void setAudio(AndroidAudio audio) {}
    @Override
    public void setMobile(AndroidMobile mobile) {}
    @Override
    public JSONObject getSave() { return this.save; }
}
