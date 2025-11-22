package com.example.androidengine;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;
import static android.view.MotionEvent.ACTION_UP;

import android.view.MotionEvent;
import android.view.View;

import com.example.engine.Input;
import com.example.engine.TouchEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AndroidInput implements Input,View.OnTouchListener{

    ArrayList<TouchEvent> events;
    ArrayList<TouchEvent> pendingEvents;
    public AndroidInput(){
        events = new ArrayList<TouchEvent>();
        pendingEvents = new ArrayList<TouchEvent>();
    }

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

    @Override
    public synchronized ArrayList<TouchEvent> getTouchEvents() {
        this.events.addAll(this.pendingEvents);
        this.pendingEvents.clear();
        return this.events;
    }
}
