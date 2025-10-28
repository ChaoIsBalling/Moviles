package com.example.androidengine;
import com.example.engine.Sound;

public class AndroidSound implements Sound {
    private int id;

    AndroidSound(int id)
    {
        this.id=id;
    }
    public int getID()
    {
        return this.id;
    }
}
