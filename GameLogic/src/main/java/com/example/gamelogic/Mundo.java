package com.example.gamelogic;

import com.example.engine.Audio;
import com.example.engine.Engine;
import com.example.engine.Graphics;
import com.example.engine.Mobile;
import com.example.engine.State;
import com.example.engine.TouchEvent;

import org.gradle.internal.impldep.com.google.api.client.json.Json;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;

public class Mundo implements State {


    private Text textoMundo;
    private Square fondoTexto;

    private Button siguienteMundo;
    private Button anteriorMundo;
    private Button botonVolver;
    private ArrayList<Button> niveles;
    String ColorCompleted;
    private Engine engine;

    private int mundo;

    //variable que inspecciona cuantos niveles hemos derrotado
    int completed;

    JSONObject botones;

    //constructora del estado que crea e inicializa los botones de la escena
    public Mundo(Engine engine,int mundo){
        this.engine=engine;
        this.mundo=mundo;
        JSONObject save=this.engine.readJsonFile2("save");
        this.completed= save.getInt("completed");

        botones=engine.readJsonFile("Mundo/style.json");
        JSONObject mundoInfo=engine.readJsonFile("Mundo/World"+this.mundo+"World"+this.mundo+".json");

        int fil =botones.getInt("nivelFilas");
        int col=botones.getInt("nivelColumnas");
        niveles=new ArrayList<Button>();
        this.ColorCompleted=botones.getString("colorCompleted");
        //inicialización de todos los botones de niveles
        for(int i=0;i<fil;i++)
        {
            for(int j=0;j<col;j++)
            {
                Button nivelMundo = new Button(botones.getJSONObject("NivelMundo"));
                Text nivel = new Text(botones.getJSONObject("TextoNivel"));
                nivel.setText("X");
                nivelMundo.setText(nivel);
                nivelMundo.setX(nivelMundo.getX()+nivelMundo.getWidth()*(float)j*1.5f);
                nivelMundo.setY(nivelMundo.getY()+nivelMundo.getHeight()*(float)i*1.5f);

                niveles.add(nivelMundo);

            }
        }
        for(int i=0;i<=Math.min(completed,this.niveles.size()-1);i++)
        {
            niveles.get(i).setColor(this.ColorCompleted);
            niveles.get(i).changeText(String.valueOf(i+1));

        }

        this.textoMundo = new Text(botones.getJSONObject("TextoMundo"));
        this.fondoTexto = new Square(300,50,300,70,true);
        this.fondoTexto.setColor(0xff009900);

        this.siguienteMundo = new Button(botones.getJSONObject("SiguienteMundo"));
        this.anteriorMundo = new Button(botones.getJSONObject("AnteriorMundo"));
        this.botonVolver = new Button(botones.getJSONObject("BotonVolver"));
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
        this.siguienteMundo.Render(gr);
        this.anteriorMundo.Render(gr);
        this.botonVolver.Render(gr);
    }

    @Override
    public void setGraphics(Graphics gr) {
        this.botonVolver.setImagen(new Image(botones.getJSONObject("ImagenVolver"),gr));
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
                    if(this.botonVolver.contains(e.x,e.y)){
                        Menu menu = new Menu(this.engine);
                        this.engine.setState(menu);
                    }
                    else{
                     for(int i=0;i< niveles.size();i++)
                     {
                         if(niveles.get(i).contains(e.x,e.y)&&i<=this.completed)
                         {
                             GameLogic gameLogic = new GameLogic(this.engine,"Mundo/World1/Level"+(i+1)+".json");
                             this.engine.setState(gameLogic);
                         }
                     }
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
    public void setAudio(Audio audio) {

    }

    @Override
    public void setMobile(Mobile mobile) {

    }
}
