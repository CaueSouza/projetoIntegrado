package com.example.projetointegrado;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.app.PendingIntent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.app.AlarmManager.RTC_WAKEUP;
import static com.example.projetointegrado.Constants.ALARM_TYPE;
import static com.example.projetointegrado.Constants.ATIVO;
import static com.example.projetointegrado.Constants.BASE_URL;
import static com.example.projetointegrado.Constants.DOMINGO;
import static com.example.projetointegrado.Constants.DOSAGEM;
import static com.example.projetointegrado.Constants.HORA;
import static com.example.projetointegrado.Constants.MEDICINE_TYPE;
import static com.example.projetointegrado.Constants.MINUTO;
import static com.example.projetointegrado.Constants.NOME_REMEDIO;
import static com.example.projetointegrado.Constants.NOTIFICATION_ID;
import static com.example.projetointegrado.Constants.PERIODO_HORA;
import static com.example.projetointegrado.Constants.PERIODO_MIN;
import static com.example.projetointegrado.Constants.QUANTIDADE;
import static com.example.projetointegrado.Constants.QUANTIDADE_BOX;
import static com.example.projetointegrado.Constants.QUARTA;
import static com.example.projetointegrado.Constants.QUINTA;
import static com.example.projetointegrado.Constants.SABADO;
import static com.example.projetointegrado.Constants.SEGUNDA;
import static com.example.projetointegrado.Constants.SEXTA;
import static com.example.projetointegrado.Constants.TERCA;
import static com.example.projetointegrado.Constants.VEZES_DIA;

public class AlarmeListAdapter extends ArrayAdapter<AlarmeItem> {

    private static final String TAG = "AlarmListAdapter";

    private Context mContext;
    private int mResource;
    private DataBaseAlarmsHelper mDataBaseAlarmsHelper;
    private String nome;
    private int horas;
    private int minutos;
    private int notificationId;

