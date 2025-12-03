package com.example.gamelogic;

import com.example.engine.Engine;
import com.example.engine.Graphics;
import com.example.engine.Mobile;
import com.example.engine.State;
import com.example.engine.TouchEvent;
import com.example.engine.Audio;
import com.example.engine.Sound;

import java.awt.Color;
import java.util.ArrayList;

/**
 * Clase que representa el estado de final de partida
 */
public class GameOver implements State {

    //Botones de retorno al menú y al menú de dificultad
    private Button botonMenu;
    private Button botonReintentar;

    //Textos
    private Text textoBoton;
    private Text textoReintentar;
    private Text textoInicial;

    //Sonidos de victoria y derrota
    private Sound victory;
    private Sound lose;
    private Audio audio;
    Engine engine;
    //Determina si el jugador ha ganado
    boolean win;
    public GameOver(Engine engine, Audio audio,boolean win)
    {
        //Inicializamos los botones y textos
        this.engine = engine;
        this.win=win;

        botonMenu = new Button(120, 250, 200,50,true,20);
        botonMenu.setColor("#FF999999");

        botonReintentar = new Button(480, 250, 200,50,true,20);
        botonReintentar.setColor("#FF999999");

        textoBoton = new Text("Inika-Regular.ttf","Menu",0,0,30,true,true);
        textoBoton.setColor("#FF00FFFF");
        textoReintentar = new Text("Inika-Regular.ttf","Reintentar",0,0,30,true,true);
        textoReintentar.setColor("#FF00FFFF");
        botonMenu.setText(textoBoton);
        botonReintentar.setText(textoReintentar);
        //Dependiendo del resultado reproducimos un sonido distinto
        if(win) {
            textoInicial = new Text("Inika-Regular.ttf", "VICTORIA", 300, 150, 40, true, true);
            this.setAudio(audio);
            this.victory = this.audio.newSound("victory_trumpet.wav");
            this.audio.playSound(this.victory);
        }
        else {
            textoInicial = new Text("Inika-Regular.ttf", "DERROTA", 300, 150, 40, true, true);
            this.setAudio(audio);
            this.lose = this.audio.newSound("death_sound.wav");
            this.audio.playSound(this.lose);
        }
        textoInicial.setColor("#FF000000");
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

    }

}
