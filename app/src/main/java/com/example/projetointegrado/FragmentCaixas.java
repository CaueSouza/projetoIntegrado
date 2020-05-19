package com.example.projetointegrado;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.projetointegrado.databinding.FragmentCaixasBinding;

import java.util.ArrayList;

public class FragmentCaixas extends Fragment {

    private FragmentCaixasBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentCaixasBinding.inflate(getLayoutInflater());

        CaixaItem caixa1 = new CaixaItem("Caixa um", "192.168.0.1");
        CaixaItem caixa2 = new CaixaItem("Caixa dois", "192.168.0.2");

        ArrayList<CaixaItem> caixas = new ArrayList<>();
        caixas.add(caixa1);
        caixas.add(caixa2);

        CaixaListAdapter adapter = new CaixaListAdapter(getContext(), R.layout.caixas_list_item, caixas);
        binding.caixasListView.setAdapter(adapter);

        return binding.getRoot();
    }
}
