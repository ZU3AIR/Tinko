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
import com.google.firebase.database.FirebaseDatabase;

public class UserSignup extends AppCompatActivity implements View.OnClickListener {

    // setting up all required variables
    private FirebaseAuth mAuth;
    private TextView returnhome, completeSignup;
    private EditText userFirst, userSur, userEmail, userPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_signup);

        mAuth = FirebaseAuth.getInstance();

        // making the return button interactive
        returnhome = findViewById(R.id.textView);
        returnhome.setOnClickListener(this);

        // making the submit button for signups
        completeSignup = findViewById(R.id.button2);
        completeSignup.setOnClickListener(this);

        // making the first name, surname, email & password text boxes
        userFirst = findViewById(R.id.editTextTextPersonName3);
        userSur = findViewById(R.id.editTextTextPersonName4);
        userEmail =  findViewById(R.id.editTextTextEmailAddress2);
        userPassword = findViewById(R.id.editTextTextPassword2);
    }

    @Override
    public void onClick(View view) {
        // for the onclick redirection
        // take the id of the button that was clicked
        switch (view.getId()){
            case R.id.textView:// this is for return home
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.button2:// this is for complete signup
                confirmUserSignup();
                break;
        }
    }
    // function that makes sure the data inserted is correct
    private void confirmUserSignup() {
        //converting each data entered into a string
        String fname= userFirst.getText().toString().trim();
        String sname= userSur.getText().toString().trim();
        String email= userEmail.getText().toString().trim();
        String pword= userPassword.getText().toString().trim();

        // adding some validation cases
        if(fname.isEmpty()){
            userFirst.setError("Firstname required!");
            userFirst.requestFocus();
            return;
        }

        if(sname.isEmpty()){
            userSur.setError("Surname required!");
            userSur.requestFocus();
            return;
        }

        if(email.isEmpty()){
            userEmail.setError("Email is required!");
            userEmail.requestFocus();
            return;
        }
        // if it does not match, throw an error
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            userEmail.setError("Email is invalid. Please try again!");
            userEmail.requestFocus();
            return;
        }

        if(pword.isEmpty()){
            userPassword.setError("Password is required!");
            userPassword.requestFocus();
            return;
        }

        if(pword.length() < 8){
            userPassword.setError("Password must be atleast 8 characters long!");
            userPassword.requestFocus();
            return;
        }

        // object of firebase authentication
        mAuth.createUserWithEmailAndPassword(email, pword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            User cUser = new User(fname, sname, email);

                            // once we have a user object we must now send it to the realtime database
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())//will return the id of the registered user
                                    .setValue(cUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {//check if the task has been successful
                                    if(task.isSuccessful()){
                                        //prompt a little notification
                                        Toast.makeText(UserSignup.this, "User registration is successful", Toast.LENGTH_LONG).show();
                                    }
                                    else{
                                        Toast.makeText(UserSignup.this, "User registration failed", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }else{
                            Toast.makeText(UserSignup.this, "User registration failed", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}