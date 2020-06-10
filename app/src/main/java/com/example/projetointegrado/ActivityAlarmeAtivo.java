package com.example.projetointegrado;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.Settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;

import com.example.projetointegrado.databinding.ActivityAlarmeAtivoBinding;

public class ActivityAlarmeAtivo extends AppCompatActivity {

    ActivityAlarmeAtivoBinding binding;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAlarmeAtivoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setTitle("Alarme");

        mediaPlayer = MediaPlayer.create(this, Settings.System.DEFAULT_RINGTONE_URI);
        mediaPlayer.start();

        binding.buttonAlarmConfirm.setOnClickListener(v -> {
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
            notificationManagerCompat.cancel(getIntent().getIntExtra("NOTIFICATION_ID", 0));
            mediaPlayer.stop();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
}