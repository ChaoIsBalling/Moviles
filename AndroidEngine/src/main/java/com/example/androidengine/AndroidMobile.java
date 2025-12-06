package com.example.androidengine;

import android.app.Activity;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.admanager.AdManagerAdRequest;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

import com.example.engine.Mobile;

/**
 * Clase que implementa los metodos para mostrar anuncios y gestionar las notificaciones
 * Implementa la interfaz Mobile del motor
 */
public class AndroidMobile implements Mobile {
    //anuncio banner y su contenedor en el xml
    private AdView adView;
    private FrameLayout adContainer;
    //El main Activity
    private Activity activity;
    private SurfaceView surfaceView;

    //Anuncio recompensado
    private RewardedAd rewardedAd;

    //ID`s de unidad de anuncios de prueba, tanto para banner como para reward
    private static final String AD_UNIT_ID = "ca-app-pub-3940256099942544/9214589741";
    private static final String AD_REWARD_UNIT_ID = "ca-app-pub-3940256099942544/5224354917";
    private static final String TAG = "MainActivity";
    public AndroidMobile(Activity activity, SurfaceView surfaceView,FrameLayout adContainer){
        this.activity = activity;
        //this.adView = adView;
        this.surfaceView = surfaceView;
        this.adContainer = adContainer;

        MobileAds.initialize(activity, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });
        //Cargamos anuncio recompensado
        loadRewardedAd();
        //Cargamos anuncio banner
        loadBannerAd();
    }

    /**
     * Metodo para mostrar un anuncio Banner
     */
    private void loadBannerAd(){
        // cargamos el anuncio banner
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
     * Metodo para mostrar el anuncio recompensado con Admob
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
                                        // Don't forget to set the ad reference to null so you
                                        // don't show the ad a second time.
                                        //rewardedAd = null;
                                        //Toast.makeText(activity, "onAdDismissedFullScreenContent", Toast.LENGTH_SHORT).show();

                                        loadRewardedAd(); //volvemos a recargar anunciop, una vez ha sido consumido el anterior
                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                                        // Called when fullscreen content failed to show.
                                        Log.d(TAG, "Ad failed to show.");
                                        // Don't forget to set the ad reference to null so you
                                        // don't show the ad a second time.
                                        rewardedAd = null;
                                        Toast.makeText(activity, "onAdFailedToShowFullScreenContent", Toast.LENGTH_SHORT)
                                                .show();
                                    }

                                    @Override
                                    public void onAdShowedFullScreenContent() {
                                        // Called when fullscreen content is shown.
                                        Log.d(TAG, "Ad showed fullscreen content.");

                                        Toast.makeText(activity, "onAdShowedFullScreenContent", Toast.LENGTH_SHORT)
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
    public void showRewardedVideo() {

    }
    @Override
    public void makeNotification() {

    }

    @Override
    public void showRewardedAd() {
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
                            //int rewardAmount = rewardItem.getAmount();
                            //String rewardType = rewardItem.getType();
                        }
                    });
                } else {
                    System.out.println("The rewarded ad wasn't ready yet.");
                    Log.d(TAG, "The rewarded ad wasn't ready yet.");
                }
            }
        });
    }


    @Override
    public void scheduleNotificationWithWorkManager() {

    }
}
