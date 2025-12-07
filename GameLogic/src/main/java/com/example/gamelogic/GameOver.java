package com.example.gamelogic;

import com.example.engine.Engine;
import com.example.engine.Graphics;
import com.example.engine.Mobile;
import com.example.engine.State;
import com.example.engine.TouchEvent;
import com.example.engine.Audio;
import com.example.engine.Sound;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Clase que representa el estado de final de partida
 */
public class GameOver implements State {

    //Botones de retorno al menú y al menú de dificultad
    private Button botonMenu;
    private Button botonReintentar;

    //Boton de retorno al mapa de mundos del modo aventura
    private Button botonVolverMundo;

    //Botones de ver anuncio recompensado y compartir en redes sociales
    private Button botonRecompensaAd;
    private Button botonCompartir;

    //Texto de resultado
    private Text textoInicial;

    //Sonidos de victoria y derrota
    private Sound victory;
    private Sound lose;
    private Audio audio;
    Engine engine;

    Mobile mobile;
    //Determina si el jugador ha ganado
    boolean win;

    //Dificultad con la que se ha superado el nivel (Para saber el modo de juego)
    GameLogic.Dificultad dificultad;
    public GameOver(Engine engine, Audio audio, Mobile mobile, GameLogic.Dificultad dificultad ,boolean win)
    {
        //Inicializamos los botones y textos
        this.engine = engine;
        JSONObject botones=engine.readJsonFile("GameOver/style.json");
        this.win=win;
        this.dificultad = dificultad;
        this.setAudio(audio);
        this.audio = audio;
        this.mobile = mobile;

        //Botones comunes de ambos modos
        botonMenu = new Button(botones.getJSONObject("BotonMenu"));
        botonCompartir =new Button(botones.getJSONObject("BotonIntent"));
        botonMenu.setText(new Text(botones.getJSONObject("TextoBoton")));


        //botones exclusivos del modo normal y del modo aventura
        if(dificultad != GameLogic.Dificultad.aventura) {
            botonReintentar = new Button(botones.getJSONObject("BotonReintentar"));
            botonReintentar.setText(new Text(botones.getJSONObject("TextoReintentar")));
        }
        else{
            botonVolverMundo = new Button(botones.getJSONObject("BotonVolverMundo"));
            botonVolverMundo.setText(new Text(botones.getJSONObject("TextoVolverMundo")));

            botonRecompensaAd = new Button(botones.getJSONObject("BotonRecompensaAd"));
            botonRecompensaAd.setText(new Text(botones.getJSONObject("TextoAd")));
        }

        //Dependiendo del resultado de la partida reproducimos un sonido distinto
        if(win) {
            textoInicial = new Text(botones.getJSONObject("TextoWin"));

            this.victory = this.audio.newSound("victory_trumpet.wav");
            this.audio.playSound(this.victory);
        }
        else {
            textoInicial = new Text(botones.getJSONObject("TextoLose"));

            this.lose = this.audio.newSound("death_sound.wav");
            this.audio.playSound(this.lose);
        }

        this.mobile.setVisibleAdBanner(true);

    }
    @Override
    public void update(double deltaTime) {

    }

    /**
     * Renderiza los elementos de la UI
     * @param gr Graphics del motor
     */
    @Override
    public void render(Graphics gr) {
        gr.setColor(0x00000000);

        //botones y texto coumunes de ambos modos de juego
        textoInicial.Render(gr);
        botonCompartir.Render(gr);

        if(this.dificultad!= GameLogic.Dificultad.aventura){
            botonMenu.Render(gr);
            botonReintentar.Render(gr);
        }else{
            botonMenu.Render(gr);
            botonRecompensaAd.Render(gr);
            botonVolverMundo.Render(gr);
        }
    }

    @Override
    public void setGraphics(Graphics gr) {

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
                    if(canClickButton(botonMenu,e.x,e.y)){
                        Menu menu= new Menu(this.engine,this.mobile);
                        this.engine.setState(menu);
                    }
                    if(canClickButton(botonReintentar,e.x,e.y)){
                        Dificultad dificultad = new Dificultad(this.engine,this.mobile);
                        this.engine.setState(dificultad);
                    }
                    if(canClickButton(botonRecompensaAd,e.x,e.y) && this.botonRecompensaAd.isEnable()){
                        this.mobile.showRewardedAd();
                        this.botonRecompensaAd.setEnabled(false);
                        this.botonRecompensaAd.setVisible(false);
                    }
                    if(canClickButton(botonCompartir,e.x,e.y))
                    {
                        String message;
                        if(this.win)
                        {
                            message="Mira lo bueno que soy en este juego";
                        }
                        else
                        {
                            message="Soy una desgracia >:(";
                        }
                        this.engine.luanchShareIntent(message);
                    }
                    if(canClickButton(botonVolverMundo,e.x,e.y)){
                        Mundo mundo = new Mundo(this.engine,this.mobile,1);
                        this.engine.setState(mundo);
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
     * Inicializamos la referencia al Audio Manager
     * @param audio Interfaz Audio
     */
    @Override
    public void setAudio(Audio audio) {
        this.audio=audio;
    }

    @Override
    public void setMobile(Mobile mobile) {
        this.mobile = mobile;
    }

    private boolean canClickButton(Button boton,float x, float y){
        if(boton != null && boton.contains(x,y)){
            return true;
        }
        return false;

    }

}
