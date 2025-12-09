package com.example.gamelogic;

import com.example.engine.Audio;
import com.example.engine.Engine;
import com.example.engine.Graphics;
import com.example.engine.Mobile;
import com.example.engine.State;
import com.example.engine.TouchEvent;

import org.json.JSONObject;

import java.util.ArrayList;

public class Mundo implements State {
    private Text textoMundo;
    private Square fondoTexto;

    private Button siguienteMundo;
    private Button anteriorMundo;
    private Button botonVolver;
    private ArrayList<Button> niveles;
    private Engine engine;

    private Mobile mobile;
    //booleanos que determinan si el mundo actual tiene un mundo anterior o posterior
    private boolean next;
    private boolean previous;

    private int numNiveles;

    //en que mundo estamos ahora
    private int mundo;

    //cuantos niveles han habido hasta ahora de cada mundo
    private int nivelesHastaAhora=0;

    //variable que inspecciona cuantos niveles hemos derrotado
    int completed;

    JSONObject botones;

    //Ultima cordenada Y tocada
    float lastTouchedY;

    //bool que nos dice si estamos haciendo scroll de pantalla
    boolean scroll;

    //valor minimo que puede tener la Y
    float minY;
    //valor maximo que puede tener la Y
    float maxY;

    //constructora del estado que crea e inicializa los botones de la escena
    public Mundo(Engine engine,Mobile mobile, int mundo){
        this.engine=engine;
        this.mundo=mundo;
        this.mobile = mobile;

        JSONObject save=this.engine.readJsonFile2("save");
        this.completed= save.getInt("completed");

        botones=engine.readJsonFile("Mundo/style.json");
        JSONObject mundoInfo=engine.readJsonFile("Mundo/World"+this.mundo+"/World"+this.mundo+".json");

        //esto calcula cuantos niveles han habido hasta este mundo
        for(int i=1;i<this.mundo;i++)
        {
            JSONObject obj=engine.readJsonFile("Mundo/World"+i+"/World"+i+".json");
            this.nivelesHastaAhora+=obj.getInt("niveles");
        }

        this.numNiveles=mundoInfo.getInt("niveles");

        this.next=mundoInfo.getBoolean("next");
        this.previous=mundoInfo.getBoolean("previous");

        niveles=new ArrayList<Button>();

        String colorCompleted=mundoInfo.getString("colorCompleted");
        String colorLocked=mundoInfo.getString("colorLocked");
        //inicialización de todos los botones de niveles
        for(int i=0;i<this.numNiveles;i++)
        {
                Button nivelMundo = new Button(botones.getJSONObject("NivelMundo"));
                Text nivel = new Text(botones.getJSONObject("TextoNivel"));
                nivel.setText("X");
                nivelMundo.setText(nivel);
                nivelMundo.setY(nivelMundo.getY()+nivelMundo.getHeight()*(float)i*1.5f);
                nivelMundo.setColor(colorLocked);
                niveles.add(nivelMundo);
        }
        for(int i=0;i<=Math.min(niveles.size()-1,this.completed-this.nivelesHastaAhora);i++)
        {
            niveles.get(i).setColor(colorCompleted);
            niveles.get(i).changeText(String.valueOf(i+1));

        }

        this.textoMundo = new Text(botones.getJSONObject("TextoMundo"));
        this.textoMundo.setText("Mundo "+this.mundo);
        this.fondoTexto = new Square(300,50,300,70,true);
        this.fondoTexto.setColor("#ffDAB628");

        //en caso de que hava un siguiente o anterior mundo inicializamos los botones correspondientes
        if(this.next) {
            this.siguienteMundo = new Button(botones.getJSONObject("SiguienteMundo"));
        }
        if(this.previous) {
            this.anteriorMundo = new Button(botones.getJSONObject("AnteriorMundo"));
        }
        this.botonVolver = new Button(botones.getJSONObject("BotonVolver"));

        this.mobile.setVisibleAdBanner(false);
    }

    @Override
    public void update(double deltatime) {

    }
    //renderización de todos los botones y texto
    @Override
    public void render(Graphics gr) {
        for(int i=0;i<niveles.size();i++)
            niveles.get(i).Render(gr);
        this.fondoTexto.Render(gr);
        this.textoMundo.Render(gr);
        if(this.next)
            this.siguienteMundo.Render(gr);
        if(this.previous)
            this.anteriorMundo.Render(gr);
        this.botonVolver.Render(gr);

    }

    @Override
    public void setGraphics(Graphics gr) {
        this.botonVolver.setImagen(new Image(botones.getJSONObject("ImagenVolver"),gr));
        if(this.next) {
            this.siguienteMundo.setImagen(new Image(botones.getJSONObject("ImagenSiguiente"),gr));
        }
        if(this.previous) {
            this.anteriorMundo.setImagen(new Image(botones.getJSONObject("ImagenAnterior"),gr));
        }
    }
//manejo de los inputs
    @Override
    public void handleInput(ArrayList<TouchEvent> list, double elapseTime) {
        for(TouchEvent e: list){
            //Si es nulo no se procesa
            if (e == null || e.type == null) {
                continue;
            }
            switch (e.type){
                case TOUCH_DOWN:
                   gestionBotones(e);
                   onTouchDown(e);
                    break;
                case TOUCH_UP:
                    onTouchUp();
                    break;
                case TOUCH_MOVE:
                    onTouchMove(e);
                    break;
            }
        }
    }

    private void onTouchDown(TouchEvent e){
    lastTouchedY=e.y;
    scroll=true;
    }
    private void onTouchUp(){
    scroll = false;
    }
    private void onTouchMove(TouchEvent e)
    {
     if(!scroll)
         return;

     float destY=e.y-lastTouchedY;
     lastTouchedY=e.y;

     for(int i=0;i<niveles.size();i++)
     {
        float newY=niveles.get(i).getY()+destY;
        niveles.get(i).setY(newY);
     }
    }

    //metodo que gestiono lo que se hace si se presiona sobre cualquier boton de la escena
    private void gestionBotones(TouchEvent e) {
        if (this.botonVolver.contains(e.x, e.y)) {
            Menu menu = new Menu(this.engine, this.mobile);
            this.engine.setState(menu);
        } else {
            for (int i = 0; i < niveles.size(); i++) {
                if (niveles.get(i).contains(e.x, e.y) && i <= this.completed - this.nivelesHastaAhora) {
                    GameLogic gameLogic = new GameLogic(this.engine, this.mobile, "Mundo/World" + this.mundo + "/Level" + (i + 1) + ".json");
                    this.engine.setState(gameLogic);
                }
            }
            if (this.previous) {
                if (this.anteriorMundo.contains(e.x, e.y)) {
                    Mundo mundoAnterior = new Mundo(this.engine, this.mobile, this.mundo - 1);
                    this.engine.setState(mundoAnterior);
                }
            }
            if (this.next) {
                if (siguienteMundo.contains(e.x, e.y)) {
                    Mundo mundoNext = new Mundo(this.engine, this.mobile, this.mundo + 1);
                    this.engine.setState(mundoNext);
                }
            }
        }
    }
    @Override
    public void setAudio(Audio audio) {
    }

    @Override
    public void setMobile(Mobile mobile) {
    }
}
