package com.example.projetointegrado;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projetointegrado.databinding.ActivityHorarioFixBinding;

import java.util.Calendar;

import static android.app.AlarmManager.RTC_WAKEUP;

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

            binding.sundayDay.setChecked(data.getInt(10) == 1);
            binding.mondayDay.setChecked(data.getInt(11) == 1);
            binding.tuesdayDay.setChecked(data.getInt(12) == 1);
            binding.wednesdayDay.setChecked(data.getInt(13) == 1);
            binding.thursdayDay.setChecked(data.getInt(14) == 1);
            binding.fridayDay.setChecked(data.getInt(15) == 1);
            binding.saturdayDay.setChecked(data.getInt(16) == 1);
            binding.idClockSchedule.setHour(data.getInt(8));
            binding.idClockSchedule.setMinute(data.getInt(9));
        }

        binding.backButtonRegisterMedicine.setOnClickListener(v -> finish());

        binding.nextButtonRegisterMedicine.setOnClickListener(v -> {
            int medTipo = getIntent().getIntExtra("MEDICINE_TYPE", 0);
            String nome = getIntent().getStringExtra("MEDICINE_NAME");
            int notificationId = getIntent().getIntExtra("NOTIFICATION_ID", 0);

            if (medTipo == 1) {
                int quantidade = getIntent().getIntExtra("MEDICINE_QUANTITY", 0);
                int quantidadeCaixa = getIntent().getIntExtra("MEDICINE_BOX_QUANTITY", 0);
                addDataDB(nome, quantidade, quantidadeCaixa, notificationId);
            } else if (medTipo == 2) {
                int dosagem = getIntent().getIntExtra("MEDICINE_DOSAGE", 0);
                addDataDB(nome, dosagem, notificationId);
            }
        });
    }

    private void addDataDB(String nome, int quantidade, int quantidadeCaixa, int notificationId) {
        int horas = binding.idClockSchedule.getHour();
        int minutos = binding.idClockSchedule.getMinute();

        int[] dias = new int[7];
        dias[0] = binding.sundayDay.isChecked() ? 1 : 0;
        dias[1] = binding.mondayDay.isChecked() ? 1 : 0;
        dias[2] = binding.tuesdayDay.isChecked() ? 1 : 0;
        dias[3] = binding.wednesdayDay.isChecked() ? 1 : 0;
        dias[4] = binding.thursdayDay.isChecked() ? 1 : 0;
        dias[5] = binding.fridayDay.isChecked() ? 1 : 0;
        dias[6] = binding.saturdayDay.isChecked() ? 1 : 0;

        boolean confirmation;

        if (isEdit) {
            int ativo = data.getInt(3);
            confirmation = mDataBaseAlarmsHelper.updateData(
                    String.valueOf(alarmEditPosition + 1),
                    1,
                    1,
                    ativo,
                    nome,
                    0,
                    quantidade,
                    quantidadeCaixa,
                    horas,
                    minutos,
                    dias,
                    0,
                    0,
                    0,
                    notificationId);
        } else {
            confirmation = mDataBaseAlarmsHelper.addData(
                    1,
                    2,
                    1,
                    nome,
                    0,
                    quantidade,
                    quantidadeCaixa,
                    horas,
                    minutos,
                    dias,
                    0,
                    0,
                    0,
                    notificationId);
        }

        if (confirmation) {
            createAlarmIntent(horas, minutos, dias, notificationId);
            Intent intent = new Intent(this, FragmentsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else Toast.makeText(this, "Algo deu errado", Toast.LENGTH_LONG).show();
    }

    private void addDataDB(String nome, int dosagem, int notificationId) {
        int horas = binding.idClockSchedule.getHour();
        int minutos = binding.idClockSchedule.getMinute();

        boolean confirmation;

        int[] dias = new int[7];
        dias[0] = binding.sundayDay.isChecked() ? 1 : 0;
        dias[1] = binding.mondayDay.isChecked() ? 1 : 0;
        dias[2] = binding.tuesdayDay.isChecked() ? 1 : 0;
        dias[3] = binding.wednesdayDay.isChecked() ? 1 : 0;
        dias[4] = binding.thursdayDay.isChecked() ? 1 : 0;
        dias[5] = binding.fridayDay.isChecked() ? 1 : 0;
        dias[6] = binding.saturdayDay.isChecked() ? 1 : 0;

        if (isEdit) {
            int ativo = data.getInt(3);

            confirmation = mDataBaseAlarmsHelper.updateData(
                    String.valueOf(alarmEditPosition + 1),
                    1,
                    2,
                    ativo,
                    nome,
                    dosagem,
                    0,
                    0,
                    horas,
                    minutos,
                    dias,
                    0,
                    0,
                    0,
                    notificationId);
        } else {
            confirmation = mDataBaseAlarmsHelper.addData(
                    1,
                    2,
                    1,
                    nome,
                    dosagem,
                    0,
                    0,
                    horas,
                    minutos,
                    dias,
                    0,
                    0,
                    0,
                    notificationId);
        }

        if (confirmation) {
            createAlarmIntent(horas, minutos, dias, notificationId);
            Intent intent = new Intent(this, FragmentsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else Toast.makeText(this, "Algo deu errado", Toast.LENGTH_LONG).show();
    }

    private void createAlarmIntent(int horas, int minutos, int[] dias, int notificationId) {

        //TODO CHECK AND REQUEST TO NEXT DAY
        Calendar calendar = Calendar.getInstance();
        calendar.set(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                horas,
                minutos,
                0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmeReceiver.class);
        intent.putExtra("NOTIFICATION_ID", notificationId);
        intent.putExtra("ALARM_TYPE", 1);
        intent.putExtra("ALARM_HOUR", horas);
        intent.putExtra("ALARM_MINUTES", minutos);
        intent.putExtra("ALARM_DAYS", dias);
        intent.putExtra("MUST_PLAY_NOTIFICATION", true);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, notificationId, intent, 0);
        alarmManager.setExactAndAllowWhileIdle(RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }
}
