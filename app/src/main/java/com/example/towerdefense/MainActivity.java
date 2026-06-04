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
        if(!this.engine.checkFileExists("save"))
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
            obj.put("gems",10000);
            obj.put("completed",0);


            //Informacion relevante para la tienda
            JSONObject shop = new JSONObject();


            //Objeto en el que se almacenan las cosas ya compradas
            JSONObject purchases = new JSONObject();

            //Skins que tengo
            JSONArray skinsPur = new JSONArray();
            purchases.put("skins", skinsPur);

            //Torres que tengo
            JSONArray towersPur = new JSONArray();
            //Al principio tengo las 3 torres principales
            //0 -> Rayo, 1 -> Hielo y 2 -> Fuego
            towersPur.put(0);
            towersPur.put(1);
            towersPur.put(2);
            purchases.put("towers", towersPur);


            //Backgrounds que tengo
            JSONArray bgPur = new JSONArray();
            bgPur.put(0);
            purchases.put("bg", bgPur);

            shop.put( "purchases", purchases);

            //Objetos desbloqueados en la tienda
            JSONObject unlocks = new JSONObject();

            JSONArray skinsUnl = new JSONArray();
            skinsUnl.put(0);
            skinsUnl.put(1);
            skinsUnl.put(2);
            skinsUnl.put(3);
            skinsUnl.put(4);
            skinsUnl.put(5);
            unlocks.put("skins", skinsUnl);

            //Torres desbloqueadas
            JSONArray towersUnl = new JSONArray();
            towersUnl.put(0);
            towersUnl.put(1);
            towersUnl.put(2);
            unlocks.put("towers", towersUnl);

            shop.put("unlocks", unlocks);

            //Objetos equipados
            JSONObject equips = new JSONObject();

            JSONArray skinsEq = new JSONArray();
            // tengo equipadas las skins base (0, 1 y 2) para sus torres
            skinsEq.put(0);
            skinsEq.put(1);
            skinsEq.put(2);
            equips.put("skins", skinsEq);

            //equips.put("buttons", -1);
            equips.put("bg", "#FFFFFFFF");

            JSONArray towersEq = new JSONArray();
            // Las torres activas en su inventario de partida
            towersEq.put(0);
            towersEq.put(1);
            towersEq.put(2);
            equips.put("towers", towersEq);

            shop.put("equips", equips);

            obj.put("shop", shop);



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


