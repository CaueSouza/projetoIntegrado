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

public class CaixaListAdapter extends ArrayAdapter<CaixaItem> {

    private static final String TAG = "CaixaListAdapter";

    private Context mContext;
    private int mResource;

    /**
     * Default constructor for the AlarmeListAdapter
     *
     * @param context
     * @param resource
     * @param objects
     */
    public CaixaListAdapter(Context context, int resource, ArrayList<CaixaItem> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String nome = getItem(position).getNome();
        String IP = getItem(position).getIP();

        CaixaItem caixa = new CaixaItem(nome, IP);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView nameView = (TextView) convertView.findViewById(R.id.box_name);
        TextView IpView = (TextView) convertView.findViewById(R.id.box_ip);

        nameView.setText(nome);
        IpView.setText(IP);

        ImageView imageView = (ImageView) convertView.findViewById(R.id.caixa_list_image);
        imageView.setOnClickListener(v -> {
            CaixaListAdapter.this.remove(getItem(position));
            CaixaListAdapter.this.notifyDataSetChanged();
        });

        return convertView;
    }
}
