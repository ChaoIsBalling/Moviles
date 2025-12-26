package com.example.towerdefense;

//de Android
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.SurfaceView;
import android.widget.FrameLayout;
import androidx.appcompat.app.AppCompatActivity;

//del motor de android y gameLogic
import com.example.androidengine.AndroidEngine;
import com.example.androidengine.AndroidMobile;
import com.example.gamelogic.Menu;
import com.example.gamelogic.State;
//de Java
import java.util.concurrent.TimeUnit;

/**
 * Actividad principal de la app
 */
public class MainActivity extends AppCompatActivity {
    private SurfaceView renderView; //Superficie donde dibujamos el juego
    private AndroidEngine engine; //Motor del juego para Android
    private AndroidMobile mobile;//Interfaz para conectar el motor con el MainActivity de la app
    private FrameLayout adContainerView; //Contenedor sobre el que ponemos el anuncio Banner

    //ID`s de unidad de anuncios de prueba, tanto para banner como para reward
    private static final String AD_UNIT_ID = "ca-app-pub-3940256099942544/9214589741";
    private static final String AD_REWARD_UNIT_ID = "ca-app-pub-3940256099942544/5224354917";
    private static final String TAG = "MainActivity";

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

        this.engine.setState(new Menu(engine,this.mobile)); //Estado inicial del juego
    }

    @Override
    protected void onResume(){
        super.onResume();
        this.engine.resume();
        requestPermissionNotifiactions();
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
            this.engine.incrementarParametro("gems",10); //incrementamos el numero de diamantes en el archivo de guarado del juego
        }
    }
}


