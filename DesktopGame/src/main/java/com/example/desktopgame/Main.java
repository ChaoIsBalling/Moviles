package com.example.desktopgame;

import com.example.desktopengine.DesktopEngine;
import com.example.gamelogic.GameLogic;
import com.example.gamelogic.Menu;


import java.awt.Insets;

import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {

        //Creamos una ventana para los gráficos usando JFrame
        JFrame renderView = new JFrame("Balatrito");

        renderView.setSize(600, 400);
        renderView.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        renderView.setIgnoreRepaint(true);

        renderView.setVisible(true);

        // Intentamos crear el buffer strategy con 2 buffers.
        int intentos = 100;
        while(intentos-- > 0) {
            try {
                renderView.createBufferStrategy(2);
                break;
            }
            catch(Exception e) {
            }
        } // while pidiendo la creación de la buffeStrategy
        if (intentos == 0) {
            System.err.println("No pude crear la BufferStrategy");
            return;
        }
        else {
            // En "modo debug" podríamos querer escribir esto.
            //System.out.println("BufferStrategy tras " + (100 - intentos) + " intentos.");
        }

        // Hacemos visible la ventana.
        renderView.setVisible(true);
        //Inicializamos el motor gráfico de escritorio usando el JFrame renderView
        DesktopEngine engine = new DesktopEngine(renderView);

        engine.setState(new Menu(engine));


        //Inicializamos el motor de juego
        engine.resume();
    }
}