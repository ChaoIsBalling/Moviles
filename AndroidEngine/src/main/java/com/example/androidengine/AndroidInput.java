package com.example.androidengine;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;
import static android.view.MotionEvent.ACTION_UP;

import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class AndroidInput implements View.OnTouchListener{
    ArrayList<TouchEvent> events;
    ArrayList<TouchEvent> pendingEvents;
    boolean dragging =false;
    float lastY;
    //inicializa las listas de la clase
    public AndroidInput(){
        events = new ArrayList<TouchEvent>();
        pendingEvents = new ArrayList<TouchEvent>();
    }
//metodo que registra una acción de touch cuando tocas la pantalla del movil y lo pasa a una lista de eventos pendientes
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //pasamos la posición del touch respecto a la pantalla y dependiendo del tipo de movimiento le asignamos
        //un touch type
        TouchEvent myEvent = new TouchEvent();
        myEvent.x = event.getX();
        myEvent.y = event.getY();
        myEvent.finger =0;
        int action = event.getActionMasked();

        switch (action){
            case ACTION_DOWN:
                myEvent.type= TouchEvent.TouchEventType.TOUCH_DOWN;
                break;
            case ACTION_UP:
                myEvent.type= TouchEvent.TouchEventType.TOUCH_UP;
                break;
            case ACTION_MOVE:
                myEvent.type= TouchEvent.TouchEventType.TOUCH_MOVE;
                break;
        }

        synchronized (this){
            this.pendingEvents.add(myEvent);
        }

        return true;
    }
    /**
     * Añade los eventos pendientes a la lista de eventos actaules y limpia la lista de pendientes
     * @return Eventos pendientes
     */
    public synchronized ArrayList<TouchEvent> getTouchEvents() {
        this.events.addAll(this.pendingEvents);
        this.pendingEvents.clear();
        return this.events;
    }

}
