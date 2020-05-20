package com.example.projetointegrado;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;

import com.example.projetointegrado.databinding.ActivityHorarioFixBinding;

public class HorarioFixActivity extends AppCompatActivity {

    private ActivityHorarioFixBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHorarioFixBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setTitle(R.string.action_button_fix_time);
    }
}
