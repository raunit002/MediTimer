package com.example.medicinedatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataSource extends SQLiteOpenHelper {

    private static final String DB_NAME = "medbase";
    private static final int DB_VERSION = 1;

    public DataSource(Context baseContext) {
        super(baseContext, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE meds(name TEXT, date TEXT, time TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) { }

    public boolean insertValues(String medName, String date, String time) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", medName);
        contentValues.put("date", date);
        contentValues.put("time", time);
        long res = database.insert("meds", null, contentValues);
        return res != -1;
    }

    public Cursor readData() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM meds";
        return db.rawQuery(query, null);
    }
}
