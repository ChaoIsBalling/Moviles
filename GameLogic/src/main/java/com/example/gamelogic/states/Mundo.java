package com.example.gamelogic.states;

import com.example.androidengine.State;
import com.example.androidengine.TouchEvent;
import com.example.androidengine.AndroidEngine;
import com.example.androidengine.AndroidGraphics;
import com.example.androidengine.AndroidAudio;
import org.json.JSONException;
import org.json.JSONObject;
import com.example.androidengine.AndroidMobile;
import com.example.gamelogic.button.Button;
import com.example.gamelogic.figure.Square;
import com.example.gamelogic.Text;
import com.example.gamelogic.managers.UIManager;

import java.util.ArrayList;

public class Mundo implements State {
    private AndroidEngine engine;

    private AndroidMobile mobile;

    private AndroidGraphics gr;
    //booleanos que determinan si el mundo actual tiene un mundo anterior o posterior
    private boolean next;
    private boolean previous;

    private int numNiveles;
    //contador de niveles del mundo usado por el callback de niveles
    private int nivelesCount=0;

    //en que mundo estamos ahora
    private int mundo;

    //El archivo de guardado del juego
    private JSONObject save;


    //variable que inspecciona cuantos niveles hemos derrotado
    int completed;
    //JSON que almacena los prefabs necesarios para este estado
    JSONObject prefabs;

    //JSON en el que se van a almacenar todos los estilos para botones y textos
    JSONObject style;

    //Ui Manager que gestiona el funcionamiento de los botones
    UIManager ui;
    private int nivelesCompletadosEnMundo;
    private String colorCompleted;
    private String colorLocked;

    //constructora del estado que crea e inicializa los botones de la escena
    public Mundo(AndroidEngine engine,AndroidMobile mobile, int mundo, JSONObject save){
        this.save=save;
        this.engine=engine;
        this.mundo=mundo;
        this.mobile = mobile;

        this.mobile.setVisibleAdBanner(false);

    }

    @Override
    public void update(double deltatime) {}
    //renderización de todos los botones y texto
    @Override
    public void render(AndroidGraphics gr) {
        gr.EmpezarLimiteDibujado(0,100,600,400);
        gr.TerminarLimiteDibujado();
        this.ui.render(gr);
    }

    @Override
    public void setGraphics(AndroidGraphics gr) {
        this.gr = gr;
        this.style = engine.readJsonFile("Mundo/style.json");
        try {
            this.prefabs = engine.readJsonFile("Mundo/prefabs.json").getJSONObject("NivelMundo");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        inicializarUI();
    }

    @Override
    public void handleInput(ArrayList<TouchEvent> list, double elapseTime) {
        for(TouchEvent e: list){
            //Si es nulo no se procesa
            if (e == null || e.type == null) {
                continue;
            }
            switch (e.type){
                case TOUCH_DOWN:
                this.ui.handleInput(e);
                //gestionBotones(e);
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

    /**
     * Metodo que se encarga de leer los datos del mundo actual
     * e inicializar parametros necesarios para el renderizado
     */
    private void cargarDatosMundo(){
        try {
            //Json con la información del mundo
            JSONObject mundoInfo = engine.readJsonFile("Mundo/World"+ this.mundo + "/World"+ this.mundo +".json");

            //Numero de niveles del mundo actual
            this.numNiveles=mundoInfo.getInt("niveles");

            //Booleanos que indican si hay nivel antes o despues del mundo actual
            this.next=mundoInfo.getBoolean("next");
            this.previous=mundoInfo.getBoolean("previous");

            //Color para colorear los niveles completados y no completados
            this.colorCompleted = mundoInfo.getString("colorCompleted");
            this.colorLocked = mundoInfo.getString("colorLocked");

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Metodo que se encarga de clacular cuantos niveles se ha pasado el jugador en este mundo
     */
    private void calcularProgreso(){
        try {
            this.completed= this.save.getInt("completed");

            int nivelesHastaAhora =0;
            //esto calcula cuantos niveles han habido hasta este mundo
            for(int i = 1; i < this.mundo; i++)
            {
                JSONObject obj=engine.readJsonFile("Mundo/World"+i+"/World"+i+".json");
                nivelesHastaAhora+=obj.getInt("niveles");
            }
            //determina que niveles de ESTE mundo se han completado
            this.nivelesCompletadosEnMundo = this.completed-nivelesHastaAhora;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Metodo que se encarga de inicializar el nivel i + 1
     * @param index indice de [0 - n) niveles del mundo actual
     */
    private void iniciarNivel(int index){
            GameLogic gameLogic = new GameLogic(this.engine, this.mobile, "Mundo/World" +
                    this.mundo + "/Level" + (index + 1) + ".json", index+1,this.mundo,this.save);
            this.engine.setState(gameLogic);
    }

    /**
     * Metodo que setea el Callback de los niveles
     * @param b Boton que se le quiere setear el callback
     */
    public void setCallbackButtonLevel(Button b)
    {
        if(this.nivelesCount<=Math.min(this.numNiveles - 1, nivelesCompletadosEnMundo)) {
            b.setColor(colorCompleted);
            if(this.nivelesCount==nivelesCompletadosEnMundo)
                b.setColor(colorLocked);
            b.changeText(String.valueOf(this.nivelesCount+1));
            int ind=this.nivelesCount;
            b.setOnClickListener(() -> iniciarNivel(ind));
        }
        this.nivelesCount++;

    }
    /**
     * Metodo para setear el callback de los botones de siguiente y anterior mundo
     * @param b el boton al que le pasamos el callback
     */
    public void setCallbackButtonMundo(Button b) {
        JSONObject callback = b.getCallback();
        try {
            boolean nextWorld=callback.getBoolean("nextWorld");
            if((nextWorld&&!this.next)||(!nextWorld&&!this.previous)) {
                b.setVisible(false);
                b.setEnabled(false);
            }
            else {
                int targetWorld=(nextWorld)? 1:-1;
                b.setOnClickListener(  ()->{
                    Mundo mundoDestino = new Mundo(this.engine, this.mobile, this.mundo + targetWorld,this.save);
                    this.engine.setState(mundoDestino);});
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Metodo para setear el callback de volver a la pantalla anterior
     * @param b el boton al que le pasamos el callback
     */
    public void setCallbackButtonReturn(Button b)
    {
        b.setOnClickListener( () -> {
            Menu menu = new Menu(this.engine,this.mobile,this.save);
            this.engine.setState(menu);
        });
    }

    /**
     * Metodo que se encarga de inicializar todo lo que tiene que ver con la UI
     */
    private void inicializarUI(){
        cargarDatosMundo();
        this.ui = new UIManager(this.style,this.engine,this.gr);
        this.ui.setAllCallbacks();
        calcularProgreso();
        this.ui.createPrefabs(prefabs,this.numNiveles);
        this.ui.configurarLimitesScroll();
        this.ui.getTextUI("TEXT_MUNDO").setText("Mundo " + this.mundo);
    }

    @Override
    public void setAudio(AndroidAudio audio) {}
    @Override
    public void setMobile(AndroidMobile mobile) {}
    @Override
    public JSONObject getSave() {
        return this.save;
    }
}
