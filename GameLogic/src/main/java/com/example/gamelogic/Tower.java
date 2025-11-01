package com.example.gamelogic;

import com.example.engine.Graphics;

import java.util.ArrayList;

public interface Tower {
    public void UpdateAttack(float mejora);
    public void UpdateRange(float mejora);
    public void UpdateFireRate(float mejora);
    public void setListaEnemigos(ArrayList<Enemy> enemigos);
    public void Update(double deltaTime);
    public void Render(Graphics gr);
    public float getRange();
}
