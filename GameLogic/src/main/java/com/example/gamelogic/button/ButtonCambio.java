package com.example.gamelogic.button;

import org.json.JSONException;
import org.json.JSONObject;

public class ButtonCambio extends Button {

    String desbloqueo;
    String guardado;
    String datoGuardado;

    //Constructora que te crea un boton a partir de un Json
    public ButtonCambio(JSONObject json)
    {
        super(json);
        try {
            this.desbloqueo=json.getString("desbloqueo");
            this.guardado=json.getString("guardado");
            this.datoGuardado=json.getString("datoGuardado");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public String getDesbloqueo() {
        return desbloqueo;
    }

    public String getGuardado() {
        return guardado;
    }

    public String getDatoGuardado() {
        return datoGuardado;
    }
}
