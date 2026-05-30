package com.example.gamelogic.states;

import com.example.androidengine.AndroidEngine;
import com.example.androidengine.AndroidGraphics;
import com.example.androidengine.State;
import com.example.androidengine.RewardCallback;
import com.example.androidengine.TouchEvent;
import com.example.androidengine.AndroidAudio;
import com.example.androidengine.AndroidSound;
import com.example.androidengine.AndroidMobile;
import com.example.gamelogic.button.Button;
import com.example.gamelogic.managers.UIManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Clase que representa el estado de final de partida
 */
public class GameOver implements State {
    //Sonidos de victoria y derrota
    private AndroidSound victory;
    private AndroidSound lose;

    //Modulos del motor
    private AndroidAudio audio;
    AndroidEngine engine;
    AndroidMobile mobile;
    private AndroidGraphics gr;

    //Determina si el jugador ha ganado
    boolean win;

    //nivel, mundo y oleada actual
    int nivel;
    int mundo;
    int oleada;

    //determina si el jugador completó el nivel con anterioridad en el modo aventura
    boolean isCompleted;
    int numDiamantes; //la cantidad total de diamantes que tiene el jugador antes de retirar la recompensa

    //archivo de guardado de JSON
    private JSONObject save;
    //Recompensa que ganará el jugador
    int recompensa = 0;

    //Dificultad con la que se ha superado el nivel (Para saber el modo de juego)
    GameLogic.Dificultad dificultad;
    //Manager de la interfaz
    UIManager ui;
    //JSON Object que contiene el estilo de los elementos de la UI
    JSONObject style;
    public GameOver(AndroidEngine engine, AndroidAudio audio, AndroidMobile mobile, GameLogic.Dificultad dificultad ,boolean win, boolean isCompleted, int nivel, int mundo, int oleada,JSONObject save)
    {
        //Inicializamos los botones y textos
        this.save=save;
        this.engine = engine;
        this.win=win;
        this.dificultad = dificultad;

        this.setAudio(audio);
        this.audio = audio;
        this.mobile = mobile;
        this.nivel=nivel; this.mundo=mundo; this.oleada = oleada;

        //si estamos en modo aventura, se detectara si el nivel se completó, en modo normal será true por defecto
        this.isCompleted =
                (this.dificultad == GameLogic.Dificultad.aventura) ? isCompleted : true;

        this.mobile.setVisibleAdBanner(true);

        //Si no se ha completado el nivel la recompensa será de 10 diamantes de base
        if(!isCompleted)
            this.recompensa = 10;
        else
            this.recompensa = 0;
    }
    private void configurarUI() {
        if (dificultad==GameLogic.Dificultad.aventura)
                configurarUIAventura();
        if (win)
            configurarVictoria();
        else
            configurarDerrota();

    }

    private void configurarVictoria() {
        //Sonido
        victory = audio.newSound("victory_trumpet.wav");
        audio.playSound(victory);

        //Logica exclusiva del modo aventura
        if (dificultad == GameLogic.Dificultad.aventura) {
            gestionarRecompensaVictoria();
        }
    }

    /**
     * Metodo que gestiona la recompensacion por la victoria
     */
    private void gestionarRecompensaVictoria() {
        //Si el nivel no estaba completado
        if (!isCompleted) {
            //Texto con la cantidad de diamantes actuales (sin recompensa)
            ui.setTextUI(
                    "TEXT_DIAMANTES_ACTUALES",
                    String.valueOf(numDiamantes)
            );
            numDiamantes += recompensa;

            //Guardamos los diamantes por si acaso el usuario no ve el anuncio
            try {
                save.put("gems", numDiamantes);
            }
            catch (JSONException e) {
                throw new RuntimeException(e);
            }
            ui.setTextUI(
                    "TEXT_RECOMPENSA_AD",
                    "+10"
            );
        }
        else {
            recompensa = 0;
            //Solo puede ganar viendo anuncio
            ui.setTextUI("TEXT_RECOMPENSA_AD", "+10");
        }
    }
    private void configurarDerrota() {
        //Sonido de derrota
        lose = audio.newSound("death_sound.wav");
        audio.playSound(lose);
    }

    public void setCallbackButtonReturn(Button b) {
        if (dificultad != GameLogic.Dificultad.aventura) {
        b.setOnClickListener(() -> {
            Dificultad dificultad = new Dificultad(engine, mobile, save);
            engine.setState(dificultad);});
        }
        else {
        b.setOnClickListener(() -> {
            Mundo mundo = new Mundo(engine, mobile, this.mundo, save);
            engine.setState(mundo);});
        }
    }
    public void setCallbackButtonMenu(Button b) {
        b.setOnClickListener(() -> {
        Menu menu= new Menu(this.engine,this.mobile,this.save);
        this.engine.setState(menu);});
    }
    public void setCallbackButtonIntent(Button b) {
        b.setOnClickListener(() -> this.compartirMensaje());
    }
    public void setCallbackButtonWinAd(Button b) {
        b.setOnClickListener(this::reclamarRecompensaDuplicada);
        //Si el nivel ya estaba completado NO se duplica la recompensa, se dará 10 diamantes solamente
        if (this.isCompleted)
            b.changeText("AD");
    }

