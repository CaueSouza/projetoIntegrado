package com.example.projetointegrado;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projetointegrado.databinding.ActivityCadastroBinding;

public class CadastroActivity extends AppCompatActivity {

    private ActivityCadastroBinding binding;
    DataBaseUserHelper mDataBaseUserHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCadastroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mDataBaseUserHelper = new DataBaseUserHelper(this);

        binding.signInButton.setOnClickListener(v -> addDataDB());
        binding.backButton.setOnClickListener(v -> finish());
    }

    private void addDataDB() {
        String nome = binding.nomeLayout.getText().toString();
        String fone = binding.celularLayout.getText().toString();
        String email = binding.emailLayout.getText().toString();
        String senha = binding.senhaLayout.getText().toString();
        int tipo = binding.radioButtonCuidador.isChecked() ? 1 : binding.radioButtonIdoso.isChecked() ? 2 : 0;

        if (nome.isEmpty() || fone.isEmpty() || email.isEmpty() || senha.isEmpty() || tipo == 0)
            Toast.makeText(this, "Dados incompletos", Toast.LENGTH_SHORT).show();
        else {
            //verifica se a string do telefone possui somente numeros
            if (!fone.matches("\\d+") || fone.length() <= 2) return;

            Cursor data = mDataBaseUserHelper.getData();

            while (data.moveToNext()) {
                if (data.getString(4).equals(email)) {
                    Toast.makeText(this, "E-mail ja utilizado", Toast.LENGTH_LONG).show();
                    return;
                }
                if (data.getString(3).equals(fone)) {
                    Toast.makeText(this, "Telefone ja utilizado", Toast.LENGTH_LONG).show();
                    return;
                }
            }

            boolean insertData = mDataBaseUserHelper.addData(tipo, nome, fone, email, senha);

            if (insertData) {
                Intent intent = new Intent(this, FragmentsActivity.class);
                startActivity(intent);
                finish();
            } else Toast.makeText(this, "Algo deu errado", Toast.LENGTH_LONG).show();
        }
    }


}
