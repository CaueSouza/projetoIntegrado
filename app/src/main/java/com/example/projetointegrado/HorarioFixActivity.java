package com.example.projetointegrado;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projetointegrado.databinding.ActivityHorarioFixBinding;

public class HorarioFixActivity extends AppCompatActivity {

    private ActivityHorarioFixBinding binding;
    private DataBaseAlarmsHelper mDataBaseAlarmsHelper;
    private boolean isEdit;
    private int alarmEditPosition;
    private Cursor data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHorarioFixBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setTitle(R.string.action_button_fix_time);

        isEdit = getIntent().getBooleanExtra("IS_EDIT", false);
        mDataBaseAlarmsHelper = new DataBaseAlarmsHelper(this);
        data = mDataBaseAlarmsHelper.getData();

        if (isEdit) {
            alarmEditPosition = getIntent().getIntExtra("POSITION", -1);
            data.move(alarmEditPosition + 1);

            binding.idClockSchedule.setHour(data.getInt(8));
            binding.idClockSchedule.setMinute(data.getInt(9));
        }

        binding.backButtonRegisterMedicine.setOnClickListener(v -> finish());

        binding.nextButtonRegisterMedicine.setOnClickListener(v -> {
            int medTipo = getIntent().getIntExtra("MEDICINE_TYPE", 0);
            String nome = getIntent().getStringExtra("MEDICINE_NAME");
            int dosagem = getIntent().getIntExtra("MEDICINE_DOSAGE", 0);
            int quantidade = getIntent().getIntExtra("MEDICINE_QUANTITY", 0);
            int quantidadeCaixa = getIntent().getIntExtra("MEDICINE_BOX_QUANTITY", 0);

            if (medTipo == 1) {
                addDataDB(nome, quantidade, quantidadeCaixa);
            } else if (medTipo == 2){
                addDataDB(nome, dosagem);
            }

            finish();
        });
    }

    private void addDataDB(String nome, int quantidade, int quantidadeCaixa) {
        int horas = binding.idClockSchedule.getHour();
        int minutos = binding.idClockSchedule.getMinute();
        boolean confirmation;

        if (isEdit) {
            int ativo = data.getInt(3);

            confirmation = mDataBaseAlarmsHelper.updateData(String.valueOf(alarmEditPosition + 1), ativo, nome, quantidade, quantidadeCaixa, horas, minutos);
        } else {
            confirmation = mDataBaseAlarmsHelper.addDataFix(nome, quantidade, quantidadeCaixa, horas, minutos);
        }

        if (confirmation) {
            Intent intent = new Intent(this, FragmentsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else Toast.makeText(this, "Algo deu errado", Toast.LENGTH_LONG).show();
    }

    private void addDataDB(String nome, int dosagem) {
        int horas = binding.idClockSchedule.getHour();
        int minutos = binding.idClockSchedule.getMinute();
        boolean confirmation;

        if (isEdit) {
            int ativo = data.getInt(3);

            confirmation = mDataBaseAlarmsHelper.updateData(String.valueOf(alarmEditPosition + 1), ativo, nome, dosagem, horas, minutos);
        } else {
            confirmation = mDataBaseAlarmsHelper.addDataFix(nome, dosagem, horas, minutos);
        }

        if (confirmation) {
            Intent intent = new Intent(this, FragmentsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else Toast.makeText(this, "Algo deu errado", Toast.LENGTH_LONG).show();
    }
}
