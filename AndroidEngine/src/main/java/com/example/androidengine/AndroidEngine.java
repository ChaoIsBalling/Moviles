package com.example.androidengine;

import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.res.AssetManager;
import android.os.Build;
import android.view.SurfaceView;

import androidx.core.app.NotificationManagerCompat;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.util.concurrent.TimeUnit;
import java.io.File;

import org.json.JSONObject;
import org.json.JSONException;

//import androidx.work.Worker;
//import androidx.work.WorkerParameters;
import android.content.Intent;

/**
 * Clase que implementa el motor principal del juego para Android.
 * Se encarga de la inicialización de otros módulos del motor y gestiona el bucle principal
 * Implementa Runnable (propia de Java) y la interfaz Engine.
 */

public class AndroidEngine implements Runnable {
    private AndroidGraphics gr; //Instancia del Android Graphics
    private AssetManager assetManager;   //Instancia de los AssetManager de Android
    private Thread renderThread; //Hilo

    private boolean running;

    private SurfaceView sView;

    private State state;

    private AndroidInput input;

    private AndroidAudio audio;

    private AndroidMobile mobile;

    private String filesDir="Files/";

    private String sharedPrefFile = "sharedprefs";

    private int iconNotification; //icono de la app

    //clave para el hash
    private String password="Mahjong";


    //Strings para mostrar notificaciones en el juego
    private final String CHANNEL_NAME = "channel";
    private final String CHANNEL_DESCRIPTION = "description";
    private final String CHANNEL_ID = "id";

    /**
     * Constructora del motor de Android
     * @param view Surface view donde renderizamos el juego
     * @param androidMobile Interfaz mobile de Android
     */
    public AndroidEngine(SurfaceView view, AndroidMobile androidMobile){
        this.sView = view;
        this.input = new AndroidInput(); //incializa input
        this.sView.setOnTouchListener(this.input);
        assetManager=this.sView.getContext().getAssets();
        this.gr = new AndroidGraphics(view);
        this.audio= new AndroidAudio(sView.getContext().getAssets());
        this.mobile = androidMobile;


        //Crea el canal para mostrar las notificaciones del juego
        createNotificationChannel();

        System.loadLibrary("AndroidEngine");
    }

    /**
     * Metodo para reanudar el funcionamiento del motor
     */
    public void resume(){
        if(!this.running){
            this.running = true;
            this.renderThread = new Thread(this);
            this.renderThread.start();
        }
    }

    /**
     * Metodo para cambiar el estado de juego del motor
     * @param state Estado al que queremos cambiar
     */
    public void setState(State state){
        this.state = state;
        this.state.setGraphics(this.gr);
        this.state.setAudio(this.audio);
    }

    /**
     * Metodo para obtener el gestor de audio del motor
     * @return AndroidSound
     */
    public AndroidAudio getAudio() {
        return this.audio;
    }

    /**
     * Metodo para obtener el estado actual del motor
     * @return estado
     */
    public State getState()
    {
        return this.state;
    }

    /**
     * Metodo para obtener el gestor de los graficos del motor
     * @return AndroidGraphics
     */
    public AndroidGraphics getGraphics(){
        return this.gr;
    }

