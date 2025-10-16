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
<<<<<<< HEAD
    public void mousePressed(MouseEvent mouseEvent)
    {
        if(mouseEvent.getButton()== mouseEvent.BUTTON1) {
=======
    public void mousePressed(MouseEvent mouseEvent) {
        if(mouseEvent.getButton() == MouseEvent.BUTTON1 || mouseEvent.getButton() == MouseEvent.BUTTON2){
>>>>>>> parent of 5c1a549 (Merge branch 'main' of https://github.com/ChaoIsBalling/Moviles)
            TouchEvent event = new TouchEvent();
            event.x = mouseEvent.getX();
<<<<<<< HEAD
            event.y = mouseEvent.getY(); //Las coordenadas son de la ventana!
            event.finger = 0;
=======
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
>>>>>>> parent of 5c1a549 (Merge branch 'main' of https://github.com/ChaoIsBalling/Moviles)

            this.events.add(event);
        }
    }

    @Override
<<<<<<< HEAD
    public void mouseReleased(MouseEvent mouseEvent) {
        if(mouseEvent.getButton()== mouseEvent.BUTTON1){
            TouchEvent event = new TouchEvent();
            event.type= TouchEvent.TouchEventType.TOUCH_UP;
            event.x = mouseEvent.getX(); //La x va a ser la de la ventana NO la del juego
            event.y = mouseEvent.getY();
            event.finger = 0;
            this.events.add(event); //Añado a la lista de eventos
        }
    }

    @Override
=======
>>>>>>> parent of 5c1a549 (Merge branch 'main' of https://github.com/ChaoIsBalling/Moviles)
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }
<<<<<<< HEAD

    @Override
    public List<TouchEvent> getTouchEvents() {
        return this.events;
    }
=======
>>>>>>> parent of 5c1a549 (Merge branch 'main' of https://github.com/ChaoIsBalling/Moviles)
}
