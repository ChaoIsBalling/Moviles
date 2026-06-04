package com.example.gamelogic.states;

import android.webkit.HttpAuthHandler;

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
    private JSONObject save,style,prefabs, shop,items;
    AndroidMobile mobile;
    private int numGems = 0;
    //El item seleccionado en la tienda
    private JSONObject currentItem=null;
    private UIManager ui;

    JSONArray shopSection=null;
    Iterator<String> shopItem=null;

    // El indice del item de la tienda selccionado actualmente (en el jsonArray de shop)
    private int shopItemIndex = 0;

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
        this.items=engine.readJsonFile("items.json");
    }
    @Override
    public void update(double deltatime) {}

    @Override
    public void render(AndroidGraphics gr) {
        this.ui.render(gr);
    }


    public void readShop(){

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

    /**
     * Metodo que inicializa el inventario que hay en la tienda
     */
    private void inicializarScroll(){
        float initXText = 0;
        float initYText = 200, offsetTextY = 1.5f;
        int i=0;

       try {
           initSections(initXText,initYText,offsetTextY,this.shop.getJSONObject("shop").getJSONArray( "towers"),i,"Nuevas Torres");
           initYText = this.ui.getLastScrollable().getY() + this.ui.getLastScrollable().getHeight() * 1.2f;
           i++;

           //reiniciamos contador que apunta al ultimo elemento de la seccion
           shopItemIndex = 0;

           initSections(initXText,initYText,offsetTextY,this.shop.getJSONObject("shop").getJSONArray( "skins"),i,"Nuevas Skins");
           initYText = this.ui.getLastScrollable().getY() + this.ui.getLastScrollable().getHeight() * 1.2f;
           i++;

           shopItemIndex = 0;
           initSections(initXText,initYText,offsetTextY,this.shop.getJSONObject("shop").getJSONArray( "bg"),i,"Fondos");
       } catch (JSONException e) {
           throw new RuntimeException(e);
      }

    }
    public void initSections( float initXText ,float initYText, float offsetTextY,JSONArray section, int i, String nombreSeccion ){
        try {
            Text textoSeccion = new Text(prefabs.getJSONObject("textoTienda"), this.gr);

            //Array perteneciente a la seccion de la tienda
            shopSection = section;

            //Setamos valores del texto de la seccion
            textoSeccion.setText(nombreSeccion);
            textoSeccion.setId(textoSeccion.getId() + i);
            textoSeccion.setX(initXText);
            textoSeccion.setY(initYText);
            //Se añade al scroll
            this.ui.addVisualElementToArray(textoSeccion, prefabs.getJSONObject("textoTienda"));

            //Desplazamos el offset de Y
            float ny = (textoSeccion.getY() + textoSeccion.getHeight()* offsetTextY) ;
            this.prefabs.getJSONObject("BotonItem").put("y", ny);
            this.prefabs.getJSONObject("BotonItem").put("id",
                    this.prefabs.getJSONObject("BotonItem").getString("id") + i);
            //Creamos n numero de botones
            this.ui.createPrefabs(prefabs.getJSONObject("BotonItem"), section.length());
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
            final JSONObject item = shopSection.getJSONObject(shopItemIndex);

            shopItemIndex++;

            //Le seteamos al item un tipo de compra determinado
            item.put("TipoCompra", "torre");

            initShopButton(b,item);
            b.setOnClickListener( () -> {prepararCompra(item,b);
            });
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public void initShopButton(Button b, JSONObject item)
    {
        try {
            //Tipo de la compra definido en el json
            String tipoCompra = item.getString("TipoCompra");
            //procesamos cada tipo de compra
            switch (tipoCompra){
                case "skin":
                    b.setAppeareance(this.items.getJSONObject("Skins")
                            .getJSONObject(item.getString("id")));
                   break;
                case "torre":
                    b.setAppeareance(this.items.getJSONObject("Skins")
                            .getJSONObject(item.getString("id")));
                    break;
                case "fondo":
                    b.setFigure(this.prefabs.getJSONObject("FiguraBoton"));
                    b.getFigButton().setColor(item.getString("id"));
                    break;
            }
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
                if(this.save.getInt("gems")>=this.currentItem.getInt("cost")) {
                    numGems-=this.currentItem.getInt("cost");
                    this.ui.getTextUI("TEXT_DIAMANTES").setText(String.valueOf(numGems));
                    buyItem();
                    this.ui.changeVisualElementStateOfType("compra",false);
                    this.ui.changeVisualElementStateOfType("yacomprado",true);
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
                this.ui.getTextUI("TEXT_DESCRIPTION").setText(item.getString("description"));
                this.ui.getTextUI("TEXT_PRECIO").setText("Coste:"+item.getInt("cost"));
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
            //Accedemos a los objetos que tenemos comprados
            JSONObject purchases = this.save.getJSONObject("shop")
                    .getJSONObject("purchases");

            //Tipo de la compra del item actual
            String tipoCompra = currentItem.getString("TipoCompra");


            //ID del item que se quiere buscar
            int idItem = Integer.parseInt(currentItem.getString("id"));

            JSONArray arrayPur = null;

            //procesamos cada tipo de compra
            switch (tipoCompra){
                case "skin":
                    arrayPur = purchases.getJSONArray("skins");
                    break;
                case "torre":
                    arrayPur = purchases.getJSONArray("towers");
                    break;
                case "fondo":
                    arrayPur = purchases.getJSONArray("bg");
                    break;
            }

            //Determino si el indice esta en el array
            if(arrayPur != null){
                for (int i = 0; i < arrayPur.length(); i++) {
                    if (arrayPur.getInt(i) == idItem) {
                        return true; // El ítem ya ha sido comprado
                    }
                }
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public void buyItem()
    {
        try {
            //Tipo de la compra definido en el json
            String tipoCompra = currentItem.getString("TipoCompra");

            int idItem = Integer.parseInt(currentItem.getString("id"));

            JSONObject shopPurchases = this.save.getJSONObject("shop").getJSONObject("purchases");
            //procesamos cada tipo de compra
            switch (tipoCompra){
                case "skin":
                    //skins.get(currentItem.getString("Torre")).put(currentItem.getString("id"),true);
                    shopPurchases.getJSONArray("skins").put(idItem);
                    break;
                case "torre":
                    //torres.put(currentItem.getString("id"),true);
                    shopPurchases.getJSONArray("towers").put(idItem);
                    break;
                case "fondo":
                    //fondos.put(currentItem.getString("id"),true);
                    shopPurchases.getJSONArray("bg").put(idItem);
                    break;
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
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
