package com.example.desktopengine;

import com.example.engine.IImage;
import java.awt.Image;
import java.awt.image.ImageObserver;

public class DesktopImage  implements IImage{
    private Image img; //Guarda la imagen real
    DesktopImage(Image im){
        this.img =  im;
//        try {
//            img  = ImageIO.read(new File(file));
//        } catch (IOException e) {
//            System.out.println("Error al cargar una imagen: " + e.getMessage());
//            throw new RuntimeException(e);
//        }

    }

    public Image getCurrentImage(){
        return this.img;
    }



    @Override
    public int getWidth() {
        return 0;
    }

    @Override
    public int getHeight() {
        return 0;
    }
}
