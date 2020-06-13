package com.example.projetointegrado;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projetointegrado.databinding.ActivityIntervaloHorarioBinding;

import java.util.Calendar;

import static android.app.AlarmManager.RTC_WAKEUP;

public class IntervaloHorarioActivity extends AppCompatActivity {

    private ActivityIntervaloHorarioBinding binding;
    private DataBaseAlarmsHelper mDataBaseAlarmsHelper;
    private boolean isEdit;
    private int alarmEditPosition;
    private Cursor data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityIntervaloHorarioBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setTitle(R.string.action_button_interval);

        isEdit = getIntent().getBooleanExtra("IS_EDIT", false);
        mDataBaseAlarmsHelper = new DataBaseAlarmsHelper(this);
        data = mDataBaseAlarmsHelper.getData();

        binding.startTimeIntervalText.addTextChangedListener(MaskEditUtil.mask(binding.startTimeIntervalText, MaskEditUtil.FORMAT_HOUR));
        binding.timeIntervalText.addTextChangedListener(MaskEditUtil.mask(binding.timeIntervalText, MaskEditUtil.FORMAT_HOUR));

        if (isEdit) {
            alarmEditPosition = getIntent().getIntExtra("POSITION", -1);
            data.move(alarmEditPosition + 1);

            String hora_inicio = String.valueOf(data.getInt(8));
            String min_inicio = String.valueOf(data.getInt(9));

            if (hora_inicio.length() == 1) hora_inicio = "0" + hora_inicio;
            if (min_inicio.length() == 1) min_inicio = "0" + min_inicio;

            binding.startTimeIntervalText.setText(String.format("%s%s", hora_inicio, min_inicio));

            String hora_periodo = String.valueOf(data.getInt(18));
            String min_periodo = String.valueOf(data.getInt(19));

            if (hora_periodo.length() == 1) hora_periodo = "0" + hora_periodo;
            if (min_periodo.length() == 1) min_periodo = "0" + min_periodo;
            binding.timeIntervalText.setText(String.format("%s%s", hora_periodo, min_periodo));

            String vezes_dia = String.valueOf(data.getInt(17));
            binding.howManyTimesIntervalText.setText(vezes_dia);
        }

        binding.startTimeIntervalTextImage.setOnClickListener(v -> imageInfoClick(binding.startTimeIntervalTextImage));
        binding.howManyTimesIntervalTextImage.setOnClickListener(v -> imageInfoClick(binding.howManyTimesIntervalTextImage));
        binding.timeIntervalTextImage.setOnClickListener(v -> imageInfoClick(binding.timeIntervalTextImage));

        binding.backButtonIntervalClock.setOnClickListener(v -> finish());
        binding.nextButtonIntervalClock.setOnClickListener(v -> {
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

        if (MaskEditUtil.unmask(binding.startTimeIntervalText.getText().toString()).length() == 4 && MaskEditUtil.unmask(binding.timeIntervalText.getText().toString()).length() == 4) {
            int hora_inicio = Integer.parseInt(binding.startTimeIntervalText.getText().toString().substring(0, 2));
            int min_inicio = Integer.parseInt(binding.startTimeIntervalText.getText().toString().substring(3, 5));

            int hora_periodo = Integer.parseInt(binding.timeIntervalText.getText().toString().substring(0, 2));
            int min_periodo = Integer.parseInt(binding.timeIntervalText.getText().toString().substring(3, 5));

            String vezes_dia_str = binding.howManyTimesIntervalText.getText().toString();

            if (hora_inicio < 24 && min_inicio < 60 && hora_periodo < 24 && min_periodo < 60) {

                if (vezes_dia_str.length() < 10) {
                    int vezes_dia = Integer.parseInt(vezes_dia_str);

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
                                hora_inicio,
                                min_inicio,
                                new int[7],
                                vezes_dia,
                                hora_periodo,
                                min_periodo,
                                notificationId);
                    } else {
                        confirmation = mDataBaseAlarmsHelper.updateData(
                                String.valueOf(alarmEditPosition + 1),
                                1,
                                1,
                                1,
                                nome,
                                0,
                                quantidade,
                                quantidadeCaixa,
                                hora_inicio,
                                min_inicio,
                                new int[7],
                                vezes_dia,
                                hora_periodo,
                                min_periodo,
                                notificationId);
                    }

                    if (confirmation) {
                        //createAlarmIntent(horas, minutos);
                        Intent intent = new Intent(this, FragmentsActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    } else Toast.makeText(this, "Algo deu errado", Toast.LENGTH_LONG).show();
                } else Toast.makeText(this, "Número muito grande", Toast.LENGTH_SHORT).show();
            } else Toast.makeText(this, "Insira os dados corretamente", Toast.LENGTH_SHORT).show();
        } else Toast.makeText(this, "Insira os dados corretamente", Toast.LENGTH_SHORT).show();
    }

