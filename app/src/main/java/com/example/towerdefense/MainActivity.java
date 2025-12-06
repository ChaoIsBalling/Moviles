package com.example.towerdefense;

//de Android
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

//del motor de android y gameLogic
import com.example.androidengine.AndroidEngine;
import com.example.androidengine.AndroidMobile;
import com.example.gamelogic.GameLogic;
import com.example.gamelogic.GameOver;
import com.example.gamelogic.Menu;

//Importaciones de gms
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

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
        this.engine.setState(new Menu(engine));




        //Metodo auxiliar para probar los anuncios
        /*butn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRewardedVideo();
            }
        });*/
    }

    @Override
    protected void onResume(){
        super.onResume();
        this.engine.resume();
    }

    @Override
    protected void onPause(){
        super.onPause();
        this.engine.pause();
    }
}


