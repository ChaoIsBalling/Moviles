package com.example.androidengine;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.SoundPool;

import com.example.engine.Sound;
import com.example.engine.Audio;

import java.io.IOException;

import android.media.AudioAttributes;


/**
 * Clase que hereda de la interfaz Audio de Engine y que
 * se encarga de la gestión de sonidos en Android
 */
public class AndroidAudio implements Audio {
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
    @Override
    public Sound newSound(String file)
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
    @Override
    public void playSound(Sound sound)
    {
        AndroidSound s =(AndroidSound)sound;
        this.spool.play(s.getID(),1,1,0,0,1);
    }

    /**
     * Metodo que habilita el loop en un sonido a través
     * de su interfaz Sound en nuestra app
     * @param sound Interfaz sound del engine
     */
    @Override
    public void loopSound(Sound sound) {
        AndroidSound s =(AndroidSound)sound;
        this.spool.play(s.getID(),1,1,0,-1,1);
    }

    /**
     * Metodo que detiene el sonido a traves de la interfaz del sonido en nuestra app
     * @param sound Interfaz sound del engine
     */
    @Override
    public void stopSound(Sound sound)
    {
        AndroidSound s =(AndroidSound)sound;
        this.spool.stop(s.getID());
    }
}
