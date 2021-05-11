package com.Sislab.WaterMonitoring;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ChangeDetail_Form extends AppCompatActivity {
    //Object Day Of Birth
    private DatePickerDialog datePickerDialog;
    private EditText fullName,dayOfBirth,address,number;
    private RadioGroup radioGroup;
    private RadioButton radioButton,male,female;
    private Button applyChange;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_detail__form);
        fullName = (EditText) findViewById(R.id.change_fullName);
        address = (EditText) findViewById(R.id.change_address);
        number = (EditText) findViewById(R.id.change_phoneNumber);
        radioGroup = (RadioGroup) findViewById(R.id.change_radioGroup);
        female = (RadioButton) findViewById(R.id.change_femaleRadio);

        male = (RadioButton) findViewById(R.id.change_maleRadio);
        /*pop-up Dayof Birth*/
        dayOfBirth = (EditText) findViewById(R.id.change_dayOfBirth);

        firebaseUser = FirebaseAuth.getInstance();
        final String userID = firebaseUser.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                fullName.setText(dataSnapshot.child("Users").child(userID).child("Full Name").getValue(String.class));
                if(!dataSnapshot.child("Users").child(userID).child("Phone Number").getValue(String.class).equals("Empty, please fill in!")){
                    number.setText(dataSnapshot.child("Users").child(userID).child("Phone Number").getValue(String.class));
                }
                if(!dataSnapshot.child("Users").child(userID).child("Address").getValue(String.class).equals("Empty, please fill in!")){
                    address.setText(dataSnapshot.child("Users").child(userID).child("Address").getValue(String.class));
                }
                dayOfBirth.setText(dataSnapshot.child("Users").child(userID).child("Date Of Birth").getValue(String.class));
                if(dataSnapshot.child("Users").child(userID).child("Gender").getValue(String.class).equals("Male")){
                    male.setChecked(true);
                    female.setChecked(false);
                }
                else{
                    male.setChecked(false);
                    female.setChecked(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        dayOfBirth.setInputType(InputType.TYPE_NULL);
        dayOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);
                datePickerDialog = new DatePickerDialog(ChangeDetail_Form.this,R.style.CustomDatePickerDialog,new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int mounthOfYear, int dayOfMonth) {
                        dayOfBirth.setText(dayOfMonth+"/"+ (mounthOfYear+1)+"/"+year);
                    }
                }, year, month, day);

                datePickerDialog.show();
            }
        });

        applyChange = (Button) findViewById(R.id.change_ApplyButton);
        applyChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = radioGroup.getCheckedRadioButtonId();
                radioButton = (RadioButton) findViewById(id);
                if(!validateFullname() || !validateAddress() || !validatedayOfBirth() || !validateNumber()){
                    Toast.makeText(ChangeDetail_Form.this,"Update Failed!",Toast.LENGTH_LONG).show();
                    return;
                }else {
                    databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);
                    Map newPost = new HashMap();
                    newPost.put("Full Name",fullName.getText().toString());
                    newPost.put("Gender",radioButton.getText().toString());
                    newPost.put("Address",address.getText().toString());
                    newPost.put("Phone Number",number.getText().toString());
                    newPost.put("Date Of Birth",dayOfBirth.getText().toString());
                    databaseReference.setValue(newPost);
                    Toast.makeText(ChangeDetail_Form.this,"Update Success!",Toast.LENGTH_LONG).show();
                    ChangeDetail_Form.this.finish();
                }

            }


        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
    private Boolean validateFullname(){
        String fullname = fullName.getText().toString();
        if(fullname.isEmpty()){
            fullName.setError("Please Enter Full Name");
            fullName.requestFocus();
            return false;
        }
        else {
            fullName.setError(null);
            return true;
        }
    }
    private Boolean validatedayOfBirth(){
        String DOB = dayOfBirth.getText().toString();
        if(DOB.isEmpty()){
            dayOfBirth.setError("Please Enter Date Of Birth");
            dayOfBirth.requestFocus();
            return false;
        }
        else {
            dayOfBirth.setError(null);
            return true;
        }
    }
    private Boolean validateAddress(){
        String addr = address.getText().toString();
        if(addr.isEmpty()){
            address.setError("Please Enter Address");
            address.requestFocus();
            return false;
        }
        else {
            address.setError(null);
            return true;
        }
    }
    private Boolean validateNumber(){
        String phone = number.getText().toString();
        if(phone.isEmpty()){
            number.setError("Please Enter Phone Number");
            number.requestFocus();
            return false;
        }
        else {
            number.setError(null);
            return true;
        }
    }


}
