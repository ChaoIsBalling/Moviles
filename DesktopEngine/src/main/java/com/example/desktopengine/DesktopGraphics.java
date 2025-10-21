package com.example.desktopengine;

import com.example.engine.Graphics;
import com.example.engine.State;
import com.example.engine.IFont;
import com.example.engine.IImage;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferStrategy;
import java.awt.BasicStroke;

import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;


import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class DesktopGraphics implements Runnable, Graphics{
    private BasicStroke stroke;
    private JFrame myView;
    private BufferStrategy bufferStrategy;
    private Graphics2D graphics2D;

    private Thread renderThread;
    private boolean running;
    private State state;

    private float scale;
    private float offsetX;
    private float offsetY;

    private float logicH;
    private float logicW;

    String root = "data/";
    public DesktopGraphics(JFrame view)
    {
        this.myView = view;
        this.bufferStrategy= this.myView.getBufferStrategy();
        this.graphics2D = (Graphics2D) bufferStrategy.getDrawGraphics();

        //coger escalado de ventana aquí
    }

    public void startFrame()
    {
        this.graphics2D=(Graphics2D) this.bufferStrategy.getDrawGraphics();
        this.clear(); //Llamamos a metodo que limpia la pantalla

        calculateTransforms();

        this.trasladar(offsetX,offsetY);
        this.escalar(scale,scale);
    }

    private void calculateTransforms(){
        float tempY = this.myView.getHeight()/logicH;
        float tempX = this.myView.getWidth()/logicW;

        this.scale = tempX <tempY ? tempX :tempY;

        this.offsetX = (this.myView.getWidth() -this.scale*logicW)/2;
        this.offsetY =(this.myView.getHeight() -this.scale*logicH)/2;

    }
    protected void prepareFrame()
    {

    }
    protected  void endFrame()
    {

    }
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

    //Método que limpia la pantalla
    public void clear()
    {
        //this.graphics2D.setColor(new java.awt.Color(0xFFFFFFFF, true));
        this.setColor(0xFFFFFFF);
        this.rellenarCuadrado(0,0,this.myView.getWidth(),this.myView.getHeight());
    }
    public void pintarTexto(String texto, float x, float y)
    {
        FontMetrics metrics = this.graphics2D.getFontMetrics();
        this.graphics2D.drawString(texto,x,y + (metrics.getHeight() - metrics.getDescent()));
    }

    @Override
    public void pintarImagen(IImage img, int x, int y) {
        DesktopImage imagen = (DesktopImage)img;
        this.graphics2D.drawImage(imagen.getCurrentImage(), x, y, null);
    }

    @Override
    public void pintarTextoCentrado(String texto, float x, float y) {
        FontMetrics metrics = this.graphics2D.getFontMetrics();
        Rectangle2D rectangle2D = metrics.getStringBounds(texto,this.graphics2D);
        float xc = x-(float)rectangle2D.getWidth()/2;
        float yc = y + (metrics.getHeight()/2 - metrics.getDescent());
        this.graphics2D.drawString(texto,xc,yc);
    }

    @Override
    public IImage newImage(String path)
    {
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

    @Override
    public int getWidth() {
        return 0;
    }

    @Override
    public void pintarCirculo(float x, float y, float r)
    {
        this.graphics2D.drawOval((int)(x-r),(int)(y-r),(int)r*2,(int)r*2);
        this.graphics2D.setPaintMode();
    }

    public void rellenarCirculo(float x, float y, float r)
    {
        this.graphics2D.fillOval((int)(x-r),(int)(y-r),(int)r*2,(int)r*2);
        this.graphics2D.setPaintMode();
    }

    @Override
    public float real2LogicX(float x) { return (x - offsetX) / scale; }

    @Override
    public float real2LogicY(float y) {
        return (y - offsetY) / scale;
    }

    @Override
    public void pintarCuadrado(float x, float y, float w, float h)
    {
        this.graphics2D.drawRect((int)x,(int)y,(int)w,(int)h);
        this.graphics2D.setPaintMode();
    }

    public void rellenarCuadrado(float x, float y, float w, float h)
    {
        this.graphics2D.fillRect((int)x,(int)y,(int)w,(int)h);
        this.graphics2D.setPaintMode();
    }

     public void rellanarTriangulo(float x1,float y1,float x2,float y2, float x3, float y3)
     {
         this.graphics2D.fillPolygon(new int[]{(int)x1,(int)x2,(int)x3},
                 new int[]{(int)y1,(int)y2,(int)y3},3);
     }
    public void pintarFondo(int color)
    {
        this.setColor(color);
        this.graphics2D.fillRect(0,0,this.myView.getWidth(),this.myView.getHeight());
    }
    @Override
    public void setColor(int color)
    {
        this.graphics2D.setColor(new Color(color));
    }

    public IFont newFont(String f) {

        DesktopFont font = null;
        try {
            font = new DesktopFont(root +"/fonts/"+f);
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

    @Override
    public void setFont(IFont font)
    {
        DesktopFont df = (DesktopFont) font;
        //Seteamos un font para que Graphics lo use cuando vaya a escribir
        this.graphics2D.setFont(df.getCurrentFont());
    }

    @Override
    public void escalar(float x, float y) {
        this.graphics2D.scale(x,y);
    }

    @Override
    public void trasladar(float x, float y) {
        this.graphics2D.translate(x,y);
    }

    @Override
    public void setLogicSize(float w, float h) {
        this.logicW = w;
        this.logicH = h;
    }

    @Override
    public void run() {

    }
}
