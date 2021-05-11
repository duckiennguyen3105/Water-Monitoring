package com.Sislab.WaterMonitoring;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.Sislab.WaterMonitoring.Chart.TestingChart;
import com.Sislab.WaterMonitoring.FirebaseDatabase.TremorTestFirebase;
import com.Sislab.WaterMonitoring.model.TremorTest;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TestingChart_Acitivity extends AppCompatActivity {
    private DatabaseReference databaseReference ;
    private String UserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private String time;

    private LineChart lineChart;
    private LineDataSet xAxisLine = new LineDataSet(null,null);
    private LineDataSet yAxisLine = new LineDataSet(null,null);
    private LineDataSet zAxisLine = new LineDataSet(null,null);
    private ArrayList<ILineDataSet> dataSets = new ArrayList<>();
    private LineData lineData;
    private TremorTestFirebase tremorTestFirebase;
    private TestingChart testingChart;
    private Description description = new Description();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setContentView(R.layout.activity_testing_chart__acitivity);
        lineChart = (LineChart) findViewById(R.id.testChart);

        description.setTextSize(20);

        lineChart.setDescription(description);
        lineChart.setNoDataTextColor(Color.RED);
        lineChart.setDrawGridBackground(true);
        lineChart.setVisibleXRangeMaximum(6);
        lineChart.setDragEnabled(true);
        xAxisLine.setLineWidth(2);
        xAxisLine.setColor(Color.RED);
        xAxisLine.setValueTextSize(10);
        xAxisLine.setDrawCircles(false);
        xAxisLine.setDrawValues(false);
        yAxisLine.setLineWidth(2);
        yAxisLine.setColor(Color.BLUE);
        yAxisLine.setValueTextSize(10);
        yAxisLine.setDrawCircles(false);
        yAxisLine.setDrawValues(false);
        zAxisLine.setLineWidth(2);
        zAxisLine.setColor(Color.GREEN);
        zAxisLine.setValueTextSize(10);
        zAxisLine.setDrawCircles(false);
        zAxisLine.setDrawValues(false);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(0);
        xAxis.setAxisMaximum(20);
        xAxis.setLabelCount(5, true);
        xAxis.setDrawGridLines(false);
        time = (String) getIntent().getSerializableExtra("time");
        getData(time);
        description.setText(time);

        YAxis yAxisLeft = lineChart.getAxisLeft();
        yAxisLeft.setAxisMaximum(30);
        yAxisLeft.setAxisMinimum(-30);
        yAxisLeft.setLabelCount(8,false);
        YAxis yAxisRight = lineChart.getAxisRight();
        yAxisRight.setAxisMaximum(30);
        yAxisRight.setAxisMinimum(-30);
        yAxisRight.setLabelCount(8,false);
    }

    private void showToChart(ArrayList<TremorTest>  tremorTests){
        testingChart = new TestingChart();
        xAxisLine.setValues(testingChart.showXAxis(tremorTests));
        xAxisLine.setLabel("X Axis");
        yAxisLine.setValues(testingChart.showYAxis(tremorTests));
        yAxisLine.setLabel("Y Axis");
        zAxisLine.setValues(testingChart.showZAxis(tremorTests));
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

    public void getData(String time){
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Tremor Test").child(UserId).child(time);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<TremorTest>  testArrayList = new ArrayList<>();
                if (dataSnapshot.hasChildren()) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        TremorTest tremorTest = dataSnapshot1.getValue(TremorTest.class);
                        testArrayList.add(tremorTest);
                    }
                    showToChart(testArrayList);
                }else {
                    lineChart.clear();
                    lineChart.invalidate();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void btn_deteleData(View view) {
        DatabaseReference tremorData = FirebaseDatabase.getInstance().getReference().child("Tremor Test").child(UserId).child(time);
        DatabaseReference tremorTime = FirebaseDatabase.getInstance().getReference().child("Tremor Test").child(UserId).child("History Time").child(time);

        tremorData.removeValue();
        tremorTime.removeValue();

        Toast.makeText(getApplicationContext(),"Tremor at "+time+" has been deteled",Toast.LENGTH_LONG).show();
        this.finish();
    }
}
