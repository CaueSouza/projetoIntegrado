package com.example.projetointegrado;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mPreferences = getSharedPreferences("com.example.projetointegrado", Context.MODE_PRIVATE);
        mEditor = mPreferences.edit();

        mDataBaseUserHelper = new DataBaseUserHelper(this);

        bindAll();

        checkSharedPref();
    }

    private void bindAll() {
        binding.emailRadioButton.setOnClickListener((View v) -> {
            binding.mainEditText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            binding.mainEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_email_black_24dp, 0, 0, 0);
            binding.mainEditText.setHint(R.string.login_type_email);
            binding.mainEditText.setText("");
            mEditor.putString(getString(R.string.loginTypeKey), "1");
            mEditor.commit();
        });

        binding.telefoneRadioButton.setOnClickListener((View v) -> {
            binding.mainEditText.setInputType(InputType.TYPE_CLASS_PHONE);
            binding.mainEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_local_phone_black_24dp, 0, 0, 0);
            binding.mainEditText.setHint(R.string.login_type_phone);
            binding.mainEditText.setText("");
            mEditor.putString(getString(R.string.loginTypeKey), "2");
            mEditor.commit();
        });

        binding.loginButton.setOnClickListener(v -> login());
        binding.backButton.setOnClickListener(v -> finish());
    }

    private void checkSharedPref() {
        String checkbox = mPreferences.getString(getString(R.string.checkboxKey), "False");
        String mainValue = mPreferences.getString(getString(R.string.mainValueKey), "");
        String password = mPreferences.getString(getString(R.string.passwordKey), "");
        String loginType = mPreferences.getString(getString(R.string.loginTypeKey), "1");

        if (loginType.equals("1")) {
            binding.emailRadioButton.performClick();
        } else {
            binding.telefoneRadioButton.performClick();
        }

        binding.mainEditText.setText(mainValue);
        binding.senhaEditText.setText(password);
        binding.rememberMeCheckbox.setChecked(checkbox.equals("True"));
    }

    private void login() {
        String mainChoiceString = binding.mainEditText.getText().toString();
        String password = binding.senhaEditText.getText().toString();

        if (mainChoiceString.isEmpty() || password.isEmpty())
            Toast.makeText(this, "Dados incompletos", Toast.LENGTH_SHORT).show();
        else {
            Cursor data = mDataBaseUserHelper.getData();

            String loginType = mPreferences.getString(getString(R.string.loginTypeKey), "1");
            int column = loginType.equals("1") ? 4 : 3;

            while (data.moveToNext()) {
                if (data.getString(column).equals(mainChoiceString)) {
                    if (data.getString(5).equals(password)) {

                        if (binding.rememberMeCheckbox.isChecked()) {
                            mEditor.putString(getString(R.string.checkboxKey), "True");
                            mEditor.commit();

                            mEditor.putString(getString(R.string.mainValueKey), mainChoiceString);
                            mEditor.commit();

                            mEditor.putString(getString(R.string.passwordKey), password);
                            mEditor.commit();
                        } else {
                            mEditor.putString(getString(R.string.checkboxKey), "False");
                            mEditor.commit();

                            mEditor.putString(getString(R.string.mainValueKey), "");
                            mEditor.commit();

                            mEditor.putString(getString(R.string.passwordKey), "");
                            mEditor.commit();
                        }

                        Intent intent = new Intent(this, FragmentsActivity.class);
                        startActivity(intent);
                        finish();
                        return;
                    }
                }
            }
        }

        Toast.makeText(this, "Dados incorretos", Toast.LENGTH_LONG).show();
    }
}
