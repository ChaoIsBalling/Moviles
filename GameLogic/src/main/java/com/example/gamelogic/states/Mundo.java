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
    private Text textoMundo;
    private Square fondoTexto;

    private Button siguienteMundo;
    private Button anteriorMundo;
    private Button botonVolver;
    private ArrayList<Button> niveles;
    private AndroidEngine engine;

    private AndroidMobile mobile;

    private AndroidGraphics gr;
    //booleanos que determinan si el mundo actual tiene un mundo anterior o posterior
    private boolean next;
    private boolean previous;

    private int numNiveles;

    //en que mundo estamos ahora
    private int mundo;

    //El archivo de guardado del juego
    private JSONObject save;

    //cuantos niveles han habido hasta ahora de cada mundo
    private int nivelesHastaAhora = 0;

    //variable que inspecciona cuantos niveles hemos derrotado
    int completed;
    //JSON que almacena los prefabs necesarios para este estado
    JSONObject prefabs;

    //JSON en el que se van a almacenar todos los estilos para botones y textos
    JSONObject style;

    //Ultima cordenada Y tocada
    float lastTouchedY;

    //bool que nos dice si estamos haciendo scroll de pantalla
    boolean scroll;

    //La posición minima que puede tener el promer boton de la lista en la posicion y
    float minY;
    //La posición minima que puede tener el promer boton de la lista en la posicion y
    float maxY;

    //Altura de la ventana de scroll de niveles
    float tamScroll = 400;

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

        /*try {
            this.completed= this.save.getInt("completed");

            botones=engine.readJsonFile("Mundo/style.json");
            JSONObject mundoInfo=engine.readJsonFile("Mundo/World"+this.mundo+"/World"+this.mundo+".json");

            //esto calcula cuantos niveles han habido hasta este mundo
            for(int i=1;i<this.mundo;i++)
            {
                JSONObject obj=engine.readJsonFile("Mundo/World"+i+"/World"+i+".json");
                this.nivelesHastaAhora+=obj.getInt("niveles");
            }

            //Numero de niveles del mundo actual
            this.numNiveles=mundoInfo.getInt("niveles");

            //Booleanos que indican si hay nivel antes o despues del mundo actual
            this.next=mundoInfo.getBoolean("next");
            this.previous=mundoInfo.getBoolean("previous");

            //Array donde guardamos los botones de niveles
            niveles=new ArrayList<Button>();

            //Color para colorear los niveles completados y no completados
            String colorCompleted = mundoInfo.getString("colorCompleted");
            String colorLocked = mundoInfo.getString("colorLocked");

            //Los botones deben estar entre estas dos posiciones
            //Posicion más alta permitida para el scroll alrededor de Y
            this.minY=(float)botones.getJSONObject("NivelMundo").getInt("y");
            //Posicion más baja permitaida para el scroll
            this.maxY=this.tamScroll - botones.getJSONObject("NivelMundo").getInt("h");

            //inicialización de todos los botones de niveles
            for(int i=0;i<this.numNiveles;i++)
            {
                //Seteamos a todos con una x y un color gris
                Button nivelMundo = new Button(botones.getJSONObject("NivelMundo"));
                Text nivel = new Text(botones.getJSONObject("TextoNivel"));
                nivel.setText("X");
                nivelMundo.setText(nivel);
                nivelMundo.setY(nivelMundo.getY()+nivelMundo.getHeight()*(float)i*1.5f);
                nivelMundo.setColor("#FFABABAC");
                niveles.add(nivelMundo);
            }

            //determina que niveles de ESTE mundo se han completado
            int nivelesCompletadosEnMundo = this.completed-this.nivelesHastaAhora;
            int rangoNivelesPasados = Math.min(niveles.size()-1, nivelesCompletadosEnMundo);

            //Ahora determinamos cuales son los niveles completados
            for(int i=0; i <= rangoNivelesPasados; i++)
            {
                niveles.get(i).setColor(colorCompleted);
                niveles.get(i).changeText(String.valueOf(i+1));
            }

            //Este es el nivel que tengo sin pasar ahora de ESTE mundo, primero comprobamos que exista
            if(nivelesCompletadosEnMundo >= 0 &&
                    nivelesCompletadosEnMundo < niveles.size())
                //Coloreamos con color de nivel bloqueado
                niveles.get(nivelesCompletadosEnMundo).setColor(colorLocked);

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


        } catch (JSONException e) {
            throw new RuntimeException(e);
        }*/
    }

    @Override
    public void update(double deltatime) {

    }
    //renderización de todos los botones y texto
    @Override
    public void render(AndroidGraphics gr) {

        gr.EmpezarLimiteDibujado(0,100,600,400);
        for(int i=0;i<niveles.size();i++)
            niveles.get(i).Render(gr);
        gr.TerminarLimiteDibujado();
        this.fondoTexto.Render(gr);

        this.ui.render(gr);
        /*this.textoMundo.Render(gr);
        if(this.next)
            this.siguienteMundo.Render(gr);
        if(this.previous)
            this.anteriorMundo.Render(gr);
        this.botonVolver.Render(gr);*/
    }

    @Override
    public void setGraphics(AndroidGraphics gr) {
        this.gr = gr;

        this.style = engine.readJsonFile("Mundo/style2.json");
        this.prefabs = engine.readJsonFile("Mundo/prefabs.json");

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

    private void calcularProgreso(){
        try {
            this.completed= this.save.getInt("completed");

            //esto calcula cuantos niveles han habido hasta este mundo
            for(int i = 1; i < this.mundo; i++)
            {
                JSONObject obj=engine.readJsonFile("Mundo/World"+i+"/World"+i+".json");
                this.nivelesHastaAhora+=obj.getInt("niveles");
            }

            //determina que niveles de ESTE mundo se han completado
            this.nivelesCompletadosEnMundo = this.completed-this.nivelesHastaAhora;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void configurarLimitesScroll() {
        //Los botones deben estar entre estas dos posiciones
        //Posicion más alta permitida para el scroll alrededor de Y
        try {
            this.minY = (float) prefabs.getJSONObject("NivelMundo").getInt("y");
            //Posicion más baja permitaida para el scroll
            this.maxY = this.tamScroll - (float) prefabs.getJSONObject("NivelMundo").getInt("h");
        }
        catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
    private void iniciarNivel(int index){
        if (index <= this.completed - this.nivelesHastaAhora) {
            //(i == this.completed - this.nivelesHastaAhora) -> Ultimo nivel sin completar
            //(i <                                         ) -> Nivel completado
            boolean isLevelCompleted = (index == this.completed - this.nivelesHastaAhora) ? false: true;
            //System.out.println(isLevelCompleted);
            GameLogic gameLogic = new GameLogic(this.engine, this.mobile, "Mundo/World" +
                    this.mundo + "/Level" + (index + 1) + ".json",isLevelCompleted, index+1,this.mundo,this.save);
            this.engine.setState(gameLogic);
        }

    }

    private void crearConfigurarNiveles(){
        //inicialización de todos los botones de niveles
        for(int i = 0;i < this.numNiveles; i++) {
            //Seteamos a todos con una x y un color gris
            Button nivelMundo = null;
            try {
                nivelMundo = new Button(prefabs.getJSONObject("NivelMundo"), this.gr);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            //Ya se hace en el json
            //Text nivel = new Text(prefabs.getJSONObject("TextoNivel"));
            //nivel.setText("X");
            //nivelMundo.setText(nivel);
            nivelMundo.setY(nivelMundo.getY() + nivelMundo.getHeight() * (float) i * 1.5f);
            nivelMundo.setColor("#FFABABAC");
            //niveles.add(nivelMundo);
            //this.ui.addButtonUI("WORLD_" + this.mundo + "_" + (i + 1), nivelMundo);
            int ind = i;
            //this.ui.getButtonUI("WORLD_" + this.mundo + "_" + (i + 1)).setOnClickListener(() -> iniciarNivel(ind + 1));
            //nivelMundo.setOnClickListener(() -> iniciarNivel(ind + 1));
            this.niveles.add(nivelMundo);
        }

        int rangoNivelesPasados = Math.min(niveles.size() - 1, nivelesCompletadosEnMundo);

        //Ahora determinamos cuales son los niveles completados
        for(int i=0; i <= rangoNivelesPasados; i++)
        {
            //this.ui.getButtonUI("WORLD_" + this.mundo +"_" + (i+1)).setColor(colorCompleted);
            //this.ui.getButtonUI("WORLD_" + this.mundo +"_" + (i+1)).changeText(String.valueOf(i+1));
            niveles.get(i).setColor(colorCompleted);
            niveles.get(i).changeText(String.valueOf(i+1));
            int ind = i;
            niveles.get(i).setOnClickListener(() -> iniciarNivel(ind));
        }

        //Este es el nivel que tengo sin pasar ahora de ESTE mundo, primero comprobamos que exista
        if(nivelesCompletadosEnMundo >= 0 &&
                nivelesCompletadosEnMundo < niveles.size()){
            //Coloreamos con color de nivel bloqueado
            //this.ui.getButtonUI("WORLD_" + this.mundo +"_" + nivelesCompletadosEnMundo).setColor(colorLocked);
            niveles.get(nivelesCompletadosEnMundo).setColor(colorLocked);
            niveles.get(nivelesCompletadosEnMundo).setOnClickListener(() -> iniciarNivel(nivelesCompletadosEnMundo));
        }

    }

    private void irAMundo(int m){
        Mundo mundoDestino = new Mundo(this.engine, this.mobile, m,this.save);
        this.engine.setState(mundoDestino);
    }

    private void crearUIElems(){
        //this.textoMundo = new Text(prefabs.getJSONObject("TextoMundo"));
        //this.textoMundo.setText("Mundo "+this.mundo);
        this.fondoTexto = new Square(300,50,300,70,true);
        this.fondoTexto.setColor("#ffDAB628");

        this.ui.getTextUI("TEXT_MUNDO").setText("Mundo " + this.mundo);

        this.ui.getButtonUI("BUT_SIGUIENTE_MUNDO").setOnClickListener(() -> {
            irAMundo(this.mundo + 1);
        });
        this.ui.getButtonUI("BUT_ANTERIOR_MUNDO").setOnClickListener(() -> {
            irAMundo(this.mundo - 1);
        });

        this.ui.getButtonUI("BUT_VOLVER").setOnClickListener( () -> {
            Menu menu = new Menu(this.engine, this.mobile,this.save);
            this.engine.setState(menu);
        });

        //Se desactivan los botones por defecto
        this.ui.buttonEnabled("BUT_SIGUIENTE_MUNDO", false);
        this.ui.buttonEnabled("BUT_ANTERIOR_MUNDO", false);
        //en caso de que hava un siguiente o anterior mundo inicializamos los botones correspondientes
        if(this.next) {
            this.ui.buttonEnabled("BUT_SIGUIENTE_MUNDO", true);
            //this.ui.getButtonUI("BUT_ANTERIOR_MUNDO").setEnabled();
            //this.siguienteMundo = new Button(prefabs.getJSONObject("SiguienteMundo"));
        }
        if(this.previous) {
            this.ui.buttonEnabled("BUT_ANTERIOR_MUNDO", true);
            //this.ui.getButtonUI("BUT_ANTERIOR_MUNDO").setOnClickListener();
            //this.anteriorMundo = new Button(prefabs.getJSONObject("AnteriorMundo"));
        }
        //this.botonVolver = new Button(prefabs.getJSONObject("BotonVolver"));
    }

    private void inicializarUI(){
        //Array donde guardamos los botones de niveles
        niveles=new ArrayList<Button>();
        this.ui = new UIManager(this.style,this.engine,this.gr);

        cargarDatosMundo();
        calcularProgreso();
        configurarLimitesScroll();
        crearConfigurarNiveles();
        crearUIElems();
    }

    //si el evento es de tipo TouchDown guardamos el ultimo valor de la Y y ponemo a true el scroll
    private void onTouchDown(TouchEvent e){
        lastTouchedY=e.y;
        scroll=true;
    }
    //si es de tipo touch up el scroll se pone a false
    private void onTouchUp(){
        scroll = false;
    }
    //si es de tipo touchmove manejamos el scroll del juego
    private void onTouchMove(TouchEvent e)
    {
        if(!scroll)
            return;

        //Desplazamiento entre el punto de origen del toque
        //y la ultima y
        float destY=e.y-lastTouchedY;
        //Actualizamos la última coordenada y tocada
        lastTouchedY=e.y;
        boolean canScroll=true;
        //checkeamos si los extremeos de los objetos scrolleables (el mas alto y el mas bajo)
        //estan entre el minimo y maximo Y que hemos definido
        //Si es asi, ya no podemos scrollear mas
        if((niveles.get(0).getY() > minY && destY>0)
                ||(niveles.get(niveles.size()-1).getY() < maxY && destY<0))
            canScroll=false;

        //Renderizo los botones en la nueva posicion y sumandole el desplazamiento
        if(canScroll) {
            for (int i = 0; i < niveles.size(); i++) {
                float newY = niveles.get(i).getY() + destY;
                niveles.get(i).setY(newY);
            }
        }
    }

    //metodo que gestiono lo que se hace si se presiona sobre cualquier boton de la escena
    private void gestionBotones(TouchEvent e) {
        for(Button b : niveles){
            b.handleInput(e);
        }

        /*if (this.botonVolver.contains(e.x, e.y)) {
            Menu menu = new Menu(this.engine, this.mobile,this.save);
            this.engine.setState(menu);
        } else {
            for (int i = 0; i < niveles.size(); i++) {
                if (niveles.get(i).contains(e.x, e.y) && i <= this.completed - this.nivelesHastaAhora) {
                    //(i == this.completed - this.nivelesHastaAhora) -> Ultimo nivel sin completar
                    //(i <                                         ) -> Nivel completado
                    boolean isLevelCompleted = (i == this.completed - this.nivelesHastaAhora) ? false: true;
                    //System.out.println(isLevelCompleted);
                    GameLogic gameLogic = new GameLogic(this.engine, this.mobile, "Mundo/World" + this.mundo + "/Level" + (i + 1) + ".json",isLevelCompleted, i+1,this.mundo,this.save);
                    this.engine.setState(gameLogic);
                }
            }
            if (this.previous) {
                if (this.anteriorMundo.contains(e.x, e.y)) {
                    Mundo mundoAnterior = new Mundo(this.engine, this.mobile, this.mundo - 1,this.save);
                    this.engine.setState(mundoAnterior);
                }
            }
            if (this.next) {
                if (siguienteMundo.contains(e.x, e.y)) {
                    Mundo mundoNext = new Mundo(this.engine, this.mobile, this.mundo + 1,this.save);
                    this.engine.setState(mundoNext);
                }
            }
        }*/
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
