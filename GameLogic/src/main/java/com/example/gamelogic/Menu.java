package com.example.gamelogic;

import com.example.engine.Engine;
import com.example.engine.Graphics;
import com.example.engine.Mobile;
import com.example.engine.State;
import com.example.engine.TouchEvent;
import com.example.engine.Audio;

import java.util.ArrayList;
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


    //Referencias al Audio Manager, al motor y a Graphics y a Mobile
    private Audio audio;
    Engine engine;
    Graphics gr;
    Mobile mobile;

    JSONObject botones;

    /**
     * Constructora del menú
     */
    public Menu(Engine engine, Mobile mobile){
        this.engine = engine;
        this.mobile = mobile;
        botones=engine.readJsonFile("Menu/style.json");
        this.botonInicial = new Button(botones.getJSONObject("BotonInicial"));
        this.botonInicial.setText(new Text(botones.getJSONObject("TextoBoton")));

        this.textoInicial = new Text(botones.getJSONObject("TextoInicial"));

        this.botonAventura = new Button(botones.getJSONObject("BotonAventura"));
        this.botonAventura.setText( new Text(botones.getJSONObject("TextoAventura")));

        this.botonTienda = new Button(botones.getJSONObject("BotonTienda"));
        this.botonTienda.setText( new Text(botones.getJSONObject("TextoTienda")));

        //cambiamos el numero de gemas dependiendo de cuanto dinero hemos ganado
        this.textoDiamantes = new Text(botones.getJSONObject("TextoDiamantes"));

        if(this.engine.checkFileExists("save"))
        {
            JSONObject obj=this.engine.readJsonFile2("save");
            String hash = this.engine.createHash(obj.toString());
            if(this.engine.checkHash(hash)){
                this.textoDiamantes.setText(String.valueOf(obj.getInt("gems")));
            }
            else{
                //resetea el progreso
                this.newGame();
            }
        }
        else {
            //Si no tenemos creamos un nuevo objeto JSON
           this.newGame();
        }

        this.mobile.setVisibleAdBanner(true);
    }
    //Creacion de una nueva partida
    void newGame()
    {
            JSONObject obj=new JSONObject();
            obj.put("gems",0);
            obj.put("completed",0);
            obj.put("rayo",false);
            obj.put("fuego",false);
            obj.put("hielo",false);
            obj.put("mini",false);
            this.engine.writeFile("hash",this.engine.createHash(obj.toString()));
            this.engine.writeFile("save",obj.toString());
    }
    @Override
    public void update(double deltaTime) {
    }

    /**
     * Renderiza la UI
     * @param gr Graphics del motor
     */
    @Override
    public void render(Graphics gr) {
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
    public void setGraphics(Graphics gr) {
        this.gr=gr;
        this.imagenDiamante = new Image(botones.getJSONObject("ImagenDiamante"),gr);
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
                            Dificultad dificultad = new Dificultad(this.engine,this.mobile);
                            this.engine.setState(dificultad);
                        }
                        else if(this.botonAventura.contains(e.x,e.y)){
                            Mundo mundo = new Mundo(this.engine,this.mobile,1);
                            this.engine.setState(mundo);
                        }
                        else if(this.botonTienda.contains(e.x,e.y)){
                            Tienda tienda = new Tienda(this.engine,this.mobile);
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
    public void setAudio(Audio audio) {
    this.audio=audio;
    }

    /**
     * Inicializa Mobile
     * @param mobile Interfaz Mobile
     */
    @Override
    public void setMobile(Mobile mobile) {
        this.mobile = mobile;
    }
}
