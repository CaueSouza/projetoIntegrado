package com.example.projetointegrado;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.PendingIntent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;

public class AlarmeListAdapter extends ArrayAdapter<AlarmeItem> {



    private static final String TAG = "AlarmListAdapter";

    private Context mContext;
    private int mResource;
    private DataBaseAlarmsHelper mDataBaseAlarmsHelper;

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

                    isUpdated = mDataBaseAlarmsHelper.updateDataFix(String.valueOf(position + 1), item.getStatus(), nome, quantidade, quantidadeCaixa, horas, minutos, dias, notificationId);
                } else if (data.getInt(2) == 2) {
                    int dosagem = data.getInt(5);
                    isUpdated = mDataBaseAlarmsHelper.updateDataFix(String.valueOf(position + 1), item.getStatus(), nome, dosagem, horas, minutos, dias, notificationId);
                }
            } else if (data.getInt(1) == 2) {
                int vezes_dia = data.getInt(17);
                int hora_periodo = data.getInt(18);
                int min_periodo = data.getInt(19);

                if (data.getInt(2) == 1) { //tipo pilula
                    int quantidade = data.getInt(6);
                    int quantidadeCaixa = data.getInt(7);

                    isUpdated = mDataBaseAlarmsHelper.updateDataInterval(String.valueOf(position + 1), item.getStatus(), nome, quantidade, quantidadeCaixa, horas, minutos, vezes_dia, hora_periodo, min_periodo, notificationId);
                } else if (data.getInt(2) == 2) {
                    int dosagem = data.getInt(5);
                    isUpdated = mDataBaseAlarmsHelper.updateDataInterval(String.valueOf(position + 1), item.getStatus(), nome, dosagem, horas, minutos, vezes_dia, hora_periodo, min_periodo, notificationId);
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
                Cursor data = mDataBaseAlarmsHelper.getData();
                data.move(position + 1);

                int isDeleted = mDataBaseAlarmsHelper.removeData(String.valueOf(data.getInt(0)));

                if (isDeleted > 0) {
                    Intent intent = new Intent(getContext().getApplicationContext(), AlarmeReceiver.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext().getApplicationContext(), notificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    pendingIntent.cancel();
                    AlarmeListAdapter.this.remove(getItem(position));
                    AlarmeListAdapter.this.notifyDataSetChanged();
                } else Toast.makeText(getContext(), "Algo deu errado", Toast.LENGTH_LONG).show();
            });

            builder.setNegativeButton(R.string.cancel, (dialog, id) -> dialog.dismiss());

            AlertDialog dialog = builder.create();
            dialog.show();
        });
        return convertView;
    }
}
