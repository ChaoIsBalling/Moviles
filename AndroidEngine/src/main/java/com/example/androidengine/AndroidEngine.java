package com.example.androidengine;


import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Build;
import android.view.SurfaceView;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.engine.Audio;
import com.example.engine.Engine;
import com.example.engine.State;
import com.example.engine.Graphics;
import com.example.engine.TouchEvent;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

import org.json.JSONObject;
import org.json.JSONException;
import org.json.JSONArray;
//import androidx.work.Worker;
//import androidx.work.WorkerParameters;



public class AndroidEngine implements Engine,Runnable {

    private AndroidGraphics gr;

    private AssetManager assetManager;

    private Thread renderThread;

    private boolean running;

    private SurfaceView sView;

    private State state;

    private AndroidInput input;

    private AndroidAudio audio;

    private String filesDir="Files/";

    private String sharedPrefFile = "sharedprefs";

    private int iconNotification;

    private final String CHANNEL_NAME = "chanel";
    private final String CHANNEL_DESCRIPTION = "description";
    private final String CHANNEL_ID = "id";

    public AndroidEngine(SurfaceView view){
        this.sView = view;
        this.input = new AndroidInput();
        this.sView.setOnTouchListener(this.input);
        assetManager=this.sView.getContext().getAssets();
        this.gr = new AndroidGraphics(view);
        this.audio=new AndroidAudio(sView.getContext().getAssets());

        System.loadLibrary("AndroidEngine");
    }
    @Override
    public ArrayList<String> readFile(String path)
    {
        ArrayList<String> file = new ArrayList<>();

        try {
            InputStream inputStream = assetManager.open(filesDir+path);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            while ((line = reader.readLine()) != null)
                file.add(line);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return file;
    }
    @Override
    public void resume(){
        if(!this.running){
            this.running = true;

            this.renderThread = new Thread(this);
            this.renderThread.start();
        }
    }

    @Override
    public void setState(State state){

        this.state = state;
        this.state.setAudio(this.audio);
        this.state.setGraphics(this.gr);
    }

    @Override
    public Audio getAudio() {
        return this.audio;
    }

    @Override
    public State getState()
    {
        return this.state;
    }
    public Graphics getGraphics(){
        return this.gr;
    }

    @Override
    public InputStream readFile2(String file) {
        InputStream is = null;
        try {
           is = this.sView.getContext().openFileInput(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return is;
    }

    @Override
    public JSONObject readJsonFile(String file) {
        JSONObject jsonObject;
    try{
        InputStream is = null;
        StringBuilder jsonText = new StringBuilder();
        is = assetManager.open(filesDir+file);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String line;
        while ((line = reader.readLine()) != null) {
            jsonText.append(line);
        }
        jsonObject = new JSONObject(jsonText.toString());
    } catch (IOException e) {
        throw new RuntimeException(e);
    }
    catch(JSONException e)
    {
        throw new RuntimeException(e);
    }

        return jsonObject;
    }

    @Override
    public String openAssetFile(String file) {
        return "";
    }

    @Override
    public OutputStream writeFile(String file) {
        OutputStream os = null;
        try {
           os=  this.sView.getContext().openFileOutput(file, this.sView.getContext().MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return os;
    }

    @Override
    public void SaveValueInt(String key, int value) {
        Context context = this.sView.getContext();
        SharedPreferences sharedPref = context.getSharedPreferences(this.sharedPrefFile , Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    @Override
    public int LoadValueInt(String key) {
        Context context = this.sView.getContext();
        SharedPreferences sharedPref = context.getSharedPreferences(this.sharedPrefFile , Context.MODE_PRIVATE);
        int value = sharedPref.getInt(key, 0);
        return value;
    }

    @Override
    public void programNotificacion(int time, TimeUnit timeunit, int icon, String title, String firstText) {
        WorkRequest request = new OneTimeWorkRequest.Builder(ReminderWorker.class)
                .setInitialDelay(time, timeunit)
                .setImputData(new Data.Builder()
                        .putString("title",title)
                        .putString(firstText,firstText)
                        .putInt(iconNotification,icon)
                );
    }

    @Override
    public void showNotificacion(String title, String firstText) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder( this.sView.getContext(), CHANNEL_ID)
                .setSmallIcon(this.iconNotification)
                .setContentTitle( title )
                .setContentText( firstText )
                .setStyle( new NotificationCompat.BigTextStyle()
                        .bigText( firstText ))
                .setPriority(NotificationCompat. PRIORITY_DEFAULT);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this.sView.getContext());
// notificationId is a unique int for each notification that you must define.
        notificationManager.notify(notificationId, builder.build());
        if(Activity.Com)
    }

    @Override
    public void setNotificationIcon(int icono) {
        this.iconNotification=icono;
    }

    @Override
    public String hashSHA256(String string) {
        return nativeHash(string);
    }

    private native String nativeHash(String s);

    private void createNotificationChannel(){
        if (Build.VERSION. SDK_INT >= Build.VERSION_CODES. O) {
            int importance = NotificationManager. IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID , CHANNEL_NAME, importance) ;
            channel.setDescription(CHANNEL_DESCRIPTION) ;
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this.sView.getContext());
            notificationManager.createNotificationChannel(channel) ;
        }

    }

    @Override
    public void pause(){
        if(this.running){
            this.running = false;
            while(true){
                try{
                    this.renderThread.join();
                    this.renderThread = null;
                    break;

                }catch (InterruptedException ie){
                }
            }
        }
    }


    @Override
    public void run() {
        if (renderThread != Thread.currentThread()) {
            //Evitamos que otra clase llame a este método
            throw new RuntimeException("run() should not be called directly");
        }

        // El thread se pone en marcha
        while (this.running && this.sView.getWidth() == 0);

        long lastFrameTime = System.nanoTime();
        long prevTime = lastFrameTime;    // Informe de FPS
        int frames = 0;

        while (running) {
            long currentTime = System.nanoTime();
            long nanoElapsedTime = currentTime - lastFrameTime;
            lastFrameTime = currentTime;

            // Informe de FPS
            double elapsedTime = (double) nanoElapsedTime / 1.0E9;

            for (TouchEvent e: this.input.getTouchEvents()){
                e.x = this.gr.real2LogicX(e.x);
                e.y = this.gr.real2LogicY(e.y);
            }

            state.handleInput(this.input.getTouchEvents(), elapsedTime);
            this.state.update(elapsedTime);
            this.input.events.clear();

            if (currentTime - prevTime > 1000000000L) {
                long fps = frames * 1000000000L / (currentTime - prevTime);
                System.out.println("" + fps + " fps");
                frames = 0;
                prevTime = currentTime;
            }
            ++frames;
            //reproducimos los sonidos
            // Pintamos el frame
            this.gr.startFrame();
            this.state.render(this.gr);
            this.gr.endFrame();
        }

    }



}
