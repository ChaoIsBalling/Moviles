package com.example.desktopengine;

import com.example.engine.Graphics;
import com.example.engine.State;
import com.example.engine.IFont;
import com.example.engine.IImage;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferStrategy;
import java.awt.BasicStroke;

import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;


import javax.imageio.ImageIO;
import javax.swing.JFrame;

/**
 * Clase que se encarga de el proceso de renderizado del motor, incluyendo el dibujo de figuras,
 * texto e imágenes sobre una ventana mediante Graphics2D y la estrategia del doble buffer.
 * Implementa Runnable (propia de Java) y la interfaz Graphics.
 */
public class DesktopGraphics implements Runnable, Graphics{
    //private BasicStroke stroke;
    /**
     * Ventana de gráficos
     */
    private JFrame myView;
    /**
     * Clase que se encarga de gestionar el doble buffer
     */
    private BufferStrategy bufferStrategy;
    /**
     * Instancia del gestor de gráficos del motor
     */
    private Graphics2D graphics2D;


    //private Thread renderThread;
    //private boolean running;
    //private State state;

    /**
     * Escala de la ventana
     */
    private float scale;

    /**
     * Offsets para gestionar la traslación
     */
    private float offsetX;
    private float offsetY;

    /**
     * Medidas lógicas del ancho y el alto de la ventana
     */
    private float logicH;
    private float logicW;

    /**
     * Márgenes del JFrame
     */
    private Insets insets;

    /**
     * Raiz de los datos a leer para pintar imagenes
     */
    String root = "data/";

    /**
     * Constructora del gestor de gráficos de Desktop
     * @param view Ventana de gráficos
     */
    public DesktopGraphics(JFrame view)
    {
        this.myView = view;
        this.bufferStrategy= this.myView.getBufferStrategy();
        this.graphics2D = (Graphics2D) bufferStrategy.getDrawGraphics();
        this.insets = myView.getInsets();
        myView.getContentPane().setBackground(Color.WHITE);
        myView.setSize(myView.getWidth()+ insets.left+ insets.right,
                myView.getHeight()+ insets.top+ insets.bottom);
    }

    /**
     * Metodo que gestiona el inicio de un nuevo frame, recalculando las escalas
     * y limpiando la pantalla
     */
    public void startFrame()
    {
        this.graphics2D=(Graphics2D) this.bufferStrategy.getDrawGraphics();

        calculateTransforms();

        //Llamamos a metodo que limpia la pantalla
        this.clear();
        
        this.trasladar(offsetX,offsetY);
        this.escalar(scale,scale);
    }

    /**
     * Metodo que calcula la escala y desplazamientos para centrar el area de juego en la ventana
     */
    private void calculateTransforms(){

        float tempX = (this.myView.getWidth()- insets.left- insets.right)/logicW;
        float tempY = (this.myView.getHeight()- insets.top- insets.bottom)/logicH;

        this.scale = tempX <tempY ? tempX :tempY;

        this.offsetX = this.insets.left + (this.myView.getWidth() - insets.left- insets.right-this.scale*logicW)/2;
        this.offsetY = this.insets.top + (this.myView.getHeight() - insets.top- insets.bottom-this.scale*logicH)/2;

    }

    /**
     * Metodos no implementados
     */
    protected void prepareFrame()
    {

    }
    protected  void endFrame()
    {

    }

    /**
     * Muestra el contenido del buffer
     * @return
     */
    public boolean swapBuffer()
    {
        this.graphics2D.dispose();
        this.graphics2D=null;
        if (this.bufferStrategy.contentsRestored()){
            return false;
        }
        this.bufferStrategy.show();
        return !this.bufferStrategy.contentsLost();
    }

    /**
     * Metodo que limpia la pantalla
     */

    @Override
    public void clear()
    {
        //this.graphics2D.setColor(new java.awt.Color(0xFFFFFFFF, true));
        this.setColor(0xFFFFFFF);
        this.graphics2D.clearRect(0,0,this.myView.getWidth(),this.myView.getHeight());
        //this.rellenarCuadrado(logicW/2,logicH/2,this.myView.getWidth(),this.myView.getHeight());
    }

    /**
     * Metodo que pinta el texto en una posición (x,y) de la ventana de escritorio
     * @param texto String que contiene el texto a escribir
     * @param x Posición x de escritorio
     * @param y Posición y de escritorio
     */
    public void pintarTexto(String texto, float x, float y)
    {
        FontMetrics metrics = this.graphics2D.getFontMetrics();
        this.graphics2D.drawString(texto,x,y + (metrics.getHeight() - metrics.getDescent()));
    }

    /**
     * Metodo que pinta una imagen en una posición (x,y) del escritorio
     * @param img Interfaz para imagenes del motor
     * @param x Posición x del escritorio
     * @param y Posición y del escritorio
     */
    @Override
    public void pintarImagen(IImage img, int x, int y) {
        DesktopImage imagen = (DesktopImage)img;
        this.graphics2D.drawImage(imagen.getCurrentImage(), x, y, null);

    }

