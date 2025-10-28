package com.example.desktopengine;
import com.example.engine.Sound;
public class DesktopSound implements Sound{
    String file;
    DesktopSound(String file)
    {
        this.file=file;
    }

    public String getName()
    {
        return this.file;
    }

}
