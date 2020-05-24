package com.example.projetointegrado;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projetointegrado.databinding.ActivityCadastrarAlarmeBinding;

public class CadastrarAlarmeActivity extends AppCompatActivity {

    private ActivityCadastrarAlarmeBinding binding;
    private DataBaseAlarmsHelper mDataBaseAlarmsHelper;
    private boolean isEdit;
    private int alarmEditPosition;
    private Cursor data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCadastrarAlarmeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(R.string.register_alarm_title);

        mDataBaseAlarmsHelper = new DataBaseAlarmsHelper(this);
        data = mDataBaseAlarmsHelper.getData();

        isEdit = getIntent().getBooleanExtra("IS_EDIT", false);

        if (isEdit) {
            alarmEditPosition = getIntent().getIntExtra("POSITION", -1);
            data.move(alarmEditPosition + 1);

            binding.nameInfMedicine.setText(data.getString(3));
            binding.infDosage.setText(String.valueOf(data.getInt(4)));
            if (data.getInt(1) == 1) {
                binding.radioButtonRegisterMedicineFixtime.setChecked(true);
                binding.radioButtonRegisterMedicineInterval.setChecked(false);
            } else {
                binding.radioButtonRegisterMedicineInterval.setChecked(true);
                binding.radioButtonRegisterMedicineFixtime.setChecked(false);
            }
        }
        binding.backButtonRegisterMedicine.setOnClickListener(v -> finish());

        binding.nextButtonRegisterMedicine.setOnClickListener(v -> {
            Class activity = binding.radioButtonRegisterMedicineFixtime.isChecked() ? HorarioFixActivity.class : binding.radioButtonRegisterMedicineInterval.isChecked() ? IntervaloHorarioActivity.class : null;

            if (activity != null) {
                if (activity == HorarioFixActivity.class) callHorarioFixActivity(activity);
                else callIntervaloHorarioActivity(activity);
            }
        });
    }

    private void callHorarioFixActivity(Class activity) {
        String nome = binding.nameInfMedicine.getText().toString();
        String dosagem = binding.infDosage.getText().toString();

        if (!nome.isEmpty() && !dosagem.isEmpty()) {
            Intent intent = new Intent(this, activity);
            if (isEdit) {
                intent.putExtra("IS_EDIT", true);
                intent.putExtra("POSITION", alarmEditPosition);
                intent.putExtra("MEDICINE_HORA", data.getInt(5));
                intent.putExtra("MEDICINE_MINUTO", data.getInt(6));
            }

            intent.putExtra("MEDICINE_NAME", nome);
            intent.putExtra("MEDICINE_DOSAGE", Integer.parseInt(dosagem));
            startActivity(intent);
        } else {
            Toast.makeText(this, "Insira os dados corretamente", Toast.LENGTH_LONG).show();
        }
    }

    public void callIntervaloHorarioActivity(Class activity) {

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
