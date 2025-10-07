package com.example.desktopengine;

import com.example.engine.Graphics;
import com.example.engine.State;
import com.example.engine.Font;
import com.example.engine.Image;

import java.awt.Color;
import java.awt.Polygon;
import java.io.IOException;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.BasicStroke;


import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.xml.stream.events.StartDocument;

public class DesktopGraphics implements Runnable, Graphics{

    String root = "data/";
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
    public DesktopGraphics(JFrame view)
    {
        this.myView=view;
        this.bufferStrategy= this.myView.getBufferStrategy();
        this.graphics2D=(Graphics2D) bufferStrategy.getDrawGraphics();
    }

    public void startFrame()
    {
        this.graphics2D=(Graphics2D) this.bufferStrategy.getDrawGraphics();
        this.clear();

        calculateTransforms();
        this.escalar(scale,scale);
        this.trasladar(offsetX,offsetY);
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
        if (this.bufferStrategy.contentsRestored())
            return false;
        this.bufferStrategy.show();
        return !this.bufferStrategy.contentsLost();
    }
    public void clear()
    {
        this.setColor(0xFFFFFFF);
        this.pintarCuadrado(0,0,this.myView.getWidth(),this.myView.getHeight());
    }
    public void pintarTexto(String texto, float x, float y)
    {
        this.graphics2D.drawString(texto,(int)x,(int)y);
    }

    @Override
    public void pintarImagen(Image img, float x, float y) {

    }


//    public Image createImage(String path)
//    {
//        java.awt.Image im =null;
//        try
//        {
//            im = ImageIO.read(new File(root+path));
//        }
//        catch(IOException io)
//        {
//            throw new RuntimeException("Error reading"+path,io);
//        }
//        return new DesktopImage(im);
//    }

    public void pintarImagen(Image image, int x, int y)
    {
        //java.awt.Image d=((DesktopImage)image).getImage();
        //this.graphics2D.drawImage(d,x,y);
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
    @Override
    public void setFont(Font font)
    {

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

    }

    @Override
    public void run() {

    }
}
