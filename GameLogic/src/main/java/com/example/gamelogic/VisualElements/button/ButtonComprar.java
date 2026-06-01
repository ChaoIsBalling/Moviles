package com.example.gamelogic.VisualElements.button;

import com.example.androidengine.AndroidGraphics;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ButtonComprar extends Button {
    int coste;
    String descripcion;
    String desbloqueo;
    private ArrayList<ButtonCambio> botonesCambio;
    //Constructora que te crea un boton a partir de un Json
    public ButtonComprar(JSONObject json)
    {
        super(json);
        try {
            this.coste=json.getInt("coste");
            this.descripcion=json.getString("descripcion");
            this.desbloqueo=json.getString("desbloqueo");
            this.botonesCambio = new ArrayList<ButtonCambio>();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public int getCoste() {
        return coste;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getDesbloqueo() {
        return desbloqueo;
    }

    public void addBotonCambio(ButtonCambio cambio){
        this.botonesCambio.add(cambio);
    }

    public boolean pulsarCambio(float x, float y, JSONObject save){
        boolean cambiado = false;
        for(int i =0; i < this.botonesCambio.size();i++){
            if(this.botonesCambio.get(i).contains(x,y)){
                try {
                    save.put(this.botonesCambio.get(i).getGuardado(),this.botonesCambio.get(i).getDatoGuardado());
                    this.botonesCambio.get(i).setColor("#ff00ff00");
                    cambiado=true;
                }catch (JSONException ex) {
                    throw new RuntimeException(ex);
                }
            }
            else{
                this.botonesCambio.get(i).setColor("#ffffffff");
            }
        }
        return cambiado;
    }

    public void RenderCambio(AndroidGraphics gr) {
        for (int i =0; i<this.botonesCambio.size();i++){
            this.botonesCambio.get(i).Render(gr);
        }
    }
}
