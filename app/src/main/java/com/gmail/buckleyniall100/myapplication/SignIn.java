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

public class SignIn extends AppCompatActivity implements View.OnClickListener {
    Button signInButton, signUpButton;
    EditText email, password;


    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mAuth = FirebaseAuth.getInstance();
        //findViewById(R.id.textViewSignUp).setOnClickListener(this);

        //Intialization Button
        signInButton = (Button) findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(SignIn.this);
        signUpButton = (Button) findViewById(R.id.sign_up_button);
        signUpButton.setOnClickListener(SignIn.this);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.sign_in_button:
                userSignIn();
                break;
            case R.id.sign_up_button:
                startActivity(new Intent(this, SignUp.class));
                break;
        }
    }

    private void userSignIn() {
        String username = email.getText().toString().trim();
        String pswrd = password.getText().toString().trim();

        if(pswrd.isEmpty()){
          password.setError("Password is required");
          password.requestFocus();
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
        if(pswrd.length()<4){
            password.setError("Minimum length of password is 5");
            password.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(username, pswrd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                     System.out.println("Success!!");
                     Intent intent = new Intent(SignIn.this, MapsActivity.class);
                     intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                     startActivity(intent);
                }//else{}
            }
        });

    }
}
