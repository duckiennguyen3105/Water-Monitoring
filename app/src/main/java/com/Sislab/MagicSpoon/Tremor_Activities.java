package com.Sislab.MagicSpoon;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.Sislab.MagicSpoon.Chart.TestingChart;
import com.Sislab.MagicSpoon.Fomatter.XAsisDateFomatter;
import com.Sislab.MagicSpoon.SQLiteDatabase.TremorTestData;
import com.Sislab.MagicSpoon.model.TremorTest;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;


public class Tremor_Activities extends Fragment implements SensorEventListener {
    private static final String TAG = "Tremor_Activities";
    private TremorTest tremorTest;
    private TremorTestData myDb;

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private TextView Xaxis,Yaxis,Zaxis,instruction;
    private Button btnStart,btnReset;


    long tMillisec= 0L,tStart=0L,tBuff=0L,tUpdate = 0L;
    int sec,millisec;

    private boolean timerRunning;
    Handler handler;
    TestingChart testingChart;

    LineChart lineChart;
    LineDataSet xAxisLine = new LineDataSet(null,null);
    LineDataSet yAxisLine = new LineDataSet(null,null);
    LineDataSet zAxisLine = new LineDataSet(null,null);
    ArrayList<ILineDataSet> dataSets = new ArrayList<>();
    LineData lineData;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_tremor__activities, container, false);

    }


    @Override
    public void onStart() {
        super.onStart();
        myDb = new TremorTestData(this.getContext());
        lineChart = (LineChart) getView().findViewById(R.id.tremor_chart);
        instruction = (TextView) getView().findViewById(R.id.instruction_hold);
        Xaxis = (TextView) getView().findViewById(R.id.X_Tremor);
        Yaxis = (TextView) getView().findViewById(R.id.Y_Tremor);
        Zaxis = (TextView) getView().findViewById(R.id.Z_Tremor);
        btnStart = (Button) getView().findViewById(R.id.btn_StartTest);
        btnReset = (Button) getView().findViewById(R.id.btn_ResetTest);
        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        lineChart.setNoDataTextColor(Color.RED);
        lineChart.setDrawGridBackground(true);
        lineChart.setVisibleXRangeMaximum(6);
        lineChart.setDragEnabled(true);
        xAxisLine.setLineWidth(2);
        xAxisLine.setColor(Color.RED);
        xAxisLine.setValueTextSize(10);
        xAxisLine.setDrawCircles(false);
        yAxisLine.setLineWidth(2);
        yAxisLine.setColor(Color.BLUE);
        yAxisLine.setValueTextSize(10);
        yAxisLine.setDrawCircles(false);
        zAxisLine.setLineWidth(2);
        zAxisLine.setColor(Color.YELLOW);
        zAxisLine.setValueTextSize(10);
        zAxisLine.setDrawCircles(false);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(0);
        xAxis.setAxisMaximum(20);
        xAxis.setLabelCount(5, true);
        xAxis.setDrawGridLines(false);



        handler = new Handler();
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(timerRunning){
                    pauseTimer();
                }else{
                    startTimer();
                }
            }
        });
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetTimer();
            }
        });

    }

    public Runnable runnable = new Runnable() {
        @Override
        public void run() {
            tremorTest = new TremorTest();
            tMillisec = SystemClock.uptimeMillis() - tStart;
            tUpdate = tBuff + tMillisec;
            sec = (int)(tUpdate/1000);
            sec %= 60;
            millisec = (int) (tUpdate%1000);
            tremorTest.setTime(Float.parseFloat(String.format("%02d",sec)+"."+String.format("%03d",millisec)));
            sensorManager.registerListener(Tremor_Activities.this,accelerometer,SensorManager.SENSOR_DELAY_NORMAL);
            instruction.setVisibility(View.VISIBLE);
            instruction.setText(String.valueOf(tremorTest.getTime()));
            handler.postDelayed(this,0);
            TremorTestData tremorTestData = new TremorTestData(getContext());
            if(sec == 20){
                resetTimer();
                handler.removeCallbacks(runnable);
                showToChart();
            }
        }
    };
    private void resetTimer() {
        btnReset.setEnabled(false);
        btnStart.setText("Starting Test");
        sensorManager.unregisterListener(Tremor_Activities.this,accelerometer);
        instruction.setVisibility(View.INVISIBLE);
        Xaxis.setText("0.00");
        Yaxis.setText("0.00");
        Zaxis.setText("0.00");
        tBuff = 0L;
        tStart = 0L;
        tMillisec = 0L;
        tUpdate = 0L;
        sec =0;
        millisec = 0;
        timerRunning = false;
    }

    private void startTimer() {
        myDb.deteleData();
        tStart = SystemClock.uptimeMillis();
        handler.postDelayed(runnable,0);
        timerRunning =true;
        btnStart.setText("Pause");
        btnReset.setEnabled(false);
    }
    private void pauseTimer() {
        tBuff += tMillisec;
        handler.removeCallbacks(runnable);
        timerRunning = false;
        btnStart.setText("Continue");
        btnReset.setEnabled(true);
        sensorManager.unregisterListener(Tremor_Activities.this,accelerometer);
    }

    private void AddData(float time, float getxAxis, float getyAxis, float getzAxis) {
        boolean insertData = myDb.addData(time,getxAxis,getyAxis,getzAxis);
        if(insertData){
            Log.d(TAG, "AddData: Success");
        }else {
            Log.d(TAG, "AddData: Fail");
        }
    }

    private void showToChart(){
        testingChart = new TestingChart();
        xAxisLine.setValues(testingChart.showXAxis(this.getContext()));
        xAxisLine.setLabel("X Axis");
        yAxisLine.setValues(testingChart.showYAxis(this.getContext()));
        yAxisLine.setLabel("Y Axis");
        zAxisLine.setValues(testingChart.showZAxis(this.getContext()));
        zAxisLine.setLabel("Z Axis");
        dataSets.clear();
        dataSets.add(xAxisLine);
        dataSets.add(yAxisLine);
        dataSets.add(zAxisLine);
        lineData = new LineData(dataSets);
        lineChart.clear();
        lineChart.setData(lineData);
        lineChart.invalidate();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Xaxis.setText(String.format("%.2f",sensorEvent.values[0]));
        Yaxis.setText(String.format("%.2f",sensorEvent.values[1]));
        Zaxis.setText(String.format("%.2f",sensorEvent.values[2]));
        tremorTest.setxAxis(Float.parseFloat(String.format("%.2f",sensorEvent.values[0])));
        tremorTest.setyAxis(Float.parseFloat(String.format("%.2f",sensorEvent.values[1])));
        tremorTest.setzAxis(Float.parseFloat(String.format("%.2f",sensorEvent.values[2])));
        System.out.println(tremorTest.getTime());
        AddData(tremorTest.getTime(),  tremorTest.getxAxis(),tremorTest.getyAxis(),tremorTest.getzAxis());
    }



    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        
    }

}
