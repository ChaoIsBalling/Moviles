package com.example.gamelogic.managers;


import com.example.androidengine.AndroidEngine;
import com.example.androidengine.AndroidGraphics;
import com.example.androidengine.TouchEvent;
import com.example.gamelogic.Image;
import com.example.gamelogic.button.Button;
import com.example.gamelogic.Text;

import java.util.HashMap;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UIManager {
    // Hash Maps donde vamos a guardar los elementos de la UI de la escena
    private HashMap<String, Button> botones = new HashMap<>();
    private HashMap<String, Text> textos = new HashMap<>();
    //Aqui se guardan las imagenes que sirven como prefab y no se renderizan todo el rato
    private HashMap<String, Image> imagenes = new HashMap<>();

    private HashMap<String, Image> imagenesHUD = new HashMap<>(); // Solo para render automático

    public UIManager(JSONObject sceneJson, AndroidEngine engine, AndroidGraphics gr) {
        // Limpiamos lo anterior para cargar la nueva escena
        botones.clear();
        textos.clear();
        imagenes.clear();

        try {
            //Cargar Botones
            if (sceneJson.has("buttons")) {
                JSONArray array = sceneJson.getJSONArray("buttons");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject bData = array.getJSONObject(i);
                    Button b = new Button(bData,gr);
                    botones.put(bData.getString("id"), b);
                }
            }

            //Textos
            if (sceneJson.has("texts")){
                JSONArray array = sceneJson.getJSONArray("texts");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject tData = array.getJSONObject(i);
                    Text t = new Text(tData);
                    //Añadimos al hashMap con su id Correspondiente
                    textos.put(tData.getString("id"), t);
                }
            }

            //Imagenes
            if(sceneJson.has("images")){
                JSONArray array = sceneJson.getJSONArray("images");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject imageData = array.getJSONObject(i);
                    Image img = new Image(imageData, engine.getGraphics());
                    imagenes.put(imageData.getString("id"), img);

                    // Si el JSON dice que es HUD, la añadimos a la lista de renderizado
                    if (imageData.optBoolean("isHUD", false)) {
                        imagenesHUD.put(imageData.getString("id"),img);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
    public void setImageUIVisibility(String id, Boolean c){
        if(imagenesHUD.containsKey(id)){
            imagenesHUD.get(id).setVisible(c);
        }
    }

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

    public String getButtonUIText(String id){
        if (botones.containsKey(id) && botones.get(id).getTextButton() != null)
            return botones.get(id).getTextButton().getText();
        return null;
    }

    public Image getButtonImage(String id){
        if(botones.containsKey(id) && botones.get(id).getImgButton() != null){
            return botones.get(id).getImgButton();
        }
        return null;
    }

    public Image getImageUI(String id){
        if(imagenes.containsKey(id))
            return imagenes.get(id);
        return null;
    }


    /**
     * Metodo para renderizar los elementos de la UI de la escena
     * @param gr Referencia al gestor de gráficos de Android
     */
    public void render(AndroidGraphics gr){
        for (Button b : botones.values()) b.Render(gr);
        for (Image img : imagenesHUD.values()) img.Render();
        for (Text txt : textos.values()) txt.Render(gr);
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