    private void addDataDB(String nome, int dosagem, int notificationId) {
        if (MaskEditUtil.unmask(binding.startTimeIntervalText.getText().toString()).length() == 4 && MaskEditUtil.unmask(binding.timeIntervalText.getText().toString()).length() == 4) {
            int hora_inicio = Integer.parseInt(binding.startTimeIntervalText.getText().toString().substring(0, 2));
            int min_inicio = Integer.parseInt(binding.startTimeIntervalText.getText().toString().substring(3, 5));

            int hora_periodo = Integer.parseInt(binding.timeIntervalText.getText().toString().substring(0, 2));
            int min_periodo = Integer.parseInt(binding.timeIntervalText.getText().toString().substring(3, 5));

            String vezes_dia_str = binding.howManyTimesIntervalText.getText().toString();

            if (hora_inicio < 24 && min_inicio < 60 && hora_periodo < 24 && min_periodo < 60) {

                if (vezes_dia_str.length() < 10) {
                    int vezes_dia = Integer.parseInt(vezes_dia_str);

                    boolean confirmation;
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
                                hora_inicio,
                                min_inicio,
                                new int[7],
                                vezes_dia,
                                hora_periodo,
                                min_periodo,
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
                                hora_inicio,
                                min_inicio,
                                new int[7],
                                vezes_dia,
                                hora_periodo,
                                min_periodo,
                                notificationId);
                    }

                    if (confirmation) {
                        //createAlarmIntent(horas, minutos);
                        Intent intent = new Intent(this, FragmentsActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    } else Toast.makeText(this, "Algo deu errado", Toast.LENGTH_LONG).show();
                } else Toast.makeText(this, "Número muito grande", Toast.LENGTH_SHORT).show();
            } else Toast.makeText(this, "Insira os dados corretamente", Toast.LENGTH_SHORT).show();
        } else Toast.makeText(this, "Insira os dados corretamente", Toast.LENGTH_SHORT).show();
    }

    public void imageInfoClick(ImageView imageView) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.info_dialog_title_text);

        if (binding.startTimeIntervalTextImage.equals(imageView)) {
            builder.setMessage(R.string.dialog_text_start_time_info);
        } else if (binding.howManyTimesIntervalTextImage.equals(imageView)) {
            builder.setMessage(R.string.dialog_text_how_many_times_info);
        } else if (binding.timeIntervalTextImage.equals(imageView)) {
            builder.setMessage(R.string.dialog_text_time_interval_info);
        }

        builder.setPositiveButton(R.string.ok, (dialog, id) -> dialog.dismiss());

        builder.create().show();
    }

    private void createAlarmIntent(int horas, int minutos) {
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
        intent.putExtra("NOTIFICATION_ID", 1);
        intent.putExtra("ALARM_TYPE", 2);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);
        alarmManager.setExactAndAllowWhileIdle(RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        //every 20 min -> 3° param = 1000 * 60 * 20
    }
}