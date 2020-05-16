package com.example.projetointegrado;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projetointegrado.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.emailRadioButton.setOnClickListener((View v) -> {
            binding.editText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            binding.editTextTitle.setText(R.string.login_type_email);
        });

        binding.telefoneRadioButton.setOnClickListener((View v) -> {
            binding.editText.setInputType(InputType.TYPE_CLASS_PHONE);
            binding.editTextTitle.setText(R.string.login_type_phone);
        });

        binding.backButton.setOnClickListener((View v) -> {
            finish();
        });
    }
}
