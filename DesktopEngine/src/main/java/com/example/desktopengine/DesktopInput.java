package com.example.desktopengine;

import com.example.engine.Input;
import com.example.engine.TouchEvent;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

/**
 * Clase que implementa la interfaz Input de Engine y MouseListener (Java)
 * para la gestión de eventos de ratón
 */
public class DesktopInput implements Input, MouseListener {
    /**
     * Lista de eventos de ratón actuales
     */
    ArrayList<TouchEvent> events;
    /**
     * Lista de eventos pendientes
     */
    ArrayList<TouchEvent> pendingEvents;

    /**
     * Constructora que inicializa los arrays de eventos de entradas
     */
    public DesktopInput(){
        events = new ArrayList<TouchEvent>();
        pendingEvents = new ArrayList<TouchEvent>();
    }

    /**
     * Añade los eventos pendientes a la lista de eventos actaules y limpia la lista de pendientes
     * @return Eventos pendientes
     */
    @Override
    public synchronized ArrayList<TouchEvent> getTouchEvents() {
        this.events.addAll(this.pendingEvents);
        this.pendingEvents.clear();
        return this.events;
    }

    /**
     * Metodo que se llama cuando se clica el ratón
     * @param mouseEvent
     */
    @Override
    public void mouseClicked(MouseEvent mouseEvent) {

    }

    /**
     * crea y añade un evento de tipo TOUCH_DOWN cuando se presiona un botón del ratón.
     * @param mouseEvent evento de raton detectado
     */
    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        if(mouseEvent.getButton() == MouseEvent.BUTTON1 || mouseEvent.getButton() == MouseEvent.BUTTON2){

            TouchEvent event = new TouchEvent();
            event.x = mouseEvent.getX();

            event.y = mouseEvent.getY(); //Las coordenadas son de la ventana!
            event.finger = 0;
            event.type= TouchEvent.TouchEventType.TOUCH_DOWN;

            synchronized (this){
                this.pendingEvents.add(event);
            }

        }

    }

    /**
     * crea y añade un evento de tipo TOUCH_UP cuando se suelta un botón del ratón.
     * @param mouseEvent evento de raton detectado
     */
    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        if(mouseEvent.getButton() == MouseEvent.BUTTON1 || mouseEvent.getButton() == MouseEvent.BUTTON2){
            TouchEvent event = new TouchEvent();
            event.x = mouseEvent.getX();
            event.y = mouseEvent.getY();
            event.finger =0;
            event.type= TouchEvent.TouchEventType.TOUCH_UP;
            synchronized (this){
                this.pendingEvents.add(event);
            }

        }
    }

    /**
     * Crean eventos de ratón(aun no implementados)
     * @param mouseEvent eventos
     */
    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }
    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }
}
