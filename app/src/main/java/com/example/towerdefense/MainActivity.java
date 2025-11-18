package com.example.towerdefense;

import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.FrameLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.androidengine.AndroidEngine;
import com.example.gamelogic.GameLogic;
import com.example.gamelogic.Menu;
import com.example.gamelogic.Secret;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class MainActivity extends AppCompatActivity {
    private SurfaceView renderView;
    private AndroidEngine engine;

    private AdView adView;
    private FrameLayout adContainerView;
    private static final String AD_UNIT_ID = "ca-app-pub-3940256099942544/9214589741";

    //private GameLogic gl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Obtenemos el surfaceView del xml
        this.renderView = findViewById(R.id.game);

        //Inicializamos motor
        this.engine = new AndroidEngine(this.renderView);
        this.engine.setState(new Menu(engine));

        //Container de anuncios
        adContainerView = findViewById(R.id.ad_view_container);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });


        // Crear un addView en el que meter el anuncio
        adView = new AdView(this);
        adView.setAdUnitId(AD_UNIT_ID);
        adView.setAdSize(AdSize.BANNER);

        //Lo añadimos al container
        adContainerView.removeAllViews();
        adContainerView.addView(adView);

        // cargamos el anuncio banner
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
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