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

public class FragmentAlarms extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View mainView = inflater.inflate(R.layout.fragment_alarmes, container, false);

        AlarmeItem alarme1 = new AlarmeItem("Dipirona", R.drawable.ic_alarm_on_white_24dp,"12:00");
        AlarmeItem alarme2 = new AlarmeItem("Epocler", R.drawable.ic_alarm_on_white_24dp, "9:50");

        ArrayList<AlarmeItem> alarmes = new ArrayList<>();
        alarmes.add(alarme1);
        alarmes.add(alarme2);

        ListView mListView = (ListView) mainView.findViewById(R.id.alarmes_list_view);
        AlarmeListAdapter adapter = new AlarmeListAdapter(getContext(), R.layout.alarmes_list_item, alarmes);
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener((parent, view, position, id) -> {
            changeListItemImage(alarmes, position);
            adapter.notifyDataSetChanged();
        });

        FloatingActionButton fab = (FloatingActionButton) mainView.findViewById(R.id.fab_alarmes_fragment);
        fab.setOnClickListener(view -> {
            AlarmeItem alarme = new AlarmeItem("Dipirona", R.drawable.ic_alarm_on_white_24dp,"12:00");
            alarmes.add(alarme);
            adapter.notifyDataSetChanged();
        });
        return mainView;
    }

    private void changeListItemImage(ArrayList<AlarmeItem> alarmes, int position){
        int image = alarmes.get(position).getImagem() == R.drawable.ic_alarm_on_white_24dp ? R.drawable.ic_alarm_off_white_24dp : R.drawable.ic_alarm_on_white_24dp;
        AlarmeItem alarmeItem = new AlarmeItem(alarmes.get(position).getNome(), image, alarmes.get(position).getHorario());
        alarmes.remove(position);
        alarmes.add(position, alarmeItem);
    }
}
