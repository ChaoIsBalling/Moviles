package com.example.androidengine;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.SoundPool;

import com.example.engine.Sound;
import com.example.engine.Audio;

import java.io.IOException;

import android.media.AudioAttributes;

public class AndroidAudio implements Audio {

    SoundPool spool;
    AssetManager assets;

    String root="Audio/";
    public AndroidAudio(AssetManager assets)
    {
        AudioAttributes audioAttributes = new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build();
        this.spool = new SoundPool.Builder().setMaxStreams(20).setAudioAttributes(audioAttributes).build();

        this.assets=assets;
       // this.spool= new SoundPool.Builder().setAudioAttributes(attributes).setMaxStreams(20).build();
    }
    @Override
    public Sound newSound(String file)
    {
        int id=-1;

        try {
            AssetFileDescriptor descriptor = this.assets.openFd(root+file);
            id=this.spool.load(descriptor,1);

        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }

        return new AndroidSound(id);
    }
    @Override
    public void playSound(Sound sound)
    {
        AndroidSound s =(AndroidSound)sound;
        this.spool.play(s.getID(),1,1,0,0,1);
    }

    @Override
    public void loopSound(Sound sound) {
        AndroidSound s =(AndroidSound)sound;
        this.spool.play(s.getID(),1,1,0,-1,1);
    }

    @Override
    public void stopSound(Sound sound)
    {
        AndroidSound s =(AndroidSound)sound;
        this.spool.stop(s.getID());
    }
}
