package com.example.desktopengine;

import com.example.engine.Input;
import com.example.engine.TouchEvent;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

public class DesktopInput implements Input, MouseListener {
    ArrayList<TouchEvent> events;
    ArrayList<TouchEvent> pendingEvents;
    public DesktopInput(){
        events = new ArrayList<TouchEvent>();
    }
    @Override
    public synchronized List<TouchEvent> getTouchEvents() {
        this.events.clear();
        this.events.addAll(this.pendingEvents);
        this.pendingEvents.clear();
        return this.events;
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {

    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        if(mouseEvent.getButton() == MouseEvent.BUTTON1 || mouseEvent.getButton() == MouseEvent.BUTTON2){
            TouchEvent event = new TouchEvent();
            event.x = mouseEvent.getX();
            event.y = mouseEvent.getY();
            event.finger =0;
            event.type= TouchEvent.TouchEventType.TOUCH_DOWN;

            synchronized (this){
                this.pendingEvents.add(event);
            }

        }

    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        if(mouseEvent.getButton() == MouseEvent.BUTTON1 || mouseEvent.getButton() == MouseEvent.BUTTON2){
            TouchEvent event = new TouchEvent();
            event.x = mouseEvent.getX();
            event.y = mouseEvent.getY();
            event.finger =0;
            event.type= TouchEvent.TouchEventType.TOUCH_UP;

            this.events.add(event);
        }
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }
}
