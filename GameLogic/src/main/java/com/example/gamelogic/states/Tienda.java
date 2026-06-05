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
import java.util.Map;

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

    //String que define la seccion actual de la tienda (torres, skins o fondos)
    String  shopSection=null;

    //Hashmap que define las compras realizadas y se usa para comprobar los elementos comprados
    private HashMap<String,HashMap<Integer,Boolean>> shopPurchases=new HashMap<>();

    // El indice del item de la tienda selccionado actualmente (en el jsonArray de shop)
    private int shopItemIndex = 0;

    public Tienda(AndroidEngine engine,AndroidMobile mobile,JSONObject save){
        this.save =save;
        this.engine=engine;
        this.mobile = mobile;
        this.style =engine.readJsonFile("Tienda/style.json");
        this.shop=engine.readJsonFile("Tienda/shop.json");
        this.prefabs=engine.readJsonFile("Tienda/prefabs.json");
        this.items=engine.readJsonFile("items.json");
        readData();
    }

    /**
     * Metodo que lee los datos de compra del save para inicializar el hashmap de shopPurchases
     */
    public void readData()
    {
        try {
            JSONObject purchases = this.save.getJSONObject("shop").getJSONObject("purchases");
            Iterator<String> it = purchases.keys();
            //el name es la seccion del save de purchases
            String name=null;
            for(int i=0;i<purchases.length();i++)
            {
                name=it.next();
                shopPurchases.put(name,new HashMap<>());
                JSONArray purchaseSection= purchases.getJSONArray(name);
                for(int j=0;j<purchaseSection.length();j++)
                    //Seteamos a true todos los elementos que esten en la seccion de purchase
                    shopPurchases.get(name).put(purchaseSection.getInt(j),true);

                JSONArray shopSection= shop.getJSONArray(name);
                for(int j=0;j<shopSection.length();j++)
                {
                    int idItem=shopSection.getJSONObject(j).getInt("id");
                    if(!shopPurchases.get(name).containsKey(idItem))
                        shopPurchases.get(name).put(idItem,false);
                }
            }

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void update(double deltatime) {}

    @Override
    public void render(AndroidGraphics gr) {
        this.ui.render(gr);
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
        this.ui.changeVisualElementStateOfType("cambio",false);

    }

    /**
     * Metodo que inicializa el inventario que hay en la tienda los cuales tienen scroll
     */
    private void inicializarScroll(){
        float initXText = 150;
        float initYText = 100, offsetTextY = 1.5f;
        int i=0;
        initSections(initXText,initYText,offsetTextY,"towers",i,"Nuevas Torres");
        initYText = this.ui.getLastScrollable().getY() + this.ui.getLastScrollable().getHeight() * 1.2f;
        i++;

        //reiniciamos contador que apunta al ultimo elemento de la seccion
        shopItemIndex = 0;

        initSections(initXText,initYText,offsetTextY,"skins",i,"Nuevas Skins");
        initYText = this.ui.getLastScrollable().getY() + this.ui.getLastScrollable().getHeight() * 1.2f;
        i++;

        shopItemIndex = 0;
        initSections(initXText,initYText,offsetTextY,"bg",i,"Fondos");
    }
    /**
     * Metodo que inicializa cada una de las secciones de la tienda y setea su apariencia
     */
    public void initSections( float initXText ,float initYText, float offsetTextY,String section, int i, String nombreSeccion ){
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

            //Desplazamos el offset de Y para el siguiente boton
            float ny = (textoSeccion.getY() + textoSeccion.getHeight() * offsetTextY) ;
            this.prefabs.getJSONObject("BotonItem").put("y", ny);
            this.prefabs.getJSONObject("BotonItem").put("id",
                    this.prefabs.getJSONObject("BotonItem").getString("id") + i);
            //Creamos n numero de botones
            this.ui.createPrefabs(prefabs.getJSONObject("BotonItem"), this.shop.getJSONArray(section).length());
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
            final JSONObject item =shop.getJSONArray(shopSection).getJSONObject(shopItemIndex);
            shopItemIndex++;

            //Le seteamos al item un tipo de compra determinado
            item.put("TipoCompra", shopSection);

            initShopButton(b,item);
            b.setOnClickListener( () -> {prepararCompra(item,b);
            });
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Metodo para setear el callback al boton que represtenta la transicion a la escena de cambio
     * @param b boton al que le queremos setear el callback
     */
    public void setCallbackButtonChange(Button b) {
            b.setOnClickListener( () -> {
                prepareChangeMenu(currentItem);
            });
    }


    /**
     * Metodo que prepara el menu de cambio de aspecto
     * @param item
     */
    public void prepareChangeMenu(JSONObject item){
        //Primero debemos saber que tipo de elemento vamos a cambiar
        String tipoItem = null;
        try {
           final JSONObject skinItems=items.getJSONObject( "Skins");
            tipoItem = item.getString("TipoCompra");
            int idTorre;
            if(tipoItem == "towers"){
                int idItemShop = item.getInt("id");
                idTorre = idItemShop;
                //idTorre = skinItems.getJSONObject(String.valueOf(idItemShop));
            }
            else if(tipoItem == "skins"){
                //Id de la torre al que perteneceran los aspectos
                idTorre = item.getInt("forTower");
            } else {
                idTorre = 0;
            }

            //Filtramos las skins que correspondan a la torre y que esten disponibles
            //y las ponemos en un arrayList Auxiliar
            ArrayList<Integer>skinsResult = new ArrayList<>();
            for (Map.Entry<Integer,Boolean> entry : shopPurchases.get("skins").entrySet()) {
                String skinId=String.valueOf(entry.getKey());
                if( skinItems.getJSONObject(skinId).getInt( "forTower") == idTorre && entry.getValue())
                    skinsResult.add(entry.getKey());
            }


            Cambio cam = new Cambio(this.engine,this.mobile, this.save, skinsResult,tipoItem);
            this.engine.setState(cam);

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
                case "skins":
                    b.setAppeareance(this.items.getJSONObject("Skins")
                            .getJSONObject(item.getString("id")));
                    break;
                case "towers":
                    //Accede al campo skin del jsonArray de shop.json
                    b.setAppeareance(this.items.getJSONObject("Skins")
                            .getJSONObject(item.getString("skin")));
                   break;
                case  "bg":
                    b.setFigure(this.prefabs.getJSONObject("FiguraBoton"));
                    b.getFigButton().setColor(this.items.getJSONObject("Fondos")
                            .getJSONObject(item.getString("id")).getString("color"));
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
            element.setColor(Color.GRIS.getHex());
        }
        b.setColor(Color.AMARILLO_CLARO.getHex());
        this.currentItem=item;
        try {
            //Caso en que el item ya esté comprado
            if(itemBought()) {
                this.ui.changeVisualElementStateOfType("compra",false);
                this.ui.changeVisualElementStateOfType("yacomprado",true);
                this.ui.changeVisualElementStateOfType("cambio",true);
            }
            //Caso en el que el item este sin comprar
            else {
                this.ui.changeVisualElementStateOfType("compra",true);
                this.ui.changeVisualElementStateOfType("yacomprado",false);
                this.ui.changeVisualElementStateOfType("cambio",false);
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
            //Tipo de la compra del item actual
            String tipoCompra = currentItem.getString("TipoCompra");
            //ID del item que se quiere buscar
            int idItem = currentItem.getInt("id");

            return shopPurchases.get(tipoCompra).get(idItem);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public void buyItem()
    {
        try {
            //Tipo de la compra definido en el json
            String tipoCompra = currentItem.getString("TipoCompra");
            int idItem = currentItem.getInt("id");
            JSONObject compras= this.save.getJSONObject("shop").getJSONObject("purchases");
            compras.getJSONArray(tipoCompra).put(idItem);
            shopPurchases.get(tipoCompra).put(idItem,true);
            this.ui.changeVisualElementStateOfType("cambio",true);
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
