package com.example.projetointegrado;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.projetointegrado.databinding.ActivityPrincipalBinding;

public class PrincipalActivity extends AppCompatActivity {

    private ActivityPrincipalBinding binding;
    private static final String TAG = "AlarmeActivity";
    private Fragment actualFragment;

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
                    binding.fabFragment.setImageDrawable(getBaseContext().getDrawable(R.drawable.ic_alarm_add_white_24dp));
                    actualFragment = new FragmentAlarms();
                    loadFragment(actualFragment);
                    break;
                case R.id.nav_caixas:
                    binding.fabFragment.setImageDrawable(getBaseContext().getDrawable(R.drawable.ic_add_box_white_24dp));
                    actualFragment = new FragmentCaixas();
                    loadFragment(actualFragment);
                    break;
            }

            return true;
        });

        binding.fabFragment.setOnClickListener(v -> {
            if (actualFragment instanceof FragmentAlarms) {
                Intent intent = new Intent(this, CadastrarAlarmeActivity.class);
                startActivity(intent);
            }
        });

        binding.bottomNavigation.setSelectedItemId(R.id.nav_alarmes);
        binding.bottomNavigation.performClick();
    }

    public void loadFragment(Fragment fragment) {
        if (fragment instanceof FragmentAlarms)
            getSupportActionBar().setTitle(R.string.menu_alarme);
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
