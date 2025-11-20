package com.example.androidengine;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.fonts.Font;
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
import android.graphics.Path;
import android.graphics.PorterDuff.Mode;

public class AndroidGraphics implements Graphics {
    AssetManager assetManager;
    private SurfaceHolder holder;
    private SurfaceView sView;
    private Paint paint;
    private Canvas canvas;

    private String imageDir="Images/";
    private String fontDir="Fonts/";


    private Thread renderThread;

    private float scale;

    private float offsetX;
    private float offsetY;

    private float logicH;
    private float logicW;
    private Context context;

    private AndroidFont af;

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


        scale =0;
        offsetX=0;
        offsetY=0;
        logicH=400;
        logicW=600;
    }

    protected void startFrame(){
        while(!this.holder.getSurface().isValid());
        this.canvas = this.holder.lockHardwareCanvas();


        calculateTransforms();
        this.clear();
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

    @Override
    public void clear(){
        this.canvas.drawColor(Color.TRANSPARENT, Mode.CLEAR);
        this.canvas.drawColor(Color.WHITE);
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
        this.paint.setStyle(Paint.Style.STROKE);
        this.canvas.drawCircle(x,y,r,this.paint);
    }
    @Override
    public void rellenarCuadradoRedondeado(float x, float y, float w, float h, float ar)
    {
        this.paint.setStyle(Paint.Style.FILL);
        RectF r = new RectF(x - w/2,y - h/2,x+w/2,y+h/2);
        this.canvas.drawRoundRect(r,x,y,this.paint);
    }
    @Override
    public void pintarCuadrado(float x, float y, float w, float h) {
        this.paint.setStyle(Paint.Style.STROKE);
        this.canvas.drawRect(x - w/2,y -h/2,x+w /2,y+h/2,this.paint);
    }

    @Override
    public void pintarPoligono(float cx, float cy, float r, int nv) {
        this.paint.setStyle(Paint.Style.STROKE);
        if(nv<3)
            return;

        //Coordendadas de los vertices del poligono
        int [] coorX = new int[nv];
        int [] coorY = new int[nv];

        double angleStep = 2 * Math.PI / nv;

        for(int i = 0; i<nv;i++){
            double angle = angleStep* i - Math.PI /2;
            int x = (int) (cx + r * Math.cos(angle));
            int y = (int) (cy + r * Math.sin(angle));
            coorX[i] = x; coorY[i]= y;
        }

        Path wallpath = new Path();
        wallpath.reset();
        wallpath.moveTo(coorX[0], coorY[0]);//Primer punto
        for(int i = 1; i<nv;i++) {
            wallpath.lineTo(coorX[i], coorY[i]);
        }
        wallpath.lineTo(coorX[0], coorY[0]);//Volvemos al primer punto
        this.canvas.drawPath(wallpath, this.paint);
    }

    @Override
    public void pintarLinea(float x1, float y1, float x2, float y2, float width) {
        float currWidth = this.paint.getStrokeWidth();
        this.paint.setStrokeWidth(width);
        this.canvas.drawLine(x1,y1,x2,y2,this.paint);
        this.paint.setStrokeWidth(0);

    }

    @Override
    public void pintarFondo(int color) {
        this.paint.setColor(color);
        this.paint.setStyle(Paint.Style.FILL);
        this.canvas.drawRect(0,0,this.canvas.getWidth(),this.canvas.getHeight(),this.paint);

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
        Paint.FontMetrics metrics= this.paint.getFontMetrics();
        Rect r= new Rect();
        this.paint.setTextSize(af.getSize());
        this.paint.getTextBounds(texto,0,texto.length(),r);
        float xc= x-r.width()/2;
        float yc= y+r.height()/2- metrics.descent;
        this.canvas.drawText(texto,xc,yc,this.paint);
    }

    @Override
    public void setColor(int color) {
        this.paint.setColor(color);
    }
    @Override
    public IImage newImage(String f) {
        InputStream is = null;
        try {
            is = assetManager.open(imageDir+f);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Bitmap bitmap = BitmapFactory.decodeStream(is);
        return new AndroidImage(bitmap);
    }
    @Override
    public IImage newImage(String f,int width, int height) {
        InputStream is = null;
        try {
            is = assetManager.open(imageDir+f);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Bitmap bitmap = BitmapFactory.decodeStream(is);
        return new AndroidImage(bitmap,width,height);
    }

    @Override
    public void setFont(IFont font) {
        this.af =(AndroidFont) font;
        this.paint.setTypeface(this.af.getTypeface());
    }

    @Override
    public IFont newFont(String f, float size) {
        return this.newFont(f, size, false, false);
    }
    @Override
    public IFont newFont(String f) {
        return this.newFont(f,10, false, false);
    }
    @Override
    public IFont newFont(String path, float size,boolean bold )
    {
        return this.newFont(path,size,bold,false);
    }
    @Override
    public IFont newFont(String path, float size,boolean bold, boolean italic )
    {
        return new AndroidFont(this.assetManager,fontDir+path,size,bold,italic);
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
        this.paint.setStyle(Paint.Style.FILL);
        this.canvas.drawCircle(x,y,r,this.paint);
    }

    @Override
    public void rellenarCuadrado(float x, float y, float w, float h) {
        this.paint.setStyle(Paint.Style.FILL);
        this.canvas.drawRect(x - w/2,y -h/2,x+w /2,y+h/2,this.paint);
    }

    @Override
    public void rellenarPoligono(float cx, float cy, float r, int nv) {
        this.paint.setStyle(Paint.Style.FILL);
        if(nv<3)
            return;

        //Coordendadas de los vertices del poligono
        int [] coorX = new int[nv];
        int [] coorY = new int[nv];

        double angleStep = 2 * Math.PI / nv;


        for(int i = 0; i<nv;i++){
            double angle = angleStep* i - Math.PI /2;
            int x = (int) (cx + r * Math.cos(angle));
            int y = (int) (cy + r * Math.sin(angle));
            coorX[i] = x; coorY[i]= y;
        }
        Path wallpath = new Path();
        wallpath.reset();
        wallpath.moveTo(coorX[0], coorY[0]);//Primer punto
        for(int i = 1; i<nv;i++) {
            wallpath.lineTo(coorX[i], coorY[i]);
        }
        wallpath.lineTo(coorX[0], coorY[0]);//Volvemos al primer punto
        this.canvas.drawPath(wallpath, this.paint);

    }

    @Override
    public void rellenarHexagono(float cx, float cy, float r) {
        this.paint.setStyle(Paint.Style.FILL);

        //Coordendadas de los vertices del poligono
        int [] coorX = new int[6];
        int [] coorY = new int[6];

        double angleStep = 2 * Math.PI / 6;

        //Para dibujar el hexagono, empezamos a dibujar en la esquina superior izquierda en vez de arriba
        for(int i = 0; i<6;i++){
            double angle = angleStep* i - (3/4) * Math.PI ;
            int x = (int) (cx + r * Math.cos(angle));
            int y = (int) (cy + r * Math.sin(angle));
            coorX[i] = x; coorY[i]= y;
        }
        Path wallpath = new Path();
        wallpath.reset();
        wallpath.moveTo(coorX[0], coorY[0]);//Primer punto
        for(int i = 1; i<6;i++) {
            wallpath.lineTo(coorX[i], coorY[i]);
        }
        wallpath.lineTo(coorX[0], coorY[0]);//Volvemos al primer punto
        this.canvas.drawPath(wallpath, this.paint);
    }

    @Override
    public float real2LogicX(float x) {
        return (x - offsetX) / scale;
    }

    @Override
    public float real2LogicY(float y) {
        return (y - offsetY) / scale;
    }

}
