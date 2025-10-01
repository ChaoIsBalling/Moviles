package com.example.androidengine;


import android.view.SurfaceView;

import java.util.List;



public class AndroidEngine implements Engine,Runnable {

    private AndroidGraphics gr;

    private Thread renderThread;

    private boolean running;

    private SurfaceView sView;

    private State state;

    public AndroidEngine(SurfaceView view){
        this.sView = view;
        this.gr = new AndroidGraphics(view);

    }

    public void resume(){
        if(this.running){
            this.running = true;

            this.renderThread = new Thread(this);
            this.renderThread.start();
        }
    }

    public void setState(State state){
        this.state = state;
    }

    public Graphics getGraphics(){
        return this.gr;
    }
    public void pause(){
        if(this.running){
            this.running = false;
            while(true){
                try{
                    this.renderThread.join();
                    this.renderThread = null;
                    break;


                }catch (InterruptedException ie{

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
        while (this.running && this.renderView.getWidth() == 0);

        long lastFrameTime = System.nanoTime();
        long prevTime = lastFrameTime;    // Informe de FPS
        int frames = 0;

        canvas = gr.getCanvas(); // Obtiene el canvas del sistema gráfico

        while (running) {
            long currentTime = System.nanoTime();
            long nanoElapsedTime = currentTime - lastFrameTime;
            lastFrameTime = currentTime;

            // Informe de FPS
            double elapsedTime = (double) nanoElapsedTime / 1.0E9;
            this.update(elapsedTime);

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
