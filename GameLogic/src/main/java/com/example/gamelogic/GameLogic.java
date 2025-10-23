package com.example.gamelogic;

import com.example.engine.Engine;
import com.example.engine.Graphics;
import com.example.engine.IFont;
import com.example.engine.State;
import com.example.engine.TouchEvent;

import java.awt.FontFormatException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameLogic implements State {
    boolean firstFrame = false;

    ArrayList<ArrayList<Square>> casillas;
    public GameLogic(){
        casillas = new ArrayList<ArrayList<Square>>();
        int fil = 8;
        int col = 15;
        for (int i =0; i<fil;i++){
            ArrayList<Square> fila = new ArrayList<Square>();
            for(int j =0; j<col;j++){
                Square cuad = new Square((float)(j*100+100),(float)(i*100+100),100,100);
                fila.add(cuad);
            }
            casillas.add(fila);
        }
    }

    @Override
    public void update(double deltaTime) {
        if(!this.firstFrame){
            this.firstFrame = !this.firstFrame;
        }
        else {

        }

    }

    @Override
    public void render(Graphics gr) {



    }

    @Override
    public void handleInput(ArrayList<TouchEvent> list, double elapseTime) {

        for(TouchEvent e: list){

            switch (e.type){
                case TOUCH_DOWN:

                    break;
                case TOUCH_UP:

                    break;
                case TOUCH_MOVE:
                    System.out.println("Dedooo");
                    break;
            }
        }
    }


}