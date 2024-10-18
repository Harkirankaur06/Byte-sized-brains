package com.example.games;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button wordleButton = findViewById(R.id.wordleButton);
        Button memoryMatchButton = findViewById(R.id.memoryMatchButton);
        Button snakeButton = findViewById(R.id.snakeButton);

        wordleButton.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, WordleActivity.class)));
        memoryMatchButton.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, MemoryMatchActivity.class)));
        snakeButton.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, SnakeActivity.class)));
    }
}
