package com.example.androidengine;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

import com.example.engine.IFont;
import com.example.engine.Graphics;
import com.example.engine.IImage;
import android.graphics.Bitmap;

import java.io.IOException;
import java.io.InputStream;
import android.graphics.BitmapFactory;
import android.content.res.AssetManager;
import android.content.Context;
public class AndroidGraphics implements Graphics {
    AssetManager assetManager;
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
    private Context context;
    String root = "data/";
    //private Asset asset;

    private boolean running;

    private final Rect rect = new Rect();


    public AndroidGraphics(SurfaceView view){
        this.sView = view;
        this.holder = this.sView.getHolder();
        this.paint = new Paint();
        this.canvas = new Canvas();
        context= sView.getContext();
        assetManager= context.getAssets();


        scale =1;
        offsetX=0;
        offsetY=0;
        logicH=600;
        logicW=400;
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
        this.paint.setColor(Color.MAGENTA);
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
        Paint.FontMetrics metrics= this.paint.getFontMetrics();
        Rect r= new Rect();
        this.paint.getTextBounds(texto,0,texto.length(),r);
        this.canvas.drawText(texto,x-r.width()/2,y+r.height()/2,this.paint);
    }


    @Override
    public void pintarImagen(IImage img, int x, int y) {
       AndroidImage image= (AndroidImage)img;
       canvas.drawBitmap(image.getBitmap(),x,y,null);
    }

    @Override
    public void pintarTextoCentrado(String texto, float x, float y) {

    }

    @Override
    public void setColor(int color) {
        this.paint.setColor(color);
    }

    @Override
    public IFont newFont(String f) {
        return null;
    }

    @Override
    public IFont newFont(String f, float size) {
        return null;
    }

    @Override
    public IFont newFont(String f, float size, boolean bold) {
        return null;
    }

    @Override
    public IFont newFont(String f, float size, boolean bold, boolean italic) {
        return null;
    }

    @Override
    public IImage newImage(String f) {
        InputStream is = null;
        try {
            is = assetManager.open(f);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Bitmap bitmap = BitmapFactory.decodeStream(is);
        return new AndroidImage(bitmap);
    }

    @Override
    public void setFont(IFont font) {
    

    }
    public IFont createFont(String path,float size)
    {
        return this.createFont(path,size,false,false);
    }
    public IFont createFont(String path, float size,boolean bold )
    {
        return this.createFont(path,size,bold,false);
    }
    public IFont createFont(String path, float size,boolean bold, boolean italic )
    {
        return new AndroidFont(this.assetManager,path,size,bold,italic);
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

    @Override
    public void rellenarCirculo(float x, float y, float r) {
        this.paint.setColor(Color.MAGENTA);
        this.paint.setStyle(Paint.Style.FILL);
        this.canvas.drawCircle(x,y,r,this.paint);
    }

    @Override
    public void rellenarCuadrado(float x, float y, float w, float h) {
    }

    @Override
    public float real2LogicX(float x) {
        return (x + this.scale*this.logicW);
    }

    @Override
    public float real2LogicY(float y) {
        return (y + this.scale*this.logicH);
    }

}