    private void configurarUIAventura() {
        try {
            this.numDiamantes = this.save.getInt("gems");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        //texto con los diamantes actuales
        ui.setTextUI("TEXT_DIAMANTES_ACTUALES", String.valueOf(this.numDiamantes));
    }

    /**
     * Metodo que determina el tipo de UI que se va a usar
     */
    private void determinarUI(){
        try {
        if(this.win) {
            this.ui.loadUIFromJson(this.style.getJSONObject("Win"));
            //Si el modo de juego es el de aventura...
            if(this.dificultad == GameLogic.Dificultad.aventura)
                this.ui.loadUIFromJson(this.style.getJSONObject( "AdventureWin"));
        }
        else
            this.ui.loadUIFromJson(this.style.getJSONObject("Lose"));
        } catch (JSONException e) {
        throw new RuntimeException(e);
        }
    }
    @Override
    public void update(double deltaTime) { }

    /**
     * Renderiza los elementos de la UI
     * @param gr Graphics del motor
     */
    @Override
    public void render(AndroidGraphics gr) {
        gr.setColor(0x00000000);
        this.ui.render(gr);
    }

    @Override
    public void setGraphics(AndroidGraphics gr) {
        this.gr = gr;

        //Determinamos que UI Setear
        String uiPath = "GameOver/style.json";
        this.style = engine.readJsonFile(uiPath);
        //Incializamos la UI
        this.ui = new UIManager(this.style , this.engine, this.gr);
        this.ui.setAllCallbacks();
        determinarUI();
        //Configuramos la UI dependiendo del resultado
        configurarUI();
    }

    /**
     * Gestion del input
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
                    this.ui.handleInput(e);
                    break;
                case TOUCH_UP:
                    break;
                case TOUCH_MOVE:
                    break;
            }
        }
    }

    private void compartirMensaje(){
        String message;
        if(this.win) {
            if(this.dificultad == GameLogic.Dificultad.corto){
                message = "Mira lo bueno que soy me he pasado una partida corta del mejor tower defense de todos los tiempos";
            }
            else if(this.dificultad == GameLogic.Dificultad.largo){
                message = "Mira lo bueno que soy me he pasado una partida larga del mejor tower defense de todos los tiempos";
            }
            else {
                message = "Por fin me he pasado el nivel " + this.nivel + " del mundo " + this.mundo + " del mejor tower defense de todos los tiempos";
            }
        }
        else
        {
            if(this.dificultad == GameLogic.Dificultad.infinito){
                message = "Mira lo bueno que soy me he llegado a la oleada "+this.oleada+" del modo infinito del mejor tower defense de todos los tiempos";
            }
            else {
                message = "Soy una desgracia >:(";
            }
        }
        this.engine.launchShareIntent(message); //intent
    }

    /**
     * Inicializamos la referencia al Audio Manager
     * @param audio Interfaz Audio
     */
    @Override
    public void setAudio(AndroidAudio audio) {
        this.audio=audio;
    }
    @Override
    public void setMobile(AndroidMobile mobile) {
        this.mobile = mobile;
    }
    @Override
    public JSONObject getSave() {
        return this.save;
    }

    /**
     * Se llama a este metodo cuando se pulsa el boton de ver anuncio para duplicar
     * u obtener más recompensas
     */
    private void reclamarRecompensaDuplicada(){
        //Llamamos a la interfaz mobile con un callback para
        //que se encargue de la gestión de recompensas una vez ya se ha reproducido el anuncio
        this.mobile.showRewardedAd(new RewardCallback() {
            @Override
            public void onReward() {

                //Se dan 10 diamantes más
                recompensa = 10;

                //Modificamos la cantidad actual de diamantes en el texto
                numDiamantes = numDiamantes + recompensa;
                try {
                    save.put("gems", numDiamantes);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                //Escondemos e inhabilitamos el boton

                System.out.println("has ganado en total " + recompensa + "diamantes");
                inhabilitarBotonRecompensa();
                actualizarTextoDiamantes(numDiamantes);  //Actualizamos el texto de Game Over del numero de diamantes
                ocultarTextoRecompensa();//quitamos la cantidad de la recompensa
            }
        }); //vemos el anuncio y si se acaba de ver, damos recompensa
    }

    /**
     * Este metodo hace que un boton no se vea y que quede inutilizable
     */
    private void inhabilitarBotonRecompensa(){
        this.ui.buttonEnabled("BUT_RECOMPENSA_AD", false);
    }

    /**
     * Oculta el texto poniendo una cadena vacía
     */
    private void ocultarTextoRecompensa(){
        this.ui.setTextUIVisibity("TEXT_RECOMPENSA_AD", false);
    }

    /**
     * Oculta el texto poniendo una cadena vacía
     */
    private void actualizarTextoDiamantes(int numDiamantes){
        this.ui.setTextUI("TEXT_DIAMANTES_ACTUALES", String.valueOf(numDiamantes));
    }
}
