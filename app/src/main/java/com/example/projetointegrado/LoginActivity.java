package com.example.projetointegrado;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.example.projetointegrado.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.emailRadioButton.setOnClickListener((View v) -> {
            binding.mainEditText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            binding.mainEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_email_black_24dp,0,0,0);
            binding.mainEditText.setHint(R.string.login_type_email);
            binding.mainEditText.setText("");
        });

        binding.telefoneRadioButton.setOnClickListener((View v) -> {
            binding.mainEditText.setInputType(InputType.TYPE_CLASS_PHONE);
            binding.mainEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_local_phone_black_24dp,0,0,0);
            binding.mainEditText.setHint(R.string.login_type_phone);
            binding.mainEditText.setText("");
        });

        binding.backButton.setOnClickListener((View v) -> {
            finish();
        });

        binding.loginButton.setOnClickListener((View v) -> {
            Intent intent = new Intent(this, PrincipalActivity.class);
            startActivity(intent);
        });
    }
}
