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
import com.example.androidengine.AndroidImage;
import com.example.engine.Image;

public class AndroidGraphics implements Graphics {
    private SurfaceHolder holder;
    private SurfaceView sView;
    private Paint paint;
    private Canvas canvas;

    private Thread renderThread;

    private float scale;

    private float offsetX;
    private float offsetY;

    private float logicH;
    private float logicW;

    //private Asset asset;

    private boolean running;

    private final Rect rect = new Rect();


    public AndroidGraphics(SurfaceView view){
        this.sView = view;
        this.holder = this.sView.getHolder();
        this.paint = new Paint();
        this.canvas = new Canvas();

        scale =1;
        offsetX=0;
        offsetY=0;
    }

    protected void startFrame(){
        while(!this.holder.getSurface().isValid());
        this.canvas = this.holder.lockHardwareCanvas();
        this.clear();

        calculateTransforms();
        this.trasladar(this.offsetX,this.offsetY);
        this.escalar(this.scale,this.scale);
    }

    private void calculateTransforms(){
        float tempY = this.sView.getHeight()/logicH;
        float tempX = this.sView.getWidth()/logicW;

        this.scale = tempX <tempY ? tempX :tempY;

        this.offsetX = (this.sView.getWidth() -this.scale*logicW)/2;
        this.offsetY =(this.sView.getHeight() -this.scale*logicH)/2;

    }

    protected void clear(){
        this.canvas.drawColor(0xFFFFFFF);;
    }

    protected void endFrame(){
        this.holder.unlockCanvasAndPost(canvas);
    }

    public void createText(String text, float x, float y){
        this.canvas.drawText(text,x,y,this.paint);
    }

    /*public Image createImage(String path){
        Bitmap bm = null;

        try{
            bm = BitmapFactory.decodeStream(this.asset.open(path));
        }catch (IOException ex){
            throw new RuntimeException("Error reading "+ path, ex);
        }
        return new AndroidImage(bm);

    }*/
    @Override
    public int getWidth()
    {
        return this.sView.getWidth();
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
    public void pintarImagen(Image img, float x, float y) {

    }

    @Override
    public void setColor(int color) {
        this.paint.setColor(color);
    }

    @Override
    public void setFont(Font font) {

    }

    @Override
    public void escalar(float x, float y) {
        this.canvas.scale(x,y);
    }

    @Override
    public void trasladar(float x, float y) {
        this.canvas.translate(x,y);
    }

    @Override
    public void setLogicSize(float w, float h) {
        this.logicW =w;
        this.logicH =h;
    }

}
