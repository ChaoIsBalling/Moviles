package com.example.desktopengine;

import com.example.engine.IImage;
import java.awt.Image;
import java.awt.image.ImageObserver;

public class DesktopImage  implements IImage{
    private Image img; //Guarda la imagen real
    private ImageObserver observer;
    DesktopImage(Image im,int width, int height){

        this.img = im.getScaledInstance(width, height, Image.SCALE_DEFAULT);

    }
    DesktopImage(Image im){

        this.img = im;

    }

    public Image getCurrentImage(){
        return this.img;
    }



    @Override
    public int getWidth() {
        return this.img.getWidth(null);
    }

    @Override
    public int getHeight() {
        return this.img.getHeight(null);
    }
}
