package com.example.gamelogic;

import org.json.JSONException;
import org.json.JSONObject;

public class ButtonComprar extends Button {
    int coste;
    String descripcion;
    String desbloqueo;
    public ButtonComprar(float x, float y, float w, float h, boolean isRound, float ar, int coste, String descripcion, String desbloqueo) {
        super(x, y, w, h, isRound, ar);
        this.coste=coste;
        this.descripcion=descripcion;
        this.desbloqueo=desbloqueo;
    }
    public ButtonComprar(float x, float y, float w, float h, int coste, String descripcion, String desbloqueo){
        super(x,y,w,h);
        this.coste=coste;
        this.descripcion=descripcion;
        this.desbloqueo=desbloqueo;
    }
    //Constructora que te crea un boton a partir de un Json
    public ButtonComprar(JSONObject json)
    {
        super(json);
        try {
            this.coste=json.getInt("coste");
            this.descripcion=json.getString("descripcion");
            this.desbloqueo=json.getString("desbloqueo");
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
}
