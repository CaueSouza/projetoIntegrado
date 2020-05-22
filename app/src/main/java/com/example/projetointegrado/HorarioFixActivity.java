package com.example.projetointegrado;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projetointegrado.databinding.ActivityHorarioFixBinding;

public class HorarioFixActivity extends AppCompatActivity {

    private ActivityHorarioFixBinding binding;
    DataBaseAlarmsHelper mDataBaseAlarmsHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHorarioFixBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setTitle(R.string.action_button_fix_time);
        mDataBaseAlarmsHelper = new DataBaseAlarmsHelper(this);

        binding.nextButtonRegisterMedicine.setOnClickListener(v -> {
            String nome = getIntent().getStringExtra("MEDICINE_NAME");
            int dosagem = getIntent().getIntExtra("MEDICINE_DOSAGE", 0);

            addDataDB(nome, dosagem);
        });
    }

    private void addDataDB(String nome, int dosagem) {
        int horas = binding.idClockSchedule.getHour();
        int minutos = binding.idClockSchedule.getMinute();

        boolean insertData = mDataBaseAlarmsHelper.addData(1, nome, dosagem, horas, minutos);

        if (insertData) {
            Intent intent = new Intent(this, FragmentsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else Toast.makeText(this, "Algo deu errado", Toast.LENGTH_LONG).show();
    }
}
