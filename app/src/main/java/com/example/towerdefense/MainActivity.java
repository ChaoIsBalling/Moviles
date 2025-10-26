package com.example.towerdefense;

import android.os.Bundle;
import android.view.SurfaceView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.androidengine.AndroidEngine;
import com.example.gamelogic.GameLogic;
import com.example.androidengine.AndroidGraphics;
import com.example.gamelogic.Menu;

public class MainActivity extends AppCompatActivity {

    private SurfaceView renderView;
    private AndroidGraphics androidGraphics;
    private AndroidEngine engine;
    private Menu menu;
    private GameLogic gameLogic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.renderView = new SurfaceView(this);
        setContentView(this.renderView);

        this.androidGraphics=new AndroidGraphics(this.renderView);
        this.engine = new AndroidEngine(this.renderView);
        this.engine.setState(this.menu);
        this.menu.render(androidGraphics);
    }

    @Override
    protected void onResume(){
        super.onResume();
        this.engine.resume();
    }

    @Override
    protected void onPause(){
        super.onPause();
        this.engine.pause();
    }
}