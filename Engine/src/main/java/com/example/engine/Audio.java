package com.example.engine;

public interface Audio {

    public Sound newSound(String file);

    public void playSound(Sound sound);

    public void stopSound(Sound sound);
}
