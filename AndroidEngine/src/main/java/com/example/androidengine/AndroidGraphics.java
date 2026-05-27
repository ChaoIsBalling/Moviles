package com.example.androidengine;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Looper;
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
import android.view.PixelCopy;



/**
 * Clase que implementa el gestor de los gráficos en Android
 */
public class AndroidGraphics{
    AssetManager assetManager;
    private SurfaceHolder holder;
    private SurfaceView sView;
    private Paint paint;
    private Canvas canvas;

    //Directorios en los que se encuentran los assets a cargar
    private String imageDir="Images/";
    private String fontDir="Fonts/";


    private Thread renderThread;

    private float scale;

    private float offsetX;
    private float offsetY;

    private float logicH;
    private float logicW;
    private Context context;

    private String colorClean;

    private AndroidFont af;

    //private Asset asset;

    private boolean running;

    private final Rect rect = new Rect();


    /**
     * Constructora de la clase Graphics
     * @param view surface view donde se va a renderizar el juego
     */
    public AndroidGraphics(SurfaceView view){
        this.sView = view;
        this.holder = this.sView.getHolder();
        this.paint = new Paint();
        this.canvas = new Canvas();
        context= sView.getContext();
        assetManager= context.getAssets();
        this.colorClean="#FFFFFFFF";


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
        this.canvas.drawColor(Color.parseColor(this.colorClean));
    }

    /**
     * Metodo que setea que el color con el que se va a limpiar la pantalla
     * @param color nombre del color que queramos
     */
    public void setColorClear(String color){
        this.colorClean = color;
    }

    /**
     * Metodo que desbloque el canvas al final de un frame
     */
    protected void endFrame(){
        this.holder.unlockCanvasAndPost(canvas);
    }

    /**
     * Metodo que limita lo que se renderiza en pantalla al tamaño logico
     * @param left lado izquierdo
     * @param top lado de arriba
     * @param right lado derecho
     * @param botton lado de abajo
     */
    public void EmpezarLimiteDibujado(float left, float top, float right, float botton){
        this.canvas.save();
        this.canvas.clipRect(left, top, right, botton);
    }

    /**
     * Metodo que termina la limitacion de pintado en pantalla
     */
    public void TerminarLimiteDibujado(){
        this.canvas.restore();
    }


    /**
     * Metodo que copia los pixeles que hay en pantalla a un bitmap para crear una captura de pantalla
     */
    public Bitmap takeScreenshot()
    {
        final Bitmap[] bitmap = {Bitmap.createBitmap(sView.getWidth(), sView.getHeight(), Bitmap.Config.RGB_565)};
        PixelCopy.request(sView, bitmap[0],(copyResult)->{
            if (copyResult != PixelCopy.SUCCESS) {
                //Esto es para debuggear y ver si ha sido un exito
               bitmap[0] =newImage("asgore.png").getBitmap();
            }
        }, new Handler(Looper.getMainLooper()));
        return bitmap[0];
    }



    /**
     * Metodo que limpia y pinta el texto
     * @param text texto a pintar
     * @param x coordenada x
     * @param y coordenada y
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
     * @param x coordenada x
     * @param y coordenada y
     * @param r radio del circulo
     */
    public void pintarCirculo(float x, float y, float r) {
        this.paint.setStyle(Paint.Style.STROKE);
        this.canvas.drawCircle(x,y,r,this.paint);
        this.paint.setStyle(Paint.Style.FILL); //Devolvemos al valor por defecto
    }

    /**
     * Metodo que pinta un cuadrado con esquinas redondeadas
     * @param x coordenada x
     * @param y coordenada y
     * @param w ancho del boton
     * @param h alto del boton
     * @param ar radio de los bordes
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
     * @param x coordenada x
     * @param y coordenada y
     * @param w ancho del cuadrado
     * @param h alto del cuadrado
     */
    public void pintarCuadrado(float x, float y, float w, float h) {
        this.paint.setStyle(Paint.Style.STROKE);
        this.canvas.drawRect(x - w/2,y -h/2,x+w /2,y+h/2,this.paint);
        this.paint.setStyle(Paint.Style.FILL); //Devolvemos al valor por defecto
    }

    /**
     * Metodo que pinta un poligono
     * @param cx coordenada x del centro
     * @param cy coordenada y del centro
     * @param r radio del poligono
     * @param nv numero de vertices
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
     * @param x1 coordenada x del punto origen
     * @param y1 coordenada y del punto origen
     * @param x2 coordenada x del punto destino
     * @param y2 coordenada y del punto destino
     * @param width ancho de la linea
     */
    public void pintarLinea(float x1, float y1, float x2, float y2, float width) {
        float currWidth = this.paint.getStrokeWidth();
        this.paint.setStrokeWidth(width);
        this.canvas.drawLine(x1,y1,x2,y2,this.paint);
        this.paint.setStrokeWidth(0);

    }

    /**
     * Metodo que rellena de un color el fondo
     * @param color color con el que queremos pintar el fondo
     */
    public void pintarFondo(int color) {
        this.paint.setColor(color);
        this.paint.setStyle(Paint.Style.FILL);
        this.canvas.drawRect(0,0,this.canvas.getWidth(),this.canvas.getHeight(),this.paint);

    }

