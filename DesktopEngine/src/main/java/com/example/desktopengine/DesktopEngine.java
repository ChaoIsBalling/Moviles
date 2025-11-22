package com.example.desktopengine;

import com.example.engine.Engine;
import com.example.engine.Graphics;
import com.example.engine.State;
import com.example.engine.TouchEvent;
import com.example.engine.Audio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.io.FileReader;

import javax.swing.JFrame;

/**
 * Clase que implementa el motor principal del juego para escritorio.
 * Se encarga de la inicialización de otros módulos del motor y gestional el bucle principal
 * Implementa Runnable (propia de Java) y la interfaz Engine.
 */
public class DesktopEngine implements Runnable, Engine {
    /**
     * Ventana del juego
     */
    private final JFrame myView;
    /**
     * Tiempo del último frame
     */
    long lastFrameTime;
    /**
     * Instancia del Desktop Graphics que se encarga del renderizado
     */
    DesktopGraphics gr;

    /**
     * booleano que indica el estado del bucle principal
     */
    boolean running;
    /**
     * Estado actual del motor que contiene la lógica de juego
     */
    private State state;
    /**
     * Hilo de renderizado
     */
    private Thread renderThread;
    /**
     * Instancia de Desktop Input que maneja la entrada del usuario
     */
    private DesktopInput input;

    /**
     * Instancia de Desktop Input que maneja el audio del motor
     */
    private DesktopAudio audio;
    /**
     * Ruta raiz de los assets que se usan para el motor
     */
    String root = "data/Files/";

    /**
     * Constructora que inicializa el motor de escritorio.
     * @param view Venta de los gráficos
     */
    public DesktopEngine(JFrame view){
        this.myView = view;
        this.gr = new DesktopGraphics(this.myView); //Sistema de gráficos
        this.input = new DesktopInput();
        this.myView.addMouseListener(this.input);
        this.audio= new DesktopAudio();
        gr.setLogicSize(600,400);
    }

    /**
     * Bucle principal del motor
     */
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

    /**
     * Inicializa el hilo de renderizado
     */
    @Override
    public void resume() {
        if(!this.running){
            this.running = true;
            this.renderThread = new Thread(this);
            this.renderThread.start();
        }
    }

    /**
     * Devuelve la instancia del gestor de audio
     */
    @Override
    public Audio getAudio()
    {
        return this.audio;
    }

    /**
     * Pausa el motor (no implementado)
     */
    @Override
    public void pause() {

    }

    /**
     * Este metodo lee un archivo .txt, en este caso lo usamos para la lectura de mapa
     * @param path Nombre del archivo
     * @return Array de strings con la lectura de mapa hecha
     */
    @Override
    public ArrayList<String> readFile(String path)
    {
        ArrayList<String> file = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(root+path));
            String line;

            while ((line = reader.readLine()) != null)
                file.add(line);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return file;
    }

    /**
     * Cambia el estado actual y setea las instancias del motor necesarias
     * @param state Estado al que queremos transicionar
     */
    @Override
    public void setState(State state) {
        this.state = state;
        this.state.setAudio(this.audio);
        this.state.setGraphics(this.gr);
    }

    /**
     * Devuelve el estado actual del juego
     * @return estado
     */
    @Override
    public State getState()
    {
        return this.state;
    }

    /**
     * Devuelve el gestor de Graficos del motor
     * @return Graphics
     */
    @Override
    public Graphics getGraphics() {
        return gr;
    }
}