package com.example.projetointegrado;

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

public class AlarmeListAdapter extends ArrayAdapter<AlarmeItem> {

    private static final String TAG = "AlarmListAdapter";

    private Context mContext;
    private int mResource;

    /**
     * Default constructor for the AlarmeListAdapter
     *
     * @param context
     * @param resource
     * @param objects
     */
    public AlarmeListAdapter(Context context, int resource, ArrayList<AlarmeItem> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String name = getItem(position).getNome();
        String horario = getItem(position).getHorario();
        int imagem = getItem(position).getImagem();

        AlarmeItem alarme = new AlarmeItem(name, imagem, horario);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        ImageView imageView = (ImageView) convertView.findViewById(R.id.adapter_image);
        TextView timeView = (TextView) convertView.findViewById(R.id.adapter_time);
        TextView textView = (TextView) convertView.findViewById(R.id.adapter_text);

        imageView.setImageResource(imagem);
        timeView.setText(horario);
        textView.setText(name);

        return convertView;
    }
}
