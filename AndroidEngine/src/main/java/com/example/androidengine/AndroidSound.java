package com.example.androidengine;
import com.example.engine.Sound;

public class AndroidSound implements Sound {
    private int id;

    private int streamId;

    AndroidSound(int id)
    {
        this.id=id;
    }
    public int getID()
    {
        return this.id;
    }

    public int getStreamId(){
        return this.streamId;
    }

    public void setStreamId(int streamId){
        this.streamId = streamId;
    }
}
