package com.example.engine;

import java.util.ArrayList;
import java.util.List;

public interface State {

    void update (double deltatime);

    void render(Graphics gr);


    void setGraphics(Graphics gr);

    void handleInput(ArrayList<TouchEvent> list, double elapseTime);

    void setAudio(Audio audio);

}
