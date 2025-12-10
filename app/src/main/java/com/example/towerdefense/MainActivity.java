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

//Importaciones de gms

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private SurfaceView renderView;
    private AndroidEngine engine;

    private AndroidMobile mobile;

    private FrameLayout adContainerView;

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

        //Inicializamos motor e interfaz de mobile que accede a los metodos de main activity
        this.mobile = new AndroidMobile(this,this.renderView,this.adContainerView);
        this.engine = new AndroidEngine(this.renderView,this.mobile);
        this.engine.setNotificationIcon(R.drawable.ic_tower_defense_noti);
        //Comprobamos si el jugador ha vuelto a entrar al juego por la notificacion recompensada
        checkRewardNotifiactionIntent();

        this.engine.setState(new Menu(engine,this.mobile));


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
        this.engine.programNotificacion(
                5,                    // Tiempo
                TimeUnit.SECONDS,          // Después x unidad de tiempo
                R.drawable.ic_tower_defense_noti,    // Icono
                "¡Te echamos de menos :(!",
                "Vuelve ahora y gana 3 diamantes gratis."
        );
        this.engine.pause();
    }

    private void requestPermissionNotifiactions(){
        //La aplicacion solicita los permisos de notifiacion al usuario si accede al
        //juego por primera vez
        if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(
                    new String[]{Manifest.permission.POST_NOTIFICATIONS},
                    101
            );
        }
    }

    private void checkRewardNotifiactionIntent(){
        //Recogemos el intent
        Intent intent = getIntent();

        //Si es por hacer click a la notifiación por bonificación, regalamos diamantes al jugador
        if(intent != null && intent.getBooleanExtra("REWARD_NOTIFICATION",false)){
            this.engine.modificarParametro("gems",3);
            System.out.println("Diamantes recibidos");
        }
    }
}


