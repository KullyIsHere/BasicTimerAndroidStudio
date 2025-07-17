package com.example.sixthtest;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;
import android.Manifest;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Timer extends AppCompatActivity {
    // confirms notifications
    private final ActivityResultLauncher<String> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
        @Override
        public void onActivityResult(Boolean o) {
            if (o) {
                Toast.makeText(Timer.this, "Post notification permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(Timer.this, "Post notification permission not granted", Toast.LENGTH_SHORT).show();
            }
        }
    });

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
        long timerDuration = TimeUnit.MINUTES.toMillis(1);
        long ticksInterval = 10;

        //Notification setup
        NotificationCompat.Builder builder = new NotificationCompat.Builder(Timer.this, "test")
                .setSmallIcon(R.drawable.baseline_circle_notifications_24)
                .setContentTitle("Study pet")
                .setContentText("Timer Finished")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);


        NotificationManager notificationManager = getSystemService(NotificationManager.class);

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
                //Here's where the notification is sent.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && ActivityCompat.checkSelfPermission(Timer.this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                    activityResultLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        NotificationChannel notificationChannel = new NotificationChannel("test",getString(R.string.app_name), NotificationManager.IMPORTANCE_DEFAULT);
                        notificationChannel.setDescription("Timer Finished");
                        notificationManager.createNotificationChannel(notificationChannel);
                        notificationManager.notify(10,builder.build());
                    }
                }

            }
        }.start();

    }

    public void ReturnSelect(View view) {
        System.out.println("Returning MaIn ");

        super.onBackPressed();
    }

}