    /**
     * Metodo que pinta el texto
     * @param texto string con el texto a pintar
     * @param x coordenada x
     * @param y coordenada y
     */
    public void pintarTexto(String texto, float x, float y) {
        Paint.FontMetrics metrics= this.paint.getFontMetrics();
        Rect r= new Rect();
        this.paint.getTextBounds(texto,0,texto.length(),r);
        this.canvas.drawText(texto,x-r.width()/2,y+r.height()/2,this.paint);
    }

    /**
     * Metodo que pinta una imagen
     * @param img Imagen del motor de Android
     * @param x coordenada x
     * @param y coordenada y
     */
    public void pintarImagen(AndroidImage img, int x, int y) {
       AndroidImage image= (AndroidImage)img;
       canvas.drawBitmap(image.getBitmap(),x,y,null);
    }

    /**
     * Metodo que pinta una imagen con la escala que se pase como parametro
     * @param img Imagen del motor de Android
     * @param x coordenada x
     * @param y coordenada y
     * @param scaleX Escala del ancho de la imagen
     * @param scaleY Escala del alto de la imagen
     */
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
     * @param texto String con el texto a pintar
     * @param x coordenada x
     * @param y coordenada y
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
     * Metodo que setea el color con el que pinta el motor grafico
     * @param color Numero en hexadecimal del color
     */
    public void setColor(int color) {
        this.paint.setColor(color);
    }

    /**
     * Metodo que setea el color con el que pinta el motor grafico
     * @param color Nombre del color
     */
    public void setColor(String color) {
        this.paint.setColor(Color.parseColor(color));
    }

    /**
     * Metodo que crea una nueva imagen a partir de un archivo
     * @param f nombre del archivo de la imagen
     * @return Imagen del motor de Andorid
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
    }

    /**
     * Metodo que crea una nueva imagen a partir de un archivo
     * @param f nombre del archivo de la imagen
     * @param width ancho de la imagen
     * @param height alto de la imagen
     * @return Imagen del motor de Andorid
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
     * Setteo de la fuente con la que se escribe un texto
     * @param font Fuente de texto de Android
     */
    public void setFont(AndroidFont font) {
        this.af =(AndroidFont) font;
        this.paint.setTypeface(this.af.getTypeface());
    }

    /**
     * creacion de una nueva fuente con tamaño
     * @param f nombre de la fuente
     * @param size tamaño deseado
     * @return Fuente de texto de Android
     */
    public AndroidFont newFont(String f, float size) {
        return this.newFont(f, size, false, false);
    }

    /**
     * creacion de una nueva fuente en el motor
     * @param f nombre
     * @return
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
     * creacion de una nueva fuente en el motor con las propiedades que queramos
     * @param path ubicacion de la fuente/nombre en el directorio
     * @param size tamaño de la fuente
     * @param bold determina si esta en negrita
     * @param italic determina si es italica
     * @return Fuente de texto de Android
     */
    public AndroidFont newFont(String path, float size,boolean bold, boolean italic)
    {
        return new AndroidFont(this.assetManager,fontDir+path,size,bold,italic);
    }

    /**
     * Metodo de escalado del canvas
     * @param x cantidad a escalar en el eje x
     * @param y cantidad a escalar en el eje y
     */
    public void escalar(float x, float y) {
        this.canvas.scale(x,y);
    }

    /**
     * Metodo de traslación del canvas
     * @param x cantidad a trasladar en el eje x
     * @param y cantidad a trasladar en el eje y
     */
    public void trasladar(float x, float y) {
        this.canvas.translate(x,y);
    }

    /**
     * Metodo que setea el tamaño logico de la ventana
     * @param w ancho
     * @param h alto
     */
    public void setLogicSize(float w, float h) {
        this.logicW =w;
        this.logicH =h;
    }

    /**
     * Metodo que crea un circulo relleno
     * @param x coordenada x
     * @param y coordenada y
     * @param r radio del circulo
     */
    public void rellenarCirculo(float x, float y, float r) {
        this.paint.setStyle(Paint.Style.FILL);
        this.canvas.drawCircle(x,y,r,this.paint);
    }

    /**
     * Metodo que crea un cuadrado relleno
     * @param x coordenada x
     * @param y coordenada y
     * @param w ancho del cuadrado
     * @param h alto del cuadrado
     */
    public void rellenarCuadrado(float x, float y, float w, float h) {
        this.paint.setStyle(Paint.Style.FILL);
        this.canvas.drawRect(x - w/2,y -h/2,x+w /2,y+h/2,this.paint);
    }

    /**
     * Metodo que pinta un poligono relleno
     * @param cx coordenada x del centro
     * @param cy coordenada y del centro
     * @param r radio del poligono
     * @param nv numero de vertices
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
     * metodo que crea un hexagono relleno (con orientacion distinta)
     * @param cx coordenada x del centro
     * @param cy coordenada y del centro
     * @param r radio del hexagono
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

    /**
     * Devuelve una conversión de la coordenada X real a la coordenada X en logica
     * @param x coordenada x
     * @return coordenada parametro con la conversion hecha
     */
    public float real2LogicX(float x) {
        return (x - offsetX) / scale;
    }
    /**
     * Devuelve una conversión de la coordenada Y real a la coordenada Y en logica
     * @param y coordenada y
     * @return coordenada parametro con la conversion hecha
     */
    public float real2LogicY(float y) {
        return (y - offsetY) / scale;
    }
}
