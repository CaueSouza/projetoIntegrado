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
    private static final String COL1 = "type";//1 == fixo 2 == intervalo
    private static final String COL2 = "ativo";// 1 == ativo 0 == inativo
    private static final String COL3 = "nome_remedio";
    private static final String COL4 = "dosagem";
    private static final String COL5 = "hora";
    private static final String COL6 = "minuto";

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
                COL3 + " TEXT," +
                COL4 + " INTEGER," +
                COL5 + " INTEGER," +
                COL6 + " INTEGER)";

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addData(int type, String nome, int dosagem, int hora, int minuto) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1, type);
        contentValues.put(COL2, 1);
        contentValues.put(COL3, nome);
        contentValues.put(COL4, dosagem);
        contentValues.put(COL5, hora);
        contentValues.put(COL6, minuto);

        long result = db.insert(TABLE_NAME, null, contentValues);

        return result != -1;
    }

    public Cursor getData() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        return db.rawQuery(query, null);
    }

    public boolean updateData(String id, int type, int isActive, String nome, int dosagem, int hora, int minuto) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL0, id);
        contentValues.put(COL1, type);
        contentValues.put(COL2, isActive);
        contentValues.put(COL3, nome);
        contentValues.put(COL4, dosagem);
        contentValues.put(COL5, hora);
        contentValues.put(COL6, minuto);

        db.update(TABLE_NAME, contentValues, "ID = ?", new String[]{id});
        return true;
    }

    Integer removeData(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?", new String[]{id});
    }
}
