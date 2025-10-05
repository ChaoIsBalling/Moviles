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
import com.example.androidengine.AndroidImage;

public class AndroidGraphics implements Graphics {
    private SurfaceHolder holder;
    private SurfaceView sView;
    private Paint paint;
    private Canvas canvas;

    private Thread renderThread;
    private Asset asset;

    private boolean running;

    private Scene scene;
    private final Rect rect = new Rect();


    public AndroidGraphics(SurfaceView view){
        this.sView = view;
        this.holder = this.sView.getHolder();
        this.paint = new Paint();
    }

    protected void startFrame(){
        while(!this.holder.getSurface().isValid());
        this.canvas = this.holder.lockHardwareCanvas();
    }

    protected void endFrame(){
        this.holder.unlockCanvasAndPost(canvas);
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

    }

    @Override
    public void pintarCirculo(float x, float y, float r) {
        this.canvas.drawCircle(x,y,r,this.paint);
    }

    @Override
    public void pintarCuadrado(float x, float y, float w, float h) {
        this.paint.setStyle(Paint.Style.FILL);
        this.canvas.drawRect(x,y,x+w,y+h,this.paint);
    }

    @Override
    public void pintarFondo(int color) {

    }

    @Override
    public void pintarTexto(String texto, float x, float y) {

    }

    @Override
    public void pintarImagen(IImage img, float x, float y) {

    }

    @Override
    public void setColor(int color) {
        this.paint.setColor(color);
    }

    @Override
    public void setFont(Font font) {

    }

}
