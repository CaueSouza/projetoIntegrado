package com.example.projetointegrado;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.projetointegrado.databinding.ActivityFragmentsBinding;

public class FragmentsActivity extends AppCompatActivity {

    private ActivityFragmentsBinding binding;
    private static final String TAG = "AlarmeActivity";
    private Fragment actualFragment;

    //TODO REMOVE AFTER CREATING ACTIVITY TO REGISTER A NEW BOX
    DataBaseBoxHelper mDataBaseBoxHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFragmentsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //TODO REMOVE AFTER CREATING ACTIVITY TO REGISTER A NEW BOX
        mDataBaseBoxHelper = new DataBaseBoxHelper(this);

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

            if (actualFragment instanceof FragmentCaixas) {
                //TODO CALL ACTIVITY TO REGISTER A NEW BOX
//                Intent intent = new Intent(this, CadastrarCaixaActivity.class);
//                startActivity(intent);

                boolean insertData = mDataBaseBoxHelper.addData("Caixa x", "192.168.1.x");

                if (insertData) {
                    Toast.makeText(this, "Caixa Registrada com Sucesso", Toast.LENGTH_LONG).show();
                    reloadFragment(actualFragment);
                } else {
                    Toast.makeText(this, "Algo deu errado", Toast.LENGTH_LONG).show();
                }
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

    public void reloadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.detach(fragment);
        transaction.attach(fragment);
        transaction.commit();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
