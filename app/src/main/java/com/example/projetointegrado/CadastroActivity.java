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

        binding.telephoneLayout.addTextChangedListener(MaskEditUtil.mask(binding.telephoneLayout, MaskEditUtil.FORMAT_FONE));

        binding.registerButtonConfirm.setOnClickListener(v -> addDataDB());
        binding.backButton.setOnClickListener(v -> finish());
    }

    private void addDataDB() {
        String nome = binding.nomeLayout.getText().toString();
        String fone = MaskEditUtil.unmask(binding.telephoneLayout.getText().toString());
        String email = binding.emailLayout.getText().toString();
        String senha = binding.senhaLayout.getText().toString();
        int tipo = 0; //1 = login por email  2 = login por telefone

        if (binding.emailRadioButton.isChecked()) {
            if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
                Toast.makeText(this, "Dados incompletos", Toast.LENGTH_SHORT).show();
                return;
            }
            tipo = 1;
        } else if (binding.telefoneRadioButton.isChecked()) {
            if (nome.isEmpty() || fone.isEmpty() || senha.isEmpty()) {
                Toast.makeText(this, "Dados incompletos", Toast.LENGTH_SHORT).show();
                return;
            }
            //verifica se a string do telefone possui somente numeros
            if (!fone.matches("\\d+") || fone.length() != 12) return;

            tipo = 2;
        }

        Cursor data = mDataBaseUserHelper.getData();

        while (data.moveToNext()) {
            if (data.getString(1).equals(String.valueOf(tipo))) {
                if (tipo == 1) {
                    if (data.getString(4).equals(email)) {
                        Toast.makeText(this, "E-mail ja utilizado", Toast.LENGTH_LONG).show();
                        return;
                    }
                } else if (tipo == 2) {
                    if (data.getString(3).equals(fone)) {
                        Toast.makeText(this, "Telefone ja utilizado", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
            }
        }

        boolean insertData = false;
        if (tipo == 1) {
            insertData = mDataBaseUserHelper.addData(tipo, nome, email, null, senha);
        } else if (tipo == 2) {
            insertData = mDataBaseUserHelper.addData(tipo, nome, null, fone, senha);
        }

        if (insertData) {
            Intent intent = new Intent(this, FragmentsActivity.class);
            startActivity(intent);
            finish();
        } else Toast.makeText(this, "Algo deu errado", Toast.LENGTH_LONG).show();
    }


}
