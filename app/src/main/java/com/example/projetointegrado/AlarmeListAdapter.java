package com.example.projetointegrado;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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

        String name = item.getNome();
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

        ImageView imageView = convertView.findViewById(R.id.adapter_image);
        TextView timeView = convertView.findViewById(R.id.adapter_time);
        TextView textView = convertView.findViewById(R.id.adapter_text);

        imageView.setImageResource(isActive == 1 ? R.drawable.ic_alarm_on_white_24dp : R.drawable.ic_alarm_off_white_24dp);
        timeView.setText(horaTotal);
        textView.setText(name);

        imageView.setOnClickListener(v -> {
            item.setStatus(item.getStatus() == 1 ? 0 : 1);
            boolean isUpdated = mDataBaseAlarmsHelper.updateData(String.valueOf(position + 1), type, item.getStatus(), name, dosagem, horas, minutos);

            if (isUpdated) {
                imageView.setImageResource(isActive == 1 ? R.drawable.ic_alarm_on_white_24dp : R.drawable.ic_alarm_off_white_24dp);
                notifyDataSetChanged();
            } else {
                item.setStatus(item.getStatus() == 1 ? 0 : 1);
            }
        });

        return convertView;
    }
}
