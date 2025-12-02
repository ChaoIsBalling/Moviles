package com.example.desktopengine;

import com.example.engine.Sound;
import com.example.engine.Audio;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Clase que hereda de la interfaz Audio de Engine
 * Se encarga de la gestión de sonidos en Desktop
 */
public class DesktopAudio implements Audio  {

    /**
     * Estructura que almacena pools de audios.
     * La clave es el nombre del sonido y el valor es un array de Clips de audio
     */
    private HashMap<String, ArrayList<Clip>>pools;
    //public static ArrayList<Clip> soundPool;

    /**
     * Contador de sonidos
     */
    public int count = 0;
    /**
     * Raiz donde se guardan todos los audios que se puedan leer
     */
    String root = "data/Audio/";

    /**
     * Constructor que incializa la pool de sonidos
     */
    public DesktopAudio()
    {
        pools = new HashMap<String, ArrayList<Clip>>();
    }

    /**
     * Metodo herdado de Engine que crea un nuevo sonido en Desktop
     * @param name nombre del sonido
     * @return Interfaz de Sonido
     */
    @Override
    public Sound newSound(String name)
    {
        if(!pools.containsKey(name)||pools.get(name).isEmpty()) {
            ArrayList<Clip> pool = new ArrayList<Clip>();
            pools.put(name,pool);
            try {
                Clip clip = AudioSystem.getClip();
                Clip clip2 = AudioSystem.getClip();
                File file = new File(root + name);
                AudioInputStream ais = AudioSystem.getAudioInputStream(file);
                clip.open(ais);
                AudioInputStream ais2 = AudioSystem.getAudioInputStream(file);
                //Igual hay que cambiar esto, ya que tener dos esta feo
                clip2.open(ais2);
                ais.close();
                ais2.close();
                pool.add(clip);
                pool.add(clip2);
            } catch (LineUnavailableException e) {
                throw new RuntimeException(e);
            } catch (UnsupportedAudioFileException e) {
                throw new RuntimeException(e);
            } catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }
        return new DesktopSound(name);
    }

    /**
     * Reproduce un sonido a partir de la interfaz de Sound
     * del sonido que queramos escuchar
     * @param sound Interfaz sound del engine
     */
    @Override
    public void playSound(Sound sound)
    {

        DesktopSound s = (DesktopSound)sound;
        ArrayList<Clip>p =this.pools.get(s.getName());
        Clip c= p.get(count);
        count= (count+1)%2;
        c.setFramePosition(0);
        c.start();
        //(DesktopSound)sound.setClip(c);
        s.setClip(c);
    }

    /**
     * Metodo que habilita el loop en un sonido a través
     * de su interfaz Sound
     * @param sound Interfaz sound del engine
     */
    @Override
    public void loopSound(Sound sound) {
        DesktopSound s = (DesktopSound)sound;
        ArrayList<Clip>p =this.pools.get(s.getName());
        Clip c= p.get(count);
        count= (count+1)%2;
        c.setFramePosition(0);
        c.start();
        c.loop(Clip.LOOP_CONTINUOUSLY);
        //(DesktopSound)sound.setClip(c);
        s.setClip(c);
    }


    /**
     * Metodo que detiene el sonido a traves de la interfaz del sonido
     * @param sound Interfaz sound del engine
     */
    @Override
    public void stopSound(Sound sound)
    {
        DesktopSound s = (DesktopSound)sound;
        s.getClip().stop();
        //(DesktopSound)sound.getClip().stop();
    }
}
