package com.example.androidengine;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

import java.io.IOException;

import com.example.engine.Font;
import com.example.engine.Graphics;
import com.example.engine.Scene;

public class AndroidGraphics implements Graphics, Runnable {
    private SurfaceHolder holder;
    private SurfaceView sView;
    private Paint paint;
    private Canvas canvas;

    private Thread renderThread;
    private Asset asset;


    private boolean running;

    private Scene scene;
    private final Rect rect = new Rect();


    AndroidGraphics(SurfaceView view){
        this.sView = view;
        this.holder = this.sView.getHolder();
        this.paint = new Paint();
        this.paint.setColor(0xFFFFFFFF);
    }

    public void startFrame(){
        while(!this.holder.getSurface().isValid());
        this.canvas = this.holder.lockHardwareCanvas();
    }

    public void createText(String text, float x, float y){
        this.canvas.drawText(text,x,y,this.paint);
    }

    public Image createImage(String path){
        Bitmap bm = null;

        try{
            bm = BitmapFactory.decodeStream(this.asset.open(path));
        }catch (IOException ex){
            throw new RuntimeException("Error reading "+ path, ex);
        }
        return new AndroidImage(bm);
    @Override
    public void setColor(int color) {

    }

    @Override
    public void setFont(Font font) {

    }

    @Override
    public void run() {

    }
}
}
