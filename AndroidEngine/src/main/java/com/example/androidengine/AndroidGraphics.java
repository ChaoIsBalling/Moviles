package com.example.androidengine;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

import com.example.engine.Font;
import com.example.engine.Graphics;
import com.example.engine.Scene;

public class AndroidGraphics implements Graphics, Runnable {
    private SurfaceHolder holder;
    private SurfaceView sView;
    private Paint paint;
    private Canvas canvas;

    private Thread renderThread;

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
