package com.example.androidengine;

import android.content.Context;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.view.PixelCopy;
import android.app.Activity;
import android.util.Log;
import android.view.SurfaceView;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import java.util.concurrent.TimeUnit;

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

public class AndroidMobile implements Mobile {
    private AdView adView;
    private Activity activity;
    private SurfaceView surfaceView;
    private RewardedAd rewardedAd;

    //ID`s de unidad de anuncios de prueba, tanto para banner como para reward
    private static final String AD_UNIT_ID = "ca-app-pub-3940256099942544/9214589741";
    private static final String AD_REWARD_UNIT_ID = "ca-app-pub-3940256099942544/5224354917";
    private static final String TAG = "MainActivity_TowerDefense";
    public AndroidMobile(Activity activity, SurfaceView surfaceView,AdView adView){
        this.activity = activity;
        this.adView = adView;
        this.surfaceView = surfaceView;

        MobileAds.initialize(activity, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                //Cargamos anuncio recompensado
                loadRewardedAd();
                //Cargamos anuncio banner
                if(adView != null){
                    loadBannerAd();
                }
            }
        });
    }

    /**
     * Metodo para cargar un anuncio Banner
     */
    private void loadBannerAd(){
        // Crear un addView en el que meter el anuncio
        adView = new AdView(activity);
        adView.setAdUnitId(AD_UNIT_ID);
        adView.setAdSize(AdSize.BANNER);

        // cargamos el anuncio banner
        AdRequest adRequest = new AdRequest.Builder().build();
        this.adView.loadAd(adRequest);
    }

    /**
     * Metodo para cargar el anuncio recompensado con Admob
     */
    private void loadRewardedAd() {
        RewardedAd.load(
                activity,
                AD_REWARD_UNIT_ID,
                new AdRequest.Builder().build(),
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
                                        rewardedAd = null;
                                        Toast.makeText(activity, "onAdDismissedFullScreenContent", Toast.LENGTH_SHORT)
                                                .show();
                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                                        // Called when fullscreen content failed to show.
                                        Log.d(TAG, "Ad failed to show.");
                                        // Don't forget to set the ad reference to null so you
                                        // don't show the ad a second time.
                                        rewardedAd = null;
                                        Toast.makeText(
                                                        activity, "onAdFailedToShowFullScreenContent", Toast.LENGTH_SHORT)
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
    private void showRewardedVideo() {
        if ( this.rewardedAd == null) {
            Log.d("AdRecompensado", "The rewarded ad wasn't ready yet.");
            return;
        }
        //showVideoButton.setVisibility(View.INVISIBLE);

        rewardedAd.show(
                activity, new OnUserEarnedRewardListener() {
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
    public void makeNotification() {

    }

    @Override
    public void scheduleNotificationWithWorkManager() {

    }
}
