package com.example.speechanalysis;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_RECORD_PERMISSION = 100;
    private TextView textView;
    private Button startButton;
    private SpeechRecognizer speechRecognizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        startButton = findViewById(R.id.startButton);

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);

        startButton.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_PERMISSION);
            } else {
                startSpeechRecognition();
            }
        });
    }

    private void startSpeechRecognition() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {
                textView.setText("Listening...");
            }

            @Override
            public void onBeginningOfSpeech() {
            }

            @Override
            public void onRmsChanged(float rmsdB) {
            }

            @Override
            public void onBufferReceived(byte[] buffer) {
            }

            @Override
            public void onEndOfSpeech() {
                textView.setText("Processing...");
            }

            @Override
            public void onError(int error) {
                textView.setText("Error: " + error);
            }

            @Override
            public void onResults(Bundle results) {
                ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (matches != null && !matches.isEmpty()) {
                    String speechText = matches.get(0);
                    textView.setText("You said: " + speechText);
                    analyzeSpeechForAnxiety(speechText);
                }
            }

            @Override
            public void onPartialResults(Bundle partialResults) {
            }

            @Override
            public void onEvent(int eventType, Bundle params) {
            }
        });

        speechRecognizer.startListening(intent);
    }

    private void analyzeSpeechForAnxiety(String speechText) {
        // Placeholder: Random anxiety level based on speech length
        int anxietyLevel = speechText.length() % 10;
        textView.append("\nAnxiety Level: " + anxietyLevel);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_RECORD_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startSpeechRecognition();
            } else {
                textView.setText("Permission denied");
            }
        }
    }
}

private void analyzeSpeechForAnxiety(String speechText) {
    // Calculate speech rate (words per minute)
    String[] words = speechText.split("\\s+");
    long speechDurationMillis = calculateSpeechDuration(speechText);
    double speechRate = (words.length / (speechDurationMillis / 60000.0)); // words per minute

    // Placeholder: Simple heuristic for anxiety level based on speech rate
    int anxietyLevel = 0;
    if (speechRate > 150) {
        anxietyLevel = 8; // High anxiety
    } else if (speechRate > 120) {
        anxietyLevel = 5; // Moderate anxiety
    } else {
        anxietyLevel = 2; // Low anxiety
    }

    textView.append("\nSpeech Rate: " + speechRate + " words/min");
    textView.append("\nAnxiety Level: " + anxietyLevel);
}

private long calculateSpeechDuration(String speechText) {
    // Simulate duration calculation (placeholder)
    return 30000; // Assume a fixed duration of 30 seconds for this example
}
