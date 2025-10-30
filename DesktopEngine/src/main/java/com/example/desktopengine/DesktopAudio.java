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

public class DesktopAudio implements Audio  {

    private HashMap<String, ArrayList<Clip>>pools;
    public static ArrayList<Clip> soundPool;
    public int count = 0;
    String root = "data/";
    public DesktopAudio()
    {
        pools = new HashMap<String, ArrayList<Clip>>();
    }
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


    @Override
    public void stopSound(Sound sound)
    {
        DesktopSound s = (DesktopSound)sound;
        s.getClip().stop();
        //(DesktopSound)sound.getClip().stop();

    }
}
