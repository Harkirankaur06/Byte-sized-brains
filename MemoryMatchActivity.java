package com.example.games;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MemoryMatchActivity extends AppCompatActivity {

    private List<Button> buttons;
    private List<Integer> buttonValues;
    private int lastButtonIndex = -1;
    private boolean isFlipping = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_match);

        GridLayout gridLayout = findViewById(R.id.gridLayout);
        buttons = new ArrayList<>();
        buttonValues = new ArrayList<>();

        for (int i = 0; i < 16; i++) {
            buttonValues.add(i / 2); // Two buttons for each value (0-7)
            Button button = new Button(this);
            button.setId(i);
            button.setOnClickListener(this::onButtonClick);
            buttons.add(button);
            gridLayout.addView(button);
        }

        Collections.shuffle(buttonValues);
    }

    private void onButtonClick(View view) {
        if (isFlipping) return;

        Button button = (Button) view;
        int index = button.getId();
        button.setText(String.valueOf(buttonValues.get(index)));

        if (lastButtonIndex == -1) {
            lastButtonIndex = index;
        } else {
            if (buttonValues.get(lastButtonIndex).equals(buttonValues.get(index))) {
                button.setEnabled(false);
                buttons.get(lastButtonIndex).setEnabled(false);
                lastButtonIndex = -1;
            } else {
                isFlipping = true;
                Handler handler = new Handler();
                handler.postDelayed(() -> {
                    button.setText("");
                    buttons.get(lastButtonIndex).setText("");
                    lastButtonIndex = -1;
                    isFlipping = false;
                }, 1000);
            }
        }
    }
}
