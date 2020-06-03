package com.example.projetointegrado;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projetointegrado.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    DataBaseUserHelper mDataBaseUserHelper;
    private String loginType = "1";
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mPreferences = getSharedPreferences("com.example.projetointegrado", Context.MODE_PRIVATE);
        mEditor = mPreferences.edit();

        mDataBaseUserHelper = new DataBaseUserHelper(this);


        bindAll();

        checkSharedPref();
    }

    private void bindAll() {
        binding.emailRadioButton.setOnClickListener((View v) -> {
            binding.emailEditText.setVisibility(View.VISIBLE);
            binding.phoneEditText.setVisibility(View.GONE);
            binding.emailEditText.setText("");
        });

        binding.telefoneRadioButton.setOnClickListener((View v) -> {
            binding.phoneEditText.setVisibility(View.VISIBLE);
            binding.emailEditText.setVisibility(View.GONE);
            binding.phoneEditText.setText("");
        });

        binding.loginButton.setOnClickListener(v -> login());
        binding.backButton.setOnClickListener(v -> finish());

        binding.phoneEditText.addTextChangedListener(MaskEditUtil.mask(binding.phoneEditText, MaskEditUtil.FORMAT_FONE));
    }

    private void checkSharedPref() {
        String checkbox = mPreferences.getString(getString(R.string.checkboxKey), "False");
        String mainValue = mPreferences.getString(getString(R.string.mainValueKey), "");
        String password = mPreferences.getString(getString(R.string.passwordKey), "");
        String loginType = mPreferences.getString(getString(R.string.loginTypeKey), "1");

        if (loginType.equals("1")) {
            binding.emailRadioButton.performClick();
            binding.emailEditText.setText(mainValue);
        } else {
            binding.telefoneRadioButton.performClick();
            binding.phoneEditText.setText(mainValue);
        }

        binding.senhaEditText.setText(password);
        binding.rememberMeCheckbox.setChecked(checkbox.equals("True"));
    }

    private void login() {
        String mainChoiceString = "";

        if (binding.emailRadioButton.isChecked()) {
            mainChoiceString = binding.emailEditText.getText().toString();
            loginType = "1";
        } else if (binding.telefoneRadioButton.isChecked()) {
            mainChoiceString = binding.phoneEditText.getText().toString();
            mainChoiceString = MaskEditUtil.unmask(mainChoiceString);
            loginType = "2";
        }

        String password = binding.senhaEditText.getText().toString();

        if (mainChoiceString.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Dados incompletos", Toast.LENGTH_SHORT).show();
        } else {
            Cursor data = mDataBaseUserHelper.getData();

            int column = loginType.equals("1") ? 4 : 3;

            while (data.moveToNext()) {
                if (data.getString(1).equals(loginType)) {
                    if (data.getString(column).equals(mainChoiceString)) {
                        if (data.getString(5).equals(password)) {

                            if (binding.rememberMeCheckbox.isChecked()) {
                                mEditor.putString(getString(R.string.checkboxKey), "True");
                                mEditor.commit();

                                mEditor.putString(getString(R.string.mainValueKey), mainChoiceString);
                                mEditor.commit();

                                mEditor.putString(getString(R.string.passwordKey), password);
                                mEditor.commit();

                                mEditor.putString(getString(R.string.loginTypeKey), loginType);
                                mEditor.commit();
                            } else {
                                mEditor.putString(getString(R.string.checkboxKey), "False");
                                mEditor.commit();

                                mEditor.putString(getString(R.string.mainValueKey), "");
                                mEditor.commit();

                                mEditor.putString(getString(R.string.passwordKey), "");
                                mEditor.commit();

                                mEditor.putString(getString(R.string.loginTypeKey), "");
                                mEditor.commit();
                            }

                            Intent intent = new Intent(LoginActivity.this, FragmentsActivity.class);
                            startActivity(intent);
                            finish();
                            return;
                        }
                    }
                }
            }
        }
    }
}