    AlarmeListAdapter(Context context, int resource, ArrayList<AlarmeItem> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
        mDataBaseAlarmsHelper = new DataBaseAlarmsHelper(context);
    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        AlarmeItem item = getItem(position);

        nome = item.getNome();
        horas = item.getHora();
        minutos = item.getMinuto();
        notificationId = item.getNotificationId();

        String horaString = horas < 10 ? "0" + horas : String.valueOf(horas);
        String minutoString = minutos < 10 ? "0" + minutos : String.valueOf(minutos);

        String horaTotal = horaString + ":" + minutoString;

        int isActive = item.getStatus();

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        ImageView imageViewStatus = convertView.findViewById(R.id.adapter_image);
        TextView timeView = convertView.findViewById(R.id.adapter_time);
        TextView textView = convertView.findViewById(R.id.adapter_text);
        ImageView imageViewDelete = convertView.findViewById(R.id.alarm_list_image);

        imageViewStatus.setImageResource(isActive == 1 ? R.drawable.ic_alarm_on_black_24dp : R.drawable.ic_alarm_off_black_24dp);
        timeView.setText(horaTotal);
        textView.setText(nome);

        View finalConvertView = convertView;
        imageViewStatus.setOnClickListener(v -> {

            Cursor data = mDataBaseAlarmsHelper.getData();
            data.move(position + 1);

            int[] dias = new int[7];
            dias[0] = data.getInt(10);
            dias[1] = data.getInt(11);
            dias[2] = data.getInt(12);
            dias[3] = data.getInt(13);
            dias[4] = data.getInt(14);
            dias[5] = data.getInt(15);
            dias[6] = data.getInt(16);

            createPostUpdateAlarm(
                    finalConvertView,
                    position,
                    data.getInt(1),
                    data.getInt(2),
                    data.getInt(3) == 1 ? 0 : 1,
                    data.getString(4),
                    data.getString(4),
                    data.getInt(5),
                    data.getInt(6),
                    data.getInt(7),
                    data.getInt(8),
                    data.getInt(9),
                    data.getInt(8),
                    data.getInt(9),
                    dias,
                    data.getInt(17),
                    data.getInt(18),
                    data.getInt(19),
                    data.getInt(20));
        });

        imageViewDelete.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setMessage(R.string.dialog_message)
                    .setTitle(R.string.dialog_title);

            builder.setPositiveButton(R.string.ok, (dialog, id) -> {
                createPostDeleteAlarm(position);
            });

            builder.setNegativeButton(R.string.cancel, (dialog, id) -> dialog.dismiss());

            AlertDialog dialog = builder.create();
            dialog.show();
        });
        return convertView;
    }

    private void createPostUpdateAlarm(View convertView, int position, int alarmType, int medicineType, int ativo, String velhoNome, String nome, int dosagem, int quantidade, int quantidadeBox, int oldHour, int oldMinute, int hora, int minuto, int[] dias, int vezes_dia, int periodo_hora, int periodo_minuto, int notificationId) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        String requestStr = formatJSONupdateAlarm(alarmType, medicineType, ativo, velhoNome, nome, dosagem, quantidade, quantidadeBox, oldHour, oldMinute, hora, minuto, dias, vezes_dia, periodo_hora, periodo_minuto, notificationId);
        JsonObject request = JsonParser.parseString(requestStr).getAsJsonObject();

        Call<JsonObject> call = jsonPlaceHolderApi.postModifyAlarm(request);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (!response.isSuccessful()) {
                    Toast.makeText(getContext(), "Um erro ocorreu", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "onResponse: " + response);
                    return;
                }

                mDataBaseAlarmsHelper.updateData(
                        String.valueOf(position + 1),
                        alarmType,
                        medicineType,
                        ativo,
                        nome,
                        dosagem,
                        quantidade,
                        quantidadeBox,
                        hora,
                        minuto,
                        dias,
                        vezes_dia,
                        periodo_hora,
                        periodo_minuto,
                        notificationId);

                ImageView imageViewStatus = convertView.findViewById(R.id.adapter_image);
                imageViewStatus.setImageResource(ativo == 1 ? R.drawable.ic_alarm_on_black_24dp : R.drawable.ic_alarm_off_black_24dp);
                AlarmeItem item = getItem(position);
                item.setStatus(item.getStatus() == 1 ? 0 : 1);
                notifyDataSetChanged();

                if (ativo == 1){
                    createAlarmIntent(alarmType, hora, minuto, dias, notificationId);
                } else {
                    cancelAlarmIntent();
                }

                Log.e(TAG, "onResponse: " + response);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "onFailure:" + t);
            }
        });
    }

    private void createAlarmIntent(int alarmType, int hora, int minuto, int[] dias, int notificationId) {
        Calendar calendar = Calendar.getInstance();

        int horaAtual = calendar.get(Calendar.HOUR_OF_DAY);
        int minutoAtual = calendar.get(Calendar.MINUTE);
        int diaAtual = calendar.get(Calendar.DAY_OF_MONTH);
        int mesAtual = calendar.get(Calendar.MONTH);
        int anoAtual = calendar.get(Calendar.YEAR);

        Calendar nextNotifTime = Calendar.getInstance();
        nextNotifTime.add(Calendar.MONTH, 1);
        nextNotifTime.set(Calendar.DATE, 1);
        nextNotifTime.add(Calendar.DATE, -1);

        if (hora < horaAtual) {
            if (diaAtual == nextNotifTime.get(Calendar.DAY_OF_MONTH)) {
                if (mesAtual == 11) {
                    anoAtual = anoAtual + 1;
                    mesAtual = 0;
                } else {
                    diaAtual = 1;
                    mesAtual = mesAtual + 1;
                }
            } else {
                diaAtual = diaAtual + 1;
            }
        } else if (hora == horaAtual) {
            if (minuto <= minutoAtual) {
                if (diaAtual == nextNotifTime.get(Calendar.DAY_OF_MONTH)) {
                    if (mesAtual == 11) {
                        anoAtual = anoAtual + 1;
                        mesAtual = 0;
                    } else {
                        diaAtual = 1;
                        mesAtual = mesAtual + 1;
                    }
                } else {
                    diaAtual = diaAtual + 1;
                }
            }
        }

        calendar.set(anoAtual, mesAtual, diaAtual, hora, minuto, 0);

        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getContext(), AlarmeReceiver.class);
        intent.putExtra("NOTIFICATION_ID", notificationId);
        intent.putExtra("ALARM_TYPE", 1);
        intent.putExtra("ALARM_HOUR", hora);
        intent.putExtra("ALARM_MINUTES", minuto);
        if (alarmType == 1) intent.putExtra("ALARM_DAYS", dias);
        intent.putExtra("MUST_PLAY_NOTIFICATION", true);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext().getApplicationContext(), notificationId, intent, 0);
        alarmManager.setExactAndAllowWhileIdle(RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    private void createPostDeleteAlarm(int position) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        String requestStr = formatJSON(nome, horas, minutos);
        JsonObject request = JsonParser.parseString(requestStr).getAsJsonObject();

        Call<JsonObject> call = jsonPlaceHolderApi.postDeleteAlarm(request);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (!response.isSuccessful()) {
                    Toast.makeText(getContext(), "Algo deu errado", Toast.LENGTH_LONG).show();
                    return;
                }
                Cursor data = mDataBaseAlarmsHelper.getData();
                data.move(position + 1);
                int isDeleted = mDataBaseAlarmsHelper.removeData(String.valueOf(data.getInt(0)));

                if (isDeleted > 0) {
                    cancelAlarmIntent();
                    AlarmeListAdapter.this.remove(getItem(position));
                    AlarmeListAdapter.this.notifyDataSetChanged();
                } else Toast.makeText(getContext(), "Algo deu errado", Toast.LENGTH_LONG).show();

                Log.e(TAG, "onResponse: " + response);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "onFailure:" + t);
            }
        });
    }

    private String formatJSON(String velhoNome, int velhaHora, int velhoMinuto) {
        final JSONObject root = new JSONObject();

        try {
            JSONObject velhoAlarme = new JSONObject();
            velhoAlarme.put(NOME_REMEDIO, velhoNome);
            velhoAlarme.put(HORA, velhaHora);
            velhoAlarme.put(MINUTO, velhoMinuto);

            root.put("id", UserIdSingleton.getInstance().getUserId());
            root.put("velhoAlarme", velhoAlarme);

            return root.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String formatJSONupdateAlarm(int alarmType, int medicineType, int ativo, String velhoNome, String nome, int dosagem, int quantidade, int quantidadeBox, int velhaHora, int velhoMinuto, int hora, int minuto, int[] dias, int vezes_dia, int periodo_hora, int periodo_minuto, int notificationId) {
        final JSONObject root = new JSONObject();

        try {
            JSONObject velhoAlarme = new JSONObject();
            velhoAlarme.put(NOME_REMEDIO, velhoNome);
            velhoAlarme.put(HORA, velhaHora);
            velhoAlarme.put(MINUTO, velhoMinuto);

            JSONObject novoAlarme = new JSONObject();
            novoAlarme.put(ALARM_TYPE, alarmType);
            novoAlarme.put(MEDICINE_TYPE, medicineType);
            novoAlarme.put(ATIVO, ativo);
            novoAlarme.put(NOME_REMEDIO, nome);
            novoAlarme.put(DOSAGEM, dosagem);
            novoAlarme.put(QUANTIDADE, quantidade);
            novoAlarme.put(QUANTIDADE_BOX, quantidadeBox);
            novoAlarme.put(HORA, hora);
            novoAlarme.put(MINUTO, minuto);
            novoAlarme.put(DOMINGO, dias[0]);
            novoAlarme.put(SEGUNDA, dias[1]);
            novoAlarme.put(TERCA, dias[2]);
            novoAlarme.put(QUARTA, dias[3]);
            novoAlarme.put(QUINTA, dias[4]);
            novoAlarme.put(SEXTA, dias[5]);
            novoAlarme.put(SABADO, dias[6]);
            novoAlarme.put(VEZES_DIA, vezes_dia);
            novoAlarme.put(PERIODO_HORA, periodo_hora);
            novoAlarme.put(PERIODO_MIN, periodo_minuto);
            novoAlarme.put(NOTIFICATION_ID, notificationId);

            root.put("id", UserIdSingleton.getInstance().getUserId());
            root.put("velhoAlarme", velhoAlarme);
            root.put("novoAlarme", novoAlarme);

            return root.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void cancelAlarmIntent(){
        Intent intent = new Intent(getContext().getApplicationContext(), AlarmeReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext().getApplicationContext(), notificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        pendingIntent.cancel();
    }
}
