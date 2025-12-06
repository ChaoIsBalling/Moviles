package com.example.androidengine;


import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.Build;
import android.view.SurfaceView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.example.engine.Audio;
import com.example.engine.Engine;
import com.example.engine.Mobile;
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
import java.io.FileInputStream;
import java.util.concurrent.TimeUnit;

import org.json.JSONObject;
import org.json.JSONException;
import org.json.JSONArray;

//import androidx.work.Worker;
//import androidx.work.WorkerParameters;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.graphics.Bitmap;

/**
 * Clase que implementa el motor principal del juego para Android.
 * Se encarga de la inicialización de otros módulos del motor y gestiona el bucle principal
 * Implementa Runnable (propia de Java) y la interfaz Engine.
 */

public class AndroidEngine implements Engine,Runnable {

    /**
     * Instancia del Android Graphics
     */
    private AndroidGraphics gr;

    /**
     * Instancia de los AssetManager de Android
     */
    private AssetManager assetManager;

    /**
     *
     */
    private Thread renderThread;

    private boolean running;

    private SurfaceView sView;

    private State state;

    private AndroidInput input;

    private AndroidAudio audio;

    private AndroidMobile mobile;

    private String filesDir="Files/";

    private String sharedPrefFile = "sharedprefs";

    private int iconNotification;

    //Strings para mostrar notificaciones en el juego
    private final String CHANNEL_NAME = "channel";
    private final String CHANNEL_DESCRIPTION = "description";
    private final String CHANNEL_ID = "id";

