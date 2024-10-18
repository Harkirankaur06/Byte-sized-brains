package com.example.games;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;

public class WordleActivity extends AppCompatActivity {

    private static final String[] WORDS =  {"apple", "bread", "climb", "dance", "earth", "frost", "grape", "horse", "jelly", "knife",
        "lemon", "mango", "night", "ocean", "pearl", "queen", "radio", "snake", "tiger", "under",
        "vivid", "water", "xerox", "yacht", "zebra", "blaze", "crane", "dwarf", "eagle", "fruit",
        "glove", "honey", "ivory", "juice", "karma", "ledge", "magic", "novel", "olive", "pulse",
        "quest", "rhyme", "storm", "trust", "unity", "valor", "woven", "xerus", "yield", "zephyr"
    };
    private String targetWord;
    private TextView resultView;
    private EditText inputWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wordle);

        Random random = new Random();
        targetWord = WORDS[random.nextInt(WORDS.length)];

        resultView = findViewById(R.id.resultView);
        inputWord = findViewById(R.id.inputWord);
        Button checkButton = findViewById(R.id.checkButton);

        checkButton.setOnClickListener(v -> checkWord());
    }

    private void checkWord() {
        String guess = inputWord.getText().toString().toLowerCase();
        if (guess.equals(targetWord)) {
            resultView.setText("Correct! The word was " + targetWord);
        } else {
            resultView.setText("Try again!");
        }
    }
}
