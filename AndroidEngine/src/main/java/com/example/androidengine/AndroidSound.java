package com.example.androidengine;

/**
 * Clase que encapsula los identificadores de un sonido gestionado por el AndroidSound.
 */
public class AndroidSound {
    private int id;
    private int streamId;

    /**
     * Metodo que inicializa el identificador del sonido
     * @param id numero de identificador
     */
    AndroidSound(int id)
    {
        this.id=id;
    }

    /**
     * Getter del id
     * @return id
     */
    public int getID()
    {
        return this.id;
    }

    /**
     * Getter del identificador stream
     * @return streamId
     */
    public int getStreamId(){
        return this.streamId;
    }

    /**
     * Setter del identificador stream
     * @param streamId valor que queremos asignar al identificador stream
     */
    public void setStreamId(int streamId){
        this.streamId = streamId;
    }
}
