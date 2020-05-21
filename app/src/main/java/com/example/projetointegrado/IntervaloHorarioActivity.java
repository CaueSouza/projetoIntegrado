package com.example.projetointegrado;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;

import com.example.projetointegrado.databinding.ActivityIntervaloHorarioBinding;

public class IntervaloHorarioActivity extends AppCompatActivity {

    private ActivityIntervaloHorarioBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityIntervaloHorarioBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setTitle(R.string.action_button_interval);
    }
}
