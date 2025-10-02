package com.example.engine;

public interface State {

    void update (double deltatime);

    void render(Graphics gr);

}
