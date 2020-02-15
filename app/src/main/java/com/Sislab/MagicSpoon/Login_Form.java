package com.Sislab.MagicSpoon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login_Form extends AppCompatActivity {
    private EditText userLogin,passwordLogin;
    private Button loginButton;
    private FirebaseAuth firebaseAuth;
    private  FirebaseAuth.AuthStateListener fiAuthStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login__form);
        getSupportActionBar().setTitle("Login");
        userLogin = (EditText) findViewById(R.id.loginName);
        passwordLogin = (EditText) findViewById(R.id.password);
        loginButton = (Button) findViewById(R.id.loginButton);

        firebaseAuth = FirebaseAuth.getInstance();
        fiAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if(user != null){
                    Intent intent = new Intent(Login_Form.this,Navigation_Menu.class);
                    startActivity(intent);
                    finish();
                }
            }
        };

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = userLogin.getText().toString();
                String password = passwordLogin.getText().toString();
                if(user.isEmpty()){
                    userLogin.setError("Please Enter Username");
                    userLogin.requestFocus();
                }
                else if(password.isEmpty()){
                    passwordLogin.setError("Please Enter Password");
                    userLogin.requestFocus();
                }
                else if(password.isEmpty() && user.isEmpty()){
                    Toast.makeText(Login_Form.this,"Fields Are Empty!",Toast.LENGTH_LONG).show();
                }
                else if(!(password.isEmpty() && user.isEmpty())){
                    firebaseAuth.signInWithEmailAndPassword(user,password).addOnCompleteListener(Login_Form.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(Login_Form.this,"Wrong User or Password",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(Login_Form.this,"Something Error! Please contact to Sislab",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(fiAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(fiAuthStateListener);
    }

    public void btn_signupForm(View view) {
        startActivity(new Intent(getApplicationContext(),SignIn_Form.class));
    }
}
