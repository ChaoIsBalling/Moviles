package com.example.gamelogic.states;

import com.example.androidengine.AndroidEngine;
import com.example.androidengine.State;
import com.example.androidengine.TouchEvent;
import com.example.androidengine.AndroidGraphics;
import com.example.androidengine.AndroidAudio;
import com.example.androidengine.AndroidMobile;
import com.example.gamelogic.Button;
import com.example.gamelogic.Image;
import com.example.gamelogic.Text;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Clase que implementa el menú inicial
 */
public class Menu implements State {

    //Boton y titulo inicial
    private Button botonInicial;
    private Text textoInicial;

    private Button botonAventura;

    private Button botonTienda;

    private Text textoDiamantes;

    private Image imagenDiamante;

    private String fondo;
    //El archivo de guardado del juego
    private JSONObject save;


    //Referencias al Audio Manager, al motor y a Graphics y a Mobile
    private AndroidAudio audio;
    AndroidEngine engine;
    AndroidGraphics gr;
    AndroidMobile mobile;

    JSONObject botones;

    /**
     * Constructora del menú
     */
    public Menu(AndroidEngine engine, AndroidMobile mobile, JSONObject save){
        this.engine = engine;
        this.mobile = mobile;
        this.save=save;
        botones=engine.readJsonFile("Menu/style.json");
        try {
            this.botonInicial = new Button(botones.getJSONObject("BotonInicial"));
            this.botonInicial.setText(new Text(botones.getJSONObject("TextoBoton")));

            this.textoInicial = new Text(botones.getJSONObject("TextoInicial"));

            this.botonAventura = new Button(botones.getJSONObject("BotonAventura"));
            this.botonAventura.setText( new Text(botones.getJSONObject("TextoAventura")));

            this.botonTienda = new Button(botones.getJSONObject("BotonTienda"));
            this.botonTienda.setText( new Text(botones.getJSONObject("TextoTienda")));

            //cambiamos el numero de gemas dependiendo de cuanto dinero hemos ganado
            this.textoDiamantes = new Text(botones.getJSONObject("TextoDiamantes"));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        this.fondo="#FFFFFFFF";
        try {
            this.textoDiamantes.setText(String.valueOf(this.save.getInt("gems")));
            this.fondo = this.save.getString("fondo");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


        this.mobile.setVisibleAdBanner(true);
    }

    @Override
    public void update(double deltaTime) {
    }

    /**
     * Renderiza la UI
     * @param gr Graphics del motor
     */
    @Override
    public void render(AndroidGraphics gr) {
        botonInicial.Render(gr);
        textoInicial.Render(gr);
        botonAventura.Render(gr);
        botonTienda.Render(gr);
        textoDiamantes.Render(gr);
        this.imagenDiamante.Render();
    }

    /**
     * Inicializa Graphics
     * @param gr Graphics
     */
    @Override
    public void setGraphics(AndroidGraphics gr) {
        this.gr=gr;
        try {
            this.imagenDiamante = new Image(botones.getJSONObject("ImagenDiamante"),gr);
            this.gr.setColorClear(this.fondo);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gestiona la entrada del jugador
     * @param list Lista de eventos
     * @param elapseTime Tiempo trascurrido
     */
    @Override
    public void handleInput(ArrayList<TouchEvent> list, double elapseTime) {
        for(TouchEvent e: list){
            //Si es nulo no se procesa
            if (e == null || e.type == null) {
                continue;
            }
            switch (e.type){
                    case TOUCH_DOWN:
                        if(this.botonInicial.contains(e.x,e.y)){
                            Dificultad dificultad = new Dificultad(this.engine,this.mobile,this.save);
                            this.engine.setState(dificultad);
                        }
                        else if(this.botonAventura.contains(e.x,e.y)){
                            Mundo mundo = new Mundo(this.engine,this.mobile,1,this.save);
                            this.engine.setState(mundo);
                        }
                        else if(this.botonTienda.contains(e.x,e.y)){
                            Tienda tienda = new Tienda(this.engine,this.mobile,this.save);
                            this.engine.setState(tienda);
                            //this.engine.showNotificacion("Hola","TOnto");
                        }
                        break;
                    case TOUCH_UP:

                        break;
                    case TOUCH_MOVE:
                        break;
                }

        }
    }

    /**
     * Inicializa el Audio
     * @param audio Interfaz Audio
     */
    @Override
    public void setAudio(AndroidAudio audio) {
    this.audio=audio;
    }

    /**
     * Inicializa Mobile
     * @param mobile Interfaz Mobile
     */
    @Override
    public void setMobile(AndroidMobile mobile) {
        this.mobile = mobile;
    }
}
