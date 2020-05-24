package com.example.projetointegrado;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
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

public class CaixaListAdapter extends ArrayAdapter<CaixaItem> {

    private static final String TAG = "CaixaListAdapter";

    private Context mContext;
    private int mResource;
    private DataBaseBoxHelper mDataBaseBoxHelper;

    CaixaListAdapter(Context context, int resource, ArrayList<CaixaItem> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
        mDataBaseBoxHelper = new DataBaseBoxHelper(context);
    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String nome = getItem(position).getNome();
        String IP = getItem(position).getIP();

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView nameView = convertView.findViewById(R.id.box_name);
        TextView IpView = convertView.findViewById(R.id.box_ip);

        nameView.setText(nome);
        IpView.setText(IP);

        ImageView imageView = convertView.findViewById(R.id.caixa_list_image);
        imageView.setOnClickListener(v -> {
            int isDeleted = mDataBaseBoxHelper.removeData(String.valueOf(position + 1));

            if (isDeleted > 0) {
                CaixaListAdapter.this.remove(getItem(position));
                CaixaListAdapter.this.notifyDataSetChanged();
            } else Toast.makeText(getContext(), "Algo deu errado", Toast.LENGTH_LONG).show();
        });

        return convertView;
    }
}
