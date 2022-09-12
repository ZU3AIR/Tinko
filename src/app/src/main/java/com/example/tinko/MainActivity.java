package com.example.tinko;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView signup;// register/signup button
    private TextView reset;// reset password
    private EditText uEmail, uPass;
    private FirebaseAuth mAuth;
    private Button logIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signup = (TextView) findViewById(R.id.textView3);
        signup.setOnClickListener(this);

        logIn = (Button) findViewById(R.id.button);
        logIn.setOnClickListener(this);

        uEmail = (EditText) findViewById(R.id.editTextTextEmailAddress);
        uPass = (EditText) findViewById(R.id.editTextTextPassword);

        mAuth = FirebaseAuth.getInstance();

        reset = (TextView) findViewById(R.id.textView8);
        reset.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.textView3:
                startActivity(new Intent(this, UserSignup.class));
                break;
            case R.id.button:
                userSignin();
                break;
            case R.id.textView8:
                startActivity(new Intent(this, ResetPassword.class));
                break;
        }
    }
    // get the users credentials and convert it back to a string
    private void userSignin() {
        String email= uEmail.getText().toString().trim();
        String pword= uPass.getText().toString().trim();

        // adding some validation cases
        if (email.isEmpty()){
            uEmail.setError("Must add Email!");
            uEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            uEmail.setError("Email is invalid!");
            uEmail.requestFocus();
            return;
        }

        if(pword.isEmpty()){
            uPass.setError("Must add Password!");
            uPass.requestFocus();
            return;
        }

        if(pword.length() < 8){
            uPass.setError("Password must be atleast 8 characters long!");
            uPass.requestFocus();
            return;
        }

        // testing whether the user has been logged in
        mAuth.signInWithEmailAndPassword(email, pword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    //redirect to the preset page
                    startActivity(new Intent(MainActivity.this, PresetPage.class));
                }else{
                    //send prompt
                    Toast.makeText(MainActivity.this, "Login Failed, Please try again.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}