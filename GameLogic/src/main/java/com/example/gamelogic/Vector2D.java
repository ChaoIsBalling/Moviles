package com.example.gamelogic;

public class Vector2D {
    private int x;
    private int y;
    public Vector2D(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public int getX() {
        return this.x;
    }
    public int getY(){
        return this.y;
    }

    public void setX(int x){
        this.x = x;
    }
    public void setY(int y){ this.y = y; }
}
