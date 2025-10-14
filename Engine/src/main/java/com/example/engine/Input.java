package com.example.engine;


import java.util.List;

public interface Input {

    public List<TouchEvent> events;
    public List<TouchEvent> pendingEvents;
    public List<TouchEvent> getInput();
}
