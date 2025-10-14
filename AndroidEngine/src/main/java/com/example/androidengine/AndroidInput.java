package com.example.androidengine;
import android.view.MotionEvent;
import android.view.View;
import com.example.engine.Input;

import androidx.constraintlayout.widget.ConstraintSet;

import com.example.engine.TouchEvent;

import java.util.List;

public class AndroidInput implements View.OnTouchListener,Input {



    public AndroidInput(){

    }
    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        TouchEvent myEvent= new TouchEvent();
        myEvent.x=event.getX();
        myEvent.y=event.getY();
        myEvent.finger=0;

        int action = event.getActionMasked();
        switch(action)
        {
            case MotionEvent.ACTION_DOWN:
                myEvent.type=TouchEvent.TouchEventType.TOUCH_DOWN;
            case MotionEvent.ACTION_UP:
                myEvent.type=TouchEvent.TouchEventType.TOUCH_UP;
            case MotionEvent.ACTION_MOVE:
                myEvent.type=TouchEvent.TouchEventType.TOUCH_MOVE;
                break;
        }

        if(event.getActionMasked()==MotionEvent.ACTION_DOWN)
        {
            myEvent.type=TouchEvent.TouchEventType.TOUCH_DOWN;
        }
        synchronized (this) {
            this.pendingEvents.add(event);
        }
        return true;
    }
    @Override
    public List<TouchEvent> getTouchEvents(){
        this.events.addAll(this.pendingEvent);
        this.pendingEvents.clear();
        return this.events;
    }
}
