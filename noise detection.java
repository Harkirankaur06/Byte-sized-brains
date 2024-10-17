import android.Manifest;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private static final int SAMPLE_RATE = 44100;  // Sample rate in Hz
    private static final int BUFFER_SIZE = AudioRecord.getMinBufferSize(SAMPLE_RATE,
                                                AudioFormat.CHANNEL_IN_MONO,
                                                AudioFormat.ENCODING_PCM_16BIT);
    private static final int REQUEST_MICROPHONE = 1;  // Request code for microphone permission

    private AudioRecord audioRecord;
    private boolean isRecording = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check for microphone permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, 
                    new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_MICROPHONE);
        } else {
            // If permission is granted, start monitoring
            startNoiseMonitoring();
        }
    }

    // Handle the result of the permission request
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_MICROPHONE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, start noise monitoring
                startNoiseMonitoring();
            } else {
                // Permission denied, show a message
                Toast.makeText(this, "Microphone permission is required to monitor noise levels.",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Start capturing audio and monitoring noise levels
    public void startNoiseMonitoring() {
        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
                                      SAMPLE_RATE,
                                      AudioFormat.CHANNEL_IN_MONO,
                                      AudioFormat.ENCODING_PCM_16BIT,
                                      BUFFER_SIZE);

        audioRecord.startRecording();
        isRecording = true;

        new Thread(new Runnable() {
            @Override
            public void run() {
                short[] audioBuffer = new short[BUFFER_SIZE];

                while (isRecording) {
                    int numSamplesRead = audioRecord.read(audioBuffer, 0, BUFFER_SIZE);

                    // Calculate the noise level in decibels
                    double rms = calculateRMS(audioBuffer, numSamplesRead);
                    final double decibelLevel = 20 * Math.log10(rms);

                    // Update the UI with the noise level
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            // Display the noise level in a Toast, or update a TextView
                            Toast.makeText(MainActivity.this, "Noise Level (dB): " + decibelLevel,
                                    Toast.LENGTH_SHORT).show();
                        }
                    });

                    // Sleep for a while to avoid overloading the CPU
                    try {
                        Thread.sleep(1000); // 1 second delay
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    // Stop noise monitoring and release resources
    public void stopNoiseMonitoring() {
        isRecording = false;
        if (audioRecord != null) {
            audioRecord.stop();
            audioRecord.release();
            audioRecord = null;
        }
    }

    // Calculate the RMS (Root Mean Square) value of the audio buffer
    private double calculateRMS(short[] audioBuffer, int numSamples) {
        double sum = 0;
        for (int i = 0; i < numSamples; i++) {
            sum += audioBuffer[i] * audioBuffer[i];
        }
        return Math.sqrt(sum / numSamples);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopNoiseMonitoring();  // Stop monitoring when the app is destroyed
    }
}