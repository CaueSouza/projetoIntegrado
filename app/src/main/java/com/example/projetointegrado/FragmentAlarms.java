package com.example.projetointegrado;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.projetointegrado.databinding.FragmentAlarmesBinding;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.projetointegrado.Constants.BASE_URL;

public class FragmentAlarms extends Fragment {

    private static final String TAG = "FragmentAlarms";
    private FragmentAlarmesBinding binding;
    private DataBaseAlarmsHelper mDataBaseAlarmsHelper;
    private JsonPlaceHolderApi jsonPlaceHolderApi;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentAlarmesBinding.inflate(getLayoutInflater());
        mDataBaseAlarmsHelper = new DataBaseAlarmsHelper(getActivity());

        Cursor data = mDataBaseAlarmsHelper.getData();
        ArrayList<AlarmeItem> alarmes = new ArrayList<>();

        while (data.moveToNext()) {
            int status = data.getInt(3);
            String nome = data.getString(4);
            int horas = data.getInt(8);
            int minutos = data.getInt(9);
            int notificationId = data.getInt(20);

            AlarmeItem alarme;
            alarme = new AlarmeItem(status, nome, horas, minutos, notificationId);
            alarmes.add(alarme);
        }

        AlarmeListAdapter adapter = new AlarmeListAdapter(getContext(), R.layout.alarmes_list_item, alarmes);
        binding.alarmesListView.setAdapter(adapter);

        binding.alarmesListView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(getContext(), CadastrarAlarmeActivity.class);
            intent.putExtra("IS_EDIT", true);
            intent.putExtra("POSITION", position);
            startActivity(intent);
        });

        return binding.getRoot();
    }
}
