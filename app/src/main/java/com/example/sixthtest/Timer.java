package com.example.sixthtest;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Timer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_timer);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        int timerValue = 0;
        if (intent != null && intent.hasExtra("StartTime")) {
            timerValue = intent.getIntExtra("StartTime", 0); // 0 is default value

        }

        // Timer script
        TextView textView = findViewById(R.id.textView);
        long timerDuration = TimeUnit.MINUTES.toMillis(timerValue);
        long ticksInterval = 10;
        //Countdown timer
        new CountDownTimer(timerDuration, ticksInterval) {
            long millis = 1000;
            @Override
            public void onTick(long millisUntilFinished) {
                millis = millis - ticksInterval;
                if (millis == 0)
                    millis = 1000;

                String timerText = String.format(Locale.getDefault(), "%02d:%02d:%03d",
                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)),
                    millis
                );

                textView.setText(timerText);
            }

            @Override
            public void onFinish() {
                textView.setText("Finished");

            }
        }.start();
    }

    public void ReturnSelect(View view) {
        System.out.println("Returning MaIn ");

        super.onBackPressed();
    }

}