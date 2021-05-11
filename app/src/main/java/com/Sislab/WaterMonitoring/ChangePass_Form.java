package com.Sislab.WaterMonitoring;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePass_Form extends AppCompatActivity {
    private EditText curPass,newPass,rePass;
    private Button applyChange;
    private FirebaseUser firebaseUser;
    private AuthCredential authCredential;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass__form);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        curPass = findViewById(R.id.change_currentPass);
        newPass = findViewById(R.id.change_newPass);
        rePass = findViewById(R.id.change_rePass);
        applyChange = findViewById(R.id.change_ApplyPass);
        applyChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!validatePassword()){
                    Toast.makeText(ChangePass_Form.this,"Apply Failed!",Toast.LENGTH_LONG).show();
                    return;
                }else {
                    authCredential = EmailAuthProvider.getCredential(firebaseUser.getEmail(),curPass.getText().toString());
                    firebaseUser.reauthenticate(authCredential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                firebaseUser.updatePassword(newPass.getText().toString());
                                Toast.makeText(ChangePass_Form.this,"Apply Success!",Toast.LENGTH_LONG).show();
                                ChangePass_Form.this.finish();
                            }else {
                                Toast.makeText(ChangePass_Form.this,"Apply Failed!",Toast.LENGTH_LONG).show();
                                curPass.setError("Current Password Is Incorrect!");
                                curPass.requestFocus();
                                return;
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
    private Boolean validatePassword(){
        String cp = curPass.getText().toString();
        String np = newPass.getText().toString();
        String rp = rePass.getText().toString();
        if(cp.isEmpty()){
            curPass.setError("Please Enter Your Current Password");
            curPass.requestFocus();
            return false;
        }else if(np.isEmpty()){
            newPass.setError("Please Enter Your New Password");
            newPass.requestFocus();
            return false;
        }
        else if(!np.equals(rp)){
            rePass.setError("Your Re-Password Is Not Match!");
            rePass.requestFocus();
            return false;
        }
        else {
            curPass.setError(null);
            newPass.setError(null);
            rePass.setError(null);
            return true;
        }
    }
}
