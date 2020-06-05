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
                int medTipo = data.getInt(2);
                int status = data.getInt(3);
                String nome = data.getString(4);
                int horas = data.getInt(8);
                int minutos = data.getInt(9);

                int []dias = new int[7];
                dias[0] = data.getInt(10);
                dias[1] = data.getInt(11);
                dias[2] = data.getInt(12);
                dias[3] = data.getInt(13);
                dias[4] = data.getInt(14);
                dias[5] = data.getInt(15);
                dias[6] = data.getInt(16);

                AlarmeFixItem alarme;

                if (medTipo == 1){
                    int quantidade = data.getInt(6);
                    int quantidadeCaixa = data.getInt(7);
                    alarme = new AlarmeFixItem(status, nome, quantidade, quantidadeCaixa, horas, minutos, dias);
                    alarmes.add(alarme);
                } else if (medTipo == 2){
                    int dosagem = data.getInt(5);
                    alarme = new AlarmeFixItem(status, nome, dosagem, horas, minutos, dias);
                    alarmes.add(alarme);
                }
            } else {
                //TODO: IMPLEMENTAR TIPO INTERVAL
            }
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
