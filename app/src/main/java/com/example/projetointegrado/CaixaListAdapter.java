package com.example.projetointegrado;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;

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
import static com.example.projetointegrado.Constants.ID_CAIXA;
import static com.example.projetointegrado.Constants.ID_USUARIO;
import static com.example.projetointegrado.Constants.MUDAR_USUARIO;
import static com.example.projetointegrado.Constants.NOME_CAIXA;

public class CaixaListAdapter extends ArrayAdapter<CaixaItem> {

    private static final String TAG = "CaixaListAdapter";

    private Context mContext;
    private int mResource;
    private DataBaseBoxHelper mDataBaseBoxHelper;
    private JsonPlaceHolderApi jsonPlaceHolderApi;
    private EditText editTextName;

    CaixaListAdapter(Context context, int resource, ArrayList<CaixaItem> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
        mDataBaseBoxHelper = new DataBaseBoxHelper(context);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String nome = getItem(position).getNome();

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView nameView = convertView.findViewById(R.id.box_name);
        nameView.setText(nome);

        View finalConvertView = convertView;
        ConstraintLayout constraintLayout = convertView.findViewById(R.id.caixa_list_layout);
        constraintLayout.setOnClickListener(v -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            LayoutInflater insideInflater = LayoutInflater.from(getContext());

            View view = insideInflater.inflate(R.layout.layout_dialog, parent, false);

            builder.setView(view)
                    .setTitle(R.string.dialog_change_name_title)
                    .setNegativeButton(R.string.cancel, (dialog, id) -> dialog.dismiss())
                    .setPositiveButton(R.string.ok, (dialog, which) -> {
                        Cursor data = mDataBaseBoxHelper.getData();
                        data.move(position + 1);

                        String novoNome = editTextName.getText().toString();

                        if (!novoNome.isEmpty()) {
                            createPostUpdateBox(finalConvertView, position, data.getString(1),novoNome);
                        } else {
                            Toast.makeText(getContext(), "Nome inválido", Toast.LENGTH_LONG).show();
                        }
                    });

            editTextName = view.findViewById(R.id.edit_box_name);

            AlertDialog dialog = builder.create();
            dialog.show();
        });

        ImageView imageView = convertView.findViewById(R.id.caixa_list_image);
        imageView.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

            builder.setMessage(R.string.dialog_message)
                    .setTitle(R.string.dialog_title)
                    .setPositiveButton(R.string.ok, (dialog, id) -> dialog.dismiss()/*createPostDeleteBox(position)*/)
                    .setNegativeButton(R.string.cancel, (dialog, id) -> dialog.dismiss());

            AlertDialog dialog = builder.create();
            dialog.show();
        });

        return convertView;
    }


    private void createPostUpdateBox(View convertView, int position, String idCaixa, String novoNome) {
        String requestStr = formatJSONUpdateAlarm(idCaixa, novoNome);
        JsonObject request = JsonParser.parseString(requestStr).getAsJsonObject();

        Call<JsonObject> call = jsonPlaceHolderApi.postCreateUpdateBox(request);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (!response.isSuccessful()){
                    Toast.makeText(getContext(), "Algo deu errado", Toast.LENGTH_LONG).show();
                    return;
                }

                mDataBaseBoxHelper.updateData(
                        String.valueOf(position + 1),
                        idCaixa,
                        novoNome);

                TextView nameView = convertView.findViewById(R.id.box_name);
                nameView.setText(novoNome);
                notifyDataSetChanged();

                Log.e(TAG, "onResponse: " + response);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "onFailure:" + t);
            }
        });
    }

    private String formatJSONUpdateAlarm(String idCaixa, String nome) {
        final JSONObject root = new JSONObject();

        try {
            root.put(ID_USUARIO, UserIdSingleton.getInstance().getUserId());
            root.put(ID_CAIXA, String.valueOf(idCaixa));
            root.put(NOME_CAIXA, String.valueOf(nome));
            root.put(MUDAR_USUARIO, String.valueOf(true));

            return root.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

//    private void createPostDeleteBox(int position/*, nome, horas, minutos*/){
//        String requestStr = formatJSON(/*nome, horas, minutos*/);
//        JsonObject request = JsonParser.parseString(requestStr).getAsJsonObject();
//
//        Call<JsonObject> call = jsonPlaceHolderApi.postDeleteAlarm(request);
//
//        call.enqueue(new Callback<JsonObject>() {
//            @Override
//            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//
//                if (!response.isSuccessful()) {
//                    Toast.makeText(getContext(), "Algo deu errado", Toast.LENGTH_LONG).show();
//                    return;
//                }
//                Cursor data = mDataBaseBoxHelper.getData();
//                data.move(position + 1);
//                int isDeleted = mDataBaseBoxHelper.removeData(String.valueOf(data.getInt(0)));
//
//                if (isDeleted > 0) {
//                    CaixaListAdapter.this.remove(getItem(position));
//                    CaixaListAdapter.this.notifyDataSetChanged();
//                } else Toast.makeText(getContext(), "Algo deu errado", Toast.LENGTH_LONG).show();
//
//                Log.e(TAG, "onResponse: " + response);
//            }
//
//            @Override
//            public void onFailure(Call<JsonObject> call, Throwable t) {
//                Log.e(TAG, "onFailure:" + t);
//            }
//        });
//    }

//    private String formatJSON(/*String velhoNome, int velhaHora, int velhoMinuto*/) {
//        final JSONObject root = new JSONObject();
//
//        try {
//            JSONObject velhoAlarme = new JSONObject();
//            velhoAlarme.put(NOME_REMEDIO, velhoNome);
//            velhoAlarme.put(HORA, String.valueOf(velhaHora));
//            velhoAlarme.put(MINUTO, String.valueOf(velhoMinuto));
//
//            root.put(ID_USUARIO, UserIdSingleton.getInstance().getUserId());
//            root.put("velhoAlarme", velhoAlarme);
//
//            return root.toString();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
}