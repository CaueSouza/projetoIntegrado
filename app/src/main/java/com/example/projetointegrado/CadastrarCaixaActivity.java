package com.example.projetointegrado;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.projetointegrado.databinding.ActivityCadastrarCaixaBinding;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class CadastrarCaixaActivity extends AppCompatActivity {

    private static String TAG = CadastrarCaixaActivity.class.getSimpleName();
    private ActivityCadastrarCaixaBinding binding;

    DataBaseBoxHelper mDataBaseBoxHelper;

    SurfaceView surfaceView;
    CameraSource cameraSource;
    BarcodeDetector barcodeDetector;
    boolean notRead = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCadastrarCaixaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("QR Code");

        mDataBaseBoxHelper = new DataBaseBoxHelper(this);

        surfaceView = binding.cameraPreview;

        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.QR_CODE).build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(640, 480).build();

        binding.cameraPreview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {

                try {
                    if (ActivityCompat.checkSelfPermission(CadastrarCaixaActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    cameraSource.start(holder);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                SparseArray<Barcode> qrCodes = detections.getDetectedItems();

                if (qrCodes.size() != 0) {
                    binding.warningTextView.post(() -> {
                        if (notRead) {
                            notRead = false;
                            Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                            vibrator.vibrate(200);


                            boolean insertData = mDataBaseBoxHelper.addData(qrCodes.valueAt(0).displayValue);

                            Intent resultIntent = new Intent();
                            resultIntent.putExtra("DB_RESULT", insertData);
                            setResult(RESULT_OK, resultIntent);
                            finish();
                        }
                    });
                }
            }
        });
    }
}
