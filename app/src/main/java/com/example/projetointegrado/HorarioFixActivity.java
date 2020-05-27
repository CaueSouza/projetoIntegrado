package com.example.projetointegrado;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projetointegrado.databinding.ActivityHorarioFixBinding;

import java.util.Calendar;

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

            binding.idClockSchedule.setHour(data.getInt(5));
            binding.idClockSchedule.setMinute(data.getInt(6));
        }

        binding.backButtonRegisterMedicine.setOnClickListener(v -> finish());

        binding.nextButtonRegisterMedicine.setOnClickListener(v -> {
            String nome = getIntent().getStringExtra("MEDICINE_NAME");
            int dosagem = getIntent().getIntExtra("MEDICINE_DOSAGE", 0);

            addDataDB(nome, dosagem);
        });
    }

    private void addDataDB(String nome, int dosagem) {
        int horas = binding.idClockSchedule.getHour();
        int minutos = binding.idClockSchedule.getMinute();
        boolean confirmation;

        if (isEdit) {
            int type = data.getInt(1);
            int ativo = data.getInt(2);

            confirmation = mDataBaseAlarmsHelper.updateData(String.valueOf(alarmEditPosition + 1), type, ativo, nome, dosagem, horas, minutos);
        } else {
            confirmation = mDataBaseAlarmsHelper.addData(1, nome, dosagem, horas, minutos);
        }

        if (confirmation) {
            AlarmManager alarmManager= (AlarmManager) this.getSystemService(ALARM_SERVICE);
            Intent broadcastIntent = new Intent(this, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, data.getInt(0), broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, horas);
            calendar.set(Calendar.MINUTE, minutos);

            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

            Intent intent = new Intent(this, FragmentsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else Toast.makeText(this, "Algo deu errado", Toast.LENGTH_LONG).show();
    }
}
