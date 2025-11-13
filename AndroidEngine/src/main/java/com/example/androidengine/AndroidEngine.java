package com.example.androidengine;


import android.content.res.AssetManager;
import android.view.SurfaceView;

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
import org.json.JSONObject;
import org.json.JSONException;
import org.json.JSONArray;




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


    public AndroidEngine(SurfaceView view){
        this.sView = view;
        this.input = new AndroidInput();
        this.sView.setOnTouchListener(this.input);
        assetManager=this.sView.getContext().getAssets();
        this.gr = new AndroidGraphics(view);
        this.audio=new AndroidAudio(sView.getContext().getAssets());

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