    public AndroidEngine(SurfaceView view, AndroidMobile androidMobile){
        this.sView = view;
        this.input = new AndroidInput();
        this.sView.setOnTouchListener(this.input);
        assetManager=this.sView.getContext().getAssets();
        this.gr = new AndroidGraphics(view);
        this.audio=new AndroidAudio(sView.getContext().getAssets());
        this.mobile = androidMobile;


        //Crea el canal para mostrar las notificaciones del juego
        createNotificationChannel();

        System.loadLibrary("AndroidEngine");
    }
    //lector de archivos
    @Override
    public ArrayList<String> readFile(String path)
    {//usa el input stream y elbuffered reader para leer linea a linea un fichero y pasarlo a un arraylist
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
    //quita el pause
    @Override
    public void resume(){
        if(!this.running){
            this.running = true;
            this.renderThread = new Thread(this);
            this.renderThread.start();
        }
    }
    //setter del estado
    @Override
    public void setState(State state){
        this.state = state;
        this.state.setAudio(this.audio);
        this.state.setGraphics(this.gr);
        this.state.setMobile(this.mobile);
    }
    //getter del audio
    @Override
    public Audio getAudio() {
        return this.audio;
    }
    //getter del estado
    @Override
    public State getState()
    {
        return this.state;
    }
    //getter de los graficos
    public Graphics getGraphics(){
        return this.gr;
    }

    //metodo que inicializa un intent implícito pasandole un string
    @Override
    public void launchIntent(String application) {
        Intent intent = null;
        intent = new Intent(Intent.ACTION_VIEW, Uri.parse(application));
        this.sView.getContext().startActivity(intent);
    }
    //metodo que inicializa un intent implícito pasandole un string y con mas parametros
    //en su creación
    @Override
    public void launchIntent(String application, String text, String parameter)
    {
        Intent intent = null;
        intent = new Intent(Intent.ACTION_VIEW, Uri.parse(application).buildUpon()
                .appendQueryParameter( text, parameter )
                .build());
        this.sView.getContext().startActivity(intent);
    }
    //metodo que lanza un intent que comparte un mensaje de texto
    @Override
    public void luanchShareIntent(String message)
    {

        Intent shareIntent = new Intent(Intent. ACTION_SEND);
        shareIntent.setType("plain/text");
        shareIntent.putExtra(Intent.EXTRA_TEXT, message );
        this.sView.getContext().startActivity(Intent. createChooser(shareIntent , "Share Text" ));
    }


    //getter de mobile
    @Override
    public Mobile getMobile() { return this.mobile; }

    //lector que coje un archivo interno y lo convierte a Json
    @Override
    public JSONObject readJsonFile2(String file) {
        JSONObject obj = null;
        try {
            FileInputStream fis = this.sView.getContext().openFileInput(file);
            InputStreamReader inputStreamReader = new InputStreamReader(fis);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String a="";
            String line;
            while ((line = reader.readLine()) != null) {
                a += line;
            }
             obj= new JSONObject(a);
            System.out.print(a);

        } catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }
        return obj;
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
    public OutputStream writeFile(String file,String output) {
        OutputStream os = null;
        try {
           os=  this.sView.getContext().openFileOutput(file, this.sView.getContext().MODE_PRIVATE);
           os.write(output.getBytes());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        catch (IOException e) {
        throw new RuntimeException(e);
        }
        return os;
    }

    @Override
    public void programNotificacion(int time, TimeUnit timeunit, int icon, String title, String firstText) {
        WorkRequest request = new OneTimeWorkRequest.Builder(ReminderWorker.class)
                .setInitialDelay(time, timeunit) //Tiempo que tiene que pasar para que se envie la notificacion
                .setInputData(new Data.Builder() //datos que le pasamos a la constructora del worker
                        .putString("title",title)
                        .putString("text",firstText)
                        .putInt("icon",this.iconNotification)
                        .build())
                .build();

        //En el WorkManager ponemos en la cola de workers pendientes a la notifiacion programada que acabamos de crear
        WorkManager.getInstance(this.sView.getContext()).enqueue(request);

    }

    @Override
    public void showNotificacion(String title, String firstText) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder( this.sView.getContext(), CHANNEL_ID)
                .setSmallIcon(this.iconNotification)
                .setContentTitle(title)
                .setContentText(firstText)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(firstText))
                .setPriority(NotificationCompat. PRIORITY_DEFAULT);
        //Llamamos al manager de notifiaciones
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this.sView.getContext());
        //Comprobamos que la app tenga permisos de postear una notificación
        Activity activity = (Activity) sView.getContext();
        if (ActivityCompat.checkSelfPermission(activity,
                android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            // ActivityCompat#requestPermissions
            //si no los tiene, los solicitamos
            ActivityCompat.requestPermissions((Activity) this.sView.getContext(),new String[]{Manifest.permission.POST_NOTIFICATIONS},101);
            // here to request the missing permissions, and then overriding
            // public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                        int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            //return;
        }
        // notificationId is a unique int for each notification that you must define.
        int NOTIFICATION_ID = (int) System.currentTimeMillis();  // ID único
        notificationManager.notify(NOTIFICATION_ID, builder.build()); //Invocamos la notificación
    }

    /**
     * Metodo para crear un canal por el que transmitir notificaciones
     */
    private void createNotificationChannel(){
        // Verifica si es necesario crear un canal de notificaciones (a partir de Android 8.0)
        if (Build.VERSION. SDK_INT >= Build.VERSION_CODES. O) {
            int importance = NotificationManager. IMPORTANCE_DEFAULT; //Importancia
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID , CHANNEL_NAME, importance); //Creación del canal
            channel.setDescription(CHANNEL_DESCRIPTION); //Descripción del canal
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this.sView.getContext());
            notificationManager.createNotificationChannel(channel) ; //Crea el canal en el sistema
        }
    }

    @Override
    public void setNotificationIcon(int icon) {
        this.iconNotification=icon;
    }

    @Override
    public String hashSHA256(String string) {
        return nativeHash(string);
    }

    private native String nativeHash(String s);

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
    //Metodo que se encarga de desde un json inicializar todos los parametros de un boton


    //Metodo que corre el bucle principal del motor
    @Override
    public void run() {
        if (renderThread != Thread.currentThread()) {
            //Evitamos que otra clase llame a este metodo
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

            if (this.state != null) {
                for (TouchEvent e: this.input.getTouchEvents()){
                    e.x = this.gr.real2LogicX(e.x);
                    e.y = this.gr.real2LogicY(e.y);
                }
                state.handleInput(this.input.getTouchEvents(), elapsedTime);
                this.state.update(elapsedTime);
                this.input.events.clear();
            }


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
