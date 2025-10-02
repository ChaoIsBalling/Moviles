package com.example.towerdefense;

import android.os.Bundle;
import android.view.SurfaceView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.androidengine.AndroidGraphics;
import com.example.gamelogic.GameLogic;

public class MainActivity extends AppCompatActivity {

    private SurfaceView renderView;

    private AndroidGraphics render;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.renderView = new SurfaceView(this);
        setContentView(this.renderView);
        GameLogic gameLogic = new GameLogic();

        this.render = new AndroidGraphics(this.renderView);
        gameLogic.init(render);

        render.setScene(gameLogic);
    }

    protected void onResume(){
        super.onResume();
        this.render.resume();
    }
}