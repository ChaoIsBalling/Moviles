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
        if(mouseEvent.getButton()==mouseEvent.BUTTON1) {
            TouchEvent event = new TouchEvent();
            event.type= TouchEvent.TouchEventType.TOUCH_DOWN;
            event.x = mouseEvent.getX();
            event.y = mouseEvent.getY();
            event.finger = 0;

            this.events.add(event);
        }
    }

}
