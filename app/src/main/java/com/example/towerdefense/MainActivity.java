package com.example.towerdefense;

//de Android
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.SurfaceView;
import android.widget.FrameLayout;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;

//del motor de android y gameLogic
import com.example.androidengine.AndroidEngine;
import com.example.androidengine.AndroidMobile;
import com.example.gamelogic.states.Menu;
//de Java
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

/**
 * Actividad principal de la app
 */
public class MainActivity extends AppCompatActivity {
    private SurfaceView renderView; //Superficie donde dibujamos el juego
    private AndroidEngine engine; //Motor del juego para Android
    private AndroidMobile mobile;//Interfaz para conectar el motor con el MainActivity de la app
    private FrameLayout adContainerView; //Contenedor sobre el que ponemos el anuncio Banner
    private JSONObject save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Vistas principales
        //Obtenemos el surfaceView del xml donde renderizamos el juego
        this.renderView = findViewById(R.id.game);
        //Container de anuncios
        this.adContainerView = findViewById(R.id.ad_view_container);

        //Inicializamos motor e interfaz de mobile que accede a los métodos de main activity
        this.mobile = new AndroidMobile(this,this.renderView,this.adContainerView);
        this.engine = new AndroidEngine(this.renderView,this.mobile);
        this.engine.setNotificationIcon(R.drawable.ic_tower_defense_noti); //icono de notificación

        checkRewardNotifiactionIntent(); //Comprobamos si el jugador ha vuelto a entrar al juego por la notificacion recompensada
        if(this.engine.checkFileExists("save"))
        {
            save=this.engine.readInternalJsonFile("save");
            String hash = this.engine.createHash(save.toString());
            if(!this.engine.checkHash(hash)){
                save=this.newGame();
            }
        }
        else {
            //Si no tenemos creamos un nuevo objeto JSON
            save=this.newGame();
        }
        this.engine.setState(new Menu(engine,this.mobile,save)); //Estado inicial del juego
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.save=this.engine.getState().getSave();
        this.engine.writeFile("hash",this.engine.createHash(this.save.toString()));
        this.engine.writeFile("save",this.save.toString());
    }
    /**
     * Metodo para crear un nuevo archivo de guardado en caso de no tener archivo de guardado
     * o que el archivo no sea consistente con el hash
     */
    JSONObject newGame()
    {
        JSONObject obj=new JSONObject();
        try {
            obj.put("gems",0);
            obj.put("completed",0);

            //Array que contiene las torres desbloqueadas
            JSONArray towers = new JSONArray();
            towers.put("RAYO");
            towers.put("HIELO");
            towers.put("FUEGO");
            obj.put("towers_unlocked", towers);


            //Object que contiene las skins equipadas
            JSONObject skins_eq = new JSONObject();
            skins_eq.put("RAYO", "Figura");
            skins_eq.put("HIELO", "Figura");
            skins_eq.put("FUEGO", "Figura");
            obj.put("skins_equipped", skins_eq);

            //Object con las skins desbloqueadas para cada torre
            JSONObject skins_des = new JSONObject();
            //Rayo
            JSONArray skinsRayo = new JSONArray();
            skinsRayo.put("Figura");
            skins_des.put("RAYO", skinsRayo);

            //Hielo
            JSONArray skinsHielo = new JSONArray();
            skinsHielo.put("Figura");
            skins_des.put("HIELO", skinsHielo);

            //Fuego
            JSONArray skinsFuego = new JSONArray();
            skinsFuego.put("Figura");
            skins_des.put("FUEGO", skinsFuego);

            obj.put("skins_available", skins_des);

            obj.put("background", "#FFFFFFFF");

            /*obj.put("rayo",false);
            obj.put("fuego",false);
            obj.put("hielo",false);
            obj.put("mini",false);

            JSONObject itemsComprados= new JSONObject();
            obj.put("itemsComprados",itemsComprados);

            JSONObject torres=new JSONObject();
            JSONObject torre = new JSONObject();
            torre.put("id", "BUT_RAYO");
            torre.put("skin", "Figura");
            torre.put("active",true);
            torres.put("RAYO",torre);
            torre = new JSONObject();
            torre.put("id", "BUT_HIELO");
            torre.put("skin", "Figura");
            torre.put("active",true);
            torres.put("HIELO",torre);
            torre = new JSONObject();
            torre.put("id", "BUT_FUEGO");
            torre.put("skin", "Figura");
            torre.put("active",true);
            torres.put("FUEGO",torre);
            torre = new JSONObject();
            torre.put("id", "BUT_MINI");
            torre.put("skin", "Figura");
            torre.put("active",false);
            torres.put("MINI",torre);
            obj.put("torres",torres);

            obj.put("skinRayo","Figura");
            obj.put("skinFuego","Figura");
            obj.put("skinHielo","Figura");
            obj.put("rojo",false);
            obj.put("azul",false);
            obj.put("fondo","#FFFFFFFF");*/
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        this.engine.writeFile("hash",this.engine.createHash(obj.toString()));
        this.engine.writeFile("save",obj.toString());
        return obj;
    }
    @Override
    protected void onResume(){
        super.onResume();
        this.engine.resume();
        requestPermissionNotifiactions();
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        this.engine.writeFile("hash",this.engine.createHash(this.save.toString()));
        this.engine.writeFile("save",this.save.toString());
    }

    @Override
    protected void onPause(){
        super.onPause();
        //Programamos notifiacion push que se muestra despues de un tiempo
        this.engine.programNotificacion(
                1,                    // Tiempo
                TimeUnit.HOURS,          // Después x unidad de tiempo
                R.drawable.ic_tower_defense_noti,    // Icono
                "¡Te echamos de menos! :(",
                "Vuelve ahora y gana 10 diamantes gratis."
        );
        this.save=this.engine.getState().getSave();
        this.engine.writeFile("hash",this.engine.createHash(this.save.toString()));
        this.engine.writeFile("save",this.save.toString());
        this.engine.pause();
    }

    /**
     *  La aplicacion solicita los permisos de notifiacion al usuario si accede al
     *  juego por primera vez. Dar allow en el emulador para activar las notifiaciones
     */
    private void requestPermissionNotifiactions(){
        if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                    new String[]{Manifest.permission.POST_NOTIFICATIONS},
                    101 );
        }
    }

    /**
     * Si el jugador entra por una notifiación push, se le da una recompensa
     */
    private void checkRewardNotifiactionIntent(){
        //Recogemos el intent
        Intent intent = getIntent();

        //Si es por hacer click a la notifiación por bonificación, regalamos diamantes al jugador
        if(intent != null && intent.getBooleanExtra("REWARD_NOTIFICATION",false)){
            int numDiamantes = 0;
            //incrementamos el numero de diamantes en el archivo de guardado del juego
            try {
                numDiamantes=this.save.getInt("gems");
                this.save.put("gems",numDiamantes+10);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

        }
    }
}


