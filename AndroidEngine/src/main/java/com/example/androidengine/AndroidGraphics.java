package com.example.androidengine;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

import com.example.engine.Font;
import com.example.engine.Graphics;

public class AndroidGraphics implements Graphics {
    private SurfaceHolder holder;
    private SurfaceView sView;
    private Paint paint;
    private Canvas canvas;


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
}
