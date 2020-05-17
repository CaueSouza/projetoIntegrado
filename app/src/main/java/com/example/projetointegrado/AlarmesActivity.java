package com.example.projetointegrado;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projetointegrado.databinding.ActivityAlarmesBinding;

import java.util.ArrayList;

public class AlarmesActivity extends AppCompatActivity {

    ActivityAlarmesBinding binding;
    private static final String TAG = "AlarmeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAlarmesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ListView mListView = (ListView) binding.listView;

        AlarmeItem alarme1 = new AlarmeItem("Dipirona", R.drawable.ic_alarm_on_black_24dp,"12:00");
        AlarmeItem alarme2 = new AlarmeItem("Epocler", R.drawable.ic_alarm_on_black_24dp, "9:50");

        ArrayList<AlarmeItem> alarmes = new ArrayList<>();
        alarmes.add(alarme1);
        alarmes.add(alarme2);

        AlarmeListAdapter adapter = new AlarmeListAdapter(this, R.layout.adapter_view_layout, alarmes);
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener((parent, view, position, id) -> {
            changeListItemImage(alarmes, position);
            adapter.notifyDataSetChanged();
        });
    }

    private void changeListItemImage(ArrayList<AlarmeItem> alarmes, int position){
        int image = alarmes.get(position).getImagem() == R.drawable.ic_alarm_on_black_24dp ? R.drawable.ic_alarm_off_black_24dp : R.drawable.ic_alarm_on_black_24dp;
        AlarmeItem alarmeItem = new AlarmeItem(alarmes.get(position).getNome(), image, alarmes.get(position).getHorario());
        alarmes.remove(position);
        alarmes.add(position, alarmeItem);
    }
}
