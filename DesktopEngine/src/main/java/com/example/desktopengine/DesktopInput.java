package com.example.desktopengine;
import com.example.engine.Input;
import com.example.engine.TouchEvent;

import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.awt.event.MouseEvent;
import java.util.List;

public class DesktopInput implements Input, MouseListener {

    List<TouchEvent> events;

    public DesktopInput(){
        events=new ArrayList<TouchEvent>();
    }
    @Override
    public void mouseClicked(MouseEvent mouseEvent)
    {

    }
    @Override
    public void mousePressed(MouseEvent mouseEvent)
    {
        if(mouseEvent.getButton()== mouseEvent.BUTTON1) {
            TouchEvent event = new TouchEvent();
            event.type= TouchEvent.TouchEventType.TOUCH_DOWN;
            event.x = mouseEvent.getX();
            event.y = mouseEvent.getY(); //Las coordenadas son de la ventana!
            event.finger = 0;

            this.events.add(event);
        }
    }

    @Override
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
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }

    @Override
    public List<TouchEvent> getTouchEvents() {
        return this.events;
    }
}
