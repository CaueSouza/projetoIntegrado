package com.example.projetointegrado;

import android.annotation.SuppressLint;
import android.content.Context;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.projetointegrado.Constants.BASE_URL;
import static com.example.projetointegrado.Constants.HORA;
import static com.example.projetointegrado.Constants.MINUTO;
import static com.example.projetointegrado.Constants.NOME_REMEDIO;

public class AlarmeListAdapter extends ArrayAdapter<AlarmeItem> {

    private static final String TAG = "AlarmListAdapter";

    private Context mContext;
    private int mResource;
    private DataBaseAlarmsHelper mDataBaseAlarmsHelper;
    private String userId;
    private ProgressBar progressBar;

    AlarmeListAdapter(Context context, int resource, ArrayList<AlarmeItem> objects, String userId) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
        this.userId = userId;
        mDataBaseAlarmsHelper = new DataBaseAlarmsHelper(context);
    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        AlarmeItem item = getItem(position);

        String nome = item.getNome();
        int horas = item.getHora();
        int minutos = item.getMinuto();
        int notificationId = item.getNotificationId();

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
        progressBar = (ProgressBar) convertView.findViewById(R.id.progress_bar_list);

        imageViewStatus.setImageResource(isActive == 1 ? R.drawable.ic_alarm_on_black_24dp : R.drawable.ic_alarm_off_black_24dp);
        timeView.setText(horaTotal);
        textView.setText(nome);

        imageViewStatus.setOnClickListener(v -> {
            item.setStatus(item.getStatus() == 1 ? 0 : 1);

            boolean isUpdated = false;
            Cursor data = mDataBaseAlarmsHelper.getData();
            data.move(position + 1);

            if (data.getInt(1) == 1) {
                int[] dias = new int[7];
                dias[0] = data.getInt(10);
                dias[1] = data.getInt(11);
                dias[2] = data.getInt(12);
                dias[3] = data.getInt(13);
                dias[4] = data.getInt(14);
                dias[5] = data.getInt(15);
                dias[6] = data.getInt(16);

                if (data.getInt(2) == 1) { //tipo pilula
                    int quantidade = data.getInt(6);
                    int quantidadeCaixa = data.getInt(7);

                    isUpdated = mDataBaseAlarmsHelper.updateData(
                            String.valueOf(position + 1),
                            1,
                            1,
                            item.getStatus(),
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
                } else if (data.getInt(2) == 2) {
                    int dosagem = data.getInt(5);

                    isUpdated = mDataBaseAlarmsHelper.updateData(
                            String.valueOf(position + 1),
                            1,
                            2,
                            item.getStatus(),
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
            } else if (data.getInt(1) == 2) {
                int vezes_dia = data.getInt(17);
                int hora_periodo = data.getInt(18);
                int min_periodo = data.getInt(19);

                if (data.getInt(2) == 1) { //tipo pilula
                    int quantidade = data.getInt(6);
                    int quantidadeCaixa = data.getInt(7);

                    isUpdated = mDataBaseAlarmsHelper.updateData(
                            String.valueOf(position + 1),
                            1,
                            1,
                            item.getStatus(),
                            nome,
                            0,
                            quantidade,
                            quantidadeCaixa,
                            horas,
                            minutos,
                            new int[7],
                            vezes_dia,
                            hora_periodo,
                            min_periodo,
                            notificationId);
                } else if (data.getInt(2) == 2) {
                    int dosagem = data.getInt(5);

                    isUpdated = mDataBaseAlarmsHelper.updateData(
                            String.valueOf(position + 1),
                            1,
                            2,
                            item.getStatus(),
                            nome,
                            dosagem,
                            0,
                            0,
                            horas,
                            minutos,
                            new int[7],
                            vezes_dia,
                            hora_periodo,
                            min_periodo,
                            notificationId);
                }
            }

            if (isUpdated) {
                imageViewStatus.setImageResource(isActive == 1 ? R.drawable.ic_alarm_on_black_24dp : R.drawable.ic_alarm_off_black_24dp);
                notifyDataSetChanged();
            } else {
                item.setStatus(item.getStatus() == 1 ? 0 : 1);
            }
        });

        imageViewDelete.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setMessage(R.string.dialog_message)
                    .setTitle(R.string.dialog_title);

            builder.setPositiveButton(R.string.ok, (dialog, id) -> {
                progressBar.setVisibility(View.VISIBLE);
                createPostDeleteAlarm(position, nome, horas, minutos);
            });

            builder.setNegativeButton(R.string.cancel, (dialog, id) -> dialog.dismiss());

            AlertDialog dialog = builder.create();
            dialog.show();
        });
        return convertView;
    }

    private void createPostDeleteAlarm(int position, String nome, int horas, int minutos) {
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
                progressBar.setVisibility(View.GONE);

                if (!response.isSuccessful()) {
                    Toast.makeText(getContext(), "Algo deu errado", Toast.LENGTH_LONG).show();
                    return;
                }
                Cursor data = mDataBaseAlarmsHelper.getData();
                data.move(position + 1);
                int isDeleted = mDataBaseAlarmsHelper.removeData(String.valueOf(data.getInt(0)));

                if (isDeleted > 0) {
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

            root.put("id", userId);
            root.put("velhoAlarme", velhoAlarme);

            return root.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
