package com.example.engine;
import java.util.List;

public interface State {

    void update (double deltatime);

    void render(Graphics gr);

    void handleInput(List<TouchEvent>events, float elapsedtime);

}
