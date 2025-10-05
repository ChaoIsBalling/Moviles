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

public class MainActivity extends AppCompatActivity {

    private SurfaceView renderView;

    private AndroidEngine engine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.renderView = new SurfaceView(this);
        setContentView(this.renderView);
        GameLogic gameLogic = new GameLogic();

        this.engine = new AndroidEngine(this.renderView);

        this.engine.setState(gameLogic);

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