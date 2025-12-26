package com.example.androidengine;

public class AndroidSound {
    private int id;

    private int streamId;

    //metodo que inicializa la id del sonido
    AndroidSound(int id)
    {
        this.id=id;
    }

    //getter del id
    public int getID()
    {
        return this.id;
    }
    //getter del streram id
    public int getStreamId(){
        return this.streamId;
    }
    //setter del stream id
    public void setStreamId(int streamId){
        this.streamId = streamId;
    }
}
