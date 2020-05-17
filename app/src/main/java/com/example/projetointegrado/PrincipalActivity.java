package com.example.projetointegrado;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
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

        loadFragment(new FragmentAlarms());

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
    }

    public void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.commit();
    }
}
