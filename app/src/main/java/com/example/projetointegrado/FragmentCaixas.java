package com.example.projetointegrado;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class FragmentCaixas extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.fragment_caixas, container, false);

        CaixaItem caixa1 = new CaixaItem("Caixa um","192.168.0.1");
        CaixaItem caixa2 = new CaixaItem("Caixa dois","192.168.0.2");

        ArrayList<CaixaItem> caixas = new ArrayList<>();
        caixas.add(caixa1);
        caixas.add(caixa2);

        ListView mListView = mainView.findViewById(R.id.caixas_list_view);
        CaixaListAdapter adapter = new CaixaListAdapter(getContext(), R.layout.caixas_list_item, caixas);
        mListView.setAdapter(adapter);

        FloatingActionButton fab = mainView.findViewById(R.id.fab_caixas_fragment);
        fab.setOnClickListener(view -> {
            CaixaItem caixa = new CaixaItem("Caixa Extra", "192.168.0.x");
            caixas.add(caixa);
            adapter.notifyDataSetChanged();
        });
        return mainView;
    }
}
