package com.example.projetointegrado;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projetointegrado.databinding.ActivityCadastroBinding;

public class CadastroActivity extends AppCompatActivity {
    ActivityCadastroBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCadastroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backButton.setOnClickListener((View v) -> {
            finish();
        });

        binding.signInButton.setOnClickListener((View v) -> {
            Intent intent = new Intent(this, AlarmesActivity.class);
            startActivity(intent);
        });
    }
}
