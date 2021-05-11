package com.Sislab.WaterMonitoring;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class SignIn_Form extends AppCompatActivity {
    //Object Day Of Birth
    private DatePickerDialog datePickerDialog;
    private EditText dayOfBirth;
    //Object Gender
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    //Register Button
    private Button registerButton;
    //object Full name
    private EditText fullName;
    //object Email
    private EditText emailLogin;
    //object password
    private EditText passwordLogin,repassword;
    //Firebase
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    //Login Button
    private TextView loginButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in__form);
        getSupportActionBar().setTitle("Sign Up");
        firebaseAuth = FirebaseAuth.getInstance();
        fullName = (EditText) findViewById(R.id.fullName);
        emailLogin = (EditText) findViewById(R.id.userEmail);
        passwordLogin = (EditText) findViewById(R.id.userPassword);
        repassword = (EditText) findViewById(R.id.rePassword);
        /*Gender Button*/
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        //Gender ID (confirm in radioButton.getText())
        /*Change to Login Form*/
        loginButton = (TextView) findViewById(R.id.loginForm);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignIn_Form.this,Login_Form.class));
            }
        });
        /*pop-up Dayof Birth*/
        dayOfBirth = (EditText) findViewById(R.id.dayOfBirth);
        dayOfBirth.setInputType(InputType.TYPE_NULL);
        dayOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);
                datePickerDialog = new DatePickerDialog(SignIn_Form.this,R.style.CustomDatePickerDialog,new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int mounthOfYear, int dayOfMonth) {
                        dayOfBirth.setText(dayOfMonth+"/"+ (mounthOfYear+1)+"/"+year);
                    }
                }, year, month, day);

                datePickerDialog.show();
            }
        });

        registerButton = (Button) findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!validateFullname() || !validateEmail() || !validateDayofBirth() || !validatePassword()){
                    Toast.makeText(SignIn_Form.this,"Sign Up Fail! Please Correct All Of It",Toast.LENGTH_LONG).show();
                    return;
                }
                else {
                    int radioId = radioGroup.getCheckedRadioButtonId();
                    radioButton = (RadioButton) findViewById(radioId);
                    String email = emailLogin.getText().toString();
                    String password = passwordLogin.getText().toString();
                    firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(SignIn_Form.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(SignIn_Form.this,"Your Email Has Been Already Sign Up!",Toast.LENGTH_LONG).show();
                            }else {
                                Toast.makeText(SignIn_Form.this,"Sign Up Success!",Toast.LENGTH_LONG).show();
                                String userID = firebaseAuth.getCurrentUser().getUid();
                                databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);
                                Map newPost = new HashMap();
                                newPost.put("Full Name",fullName.getText().toString());
                                newPost.put("Gender",radioButton.getText().toString());
                                newPost.put("Date Of Birth",dayOfBirth.getText().toString());
                                newPost.put("Address","Empty, please fill in!");
                                newPost.put("Phone Number","Empty, please fill in!");
                                databaseReference.setValue(newPost);
                                startActivity(new Intent(SignIn_Form.this,Login_Form.class));
                            }
                        }
                    });
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
    private Boolean validateEmail(){
        String email = emailLogin.getText().toString();

        if(email.isEmpty()){
            emailLogin.setError("Please Enter Email");
            emailLogin.requestFocus();
            return false;
        }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailLogin.setError("Please Enter Valid Email Adress");
            emailLogin.requestFocus();
            return false;
        }
        else {
            emailLogin.setError(null);
            return true;
        }
    }
    private Boolean validateDayofBirth(){
        String dayofbirth = dayOfBirth.getText().toString();
        if(dayofbirth.isEmpty()){
            dayOfBirth.setError("Please Select Day Of Birth");
            dayOfBirth.requestFocus();
            return false;
        }
        else {
            dayOfBirth.setError(null);
            return true;
        }
    }
    private Boolean validatePassword(){
        String password = passwordLogin.getText().toString();
        String repass = repassword.getText().toString();
        if(password.isEmpty()){
            passwordLogin.setError("Please Enter Your Password");
            passwordLogin.requestFocus();
            return false;
        }else if(!password.equals(repass)){
            repassword.setError("Your Re-Password Is Not Match!");
            repassword.requestFocus();
            return false;
        }
        else {
            passwordLogin.setError(null);
            return true;
        }
    }

}
