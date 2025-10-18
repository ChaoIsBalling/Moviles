package com.example.gamelogic;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;

public class Button {
    class Imagen{
        int x;
        int y;
        int w;
        int h;
        String image;
        void Render(Graphics gr){

        }
    }
    class Texto{
        int x;
        int y;
        int w;
        int h;
        String font;
        String texto;

        void Render(Graphics gr){

        }

    }
    interface Figura{
        int x = 0;
        int y =0;
        int color =0;
        void Render(Graphics gr);

    }
    class Rectangulo implements Figura{
        int w;
        int h;
        @Override
        public void Render(Graphics gr) {

        }
    }
    class Triangulo implements Figura{
        int x2;
        int y2;
        int x3;
        int y3;
        @Override
        public void Render(Graphics gr) {

        }
    }
    class Pentagono implements Figura{
        int x2;
        int y2;
        int x3;
        int y3;
        int x4;
        int y4;
        int x5;
        int y5;
        @Override
        public void Render(Graphics gr) {

        }
    }

    int x;
    int y;
    int w;
    int h;
    int color;
    Imagen imagen;
    Texto texto;
    ArrayList<Figura> figuras;

    public void Render(Graphics gr) {

    }

}
