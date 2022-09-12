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
import com.google.firebase.auth.FirebaseAuth;

public class ResetPassword extends AppCompatActivity {

    FirebaseAuth authentication;
    private Button resetPasswordButton;
    private EditText emailsection;
    private TextView returnhome2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        emailsection = (EditText) findViewById(R.id.editTextTextEmailAddress3);
        resetPasswordButton = (Button) findViewById(R.id.button6);

        authentication = FirebaseAuth.getInstance();

        returnhome2 = findViewById(R.id.textView2);
        returnhome2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBackToSignIn();
            }
        });

        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){
                resetnow();
            }
        });
    }

    private void resetnow() {
        String e = emailsection.getText().toString().trim();

        // adding some validation cases
        if (e.isEmpty()){
            emailsection.setError("Must add Email!");
            emailsection.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(e).matches()){
            emailsection.setError("Email is invalid!");
            emailsection.requestFocus();
            return;
        }

        authentication.sendPasswordResetEmail(e).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ResetPassword.this, "Reset link has been sent to your email!", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(ResetPassword.this, "Error Occured, Please try again!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void goBackToSignIn() {
        Intent activeIntent = new Intent(this, MainActivity.class);
        startActivity(activeIntent);
    }
}