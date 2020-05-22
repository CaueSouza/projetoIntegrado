package com.example.projetointegrado;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

        binding.backButtonRegisterMedicine.setOnClickListener(v -> finish());

        binding.nextButtonRegisterMedicine.setOnClickListener(v -> {
            Class activity = binding.radioButtonRegisterMedicineFixtime.isChecked() ? HorarioFixActivity.class : binding.radioButtonRegisterMedicineInterval.isChecked() ? IntervaloHorarioActivity.class : null;

            if (activity != null) callNextScreen(activity);
        });
    }

    private void callNextScreen(Class activity) {
        String nome = binding.nameInfMedicine.getText().toString();
        String dosagem = binding.infDosage.getText().toString();

        if (!nome.isEmpty() && !dosagem.isEmpty()) {
            Intent intent = new Intent(this, activity);
            intent.putExtra("MEDICINE_NAME", nome);
            intent.putExtra("MEDICINE_DOSAGE", Integer.parseInt(dosagem));
            startActivity(intent);
        } else {
            Toast.makeText(this, "Insira os dados corretamente", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
