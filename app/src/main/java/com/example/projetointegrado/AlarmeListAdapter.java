package com.example.projetointegrado;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;

public class AlarmeListAdapter extends ArrayAdapter<AlarmeFixItem> {

    private static final String TAG = "AlarmListAdapter";

    private Context mContext;
    private int mResource;
    private DataBaseAlarmsHelper mDataBaseAlarmsHelper;

    AlarmeListAdapter(Context context, int resource, ArrayList<AlarmeFixItem> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
        mDataBaseAlarmsHelper = new DataBaseAlarmsHelper(context);
    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Object itemObj = getItem(position);
        AlarmeFixItem item;

        if (itemObj instanceof AlarmeFixItem) {
            item = (AlarmeFixItem) itemObj;
        } else {
            item = (AlarmeFixItem) itemObj;
        }

        String nome = item.getNome();
        int type = item instanceof AlarmeFixItem ? 1 : 2;
        int dosagem = item.getDosagem();
        int horas = item.getHora();
        int minutos = item.getMinuto();
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

        imageViewStatus.setImageResource(isActive == 1 ? R.drawable.ic_alarm_on_white_24dp : R.drawable.ic_alarm_off_white_24dp);
        timeView.setText(horaTotal);
        textView.setText(nome);

        imageViewStatus.setOnClickListener(v -> {
            item.setStatus(item.getStatus() == 1 ? 0 : 1);
            boolean isUpdated = mDataBaseAlarmsHelper.updateData(String.valueOf(position + 1), type, item.getStatus(), nome, dosagem, horas, minutos);

            if (isUpdated) {
                imageViewStatus.setImageResource(isActive == 1 ? R.drawable.ic_alarm_on_white_24dp : R.drawable.ic_alarm_off_white_24dp);
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
