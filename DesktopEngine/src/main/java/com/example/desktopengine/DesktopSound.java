package com.example.desktopengine;
import com.example.engine.Sound;

import javax.sound.sampled.Clip;

public class DesktopSound implements Sound{
    private String file;
    private Clip clip;

    DesktopSound(String file) {
        this.file=file;
    }

    public String getName()
    {
        return this.file;
    }

    public Object setClip(Clip c) {
        return this.clip = c;
    }

    protected Clip getClip(){
        return this.clip;
    }

}
