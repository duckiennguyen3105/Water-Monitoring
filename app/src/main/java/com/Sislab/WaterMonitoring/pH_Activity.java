package com.Sislab.WaterMonitoring;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.Sislab.WaterMonitoring.Fomatter.XAsisDateFomatter;
import com.Sislab.WaterMonitoring.model.PHViewer;
import com.Sislab.WaterMonitoring.model.PointViewer;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class pH_Activity extends Fragment {
    private LineChart mpLineChart;
    private ArrayList<ILineDataSet> dataSets = new ArrayList<>();
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private LineDataSet lineFx = new LineDataSet(null,null);

    private LineData data;
    private Description description = new Description();
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_p_h_,container,false);
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Quality-Timestamp/pH");

        mpLineChart = (LineChart)getView().findViewById(R.id.chart);

        description.setTextSize(20);

        mpLineChart.setNoDataTextColor(Color.RED);
        mpLineChart.setDrawGridBackground(true);
        mpLineChart.setVisibleXRangeMaximum(6);
        mpLineChart.setDragEnabled(true);
        mpLineChart.setDescription(description);
        mpLineChart.invalidate();
        lineFx.setValueTextSize(10);
        lineFx.setLineWidth(2);
        lineFx.setColor(Color.RED);


        XAxis xAxis = mpLineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelCount(5, true);
        xAxis.setDrawGridLines(false);
        xAxis.setValueFormatter(new XAsisDateFomatter());

        retrieveData();
    }

    private void retrieveData() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Entry> Xline = new ArrayList<Entry>();
                String date = null;
                if(dataSnapshot.hasChildren()){
                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()) {
                        PHViewer phViewer = dataSnapshot1.getValue(PHViewer.class);
                        date = simpleDateFormat.format(new Date(phViewer.getTimestamp()));
                        Xline.add(new Entry((phViewer.getTimestamp())%86400000, phViewer.getBasement()));
                    }
                    showVabration(date,Xline);

                }else {
                    mpLineChart.clear();
                    mpLineChart.invalidate();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void showVabration(String date, ArrayList<Entry> lineX) {
        lineFx.setValues(lineX);
        lineFx.setLabel("Basement");



        dataSets.clear();
        dataSets.add(lineFx);

        data = new LineData(dataSets);
        description.setText(date);
        mpLineChart.clear();
        mpLineChart.setData(data);
        mpLineChart.invalidate();
    }


}
