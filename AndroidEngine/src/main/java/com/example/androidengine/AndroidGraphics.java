package com.example.androidengine;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

import android.graphics.Bitmap;

import java.io.IOException;
import java.io.InputStream;
import android.graphics.BitmapFactory;
import android.content.res.AssetManager;
import android.content.Context;
import android.graphics.Path;
import android.graphics.PorterDuff.Mode;

public class AndroidGraphics{
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
    /**
     * Metodo que gestiona el inicio de un nuevo frame, recalculando las escalas
     * y limpiando la pantalla
     */
    protected void startFrame(){
        while(!this.holder.getSurface().isValid());
        this.canvas = this.holder.lockHardwareCanvas();
        calculateTransforms();
        this.clear();
        this.trasladar(this.offsetX,this.offsetY);
        this.escalar(this.scale,this.scale);


    }
    /**
     * Metodo que calcula la escala y desplazamientos para centrar el area de juego en la ventana
     */
    private void calculateTransforms(){
        float tempY = this.sView.getHeight()/logicH;
        float tempX = this.sView.getWidth()/logicW;

        this.scale = tempX <tempY ? tempX :tempY;

        this.offsetX = (this.sView.getWidth() -this.scale*logicW)/2;
        this.offsetY =(this.sView.getHeight() -this.scale*logicH)/2;

    }
    /**
     * Metodo que limpia la pantalla
     */
    public void clear(){
        this.canvas.drawColor(Color.TRANSPARENT, Mode.CLEAR);
        this.canvas.drawColor(Color.WHITE);
    }
    /**
     * Metodo que desbloque el canvas
     */
    protected void endFrame(){
        this.holder.unlockCanvasAndPost(canvas);
    }
    /**
     * Metodo que limpia y pinta el texto
     */
    public void createText(String text, float x, float y){
        this.canvas.drawText(text,x,y,this.paint);
    }
    /**
     * Metodo que devuelve el ancho del juego
     */

    public int getWidth()
    {
        return this.sView.getWidth();
    }
    /**
     * Metodo que pinta un circulo
     */

    public void pintarCirculo(float x, float y, float r) {
        this.paint.setStyle(Paint.Style.STROKE);
        this.canvas.drawCircle(x,y,r,this.paint);
        this.paint.setStyle(Paint.Style.FILL); //Devolvemos al valor por defecto
    }
    /**
     * Metodo que pinta un cuadrado con esquinas redondeadas
     */
    public void rellenarCuadradoRedondeado(float x, float y, float w, float h, float ar)
    {
        this.paint.setStyle(Paint.Style.FILL);
        RectF r = new RectF(x - w/2,y - h/2,x+w/2,y+h/2);
        this.canvas.drawRoundRect(r,ar/2,ar/2,this.paint);//El radio que le pasamos está dividido a la mitad
                                                                 //para tener un aspecto más consistente con desktop
    }
    /**
     * Metodo que pinta un cuadrado
     */
    public void pintarCuadrado(float x, float y, float w, float h) {
        this.paint.setStyle(Paint.Style.STROKE);
        this.canvas.drawRect(x - w/2,y -h/2,x+w /2,y+h/2,this.paint);
        this.paint.setStyle(Paint.Style.FILL); //Devolvemos al valor por defecto
    }
    /**
     * Metodo que pinta un poligono
     */
    public void pintarPoligono(float cx, float cy, float r, int nv) {
        this.paint.setStyle(Paint.Style.STROKE);
        if(nv<3)
            return;

        //Coordendadas de los vertices del poligono
        int [] coorX = new int[nv];
        int [] coorY = new int[nv];

        double angleStep = 2 * Math.PI / nv;
        //posicionamiento de los puntos del poligono
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
        this.paint.setStyle(Paint.Style.FILL); //Devolvemos al valor por defecto
    }
    /**
     * Metodo que pinta una linea
     */
    public void pintarLinea(float x1, float y1, float x2, float y2, float width) {
        float currWidth = this.paint.getStrokeWidth();
        this.paint.setStrokeWidth(width);
        this.canvas.drawLine(x1,y1,x2,y2,this.paint);
        this.paint.setStrokeWidth(0);

    }
    /**
     * Metodo que rellena el fondo
     */
    public void pintarFondo(int color) {
        this.paint.setColor(color);
        this.paint.setStyle(Paint.Style.FILL);
        this.canvas.drawRect(0,0,this.canvas.getWidth(),this.canvas.getHeight(),this.paint);

    }
    /**
     * Metodo que pinta el texto
     */
    public void pintarTexto(String texto, float x, float y) {
        Paint.FontMetrics metrics= this.paint.getFontMetrics();
        Rect r= new Rect();
        this.paint.getTextBounds(texto,0,texto.length(),r);
        this.canvas.drawText(texto,x-r.width()/2,y+r.height()/2,this.paint);
    }

