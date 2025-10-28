package com.example.androidengine;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.SoundPool;

import com.example.engine.Sound;
import com.example.engine.Audio;

import java.io.IOException;

public class AndroidAudio implements Audio {

    SoundPool spool;
    AssetManager assets;
    public AndroidAudio(AssetManager assets)
    {
        this.assets=assets;
        this.spool= new SoundPool.Builder().setMaxStreams(20).build();
    }
    @Override
    public Sound newSound(String file)
    {
        int id=-1;

        try {
            AssetFileDescriptor descriptor = this.assets.openFd(file);
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
        this.spool.play(s.getID(),1,1,1,0,1);

    }
    @Override
    public void stopSound(Sound sound)
    {
        AndroidSound s =(AndroidSound)sound;
        this.spool.stop(s.getID());
    }
}
