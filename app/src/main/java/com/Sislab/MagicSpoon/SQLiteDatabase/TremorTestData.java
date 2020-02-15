package com.Sislab.MagicSpoon.SQLiteDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class TremorTestData extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "MagicSpoon.db";
    private static final String TABLE_NAME = "tremor_data";

    private static final String TIME = "time";
    private static final String XAXIS = "xAxis";
    private static final String YAXIS = "yAxis";
    private static final String ZAXIS = "zAxis";

    public TremorTestData(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTable = "CREATE TABLE "+ TABLE_NAME+ " ( "+TIME+ " REAL PRIMARY KEY, "+
                XAXIS +" REAL, "+
                YAXIS +" REAL, "+
                ZAXIS +" REAL)";
        sqLiteDatabase.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

    public boolean addData(float time,float xAs,float yAs,float zAs){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TIME,time);
        contentValues.put(XAXIS,xAs);
        contentValues.put(YAXIS,yAs);
        contentValues.put(ZAXIS,zAs);

        long result = db.insert(TABLE_NAME,null,contentValues);
        if(result == -1){
            return false;
        }else {
            return true;
        }
    }
    public void deteleData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM "+TABLE_NAME;
        db.execSQL(query);
    }

    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME,null);
        //Nghiên cứu hiển thị dữ liệu lên Chart
        return data;
    }
}
