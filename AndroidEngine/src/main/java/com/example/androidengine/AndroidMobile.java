package com.example.androidengine;

import android.app.Activity;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
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

/**
 * Clase que implementa los metodos para la gestión de anuncios
 * Implementa la interfaz Mobile del motor exclusivamente para Android
 */
public class AndroidMobile {
    private AdView adView;   //anuncio banner y su contenedor en el xml del MainActivity
    private FrameLayout adContainer; //Contenedor del anuncio
    private Activity activity;  //El main Activity
    private RewardedAd rewardedAd; //Anuncio recompensado
    private SurfaceView surfaceView;

    //ID`s de unidad de anuncios de prueba, tanto para banner como para reward
    private static final String AD_UNIT_ID = "ca-app-pub-3940256099942544/9214589741";
    private static final String AD_REWARD_UNIT_ID = "ca-app-pub-3940256099942544/5224354917";
    private static final String TAG = "MainActivity";
    boolean isBannerVisible = true; //Indica si el banner debe verse o no

    /**
     * Constructor del gestor de Android Mobile.
     * @param activity actividad principal de la app
     * @param adContainer contenedor del anuncio
     */
    public AndroidMobile(Activity activity, SurfaceView surfaceView, FrameLayout adContainer){
        this.activity = activity;
        this.adContainer = adContainer;
        this.surfaceView = surfaceView;

        //Inicializa los anuncios de adMob
        MobileAds.initialize(activity, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });

        loadRewardedAd(); //Cargamos anuncio recompensado
        loadBannerAd(); //Cargamos anuncio banner
    }

    /**
     * Metodo para mostrar un anuncio Banner
     */
    private void loadBannerAd(){
        // Crear un addView en el que meter el anuncio
        adView = new AdView(activity);
        adView.setAdUnitId(AD_UNIT_ID);
        adView.setAdSize(AdSize.BANNER);

        //Lo añadimos al container
        adContainer.removeAllViews();
        adContainer.addView(adView);

        // cargamos el anuncio banner
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    /**
     * Metodo para cargar el anuncio recompensado con Admob
     */
    public void loadRewardedAd() {
        AdRequest adRequest =  new AdRequest.Builder().build();
        RewardedAd.load(
                activity,
                AD_REWARD_UNIT_ID,
                adRequest,
                new RewardedAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rAd) {
                        Log.d(TAG, "Ad was loaded.");
                        rewardedAd = rAd;
                        rewardedAd.setFullScreenContentCallback(
                                new FullScreenContentCallback() {
                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        // Called when fullscreen content is dismissed.
                                        Log.d(TAG, "Ad was dismissed.");
                                        loadRewardedAd(); //volvemos a recargar anuncio, una vez ha sido consumido el anterior
                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                                        // Called when fullscreen content failed to show.
                                        Log.d(TAG, "Ad failed to show.");
                                        // Don't forget to set the ad reference to null so you
                                        // don't show the ad a second time.
                                        rewardedAd = null;
                                        //Texto en la pantalla
                                        //Toast.makeText(activity, "onAdFailedToShowFullScreenContent", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onAdShowedFullScreenContent() {
                                        // Called when fullscreen content is shown.
                                        Log.d(TAG, "Ad showed fullscreen content.");
                                        //Texto en la pantalla
                                        //Toast.makeText(activity, "onAdShowedFullScreenContent", Toast.LENGTH_SHORT).show();

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
    public void showRewardedAd(RewardCallback callback) {
        //Llamar al hilo principal para ver el anuncio
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (rewardedAd != null) {
                    rewardedAd.show(activity, new OnUserEarnedRewardListener() {
                        @Override
                        public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                            // Handle the reward.
                            Log.d(TAG, "The user earned the reward.");
                            callback.onReward();
                        }
                    });
                } else {
                    System.out.println("The rewarded ad wasn't ready yet.");
                    Log.d(TAG, "The rewarded ad wasn't ready yet.");
                }
            }
        });
    }
    public void setVisibleAdBanner(boolean cond){
        //Si no cambia el booleano de condicion, no hacemos nada
        if(cond == isBannerVisible)
            return;

        isBannerVisible = cond; //actualizamos booleano

        //las views de nuestra UI solo se pueden modificar en el hilo principal
        activity.runOnUiThread(() -> {
            adContainer.setVisibility(isBannerVisible ? View.VISIBLE : View.GONE);
        });
    }
}
