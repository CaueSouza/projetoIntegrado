package com.example.projetointegrado;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projetointegrado.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    DataBaseUserHelper mDataBaseUserHelper;
    private int loginType = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mDataBaseUserHelper = new DataBaseUserHelper(this);

        bindAll();
    }

    private void bindAll(){
        binding.emailRadioButton.setOnClickListener((View v) -> {
            binding.mainEditText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            binding.mainEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_email_black_24dp,0,0,0);
            binding.mainEditText.setHint(R.string.login_type_email);
            binding.mainEditText.setText("");
            loginType = 1;
        });

        binding.telefoneRadioButton.setOnClickListener((View v) -> {
            binding.mainEditText.setInputType(InputType.TYPE_CLASS_PHONE);
            binding.mainEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_local_phone_black_24dp,0,0,0);
            binding.mainEditText.setHint(R.string.login_type_phone);
            binding.mainEditText.setText("");
            loginType = 2;
        });

        binding.loginButton.setOnClickListener(v -> login());
        binding.backButton.setOnClickListener(v -> finish());
    }

    private void login(){
        String mainChoiceString = binding.mainEditText.getText().toString();
        String senha = binding.senhaEditText.getText().toString();

        if (mainChoiceString.isEmpty() || senha.isEmpty()) Toast.makeText(this, "Dados incompletos", Toast.LENGTH_SHORT).show();
        else {
            int column = loginType == 1 ? 4 : 3;
            Cursor data = mDataBaseUserHelper.getData();

            while (data.moveToNext()){
                if (data.getString(column).equals(mainChoiceString)) {
                    if (data.getString(5).equals(senha)) {
                        Intent intent = new Intent(this, FragmentsActivity.class);
                        startActivity(intent);
                        return;
                    }
                }
            }
        }

        Toast.makeText(this, "Dados incorretos", Toast.LENGTH_LONG).show();
    }
}
