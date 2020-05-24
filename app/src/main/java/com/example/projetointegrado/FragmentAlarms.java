package com.example.projetointegrado;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.projetointegrado.databinding.FragmentAlarmesBinding;

import java.util.ArrayList;

public class FragmentAlarms extends Fragment {

    private FragmentAlarmesBinding binding;
    private DataBaseAlarmsHelper mDataBaseAlarmsHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentAlarmesBinding.inflate(getLayoutInflater());
        mDataBaseAlarmsHelper = new DataBaseAlarmsHelper(getActivity());

        Cursor data = mDataBaseAlarmsHelper.getData();
        ArrayList<AlarmeFixItem> alarmes = new ArrayList<>();

        while (data.moveToNext()) {
            if (data.getInt(1) == 1) { //tipo fixo
                int status = data.getInt(2);
                String nome = data.getString(3);
                int dosagem = data.getInt(4);
                int horas = data.getInt(5);
                int minutos = data.getInt(6);

                AlarmeFixItem alarme = new AlarmeFixItem(status, nome, dosagem, horas, minutos);
                alarmes.add(alarme);
            } else {
                //TODO: IMPLEMENTAR TIPO INTERVAL
            }
        }

        AlarmeListAdapter adapter = new AlarmeListAdapter(getContext(), R.layout.alarmes_list_item, alarmes);
        binding.alarmesListView.setAdapter(adapter);

        binding.alarmesListView.setOnItemClickListener((parent, view, position, id) -> {
            //TODO OPEN EDITOR TO EDIT ALARM
            Intent intent = new Intent(getContext(), CadastrarAlarmeActivity.class);
            intent.putExtra("IS_EDIT", true);
            intent.putExtra("POSITION", position);
            startActivity(intent);
        });

        return binding.getRoot();
    }
}
