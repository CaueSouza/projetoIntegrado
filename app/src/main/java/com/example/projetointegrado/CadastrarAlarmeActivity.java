package com.example.projetointegrado;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.projetointegrado.databinding.ActivityCadastrarAlarmeBinding;

public class CadastrarAlarmeActivity extends AppCompatActivity {

    private ActivityCadastrarAlarmeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCadastrarAlarmeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(R.string.register_alarm_title);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
