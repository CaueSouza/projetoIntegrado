package com.example.projetointegrado;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projetointegrado.databinding.ActivityReconhecimentoCaixaBinding;

public class ReconhecimentoCaixaActivity extends AppCompatActivity {

    private ActivityReconhecimentoCaixaBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReconhecimentoCaixaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(R.string.register_box_title);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
