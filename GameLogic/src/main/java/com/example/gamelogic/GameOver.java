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

    //determina si el jugador completó el nivel con anterioridad en el modo aventura
    boolean isCompleted;
    int diamantes; //la cantidad total de diamantes que tiene el jugador antes de retirar la recompensa
    Text textoDiamantes; //Representa la cantidad de diamantes
    Text textoRecompensa;

    JSONObject botones;

    //Recompensa que ganará el jugador
    int recompensa = 0;

    Image imagenDiamantes;

    //Dificultad con la que se ha superado el nivel (Para saber el modo de juego)
    GameLogic.Dificultad dificultad;
    public GameOver(Engine engine, Audio audio, Mobile mobile, GameLogic.Dificultad dificultad ,boolean win, boolean isCompleted)
    {
        //Inicializamos los botones y textos
        this.engine = engine;
        this.botones=engine.readJsonFile("GameOver/style.json");
        this.win=win;
        this.dificultad = dificultad;
        this.setAudio(audio);
        this.audio = audio;
        this.mobile = mobile;

        //si estamos en modo aventura, se detectara si el nivel se completó, en modo normal será true por defecto
        this.isCompleted =
                (this.dificultad == GameLogic.Dificultad.aventura) ? isCompleted : true;


        //Botones comunes de ambos modos
        botonMenu = new Button(botones.getJSONObject("BotonMenu"));
        botonCompartir =new Button(botones.getJSONObject("BotonIntent"));
        botonCompartir.setImagen(new Image(botones.getJSONObject( "ImagenCompartir"),this.engine.getGraphics()));
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
            botonRecompensaAd.setText(new Text(botones.getJSONObject("TextoAdx2")));

            //Diamantes que tenemos ahora mismo
            this.diamantes= this.engine.leerParametroInt("gems");
            this.textoDiamantes = new Text(botones.getJSONObject("TextoDiamantesActuales"));
            this.textoDiamantes.setText(String.valueOf(this.diamantes));

            this.textoRecompensa = new Text(botones.getJSONObject("TextoRecompensaAd"));

        }

        //Dependiendo del resultado de la partida reproducimos un sonido distinto
        if(win) {
            textoInicial = new Text(botones.getJSONObject("TextoWin"));
            this.victory = this.audio.newSound("victory_trumpet.wav");
            this.audio.playSound(this.victory);

            if(dificultad == GameLogic.Dificultad.aventura){
                //Si el nivel no estaba completado, añadimos 10 de recompensa
                if(!isCompleted){
                    this.recompensa = 10;
                    this.engine.modificarParametro("gems", recompensa); //incrementamos la recompensa
                    this.diamantes += this.recompensa;
                }
                else{
                    this.botonRecompensaAd.setText(new Text(botones.getJSONObject("TextoAd")));
                    this.recompensa = 0;
                }
            }
        }
        else {
            textoInicial = new Text(botones.getJSONObject("TextoLose"));
            this.lose = this.audio.newSound("death_sound.wav");
            this.recompensa = 0;
            this.audio.playSound(this.lose);

            if(dificultad == GameLogic.Dificultad.aventura){
                //Si ha perdido, no habra recompensa
                this.botonRecompensaAd.setText(new Text(botones.getJSONObject("TextoAd")));
                this.recompensa = 0;

            }
        }

        //Se muestra el banner
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
            textoDiamantes.Render(gr);
            textoRecompensa.Render(gr);
            this.imagenDiamantes = new Image(botones.getJSONObject("ImagenDiamante"),gr);
            this.imagenDiamantes.Render();
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
                        reclamarRecompensa();
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
        return boton != null && boton.contains(x, y);
    }

    private void reclamarRecompensa(){
        this.mobile.showRewardedAd(); //vemos el anuncio
        this.recompensa = 10; //incrementamos la recompensa en 10
        //Escondemos e inhabilitamos el boton
        this.botonRecompensaAd.setEnabled(false);
        this.botonRecompensaAd.setVisible(false);

        //Modificamos el parmametro añadiendole la cantidad de recompensa en el archivo de guardado
        this.engine.modificarParametro("gems", recompensa);
        //Modificamos la cantidad actual de diamantes en el texto
        this.diamantes += this.recompensa;
        //Actualizamos el texto de Game Over del numero de diamantes
        this.textoDiamantes.setText(String.valueOf(this.diamantes));
        this.textoRecompensa.setText(" ");//quitamos la cantidad de la recompensa

    }

}
