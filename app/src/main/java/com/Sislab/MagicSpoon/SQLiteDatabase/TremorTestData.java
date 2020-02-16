package com.Sislab.MagicSpoon.SQLiteDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.Sislab.MagicSpoon.model.TremorTest;
import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;

public class TremorTestData extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "MagicSpoon3.db";
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
        String createTable = "CREATE TABLE "+ TABLE_NAME+ " ( "+TIME+ " REAL, "+
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
    public ArrayList<TremorTest> getDataTremorTest(){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<TremorTest> cursorArrayList = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME,null);
        if(cursor.moveToFirst()){
            for (int i = 0 ;i < cursor.getCount();i++){
                TremorTest tremorTest = new TremorTest();
                tremorTest.setTime(cursor.getFloat(cursor.getColumnIndex(TIME)));
                tremorTest.setxAxis(cursor.getFloat(cursor.getColumnIndex(XAXIS)));
                tremorTest.setyAxis(cursor.getFloat(cursor.getColumnIndex(YAXIS)));
                tremorTest.setzAxis(cursor.getFloat(cursor.getColumnIndex(ZAXIS)));
                cursorArrayList.add(tremorTest);
                cursor.moveToNext();
            }
        }
        return cursorArrayList;
    }

}
