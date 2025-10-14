package com.example.engine;

public class TouchEvent {

    public static enum TouchEventType{
        TOUCH_DOWN,
        TOUCH_UP,
        TOUCH_MOVE,
        TOUCH_PADENTOR
    }
    public TouchEventType type;

    public float x;
    public float y;

    public int finger;
}
