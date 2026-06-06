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
        this.prefabs=engine.readJsonFile("prefabs.json");
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
            //el name es la seccion del jsonObject en save de purchases
            String nameSection = null;

            //Recorremos el archivo save para determinar que items tenemos y cuales no
            for(int i = 0;i < purchases.length();i++)
            {
                //Nueva entrada en el HashMap
                nameSection=it.next();
                shopPurchases.put(nameSection,new HashMap<>());

                //Cogemos el array de purchase a traves del nombre
                JSONArray purchaseSection = purchases.getJSONArray(nameSection);
                for(int j=0; j < purchaseSection.length(); j++)
                    //Seteamos a true todos los elementos que esten en la seccion de purchase
                    shopPurchases.get(nameSection).put(purchaseSection.getInt(j),true);

                //Ahora cogemos el array perteneciente a la seccion deseada en la shop
                JSONArray shopSection= shop.getJSONArray(nameSection);
                for(int j = 0;j < shopSection.length(); j++)
                {
                    //Cogemos el id del item en la tienda que queramos saber si lo hemos adquirido
                    int idItem = shopSection.getJSONObject(j).getInt("id");

                    //Si no se encuentra en la seccion de objetos comprados, lo ponemos a false
                    if(!shopPurchases.get(nameSection).containsKey(idItem))
                        shopPurchases.get(nameSection).put(idItem,false);
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

        //Configuracion del scroll de la tienda
        inicializarScroll();
        this.ui.configurarLimitesScroll();

        try {
            numGems = this.save.getInt("gems");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        //Seteamos valores de UI y desactivamos botones
        this.ui.getTextUI("TEXT_DIAMANTES").setText(String.valueOf(numGems));
        this.ui.changeVisualElementStateOfType("compra",false);
        this.ui.changeVisualElementStateOfType("yacomprado",false);
        this.ui.changeVisualElementStateOfType("cambio",false);
        this.ui.changeVisualElementStateOfType("activar",false);
    }

    /**
     * Metodo que inicializa el inventario que hay en la tienda los cuales tienen scroll
     */
    private void inicializarScroll(){

        //parametros para regular la posicion de los textos del scroll
        float initXText = 150;
        float initYText = 100, offsetTextY = 1.5f;
        try {
            //Nombre de la seccion de la tienda (cogemos el prefab)
            JSONObject textoSeccion=this.shop.getJSONObject("textoSeccion");
            Text nombreSeccion = null;


            //iterador para recorrer todas las secciones del json de shop
            Iterator<String> it= shop.keys();
            String name= it.next();

            //Prefab del boton para renderizar un item (podemos modificar sus atributos)
            JSONObject botonPrefab= this.prefabs.getJSONObject("BotonItem");

            for(int i = 0;i < shop.length() - 1; i++) {
                //Text que almacena el nombre de la seccion
                nombreSeccion = new Text(prefabs.getJSONObject("textoTienda"), this.gr);

                //Setamos valores del texto de la seccion
                nombreSeccion.setText(textoSeccion.getString(name));
                nombreSeccion.setId(nombreSeccion.getId() + i);
                nombreSeccion.setX(initXText); nombreSeccion.setY(initYText);

                //Array perteneciente a la seccion de la tienda (nos sirve para setear otras cosas)
                shopSection = name;

                //Se añade al scroll el texto
                this.ui.addVisualElementToArray(nombreSeccion , prefabs.getJSONObject("textoTienda"));

                //Desplazamos el offset de Y para el siguiente boton
                float ny = (nombreSeccion.getY() + nombreSeccion.getHeight() * offsetTextY) ;
                botonPrefab.put("y", ny);
                botonPrefab.put("id", botonPrefab.getString("id") + i);

                //Creamos n numero de botones en el scroll
                int numBotones = this.shop.getJSONArray(name).length();
                this.ui.createPrefabs(botonPrefab, numBotones);

                //Reseteamos el indice que nos sirve para inciaizar los items
                shopItemIndex = 0;
                //Inicializamos el siguiente texto de seccion con posicion relativa al ultimo boton de la seccion anterior
                VisualElement ultimoElementoBoton = this.ui.getLastScrollable();
                initYText = ultimoElementoBoton.getY() + ultimoElementoBoton.getHeight() * offsetTextY;
                name=it.next();
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
     * (se hace durante iniciarScroll(), a la hora de crear n botones de una seccion)
     * @param b boton al que le queremos setear el callback
     */
    public void setCallbackButtonShopItem(Button b) {
        try {
            //Accedemos al item actual al que queramos setear el callback
            final JSONObject item = shop.getJSONArray(shopSection).
                    getJSONObject(shopItemIndex);
            shopItemIndex++; //Aumentamos indice que recorre los n elementos de la seccion

            //Le seteamos al item un tipo de compra determinado (es el valor de la shopSection del shop.json)
            item.put("TipoCompra", shopSection);

            //incializamos la apariencia del boton
            initShopButton(b,item);

            //Seteamos su funcionalidad de compra
            b.setOnClickListener( () -> {
                prepararCompra(item,b);
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

    public void setCallbackButtonActiveTower(Button b){
        b.setOnClickListener( () -> {
            activeTower(currentItem);
        });
    }

    public void activeTower(JSONObject item){
        try {
                //Accedemos al array de torres activas
                JSONArray activeTowers = this.save.getJSONObject("shop").
                        getJSONObject("equips").getJSONArray("towers");

                int indexTower = item.getInt("id");

                if(!activeTowers.getBoolean(indexTower)) {
                    activeTowers.put(indexTower,true);
                    //Cambiamos el texto
                    this.ui.getButtonUI("BUT_ACTIVAR").changeText("Desactivar torre");
                }
                else {
                    activeTowers.put(indexTower,false);
                    this.ui.getButtonUI("BUT_ACTIVAR").changeText("Activar torre");
                }

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Metodo que prepara el menu de cambio de aspecto
     * (solo para skins, bg y botones)
     * @param item item que se ha seleccionado cambiar de aspecto
     */
    public void prepareChangeMenu(JSONObject item){
        try {
            //Primero debemos saber que tipo de elemento vamos a cambiar (a que seccion pertenece)
            String shopSection = item.getString("TipoCompra");
            //Skins de las torres
            final JSONObject skinItems = items.getJSONObject( "skins");
            int idTorre=0;

            if(shopSection.equals("towers"))
                idTorre  = item.getInt("id");

            //Si vamos a cambiar el aspecto a partir de una skin, cogemos su identificador forTower
            // forTower -> indice de la torre de la que es compatible una skin
            else if(shopSection.equals( "skins")){
                //Id de la torre al que perteneceran los aspectos
                idTorre = item.getInt("forTower");
            }

            //Filtramos las skins que correspondan a la torre y que esten disponibles
            //y las ponemos en un arrayList Auxiliar que se lo pasaremos al menu de cambio
            ArrayList<Integer>skinsResult = new ArrayList<>();

            //Recorremos el hashMap que corresponde a la seccion
            for (Map.Entry<Integer,Boolean> entry : shopPurchases.get(shopSection).entrySet()) {
                //Id del item en la seccion
                String entryId = String.valueOf(entry.getKey());
                //Si su valor es true
                if(entry.getValue())
                {
                    //Añadimos al arrayList la ID del item
                    //Solo lo añadimos si pertenecen a la seccion de bg, botones o torres
                    //o si el id de la skin corresponde con el id de la Torre (para la que sirve la skin)
                    if(!shopSection.equals("skins")||
                            skinItems.getJSONObject(entryId).getInt( "forTower") == idTorre)
                        skinsResult.add(entry.getKey());
                }
            }
            //Menu de cambio de aspecto
            Cambio cam = new Cambio(this.engine,this.mobile, this.save, skinsResult, shopSection);
            this.engine.setState(cam);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Metodo que se encarga de darle una aparecia a los botones de compra
     * en funcion de la seccion a la que pertenecen
     * @param b boton que se le quiere dar apariencia
     * @param item el item que contiene ese boton (es leido desde el shop.json)
     */
    public void initShopButton(Button b, JSONObject item) {
        try {
            //Tipo de la compra que se definio en el item parametro
            String tipoCompra = item.getString("TipoCompra");
            //procesamos cada tipo de compra (vamos a tener que acceder al json en el que se encuentra toda
            // la info de apareincias de los items del juego)
            switch (tipoCompra){
                case "skins":
                    //el id seria un jsonObject dentro de skins
                    b.setAppeareance(this.items.getJSONObject("skins")
                            .getJSONObject(item.getString("id")));
                    break;
                case "towers":
                    //Seteamos la skin por defecto de la torre
                    //Accede al campo skin del jsonArray de shop.json
                    b.setAppeareance(this.items.getJSONObject("skins")
                            .getJSONObject(item.getString("skinDefault")));
                   break;
                case "bg":
                case "but":
                    //Todas seran una figura en cuadarado con el color que corresponda
                    b.setFigure(this.prefabs.getJSONObject("FiguraBoton"));
                    b.getFigButton().setColor(this.items.getJSONObject(tipoCompra)
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
    {
        if(currentItem!=null) {
            try {
                int numGems = this.save.getInt("gems");
                if( numGems >= this.currentItem.getInt("cost")) {
                    numGems-=this.currentItem.getInt("cost");
                    this.ui.getTextUI("TEXT_DIAMANTES").setText(String.valueOf(numGems));
                    //procesamos la compra del item
                    buyItem();
                    this.ui.changeVisualElementStateOfType("compra",false);
                    this.ui.changeVisualElementStateOfType("yacomprado",true);
                    this.save.put("gems",numGems); //actualizamos el numero de gemas
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

        //Coloreamos todos los botones de su color por defecto
        for (VisualElement element : this.ui.getAllVisualElementsOfType("item")) {
            element.setColor(Color.GRIS.getHex());
        }
        b.setColor(Color.AMARILLO_CLARO.getHex()); //coloreamos de color amarillo el seleccionado

        //Item actual
        this.currentItem=item;

        try {
            String tipo = currentItem.getString("TipoCompra");
            //Caso en que el item ya esté comprado
            if(itemBought()) {
                //Ponemos los tectos que correspondan
                this.ui.changeVisualElementStateOfType("compra",false);
                this.ui.changeVisualElementStateOfType("yacomprado",true);

                //Cuando se compra una torre, no se puede acceder al menu de cambio de skin
                if(tipo != "towers") {
                    this.ui.changeVisualElementStateOfType("cambio",true);
                    this.ui.changeVisualElementStateOfType("activar",false);
                }
                else{
                    this.ui.changeVisualElementStateOfType("cambio",false);
                    this.ui.changeVisualElementStateOfType("activar",true);
                }
            }
            //Caso en el que el item este sin comprar
            else {
                this.ui.changeVisualElementStateOfType("compra",true);
                this.ui.changeVisualElementStateOfType("yacomprado",false);
                this.ui.changeVisualElementStateOfType("cambio",false);
                this.ui.changeVisualElementStateOfType("activar",false);
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

            //Vemos si se ha comprado el articulo en nuestro hashmap
            return shopPurchases.get(tipoCompra).get(idItem);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Metodo que procesa la compra en funcion del tipo de la compra
     * si es torre, skin, bg o butones
     */
    public void buyItem()
    {
        try {
            //Tipo de la compra definido el el Item actual
            String tipoCompra = currentItem.getString("TipoCompra");
            //Id del item a comprar
            int idItem = currentItem.getInt("id");

            //Hay que guardar la compra en el save (apartado purchases)
            JSONObject compras= this.save.getJSONObject("shop")
                    .getJSONObject("purchases");
            //Ponemos el objeto en el array correspondiente de compras (purchases)
            compras.getJSONArray(tipoCompra).put(idItem);
            //Actualizamos el hashMap
            shopPurchases.get(tipoCompra).put(idItem,true);

            //Si el item comprado es una torre, desactivamos el menu de cambio de skin
            //y activamos el boton de activar/desactivar torre
            if(!tipoCompra.equals("towers"))
                this.ui.changeVisualElementStateOfType("cambio",true);
            else
                this.ui.changeVisualElementStateOfType("activar",true);
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
