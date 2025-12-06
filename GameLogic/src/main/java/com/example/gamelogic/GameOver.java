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

    private Button botonRecompensaAd;

    private Button botonCompartir;

    //Textos
    private Text textoInicial;

    //Sonidos de victoria y derrota
    private Sound victory;
    private Sound lose;
    private Audio audio;
    Engine engine;

    Mobile mobile;
    //Determina si el jugador ha ganado
    boolean win;
    public GameOver(Engine engine, Audio audio,boolean win)
    {
        //Inicializamos los botones y textos
        this.engine = engine;
        JSONObject botones=engine.readJsonFile("GameOver/style.json");
        this.win=win;
        //this.setAudio(audio);

        botonMenu = new Button(botones.getJSONObject("BotonMenu"));
        botonReintentar = new Button(botones.getJSONObject("BotonReintentar"));
        botonRecompensaAd = new Button(botones.getJSONObject("BotonRecompensaAd"));
        this.botonCompartir =new Button(botones.getJSONObject("BotonIntent"));

        botonMenu.setText(new Text(botones.getJSONObject("TextoBoton")));
        botonReintentar.setText(new Text(botones.getJSONObject("TextoReintentar")));
        botonRecompensaAd.setText(new Text(botones.getJSONObject("TextoAd")));
        //Dependiendo del resultado reproducimos un sonido distinto
        if(win) {
            textoInicial = new Text(botones.getJSONObject("TextoWin"));

            //this.victory = this.audio.newSound("victory_trumpet.wav");
            //this.audio.playSound(this.victory);
        }
        else {
            textoInicial = new Text(botones.getJSONObject("TextoLose"));

            //this.lose = this.audio.newSound("death_sound.wav");
            //this.audio.playSound(this.lose);
        }
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
        botonMenu.Render(gr);
        textoInicial.Render(gr);
        botonReintentar.Render(gr);
        botonRecompensaAd.Render(gr);
        botonCompartir.Render(gr);
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
                    if(this.botonMenu.contains(e.x,e.y)){
                        Menu menu= new Menu(this.engine);
                        this.engine.setState(menu);
                    }
                    if(this.botonReintentar.contains(e.x,e.y)){
                        Dificultad dificultad = new Dificultad(this.engine);
                        this.engine.setState(dificultad);
                    }
                    if(this.botonRecompensaAd.contains(e.x,e.y) && this.botonRecompensaAd.isEnable()){
                        this.mobile.showRewardedAd();
                        this.botonRecompensaAd.setEnabled(false);
                        this.botonRecompensaAd.setVisible(false);
                    }
                    if(this.botonCompartir.contains(e.x,e.y))
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

}
