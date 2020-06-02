package com.example.projetointegrado;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projetointegrado.databinding.ActivityCadastrarAlarmeBinding;

public class CadastrarAlarmeActivity extends AppCompatActivity {

    private ActivityCadastrarAlarmeBinding binding;
    private DataBaseAlarmsHelper mDataBaseAlarmsHelper;
    private boolean isEdit;
    private int alarmEditPosition;
    private Cursor data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCadastrarAlarmeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(R.string.register_alarm_title);

        mDataBaseAlarmsHelper = new DataBaseAlarmsHelper(this);
        data = mDataBaseAlarmsHelper.getData();

        isEdit = getIntent().getBooleanExtra("IS_EDIT", false);

        if (isEdit) {
            alarmEditPosition = getIntent().getIntExtra("POSITION", -1);
            data.move(alarmEditPosition + 1);

            binding.nameInfMedicine.setText(data.getString(4));
            if (data.getInt(2) == 1){
                binding.radioButtonMedicineTypePill.setChecked(true);
                binding.radioButtonMedicineTypeLiquid.setChecked(false);
                binding.infDosage.setVisibility(View.GONE);
                binding.infQuantity.setVisibility(View.VISIBLE);
                binding.infBoxQuantityLayout.setVisibility(View.VISIBLE);
                binding.infQuantity.setText(String.valueOf(data.getInt(6)));
                binding.infBoxQuantity.setText(String.valueOf(data.getInt(7)));
            } else if (data.getInt(2) == 2){
                binding.radioButtonMedicineTypeLiquid.setChecked(true);
                binding.radioButtonMedicineTypePill.setChecked(false);
                binding.infDosage.setVisibility(View.VISIBLE);
                binding.infQuantity.setVisibility(View.GONE);
                binding.infBoxQuantityLayout.setVisibility(View.GONE);
                binding.infDosage.setText(String.valueOf(data.getInt(5)));
            }

            if (data.getInt(1) == 1){
                binding.radioButtonRegisterMedicineFixtime.setChecked(true);
                binding.radioButtonRegisterMedicineInterval.setChecked(false);
            } else if (data.getInt(1) == 2){
                binding.radioButtonRegisterMedicineInterval.setChecked(true);
                binding.radioButtonRegisterMedicineFixtime.setChecked(false);
            }
        } else {
            binding.radioButtonMedicineTypePill.setChecked(true);
        }

        binding.backButtonRegisterMedicine.setOnClickListener(v -> finish());

        binding.nextButtonRegisterMedicine.setOnClickListener(v -> {
            Class activity = binding.radioButtonRegisterMedicineFixtime.isChecked() ? HorarioFixActivity.class : binding.radioButtonRegisterMedicineInterval.isChecked() ? IntervaloHorarioActivity.class : null;

            if (activity != null) {
                if (activity == HorarioFixActivity.class) callHorarioFixActivity(activity);
                else callIntervaloHorarioActivity(activity);
            }
        });

        binding.infNameInfo.setOnClickListener(v -> imageInfoClick(binding.infNameInfo));
        binding.infBoxQuantityInfo.setOnClickListener(v -> imageInfoClick(binding.infBoxQuantityInfo));
        binding.infQuantityInfo.setOnClickListener(v -> imageInfoClick(binding.infQuantityInfo));

        binding.radioButtonMedicineTypePill.setOnClickListener(v -> {
            binding.infDosage.setVisibility(View.GONE);
            binding.infQuantity.setVisibility(View.VISIBLE);
            binding.infBoxQuantityLayout.setVisibility(View.VISIBLE);
        });

        binding.radioButtonMedicineTypeLiquid.setOnClickListener(v -> {
            binding.infDosage.setVisibility(View.VISIBLE);
            binding.infQuantity.setVisibility(View.GONE);
            binding.infBoxQuantityLayout.setVisibility(View.GONE);
        });

    }

    private void callHorarioFixActivity(Class activity) {
        String nome = binding.nameInfMedicine.getText().toString();

        if (binding.radioButtonMedicineTypePill.isChecked()){
            String quantidade = binding.infQuantity.getText().toString();
            String quantidadeCaixa = binding.infBoxQuantity.getText().toString();

            if (!nome.isEmpty() && !quantidade.isEmpty() && !quantidadeCaixa.isEmpty()) {
                Intent intent = new Intent(this, activity);
                if (isEdit) {
                    intent.putExtra("IS_EDIT", true);
                    intent.putExtra("POSITION", alarmEditPosition);
                    intent.putExtra("MEDICINE_HORA", data.getInt(8));
                    intent.putExtra("MEDICINE_MINUTO", data.getInt(9));
                }

                intent.putExtra("MEDICINE_TYPE", 1);
                intent.putExtra("MEDICINE_QUANTITY", Integer.parseInt(quantidade));
                intent.putExtra("MEDICINE_BOX_QUANTITY", Integer.parseInt(quantidadeCaixa));
                intent.putExtra("MEDICINE_NAME", nome);
                startActivity(intent);
            }
        } else if (binding.radioButtonMedicineTypeLiquid.isChecked()){
            String dosagem = binding.infDosage.getText().toString();

            if (!nome.isEmpty() && !dosagem.isEmpty()){
                Intent intent = new Intent(this, activity);
                if (isEdit) {
                    intent.putExtra("IS_EDIT", true);
                    intent.putExtra("POSITION", alarmEditPosition);
                    intent.putExtra("MEDICINE_HORA", data.getInt(8));
                    intent.putExtra("MEDICINE_MINUTO", data.getInt(9));
                }

                intent.putExtra("MEDICINE_TYPE", 2);
                intent.putExtra("MEDICINE_NAME", nome);
                intent.putExtra("MEDICINE_DOSAGE", Integer.parseInt(dosagem));
                startActivity(intent);
            }
        }
    }

    public void callIntervaloHorarioActivity(Class activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }

    public void imageInfoClick(ImageView imageView){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.info_dialog_title_text);

        if (binding.infNameInfo.equals(imageView)) {
            builder.setMessage(R.string.dialog_text_medicine_name_info);
        } else if (binding.infQuantityInfo.equals(imageView)) {
            if (binding.radioButtonMedicineTypePill.isChecked()){
                builder.setMessage(R.string.dialog_text_medicine_dosage_info);
            } else {
                builder.setMessage(R.string.dialog_text_medicine_quantity_info);
            }
        } else if (binding.infBoxQuantityInfo.equals(imageView)) {
            builder.setMessage(R.string.dialog_text_medicine_box_quantity_info);
        }

        builder.setPositiveButton(R.string.ok, (dialog, id) -> dialog.dismiss());

        builder.create().show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
