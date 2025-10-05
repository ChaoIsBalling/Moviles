package com.example.desktopengine;

import com.example.engine.Engine;
import com.example.engine.Graphics;
import com.example.engine.State;

public class DesktopEngine implements Engine{
    State state;
    long lastFrameTime;

    DesktopGraphics gr;
    boolean running;
    public void run(){
        long prevTime= lastFrameTime;
        int frames = 0;

        while(running){
            long currentTime = System.nanoTime();
            long nanoElpasedTime = currentTime-lastFrameTime;
            lastFrameTime = currentTime;

            double elapsedTime = (double) nanoElpasedTime/1.0E9;
            state.update(elapsedTime);

            if(currentTime - prevTime > 1000000000L){
                long fps = frames * 1000000000L /(currentTime-prevTime);
                System.out.println(" " + fps +" fps" );
                frames = 0;
                prevTime = currentTime;
            }
        }

        ++frames;
        do{
            gr.startFrame();
            state.render(gr);

            try{
                state.render(gr);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } while(!this.gr.swapBuffer());
    }
    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void setState(State state) {
        this.state = state;
    }

    @Override
    public Graphics getGraphics() {
        return null;
    }
}