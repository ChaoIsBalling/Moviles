package com.example.androidengine;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.SoundPool;

import java.io.IOException;

import android.media.AudioAttributes;


/**
 * Clase que se encarga de la gestión de sonidos en Android
 */
public class AndroidAudio {
    /**
     * Clase SoundPool propia de Android Develop que reproduce resources para
     * nuestra aplicación
     */
    SoundPool spool;

    /**
     * Instancia del AssetManager de Android
     */
    AssetManager assets;

    /**
     * Raíz en la que se almacenan nuestros audios
     */
    String root="Audio/";

    /**
     * Constructor de Android Audio
     * @param assets AssetManager de Android
     */
    public AndroidAudio(AssetManager assets)
    {
        AudioAttributes audioAttributes = new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build();
        this.spool = new SoundPool.Builder().setMaxStreams(20).setAudioAttributes(audioAttributes).build();

        this.assets=assets;
       // this.spool= new SoundPool.Builder().setAudioAttributes(attributes).setMaxStreams(20).build();
    }

    /**
     * Metodo que crea un nuevo sonido a partir del nombre de un archivo
     * @param file Nombre del archivo de sonido
     * @return Interfaz Sound del motor
     */

    public AndroidSound newSound(String file)
    {
        int id=-1;

        try {
            //se asigna el descriptor y se le pasa al spool
            AssetFileDescriptor descriptor = this.assets.openFd(root+file);
            id=this.spool.load(descriptor,1);

        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }

        return new AndroidSound(id);
    }

    /**
     * Reproduce un sonido a partir de la interfaz de Sound
     * del sonido que queramos escuchar en nuestra app
     * @param sound Interfaz sound del engine
     */

    public void playSound(AndroidSound sound)
    {
        AndroidSound s =(AndroidSound)sound;
        int streamId =this.spool.play(s.getID(),1,1,0,0,1);
        s.setStreamId(streamId);
    }

    /**
     * Metodo que habilita el loop en un sonido a través
     * de su interfaz Sound en nuestra app
     * @param sound Interfaz sound del engine
     */

    public void loopSound(AndroidSound sound) {
        AndroidSound s =(AndroidSound)sound;
        int streamId =this.spool.play(s.getID(),1,1,0,-1,1);
        s.setStreamId(streamId);
    }

    /**
     * Metodo que detiene el sonido a traves de la interfaz del sonido en nuestra app
     * @param sound Interfaz sound del engine
     */
    public void stopSound(AndroidSound sound)
    {
        AndroidSound s =(AndroidSound)sound;
        this.spool.stop(s.getStreamId());
    }
}
