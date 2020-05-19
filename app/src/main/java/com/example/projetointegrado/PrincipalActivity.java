package com.example.projetointegrado;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.projetointegrado.databinding.ActivityPrincipalBinding;

public class PrincipalActivity extends AppCompatActivity {

    ActivityPrincipalBinding binding;
    private static final String TAG = "AlarmeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPrincipalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        binding.bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_alarmes:
                    loadFragment(new FragmentAlarms());
                    break;
                case R.id.nav_caixas:
                    loadFragment(new FragmentCaixas());
                    break;
            }

            return true;
        });

        binding.bottomNavigation.setSelectedItemId(R.id.nav_alarmes);
        binding.bottomNavigation.performClick();
    }

    public void loadFragment(Fragment fragment) {
        if (fragment instanceof FragmentAlarms) getSupportActionBar().setTitle(R.string.menu_alarme);
        else getSupportActionBar().setTitle(R.string.menu_caixas);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.commit();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
