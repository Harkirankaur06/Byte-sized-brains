package com.example.games;

import android.os.Bundle;
import android.os.Handler;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class SnakeActivity extends AppCompatActivity {

    private SurfaceView surfaceView;
    private Handler handler;
    private Runnable runnable;
    private int direction = 0; // 0=right, 1=down, 2=left, 3=up

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snake);

        surfaceView = findViewById(R.id.surfaceView);
        handler = new Handler();
        runnable = this::updateGame;

        Button upButton = findViewById(R.id.upButton);
        Button downButton = findViewById(R.id.downButton);
        Button leftButton = findViewById(R.id.leftButton);
        Button rightButton = findViewById(R.id.rightButton);

        upButton.setOnClickListener(v -> direction = 3);
        downButton.setOnClickListener(v -> direction = 1);
        leftButton.setOnClickListener(v -> direction = 2);
        rightButton.setOnClickListener(v -> direction = 0);

        handler.postDelayed(runnable, 1000 / 10); // 10 FPS
    }

    private void updateGame() {
        // Update the snake's position and redraw the screen
        handler.postDelayed(runnable, 1000 / 10); // Call again in 100ms
    }
}
