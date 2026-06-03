package com.example.gamelogic.managers;


import com.example.androidengine.AndroidEngine;
import com.example.androidengine.AndroidGraphics;
import com.example.androidengine.TouchEvent;
import com.example.gamelogic.VisualElements.Image;
import com.example.gamelogic.VisualElements.VisualElement;
import com.example.gamelogic.VisualElements.button.Button;
import com.example.gamelogic.VisualElements.Text;
import com.example.gamelogic.VisualElements.figure.Circle;
import com.example.gamelogic.VisualElements.figure.Figure;
import com.example.gamelogic.VisualElements.figure.Hexagon;
import com.example.gamelogic.VisualElements.figure.Square;
import com.example.gamelogic.VisualElements.figure.Triangle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UIManager {
    //Ultima cordenada Y tocada
    float lastTouchedY;
    //bool que nos dice si estamos haciendo scroll de pantalla
    boolean scroll;
    //La posición minima que puede tener el promer boton de la lista en la posicion y
    float minY;
    //La posición minima que puede tener el promer boton de la lista en la posicion y
    float maxY;
    //Altura de la ventana de scroll de niveles
    float tamScroll = 400;
    // Hash Maps donde vamos a guardar los elementos de la UI de la escena
    private HashMap<String, Button> botones = new HashMap<>();
    private HashMap<String, Text> textos = new HashMap<>();
    private HashMap<String, Figure> figures = new HashMap<>();
    private HashMap<String, Image> imagenes = new HashMap<>();
    //ArrayList que guarda todos los objetos que pueden hacer scroll
    private ArrayList<VisualElement> scrollableElements =new ArrayList<>();
    private HashMap<String, ArrayList< VisualElement>> visualElementTypes =new HashMap<>();
    private ArrayList<ArrayList<VisualElement>> visualElementRenderList=new ArrayList<>();
    private AndroidEngine engine;
    private AndroidGraphics gr;
    //Aqui se guardan las imagenes que sirven como HUD y que se queden todo el tiempo en pantalla


    public UIManager(JSONObject sceneJson, AndroidEngine engine, AndroidGraphics gr) {
        // Limpiamos lo anterior para cargar la nueva escena
        visualElementRenderList.add(new ArrayList<>());
        botones.clear();
        textos.clear();
        imagenes.clear();
        figures.clear();
        this.engine=engine;
        this.gr=gr;
        loadUIFromJson(sceneJson);
    }
    public void loadUIFromJson(JSONObject sceneJson)
    {
        try {
            //Cargar Botones
            if (sceneJson.has("buttons")) {
                JSONArray array = sceneJson.getJSONArray("buttons");
                String type= new String();
                for (int i = 0; i < array.length(); i++) {
                    JSONObject bData = array.getJSONObject(i);
                    type=bData.getString("type");
                    Button b = new Button(bData,gr);
                    botones.put(bData.getString("id"), b);
                    addVisualElementToArray(b,bData);
                }
            }
            //Textos
            if (sceneJson.has("texts")){
                JSONArray array = sceneJson.getJSONArray("texts");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject tData = array.getJSONObject(i);
                    Text t = new Text(tData,gr);
                    //Añadimos al hashMap con su id Correspondiente
                    textos.put(tData.getString("id"), t);
                    addVisualElementToArray(t,tData);
                }
            }
            //Imagenes
            if(sceneJson.has("images")){
                JSONArray array = sceneJson.getJSONArray("images");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject imageData = array.getJSONObject(i);
                    Image img = new Image(imageData, engine.getGraphics());
                    imagenes.put(imageData.getString("id"), img);
                    addVisualElementToArray(img,imageData);
                }
            }
            if(sceneJson.has("figures")){
                JSONArray array = sceneJson.getJSONArray("figures");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject figureData = array.getJSONObject(i);
                    String form = figureData.getString("form");
                    Figure fig=null;
                    switch (form){
                        case "triangle":
                            fig=new Triangle(figureData);
                            break;
                        case "square":
                            fig=new Square(figureData);
                            break;
                        case "hexagon":
                            fig=new Hexagon(figureData);
                            break;
                        case "circle":
                            fig=new Circle(figureData);
                    }
                   figures.put(figureData.getString("id"),fig);
                    addVisualElementToArray(fig,figureData);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metodo para añadir los elementos de UI a arrays dependiendo del tipo de elemento o de su layer
     * @param element el elemento a añadir
     * @param obj el json object del objet
     */
    public void addVisualElementToArray(VisualElement element, JSONObject obj)
    {
        try {
            if(obj.has("type"))
            {
                String type= obj.getString("type");

                if(!visualElementTypes.containsKey(type))
                    visualElementTypes.put(type,new ArrayList<VisualElement>());

                visualElementTypes.get(type).add(element);
            }
            if(obj.has("scrollable"))
            {
                if(obj.getBoolean("scrollable"))
                    scrollableElements.add(element);
            }
            if(obj.has("layer"))
            {
                Integer layer= obj.getInt("layer");
                //esto es en caso de que hayan layers que no se usan o la lectura de layers no este en orden
                while(layer>=visualElementRenderList.size()) {
                    visualElementRenderList.add(new ArrayList<>());}
                 visualElementRenderList.get(layer).add(element);
            }
            else {
                //en caso de que no se especifique la layer lo añadimos a la layer 0
                visualElementRenderList.get(0).add(element);
            }
        }
        catch (JSONException e) {
            throw new RuntimeException(e);
    }
    }
    public void setAllCallbacks()
    {
        botones.forEach((k, v) ->
        {
            v.setCallback(this.engine.getState());
        });
    }
    /**
     * Metodo para cambiar el valor de un Texto
     * @param id nombre de identificacion en el JSON
     * @param value valor que se le quiere dar a dicho texto
     */
    public void setTextUI(String id, String value){
        if(textos.containsKey(id)){
            Text t = textos.get(id);
            if(t != null)
                t.setText(value);
        }
    }
    public void  unloadVisualElementOfType(String type,VisualElement element) {
        visualElementTypes.get(type).remove(element);
    }
    public ArrayList<VisualElement> getAllVisualElementsOfType(String type) {
        return visualElementTypes.get(type);
    }
    public void changeVisualElementStateOfType(String type,boolean state){
        for(VisualElement element:visualElementTypes.get(type)){
            element.setEnabled(state);
            element.setVisible(state);
        }
    }

    /**
     * Cambia la visibilidad de un texto de la UI
     * @param id ID del texto en el UIManager
     * @param c Condicion
     */
    public void setTextUIVisibity(String id, Boolean c){
        if(textos.containsKey(id)){
            textos.get(id).setVisible(c);
        }
    }

    /**
     * Metodo que cambia la visibilidad de una imagen estatica del HUD
     * @param id ID de la imagen
     * @param c condicion
     */
    /*public void setImageUIVisibility(String id, Boolean c){
        if(imagenes.containsKey(id)){
            imagenes.get(id).setVisible(c);
        }
    }*/

    /**
     * Metodo de deshabilta/habilita el boton
     * @param id ID del boton en el manager de la UI
     * @param c condicion
     */
    public void buttonEnabled(String id, boolean c){
        if(botones.containsKey(id)){
            botones.get(id).setEnabled(c);
            botones.get(id).setVisible(c);
        }
    }

    /**
     * Obtener el texto a traves de su ID en el JSON
     * @param id nombre de identificacion en el JSON
     * @return
     */
    public Text getTextUI(String id) {
        if(textos.containsKey(id))
            return textos.get(id);
        return null;
    }


    /**
     * Obtener el boton a traves de su ID en el JSON
     * @param id nombre de identificacion en el JSON
     * @return
     */
    public Button getButtonUI(String id) {
        if (botones.containsKey(id))
            return botones.get(id);
        return null;
    }

    public Image getButtonImage(String id){
        if(botones.containsKey(id) && !botones.get(id).isImageNull()){
            return botones.get(id).getImgButton();
        }
        return null;
    }
    public Figure getButtonFigure(String id){
        if(botones.containsKey(id) && !botones.get(id).isFigureNull()){
            return botones.get(id).getFigButton();
        }
        return null;
    }

    public Image getImageUI(String id){
        if(imagenes.containsKey(id))
            return imagenes.get(id);
        return null;
    }
    public Figure getFigureUI(String id){
        if(figures.containsKey(id))
            return figures.get(id);
        return null;
    }



    /*public void addButtonUI(String id, Button b){
        botones.put(id,b);
    }*/

    /**
     * Borra un boton de la lista
     * @param id ID del boton
     */
    /*public void deleteButtonUI(String id){
        if(botones.containsKey(id))
            botones.remove(id);
    }*/


    /**
     * Metodo para renderizar los elementos de la UI de la escena
     * @param gr Referencia al gestor de gráficos de Android
     */
    public void render(AndroidGraphics gr){
        for(ArrayList<VisualElement> array: visualElementRenderList) {
            for(VisualElement element: array){
                element.Render(gr);
            }
        }
    }

    /**
     * Metodo que crea un boton a partir de un archivo prefab (ideal para Mundo)
     * @param prefab JSON Object que contiene la info del prefab
     * @param amount cantidad de prefabs que se quieren generar
     */
    public void createPrefabs(JSONObject prefab, int amount)
    {   try {
        Button prefabButton = null;
        float yOffset = (float)prefab.getDouble("yOffset");
        float xOffset = (float)prefab.getDouble("xOffset");
        float xLimit = (float)prefab.getDouble("xLimit");
        int xIndex = 0;
        int yIndex = 0;
             for (int i = 0; i < amount; i++) {
                prefabButton = new Button(prefab, this.gr);
                 prefabButton.setY(prefabButton.getY() + prefabButton.getHeight() * (float) yIndex * yOffset);
                prefabButton.setX(prefabButton.getX() + prefabButton.getWidth() * (float) xIndex * xOffset);
                xIndex++;
                if (prefabButton.getX() > xLimit&&xLimit!=-1) {
                xIndex = 0;
                yIndex++;
                }
                botones.put(prefab.getString("id") + i, prefabButton);
                addVisualElementToArray(prefabButton, prefab);

                prefabButton.setCallback(this.engine.getState());
            }
        }
        catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
    public void configurarLimitesScroll() {
        Collections.sort(scrollableElements,(obj1,obj2)-> {
            if(obj1.getY()<obj2.getY()) return -1;
            if(obj1.getY()>obj2.getY()) return 1;
            return 0;
        });
        //Los botones deben estar entre estas dos posiciones
        //Posicion más alta permitida para el scroll alrededor de Y
        this.minY = scrollableElements.get(0).getY();
        //Posicion más baja permitaida para el scroll
        this.maxY = this.tamScroll - scrollableElements.get(0).getHeight();
    }
    //si es de tipo touchmove manejamos el scroll del juego
    public void onTouchMove(TouchEvent e)
    {
        if(!scroll)
            return;

        float destY=e.y-lastTouchedY;
        lastTouchedY=e.y;
        boolean canScroll=true;
        //checkeamos si los extremeos de los objetos scrolleables (el mas alto y el mas bajo)
        //estan entre el minimo y maximo Y que hemos definido
        //Si es asi, ya no podemos scrollear mas
        if((scrollableElements.get(0).getY() > minY && destY>0)
                ||(scrollableElements.get(scrollableElements.size()-1).getY() < maxY && destY<0))
            canScroll=false;

        //Renderizo los botones en la nueva posicion y sumandole el desplazamiento
        if(canScroll) {
            for (int i = 0; i < scrollableElements.size(); i++) {
                float newY = scrollableElements.get(i).getY() + destY;
                scrollableElements.get(i).setY(newY);
            }
        }
    }
    //si el evento es de tipo TouchDown guardamos el ultimo valor de la Y y ponemo a true el scroll
    public void onTouchDown(TouchEvent e){
        lastTouchedY=e.y;
        scroll=true;
    }
    /**
     * Si tenemos un TouchEvent de tipo TouchUp, El scroll se pone a falso
     */
    public void onTouchUp(){
        scroll = false;
    }
    /**
     * Recorremos el array de botones y
     * @param event
     * @return
     */
    public boolean handleInput(TouchEvent event) {
        // Recorremos la lista y comprobamos si se pulsan los botones
        for (Button b : botones.values()) {
            if (b.handleInput(event))
                return true;
        }
        return false;
    }
}
