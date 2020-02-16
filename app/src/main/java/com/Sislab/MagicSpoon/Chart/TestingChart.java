package com.Sislab.MagicSpoon.Chart;

import android.content.Context;
import com.Sislab.MagicSpoon.SQLiteDatabase.TremorTestData;
import com.Sislab.MagicSpoon.model.TremorTest;
import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;

public class TestingChart {
    public ArrayList<Entry> showXAxis(Context context){
        ArrayList<Entry> xVals = new ArrayList<>();
        TremorTestData tremorTestData = new TremorTestData(context);
        for (TremorTest tremorTest: tremorTestData.getDataTremorTest()) {
            xVals.add(new Entry(tremorTest.getTime(),tremorTest.getxAxis()));
        }
        return xVals;
    }
    public ArrayList<Entry> showYAxis(Context context){
        ArrayList<Entry> yVals = new ArrayList<>();
        TremorTestData tremorTestData = new TremorTestData(context);
        for (TremorTest tremorTest: tremorTestData.getDataTremorTest()) {
            yVals.add(new Entry(tremorTest.getTime(),tremorTest.getyAxis()));
        }
        return yVals;
    }
    public ArrayList<Entry> showZAxis(Context context){
        ArrayList<Entry> zVals = new ArrayList<>();
        TremorTestData tremorTestData = new TremorTestData(context);
        for (TremorTest tremorTest: tremorTestData.getDataTremorTest()) {
            zVals.add(new Entry(tremorTest.getTime(),tremorTest.getzAxis()));
        }
        return zVals;
    }
}
