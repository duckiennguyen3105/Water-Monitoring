package com.Sislab.MagicSpoon;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.Sislab.MagicSpoon.SQLiteDatabase.TremorTestData;
import com.Sislab.MagicSpoon.model.TremorTest;

import java.util.Date;

public class Tremor_Activities extends Fragment implements SensorEventListener {

    private static final String TAG = "Tremor_Activities";
    private TremorTest tremorTest;
    private TremorTestData myDb;

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private TextView Xaxis,Yaxis,Zaxis,instruction;
    private Button btnStart,btnReset;


    long tMillisec,tStart,tBuff,tUpdate = 0L;
    int sec,millisec;

    private boolean timerRunning;
    Handler handler;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_tremor__activities, container, false);

    }


    @Override
    public void onStart() {
        super.onStart();
        instruction = (TextView) getView().findViewById(R.id.instruction_hold);
        Xaxis = (TextView) getView().findViewById(R.id.X_Tremor);
        Yaxis = (TextView) getView().findViewById(R.id.Y_Tremor);
        Zaxis = (TextView) getView().findViewById(R.id.Z_Tremor);
        btnStart = (Button) getView().findViewById(R.id.btn_StartTest);
        btnReset = (Button) getView().findViewById(R.id.btn_ResetTest);
        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
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
            sec = ((int)(tUpdate/1000))%60 ;
            millisec = (int) (tUpdate%100);
            tremorTest.setTime(String.format("%02d",sec)+"."+String.format("%02d",millisec));
            sensorManager.registerListener(Tremor_Activities.this,accelerometer,SensorManager.SENSOR_DELAY_NORMAL);
            handler.postDelayed(this,60);
            if(sec == 20){
                resetTimer();
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
//        myDb.deteleData();
    }

    private void startTimer() {
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
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Xaxis.setText(String.format("%.2f",sensorEvent.values[0]));
        Yaxis.setText(String.format("%.2f",sensorEvent.values[1]));
        Zaxis.setText(String.format("%.2f",sensorEvent.values[2]));
        tremorTest.setxAxis(Float.parseFloat(String.format("%.2f",sensorEvent.values[0])));
        tremorTest.setyAxis(Float.parseFloat(String.format("%.2f",sensorEvent.values[1])));
        tremorTest.setzAxis(Float.parseFloat(String.format("%.2f",sensorEvent.values[2])));
        Log.d(TAG,   "Time: "+tremorTest.getTime()+", xAxis: "+tremorTest.getxAxis());
//        AddData(tremorTest.getTime(),tremorTest.getxAxis(),tremorTest.getyAxis(),tremorTest.getzAxis());
    }

    private void AddData(float time, float getxAxis, float getyAxis, float getzAxis) {
        boolean insertData = myDb.addData(time,getxAxis,getyAxis,getzAxis);
        if(insertData){
            Log.d(TAG, "AddData: Success");
        }else {
            Log.d(TAG, "AddData: Fail");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        
    }
}
