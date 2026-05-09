package com.example.gamelogic.states;
import com.example.androidengine.AndroidEngine;
import com.example.androidengine.State;
import com.example.androidengine.TouchEvent;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import com.example.androidengine.AndroidAudio;
import com.example.androidengine.AndroidGraphics;
import com.example.androidengine.AndroidMobile;
import com.example.gamelogic.button.Button;
import com.example.gamelogic.Image;
import com.example.gamelogic.Text;

/**
 * Clase que representa el menú de seleccción de dificultad
 */
public class Dificultad implements State {
    //Botones del menú de dificultad
    private Button botonCorto;
    private Button botonLargo;
    private Button botonInfinito;
    private Button botonVolver;
    //El archivo de guardado del juego
    private JSONObject save;
    //Referencias de modulos del motor
    private AndroidEngine engine;
    AndroidGraphics gr;

    AndroidMobile mobile;

    //Json del que vamos a leer los parametros de los botones
    JSONObject botones;

    /**
     * Constructora del menú de dificultad con los tres botones que representan los tres modos de juego
     * @param engine
     */
    public Dificultad(AndroidEngine engine, AndroidMobile mobile,JSONObject save){
        this.save=save;
        this.engine = engine;
        this.mobile = mobile;
        botones=engine.readJsonFile("Dificultad/style.json");//Archivo a leer

        //botones y textos
        try {
            botonCorto = new Button(botones.getJSONObject("BotonCorto"));
            botonCorto.setText(new Text(botones.getJSONObject("TextoC")));
            botonLargo = new Button(botones.getJSONObject("BotonLargo"));
            botonLargo.setText(new Text(botones.getJSONObject("TextoL")));
            botonInfinito = new Button(botones.getJSONObject("BotonInfinito"));
            botonInfinito.setText(new Text(botones.getJSONObject("TextoI")));
            this.botonVolver = new Button(botones.getJSONObject("BotonVolver"));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


        //escondemos el banner
        this.mobile.setVisibleAdBanner(false);
    }
    @Override
    public void update(double deltaTime) {

    }

    /**
     * Renderiza los botones
     */
    @Override
    public void render(AndroidGraphics gr) {
        botonCorto.Render(gr);
        botonLargo.Render(gr);
        botonInfinito.Render(gr);
        botonVolver.Render(gr);
    }

    /**
     * setter del graphics
     * @param gr Graphics
     */
    @Override
    public void setGraphics(AndroidGraphics gr) {
        this.gr=gr;
        try {
            this.botonVolver.setImagen(new Image(botones.getJSONObject("ImagenVolver"),gr));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Dependiendo del modo elegido, iremos al estado GameLogic
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
                    if(this.botonCorto.contains(e.x,e.y)){
                        GameLogic gameLogic = new GameLogic(this.engine,this.mobile, GameLogic.Dificultad.corto,this.save);
                        this.engine.setState(gameLogic);
                    }
                    else if(this.botonLargo.contains(e.x,e.y)){
                        GameLogic gameLogic = new GameLogic(this.engine,this.mobile, GameLogic.Dificultad.largo,this.save);
                        this.engine.setState(gameLogic);
                    }
                    else if(this.botonInfinito.contains(e.x,e.y)){
                        GameLogic gameLogic = new GameLogic(this.engine,this.mobile,GameLogic.Dificultad.infinito,this.save);//el -1 es para indicar que es infinito
                        this.engine.setState(gameLogic);
                    }
                    else if(this.botonVolver.contains(e.x,e.y)){
                        Menu menu = new Menu(this.engine,this.mobile,this.save);
                        this.engine.setState(menu);
                    }
                    break;
                case TOUCH_UP:

                    break;
                case TOUCH_MOVE:
                    break;
            }
        }
    }

    @Override
    public void setAudio(AndroidAudio audio) {
    }

    @Override
    public void setMobile(AndroidMobile mobile) {
    }

    @Override
    public JSONObject getSave() {
        return this.save;
    }

}