    /**
     * Metodo que pinta el texto centrado (el calculo es diferente que en android)
     * en una posición (x,y) del escritorio
     * @param texto String que contiene el texto a escribir
     * @param x Posición x
     * @param y Posición y
     */
    @Override
    public void pintarTextoCentrado(String texto, float x, float y) {
        FontMetrics metrics = this.graphics2D.getFontMetrics();
        Rectangle2D rectangle2D = metrics.getStringBounds(texto,this.graphics2D);
        float xc = x-(float)rectangle2D.getWidth()/2;
        float yc = y + (metrics.getHeight()/2 - metrics.getDescent());
        this.graphics2D.drawString(texto,xc,yc);
    }

    /**
     * Metodo que lee y crea una imagen
     * @param path Nombre de la imagen
     * @param width ancho
     * @param height alto
     * @return Interfaz Image de Desktop
     */
    @Override
    public IImage newImage(String path, int width,int height)
    {
        java.awt.Image im =null;
        try
        {
            im = ImageIO.read(new File(root +"/Images/"+path));
        }
        catch(IOException io)
        {
            throw new RuntimeException("Error al leer imagen..."+path,io);
        }
        return new DesktopImage(im,width,height);
    }

    /**
     * Devuelve el ancho de la ventana(no se usa)
     * @return
     */
    @Override
    public int getWidth() {
        return 0;
    }

    /**
     * Pinta un circulo sin relleno en escritorio
     * @param x Posicion x
     * @param y Posicion y
     * @param r Radio del circulo
     */
    @Override
    public void pintarCirculo(float x, float y, float r)
    {
        this.graphics2D.drawOval((int)(x-r),(int)(y-r),(int)r*2,(int)r*2);
        this.graphics2D.setPaintMode();
    }

    /**
     * Dibuja un círculo con relleno en escritorio
     * @param x Posicion x
     * @param y Posicion y
     * @param r Radio del circulo
     */
    public void rellenarCirculo(float x, float y, float r)
    {
        this.graphics2D.fillOval((int)(x-r),(int)(y-r),(int)r*2,(int)r*2);
        this.graphics2D.setPaintMode();
    }

    /**
     * Pasa la coordenada x real a lógica en escritorio
     * @param x Coordenada x
     * @return coordenada x lógica
     */
    @Override
    public float real2LogicX(float x) { return (x - offsetX) / scale; }

    /**
     * Pasa la coordenada y real a lógica en escritorio
     * @param y Coordenada y
     * @return coordenada y lógica
     */
    @Override
    public float real2LogicY(float y) {
        return (y - offsetY) / scale;
    }

    /**
     * Pinta un cuadrado sin relleno en escritorio
     * @param x Posicion x
     * @param y Posicion y
     * @param w Ancho del cuadrado
     * @param h Alto del cuadrado
     */
    @Override
    public void pintarCuadrado(float x, float y, float w, float h)
    {
        this.graphics2D.drawRect((int)(x-w/2),(int)(y-h/2),(int)w,(int)h);
        this.graphics2D.setPaintMode();
    }

    /**
     * Pinta un poligono sin relleno en escritorio
     * @param cx Posicion x del centro
     * @param cy Posicion y del centro
     * @param r Radio del polígono
     * @param nv Número de vértices del poligono
     */
    @Override
    public void pintarPoligono(float cx, float cy, float r, int nv) {
        //Si el numero de vertices es menor a 3 no hacemos nada
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

        graphics2D.drawPolygon(coorX,coorY,nv);
    }

    /**
     * Pinta una linea en escritorio
     * @param x1 Posicion x del inicio de linea
     * @param y1 Posicion y del inicio de linea
     * @param x2 Posicion x del final de linea
     * @param y2 Posicion y del final o de linea
     * @param width Ancho de la linea
     */
    @Override
    public void pintarLinea(float x1, float y1, float x2, float y2, float width) {
        graphics2D.setStroke(new BasicStroke(width));
        graphics2D.drawLine((int)x1,(int)y1,(int)x2,(int)y2);
        graphics2D.setStroke(new BasicStroke(1));
        this.graphics2D.setPaintMode();
    }

    /**
     * Pinta un cuadrado con relleno en esccritorio
     * @param x Posicion x
     * @param y Posicion y
     * @param w Ancho del cuadrado
     * @param h Alto del cuadrado
     */
    @Override
    public void rellenarCuadrado(float x, float y, float w, float h)
    {
        this.graphics2D.fillRect((int)(x-w/2),(int)(y-h/2),(int)w,(int)h);
        this.graphics2D.setPaintMode();
    }

    /**
     * Pinta un cuadrado con relleno en escritorio con esquinas redondas
     * @param x Posicion x
     * @param y Posicion y
     * @param w Ancho del cuadrado
     * @param h Alto del cuadrado
     * @param ar Radio del borde
     */
    @Override
    public void rellenarCuadradoRedondeado(float x, float y, float w, float h, float ar) {
        this.graphics2D.fillRoundRect((int)(x-w/2),(int)(y-h/2),(int)w,(int)h, (int)ar, (int)ar);
        this.graphics2D.setPaintMode();
    }

