package com.example.desktopengine;

import com.example.engine.Graphics;
import com.example.engine.State;
import com.example.engine.Font;
import com.example.engine.Image;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;
import javax.xml.stream.events.StartDocument;

public class DesktopGraphics implements Runnable, Graphics{
    private JFrame myView;
    private BufferStrategy bufferStrategy;
    private java.awt.Graphics graphics2D;

    private Thread renderThread;
    private boolean running;
    private State state;
    DesktopGraphics(JFrame view)
    {
        this.myView=view;
        this.bufferStrategy= this.myView.getBufferStrategy();
        this.graphics2D=(Graphics2D) bufferStrategy.getDrawGraphics();
    }

    public void startFrame()
    {
        this.graphics2D=(Graphics2D) this.bufferStrategy.getDrawGraphics();
        this.clear();
    }
    protected void prepareFrame()
    {

    }
    protected  void endFrame()
    {

    }
    public void Run()
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
    @Override
    public void clear()
    {
        this.setColor(0xFFFFFFF);
        this.pintarCuadrado(0,0,this.myView.getWidth(),this.myView.getHeight());
    }
    @Override
    public void pintarCirculo(float x, float y, float r)
    {
        this.graphics2D.fillOval((int)x,(int)y,(int)r,(int)r);
    }
    @Override
    public void pintarCuadrado(float x, float y, float w, float h)
    {

    }
    public void pintarFondo(int color)
    {
        this.setColor(color);
        this.graphics2D.fillRect(0,0,this.myView.getWidth(),this.myView.getHeight());
    }
    @Override
    public void setColor(int color)
    {

    }
    @Override
    public void setFont(Font font)
    {

    }
}
