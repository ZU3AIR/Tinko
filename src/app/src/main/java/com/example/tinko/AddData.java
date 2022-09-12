package com.example.tinko;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class AddData extends AppCompatActivity {

    EditText platform, username, accountLink, imageLink;
    Button confirmbtn;
    private FirebaseUser fbUser;
    private DatabaseReference dbRef;
    private String uID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);

        TextView returnTV = (TextView) findViewById(R.id.textView9);
        returnTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), PresetPage.class));
            }
        });
        // connecting to xml layout
        confirmbtn = (Button) findViewById(R.id.addConfirm);
        platform = (EditText) findViewById(R.id.etName);
        username = (EditText) findViewById(R.id.etUsername);
        accountLink = (EditText) findViewById(R.id.etAccountLink);
        imageLink = (EditText) findViewById(R.id.etImageLink);

        // now creating on click action
        confirmbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAccount();
                platform.setText("");
                username.setText("");
                accountLink.setText("");
                imageLink.setText("");
            }
        });

        fbUser = FirebaseAuth.getInstance().getCurrentUser();
        dbRef = FirebaseDatabase.getInstance().getReference("Users");
        uID = fbUser.getUid();
    }


// https://firebase.google.com/docs/database/admin/save-data
// https://firebase.google.com/docs/database/android/read-and-write

    private void addAccount(){
        Map<String, Object> data = new HashMap<>();
        data.put("name", platform.getText().toString());
        data.put("username", username.getText().toString());
        data.put("link", accountLink.getText().toString());
        data.put("turl", imageLink.getText().toString());

        FirebaseDatabase.getInstance().getReference().child("Users/" + uID + "/" + "accounts").push()
                .setValue(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(AddData.this, "Added Data!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(AddData.this, "Error Occured", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}