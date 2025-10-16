package com.example.androidengine;


import android.view.SurfaceView;

import com.example.engine.Engine;
import com.example.engine.State;
import com.example.engine.Graphics;
import com.example.engine.TouchEvent;

import java.util.List;



public class AndroidEngine implements Engine,Runnable {

    private AndroidGraphics gr;

    private Thread renderThread;

    private boolean running;

    private SurfaceView sView;

    private State state;

    private AndroidInput input;

    public AndroidEngine(SurfaceView view){
        this.sView = view;
        this.input = new AndroidInput();
        this.sView.setOnTouchListener(this.input);
        this.gr = new AndroidGraphics(view);

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
    }

    public Graphics getGraphics(){
        return this.gr;
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

            for (TouchEvent e: input.getTouchEvents()){
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

            // Pintamos el frame
            this.gr.startFrame();
            this.state.render(this.gr);
            this.gr.endFrame();
        }

    }



}
