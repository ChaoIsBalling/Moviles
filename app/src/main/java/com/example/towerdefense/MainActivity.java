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

    //anuncio banner y su contenedor
    private AdView adView;
    private FrameLayout adContainerView;

    //Anuncio recompensado
    private RewardedAd rewardedAd;

    private Button butn;


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

        //Añadimos banner al container
        this.adContainerView.removeAllViews();
        this.adContainerView.addView(adView);

        //Inicializamos motor e interfaz de mobile que accede a los metodos de main activity
        this.mobile = new AndroidMobile(this,this.renderView,this.adView);
        this.engine = new AndroidEngine(this.renderView,this.mobile);
        this.engine.setState(new Menu(engine));



        /*MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                //Cargamos anuncio recompensado
                loadRewardedAd();
                //Cargamos anuncio Banner
                loadBannerAd();
            }
        });

        //Metodo auxiliar para probar los anuncios
        butn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRewardedVideo();
            }
        });*/
    }

    /**
     * Metodo para cargar un anuncio Banner
     */
    private void loadBannerAd(){
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

    /**
     * Metodo para cargar el anuncio recompensado con Admob
     */
    private void loadRewardedAd() {
            RewardedAd.load(
                    this,
                    AD_REWARD_UNIT_ID,
                    new AdRequest.Builder().build(),
                    new RewardedAdLoadCallback() {
                        @Override
                        public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                            Log.d(TAG, "Ad was loaded.");
                            MainActivity.this.rewardedAd = rewardedAd;
                            MainActivity.this.rewardedAd.setFullScreenContentCallback(
                                    new FullScreenContentCallback() {
                                        @Override
                                        public void onAdDismissedFullScreenContent() {
                                            // Called when fullscreen content is dismissed.
                                            Log.d(TAG, "Ad was dismissed.");
                                            // Don't forget to set the ad reference to null so you
                                            // don't show the ad a second time.
                                            MainActivity.this.rewardedAd = null;
                                            Toast.makeText(MainActivity.this, "onAdDismissedFullScreenContent", Toast.LENGTH_SHORT)
                                                    .show();
                                        }

                                        @Override
                                        public void onAdFailedToShowFullScreenContent(AdError adError) {
                                            // Called when fullscreen content failed to show.
                                            Log.d(TAG, "Ad failed to show.");
                                            // Don't forget to set the ad reference to null so you
                                            // don't show the ad a second time.
                                            MainActivity.this.rewardedAd = null;
                                            Toast.makeText(
                                                    MainActivity.this, "onAdFailedToShowFullScreenContent", Toast.LENGTH_SHORT)
                                                    .show();
                                        }

                                        @Override
                                        public void onAdShowedFullScreenContent() {
                                            // Called when fullscreen content is shown.
                                            Log.d(TAG, "Ad showed fullscreen content.");

                                            Toast.makeText(MainActivity.this, "onAdShowedFullScreenContent", Toast.LENGTH_SHORT)
                                                    .show();

                                        }

                                        @Override
                                        public void onAdImpression() {
                                            // Called when an impression is recorded for an ad.
                                            Log.d(TAG, "Ad recorded an impression.");
                                        }

                                        @Override
                                        public void onAdClicked() {
                                            // Called when an ad is clicked.
                                            Log.d(TAG, "Ad was clicked.");
                                        }
                                    });
                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            Log.d(TAG, loadAdError.getMessage());
                            rewardedAd = null;
                        }
                    });
    }

    /**
     * Metodo que muestra un anuncio recompensado una vez ya se ha cargado
     */
    private void showRewardedVideo() {
        if ( this.rewardedAd == null) {
            Log.d("AdRecompensado", "The rewarded ad wasn't ready yet.");
            return;
        }
        //showVideoButton.setVisibility(View.INVISIBLE);

        rewardedAd.show(
                MainActivity.this, new OnUserEarnedRewardListener() {
                    /**
                     * En este metodo se recompensa al jugador si ha visto el anuncio
                     * @param rewardItem
                     */
                    @Override
                    public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                        Log.d(TAG, "User earned the reward.");
                        // Handle the reward.
                        // [START_EXCLUDE silent]
                        //addCoins(coinCount);
                        // [END_EXCLUDE]
                    }
                });
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

    /**
     * Este metodo permite que cualquier otra clase que no sea MainActivity
     * llame al anuncio recompensado
     */
    public void requestRewardedAd(){
        runOnUiThread(() -> showRewardedVideo());
    }
}


