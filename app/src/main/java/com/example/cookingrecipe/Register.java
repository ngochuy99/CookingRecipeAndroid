package com.example.cookingrecipe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class Register extends AppCompatActivity {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private EditText email,display_name,password,re_password,login_email;
    private Button btnRegister;
    private TextView error;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        email = findViewById(R.id.register_email);
        display_name = findViewById(R.id.register_displayname);
        password = findViewById(R.id.register_password);
        re_password = findViewById(R.id.re_register_password);
        btnRegister = findViewById(R.id.btnRegister);
        login_email = findViewById(R.id.login_email);
        error = findViewById(R.id.error);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!password.getText().toString().equals(re_password.getText().toString())){
                    error.setText("* Password and repeat password not match!");
                    error.setVisibility(View.VISIBLE);
                }
                else{
                    mAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                            .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                error.setText("");
                                error.setVisibility(View.INVISIBLE);
                                mAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                                        .addOnCompleteListener(Register.this, task1 -> {
                                            if(task1.isSuccessful()){
                                                FirebaseUser user = mAuth.getCurrentUser();
                                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                        .setDisplayName(display_name.getText().toString())
                                                        .build();
                                                user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()){
                                                            finish();
                                                        }
                                                    }
                                                });
                                            }
                                        });
                            } else {
                                error.setText("* Register Failed. Try again");
                                error.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                }
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser !=null){
            currentUser.reload();
        }
    }
}