    /**
     * Metodo que determina cuantos archivos tiene un directorio
     * @param dir Nombre del directorio
     * @return int que indica la longitud del directorio
     */
    public int getDirectoryLenght(String dir){
        String[] files = null;
        try {
            files = this.assetManager.list(this.filesDir + dir);
            //devolvemos la longitud del directorio
            return files != null ? files.length : 0;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    /**
     * Metodo que lanza un intent que comparte un mensaje de texto
     * @param message Mensaje que se quiere mandar
     */
    public void luanchShareIntent(String message)
    {
        Bitmap bitmap =getGraphics().takeScreenshot();
        String imageUri = MediaStore.Images.Media.insertImage(this.sView.getContext().getContentResolver(), bitmap,
            "Description" , null);
        Uri uri = Uri.parse(imageUri) ;
        Intent shareIntent = new Intent(Intent. ACTION_SEND);
        shareIntent.setType( "image/*" );
        shareIntent.putExtra(Intent.EXTRA_TEXT, message );
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        this.sView.getContext().startActivity(Intent. createChooser(shareIntent , "Share Image" ));

    }

    /**
     * Metodo para obtener la interfaz Mobile del motor
     * @return AndroidMobile
     */
    public AndroidMobile getMobile() { return this.mobile; }


    /**
     * Lector que coge un archivo interno y lo convierte a Json
     * @param file nombre del archivo a convertir
     * @return JSONObject del archivo que queriamos
     */
    public JSONObject readInternalJsonFile(String file) {
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
            fis.close();

        } catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }
        return obj;
    }

    /**
     * Metodo de lectura de archivos tipo JSON
     * @param file Nombre del archivo JSON
     * @return JSON Object del archivo
     */
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

    /**
     * Metodo para escribir en un archivo dentro del sistema de ficheros del programa
     * @param file Archivo sobre el que queremos escribir
     * @param output Contenido que queremos escribir
     */
    public void writeFile(String file,String output) {
        FileOutputStream os = null;
        try {
           os=  this.sView.getContext().openFileOutput(file, this.sView.getContext().MODE_PRIVATE);
           os.write(output.getBytes());
           os.flush();
           os.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Metodo para crear una notifiacción push programada
     * @param time tiempo
     * @param timeunit unidad de tiempo
     * @param icon icono asociado a la notificacion
     * @param title titulo de la notificacion
     * @param firstText texto de la notificacion
     */
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



    /**
     * Metodo para settear el icono de la notificacion
     * @param icon icono de la notificacion
     */
    public void setNotificationIcon(int icon) {
        this.iconNotification=icon;
    }
    /**
     * Metodo booleano el cual hace una comprobación de seguridad de si el hash ya existe en el sistema
     * @param hash Hash que queremos comprobar si existe
     * @return true si existe , false si no
     */
    public Boolean checkHash(String hash) {
        if(!checkFileExists("hash"))
            return false;
        else{
        String a= readFileString("hash");
        if (!hash.equals(a))
            return false;
        }
        return true;
    }

    /**
     * Metodo que te crea automaticamente un String hash para el encriptado el cual ya tiene contraseña
     * @param file nombre del archivo al que le queremos aplicar le hash
     * @return hashSHA256 en formato string
     */
    public String createHash(String file) {
        return hashSHA256(this.password+file);
    }

    /**
     * Lectura de archivo que se enfoca en obtener un String con los datos del fichero
     * @param file archivo que queremos leer
     * @return String con los datos
     */

    public String readFileString(String file) {
        String obj ="";
        try {
            FileInputStream fis = this.sView.getContext().openFileInput(file);
            InputStreamReader inputStreamReader = new InputStreamReader(fis);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = reader.readLine()) != null) {
                obj += line;
            }
            fis.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return obj;
    }

    /**
     * Metodo que comprueba si un archivo existe o si ya esta creado
     * @param file nombre del archivo a comprobar
     * @return booleano que determina si el archivo existe o no
     */

    public boolean checkFileExists(String file) {
        File f = new File(this.sView.getContext().getFilesDir(),file);
        return f.exists();
    }



    /**
     * Metodo que encripta un string a hashSHA256 usando el NDK
     * @param string archivo a encriptar
     * @return String del hash
     */
    public String hashSHA256(String string) {
        return nativeHash(string);
    }

    /**
     * Metodo que llama al metodo interno de c++ que crea un hash
     * @param s archivo al que aplicar el hash
     * @return String del hash
     */
    private native String nativeHash(String s);

    /**
     * Metodo para pausar el funcionamiento del motor
     */
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

    /**
     * Metodo que ejecuta el bucle principal del motor
     */
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
