package com.Sislab.MagicSpoon;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile_Form extends Fragment {
    private Button changeInfo,changePass;
    private TextView Name, Email , DOB, Number, Address, Gender;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseAuth;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_profile__form,container,false);
    }

    @Override
    public void onStart() {
        super.onStart();
        Name = (TextView) getView().findViewById(R.id.user_profile_name);
        Email = (TextView) getView().findViewById(R.id.user_profile_email);
        DOB = (TextView) getView().findViewById(R.id.user_DOB);
        Number = (TextView) getView().findViewById(R.id.user_Number);
        Address = (TextView) getView().findViewById(R.id.user_Address);
        Gender = (TextView) getView().findViewById(R.id.user_Gender);
        firebaseAuth = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Name.setText(dataSnapshot.child("Users").child(firebaseAuth.getUid()).child("Full Name").getValue(String.class));
                Email.setText(firebaseAuth.getEmail());
                DOB.setText(dataSnapshot.child("Users").child(firebaseAuth.getUid()).child("Date Of Birth").getValue(String.class));
                Number.setText(dataSnapshot.child("Users").child(firebaseAuth.getUid()).child("Phone Number").getValue(String.class));
                Address.setText(dataSnapshot.child("Users").child(firebaseAuth.getUid()).child("Address").getValue(String.class));
                Gender.setText(dataSnapshot.child("Users").child(firebaseAuth.getUid()).child("Gender").getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        changeInfo = (Button) getView().findViewById(R.id.btn_changeInfo);
        changeInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),ChangeDetail_Form.class));
            }
        });
        changePass = (Button)  getView().findViewById(R.id.btn_changePass);
        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),ChangePass_Form.class));
            }
        });
    }
}
