package com.Sislab.WaterMonitoring.FirebaseDatabase;


import com.Sislab.WaterMonitoring.model.TremorTest;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;



public class TremorTestFirebase {
    private DatabaseReference databaseReference ;
    private String UserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
    private DatabaseReference Timestamp;


    public void pushData(int id, TremorTest tremorTest){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        if (id == 1){
            Timestamp = FirebaseDatabase.getInstance().getReference().child("Tremor Test").child(UserId).child("History Time").child(sdf.format(timestamp)).child("Time");
            Timestamp.setValue(sdf.format(timestamp));
        }
        databaseReference=  FirebaseDatabase.getInstance().getReference().child("Tremor Test").child(UserId).child(sdf.format(timestamp)).child(String.valueOf(id));
        databaseReference.setValue(tremorTest);
    }
}