    /**
     * Pinta un poligono regular con relleno en escritorio
     * @param cx Posicion x del centro
     * @param cy Posicion y del centro
     * @param r Radio del polígono
     * @param nv Número de vértices del poligono
     */
    @Override
    public void rellenarPoligono(float cx, float cy, float r, int nv) {
        //Si el numero de vertices es menor a 3 no hacemos nada
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

        graphics2D.fillPolygon(coorX,coorY,nv);
    }

    /**
     * Dibuja un hexagono con relleno en escritorio (aqui empieza en otro ángulo)
     * @param cx Posicion x del centro
     * @param cy Posicion y del centro
     * @param r Radio del hexágono
     */
    @Override
    public void rellenarHexagono(float cx, float cy, float r) {
        //Coordendadas de los vertices del poligono
        int [] coorX = new int[6];
        int [] coorY = new int[6];

        double angleStep = 2 * Math.PI / 6;

        //Para dibujar el hexagono, empezamos a dibujar en la esquina superior izquierda en vez de arriba
        for(int i = 0; i<6;i++){
            double angle = angleStep* i - (3/4)*Math.PI;
            int x = (int) (cx + r * Math.cos(angle));
            int y = (int) (cy + r * Math.sin(angle));
            coorX[i] = x; coorY[i]= y;
        }

        graphics2D.fillPolygon(coorX,coorY,6);
    }

    /**
     * Pinta un triangulo con relleno(no usado)
     */
    public void rellenarTriangulo(float x1,float y1,float x2,float y2, float x3, float y3)
     {
         this.graphics2D.fillPolygon(new int[]{(int)x1,(int)x2,(int)x3},
                 new int[]{(int)y1,(int)y2,(int)y3},3);
     }

    /**
     * Pinta un Rect con el tamaño del fondo y el color que le pasemos
     * @param color color para el fondo
     */
    public void pintarFondo(int color)
    {
        this.setColor(color);
        this.graphics2D.fillRect(0,0,this.myView.getWidth(),this.myView.getHeight());
    }

    /**
     * Asigna un color al motor de gráficos de Desktop
     * @param color color que queramos poner al motor
     */
    @Override
    public void setColor(int color)
    {
        this.graphics2D.setColor(new Color(color));
    }

    /**
     * Varias construtoras de Fonts para escritorio
     * @param f Nombre del font a crear
     * size Tamaño del Font
     * bold Determina si el Font va a estar en negrita
     * italic Determina si el font va a ser itálico
     * @return
     */
    public IFont newFont(String f) {

        DesktopFont font = null;
        try {
            font = new DesktopFont(root +"/Fonts/"+f);
        } catch (FontFormatException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return font;
    }

    @Override
    public IFont newFont(String f, float size) {
        DesktopFont font = null;
        try {
            font = new DesktopFont(root +"/fonts/"+f, size);
        } catch (FontFormatException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return font;
    }

    @Override
    public IFont newFont(String f, float size, boolean bold) {
        DesktopFont font = null;
        try {
            font = new DesktopFont(root +"/fonts/"+f, size, bold);
        } catch (FontFormatException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return font;
    }

    @Override
    public IFont newFont(String f, float size, boolean bold, boolean italic) {
        DesktopFont font = null;
        try {
            font = new DesktopFont(root +"/fonts/"+f, size, bold,italic);
        } catch (FontFormatException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return font;
    }

    /**
     * Crea una nueva imagen y devuelve la interfaz de Desktop
      * @param path Nombre de la imagen
     * @return Interfaz Imagen de Desktop
     */
    @Override
    public IImage newImage(String path) {
        java.awt.Image im =null;
        try
        {
            im = ImageIO.read(new File(root +"/images/"+path));
        }
        catch(IOException io)
        {
            throw new RuntimeException("Error al leer imagen..."+path,io);
        }
        return new DesktopImage(im);
    }

    /**
     * A partir de una interfaz Font, asigna al gestor de gráficos un Font con
     * el que escribir textos
     * @param font Interfaz de Font
     */
    @Override
    public void setFont(IFont font)
    {
        DesktopFont df = (DesktopFont) font;
        //Seteamos un font para que Graphics lo use cuando vaya a escribir
        this.graphics2D.setFont(df.getCurrentFont());
    }

    /**
     * Escala la superficie de dibujo
     * @param x Coordenada x
     * @param y Coordenada y
     */
    @Override
    public void escalar(float x, float y) {
        this.graphics2D.scale(x,y);
    }

    /**
     * Aplica un desplazamiento al área de dibujo
     * @param x Coordenada x
     * @param y Coordenada y
     */
    @Override
    public void trasladar(float x, float y) {
        this.graphics2D.translate(x,y);
    }

    /**
     * Define el tamaño lógico del área de renderizado
     * @param w Ancho
     * @param h Alto
     */
    @Override
    public void setLogicSize(float w, float h) {
        this.logicW = w;
        this.logicH = h;
    }

    /**
     * Bucle principal(nada implementado)
     */
    @Override
    public void run() {

    }
}
