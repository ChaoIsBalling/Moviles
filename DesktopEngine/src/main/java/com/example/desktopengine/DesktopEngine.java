package com.example.desktopengine;

import com.example.engine.Engine;
import com.example.engine.Graphics;
import com.example.engine.State;
import com.example.engine.TouchEvent;

import javax.swing.JFrame;

public class DesktopEngine implements Runnable, Engine {
    private final JFrame myView;
    long lastFrameTime;
    DesktopGraphics gr;
    boolean running;
    private State state;
    private Thread renderThread;
    private DesktopInput input;

    public DesktopEngine(JFrame view){
        this.myView = view;
        this.gr = new DesktopGraphics(this.myView); //Sistema de gráficos
        this.input = new DesktopInput();
        this.myView.addMouseListener(this.input);
        gr.setLogicSize(600,400);

    }

    @Override
    public void run() {
        long prevTime = lastFrameTime;
        int frames = 0;

        while (running) {
            long currentTime = System.nanoTime();
            long nanoElpasedTime = currentTime - lastFrameTime;
            lastFrameTime = currentTime;

            double elapsedTime = (double) nanoElpasedTime / 1.0E9;

            for (TouchEvent e: input.getTouchEvents()){
                e.x = this.gr.real2LogicX(e.x);
                e.y = this.gr.real2LogicY(e.y);
            }

            state.handleInput(this.input.getTouchEvents(), elapsedTime);
            state.update(elapsedTime);
            this.input.events.clear();


            do {
                gr.startFrame();
                state.render(gr);

                try {
                    state.render(gr);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } while (!this.gr.swapBuffer());

            ++frames;

            //Calculamos FPS por segundo
            if (currentTime - prevTime > 1000000000L) {
                long fps = frames * 1000000000L / (currentTime - prevTime);
                System.out.println(" " + fps + " fps"); //Informamos de los FPS
                frames = 0;
                prevTime = currentTime;
            }
        }
    }

    //Este método inicia el hilo de renderizado
    @Override
    public void resume() {
        if(!this.running){
            this.running = true;
            this.renderThread = new Thread(this);
            this.renderThread.start();
        }
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
        return gr;
    }
}