package com.example.projetointegrado;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DataBaseAlarmsHelper extends SQLiteOpenHelper {

    private static final String TABLE_NAME = "alarms_table";
    private static final String COL0 = "ID";
    private static final String COL1 = "alarm_type";//1 == fixo 2 == intervalo
    private static final String COL2 = "medicine_type";//1 == pilula 2 == liquid
    private static final String COL3 = "ativo";// 1 == ativo 0 == inativo
    private static final String COL4 = "nome_remedio";
    private static final String COL5 = "dosagem";
    private static final String COL6 = "quantidade";
    private static final String COL7 = "quantidade_box";
    private static final String COL8 = "hora";
    private static final String COL9 = "minuto";
    private static final String COL10 = "domingo";
    private static final String COL11 = "segunda";
    private static final String COL12 = "terca";
    private static final String COL13 = "quarta";
    private static final String COL14 = "quinta";
    private static final String COL15 = "sexta";
    private static final String COL16 = "sabado";
    private static final String COL17 = "vezes_dia";
    private static final String COL18 = "periodo_hora";
    private static final String COL19 = "periodo_min";
    private static final String COL20 = "notification_id";

    public DataBaseAlarmsHelper(@Nullable Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COL0 + " INTEGER PRIMARY KEY," +
                COL1 + " INTEGER," +
                COL2 + " INTEGER," +
                COL3 + " INTEGER," +
                COL4 + " TEXT," +
                COL5 + " INTEGER," +
                COL6 + " INTEGER," +
                COL7 + " INTEGER," +
                COL8 + " INTEGER," +
                COL9 + " INTEGER," +
                COL10 + " INTEGER," +
                COL11 + " INTEGER," +
                COL12 + " INTEGER," +
                COL13 + " INTEGER," +
                COL14 + " INTEGER," +
                COL15 + " INTEGER," +
                COL16 + " INTEGER," +
                COL17 + " INTEGER," +
                COL18 + " INTEGER," +
                COL19 + " INTEGER," +
                COL20 + " INTEGER)";

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public Cursor getData() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        return db.rawQuery(query, null);
    }

    public boolean addData(int alarmType, int medicineType, int ativo, String nome, int dosagem, int quantidade, int quantidadeBox, int hora, int minuto, int[] dias, int vezes_dia, int periodo_hora, int periodo_minuto, int notificationId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1, alarmType);
        contentValues.put(COL2, medicineType);
        contentValues.put(COL3, ativo);
        contentValues.put(COL4, nome);
        contentValues.put(COL5, dosagem);
        contentValues.put(COL6, quantidade);
        contentValues.put(COL7, quantidadeBox);
        contentValues.put(COL8, hora);
        contentValues.put(COL9, minuto);
        contentValues.put(COL10, dias[0]);
        contentValues.put(COL11, dias[1]);
        contentValues.put(COL12, dias[2]);
        contentValues.put(COL13, dias[3]);
        contentValues.put(COL14, dias[4]);
        contentValues.put(COL15, dias[5]);
        contentValues.put(COL16, dias[6]);
        contentValues.put(COL17, vezes_dia);
        contentValues.put(COL18, periodo_hora);
        contentValues.put(COL19, periodo_minuto);
        contentValues.put(COL20, notificationId);

        long result = db.insert(TABLE_NAME, null, contentValues);

        return result != -1;
    }

    public boolean updateData(String id, int alarmType, int medicineType, int ativo, String nome, int dosagem, int quantidade, int quantidadeBox, int hora, int minuto, int[] dias, int vezes_dia, int periodo_hora, int periodo_minuto, int notificationId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL0, id);
        contentValues.put(COL1, alarmType);
        contentValues.put(COL2, medicineType);
        contentValues.put(COL3, ativo);
        contentValues.put(COL4, nome);
        contentValues.put(COL5, dosagem);
        contentValues.put(COL6, quantidade);
        contentValues.put(COL7, quantidadeBox);
        contentValues.put(COL8, hora);
        contentValues.put(COL9, minuto);
        contentValues.put(COL10, dias[0]);
        contentValues.put(COL11, dias[1]);
        contentValues.put(COL12, dias[2]);
        contentValues.put(COL13, dias[3]);
        contentValues.put(COL14, dias[4]);
        contentValues.put(COL15, dias[5]);
        contentValues.put(COL16, dias[6]);
        contentValues.put(COL17, vezes_dia);
        contentValues.put(COL18, periodo_hora);
        contentValues.put(COL19, periodo_minuto);
        contentValues.put(COL20, notificationId);

        db.update(TABLE_NAME, contentValues, "ID = ?", new String[]{id});
        return true;
    }

    Integer removeData(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?", new String[]{id});
    }
}
