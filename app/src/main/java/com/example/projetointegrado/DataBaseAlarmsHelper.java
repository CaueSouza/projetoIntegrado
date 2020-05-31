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

    //  TODO FUTURE CHANGES IN DATABASE
    //    private static final String COLx = "hora_inicio";
    //    private static final String COLx = "vezes_dia";
    //    private static final String COLx = "periodo";
    //    private static final String COLx = "quantidade";

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
                COL9 + " INTEGER)";

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

    public boolean addDataFix(String nome, int quantidade, int quantidadeBox, int hora, int minuto){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1, 1);
        contentValues.put(COL2, 1);
        contentValues.put(COL3, 1);
        contentValues.put(COL4, nome);
        contentValues.put(COL6, quantidade);
        contentValues.put(COL7, quantidadeBox);
        contentValues.put(COL8, hora);
        contentValues.put(COL9, minuto);

        long result = db.insert(TABLE_NAME, null, contentValues);

        return result != -1;
    }

    public boolean addDataFix(String nome, int dosagem, int hora, int minuto) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1, 1);
        contentValues.put(COL2, 2);
        contentValues.put(COL3, 1);
        contentValues.put(COL4, nome);
        contentValues.put(COL5, dosagem);
        contentValues.put(COL8, hora);
        contentValues.put(COL9, minuto);

        long result = db.insert(TABLE_NAME, null, contentValues);

        return result != -1;
    }

    public boolean updateData(String id, int isActive, String nome, int quantidade, int quantidadeCaixa, int hora, int minuto) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL0, id);
        contentValues.put(COL1, 1);
        contentValues.put(COL2, 1);
        contentValues.put(COL3, isActive);
        contentValues.put(COL4, nome);
        contentValues.put(COL6, quantidade);
        contentValues.put(COL7, quantidadeCaixa);
        contentValues.put(COL8, hora);
        contentValues.put(COL9, minuto);

        db.update(TABLE_NAME, contentValues, "ID = ?", new String[]{id});
        return true;
    }

    public boolean updateData(String id, int isActive, String nome, int dosagem, int hora, int minuto) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL0, id);
        contentValues.put(COL1, 1);
        contentValues.put(COL2, 2);
        contentValues.put(COL3, isActive);
        contentValues.put(COL4, nome);
        contentValues.put(COL5, dosagem);
        contentValues.put(COL8, hora);
        contentValues.put(COL9, minuto);

        db.update(TABLE_NAME, contentValues, "ID = ?", new String[]{id});
        return true;
    }

    Integer removeData(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?", new String[]{id});
    }
}
