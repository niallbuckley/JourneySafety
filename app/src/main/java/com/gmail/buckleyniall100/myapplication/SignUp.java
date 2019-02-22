package com.gmail.buckleyniall100.myapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUp extends AppCompatActivity implements View.OnClickListener {
    Button button;
    EditText email, password1, password2;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

        //Intialization Button
        button = (Button) findViewById(R.id.sign_up_button);
        button.setOnClickListener(SignUp.this);

        email = findViewById(R.id.email);
        password1 = findViewById(R.id.password1);
        password2 = findViewById(R.id.password2);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_up_button:
                registerUser();
        }
    }

    private void registerUser() {
        String username = email.getText().toString().trim();
        String password = password1.getText().toString().trim();
        String confirmPassword = password2.getText().toString().trim();

        if(password.isEmpty()){
            password1.setError("Password is required");
            password1.requestFocus();
            return;
        }
        if(username.isEmpty()){
            email.setError("Email sign in name is required");
            email.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(username).matches()){
            email.setError("Please enter a valid email");
            email.requestFocus();
            return;
        }
        if(password.length()<4){
            password1.setError("Minimum length of password is 5");
            password1.requestFocus();
            return;
        }
        if(password != confirmPassword){
            password1.setError("Passwords don't match!");
            password2.setError("Passwords don't match!");
            return;
        }
        mAuth.createUserWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    System.out.println("good lad");
                    Intent intent = new Intent(SignUp.this, MainMenu.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();

                }
                //else{} email alreay taken (in video)
            }
        });
    }
}
