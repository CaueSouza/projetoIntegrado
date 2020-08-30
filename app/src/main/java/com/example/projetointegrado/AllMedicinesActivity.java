package com.example.projetointegrado;

import android.os.Bundle;
import android.widget.ArrayAdapter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projetointegrado.databinding.ActivityAllMedicinesBinding;

import java.util.ArrayList;

public class AllMedicinesActivity extends AppCompatActivity {
    private ActivityAllMedicinesBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAllMedicinesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ArrayList<String> allMedicines = getIntent().getStringArrayListExtra("MEDICINE_LIST");
        String[] allMedicineStrings = new String[allMedicines.size()];

        for (int i = 0; i < allMedicines.size(); i++) {
            allMedicineStrings[i] = allMedicines.get(i);
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, allMedicines);

        binding.allMedListView.setAdapter(arrayAdapter);
    }
}