    /**
     * Metodo que pinta una imagen
     */
    public void pintarImagen(AndroidImage img, int x, int y) {
       AndroidImage image= (AndroidImage)img;
       canvas.drawBitmap(image.getBitmap(),x,y,null);
    }

    public void pintarImagenEscalada(AndroidImage img, int x, int y, int scaleX, int scaleY) {
        if(img instanceof AndroidImage){
            AndroidImage aImage = (AndroidImage) img; //casteamos a andorid Image
            Bitmap bm = aImage.getBitmap(); //obtenemosBitmap

            //Hacemos un nuevo bitmap escalado, por si habia que actualizar scaleX,scaleY
            Bitmap scaled = Bitmap.createScaledBitmap(bm, scaleX, scaleY, false);

            //El canvas dibuja el bitmap
            canvas.drawBitmap(scaled, (int)x, (int)y,this.paint);

        }
    }

    /**
     * Metodo que pinta un texto centrado
     */
    public void pintarTextoCentrado(String texto, float x, float y) {
        Rect r= new Rect();
        this.paint.setTextSize(af.getSize());
        this.paint.getTextBounds(texto,0,texto.length(),r);
        Paint.FontMetrics fm = this.paint.getFontMetrics();
        //calculo de posicion centrada del texto respecto a la x e y que tenemos
        float xPos = x - (paint.measureText(texto)/2);
        float yPos =  (y - ((fm.ascent+fm.descent)/2- fm.leading));
        this.canvas.drawText(texto,xPos,yPos,this.paint);
    }
    /**
     * seteo del color de la pintura
     */
    public void setColor(int color) {
        this.paint.setColor(color);
    }

    public void setColor(String color) {
        this.paint.setColor(Color.parseColor(color));
    }

    /**
     * creacion de una nueva pintura
     */
    public AndroidImage newImage(String f) {
        InputStream is = null;
        try {
            is = assetManager.open(imageDir+f);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Bitmap bitmap = BitmapFactory.decodeStream(is);
        return new AndroidImage(bitmap);
    }  /**
     * creacion de una nueva pintura con ancho y altura
     */
    public AndroidImage newImage(String f,int width, int height) {
        InputStream is = null;
        try {
            is = assetManager.open(imageDir+f);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Bitmap bitmap = BitmapFactory.decodeStream(is);
        return new AndroidImage(bitmap,width,height);
    }
    /**
     * seteo de una fuente
     */
    public void setFont(AndroidFont font) {
        this.af =(AndroidFont) font;
        this.paint.setTypeface(this.af.getTypeface());
    }
    /**
     * creacion de una nueva fuente con tamaño
     */
    public AndroidFont newFont(String f, float size) {
        return this.newFont(f, size, false, false);
    }
    /**
     * creacion de una nueva fuente
     */

    public AndroidFont newFont(String f) {
        return this.newFont(f,10, false, false);
    }
    /**
     * creacion de una nueva fuente con tamaño y negrita
     */

    public AndroidFont newFont(String path, float size,boolean bold )
    {
        return this.newFont(path,size,bold,false);
    }
    /**
     * creacion de una nueva fuente con tamaño , negrita e italics
     */

    public AndroidFont newFont(String path, float size,boolean bold, boolean italic )
    {
        return new AndroidFont(this.assetManager,fontDir+path,size,bold,italic);
    }
    /**
     * escalado del canvas
     */

    public void escalar(float x, float y) {
        this.canvas.scale(x,y);
    }
    /**
     * traslado del canvas
     */

    public void trasladar(float x, float y) {
        this.canvas.translate(x,y);
    }
    /**
     * seteo de los tamaños logicos
     */

    public void setLogicSize(float w, float h) {
        this.logicW =w;
        this.logicH =h;
    }
    /**
     * metodo que crea un circulo relleno
     */

    public void rellenarCirculo(float x, float y, float r) {
        this.paint.setStyle(Paint.Style.FILL);
        this.canvas.drawCircle(x,y,r,this.paint);
    }
    /**
     * metodo que crea un cuadrado relleno
     */

    public void rellenarCuadrado(float x, float y, float w, float h) {
        this.paint.setStyle(Paint.Style.FILL);
        this.canvas.drawRect(x - w/2,y -h/2,x+w /2,y+h/2,this.paint);
    }
    /**
     * metodo que crea un poligono relleno
     */

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
    /**
     * metodo que crea un hexagono relleno
     */

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
    //devuelve una conversión de la coordenada X real a la coordenada X en logica

    public float real2LogicX(float x) {
        return (x - offsetX) / scale;
    }
    //devuelve una conversión de la coordenada Y real a la coordenada Y en logica

    public float real2LogicY(float y) {
        return (y - offsetY) / scale;
    }

}
