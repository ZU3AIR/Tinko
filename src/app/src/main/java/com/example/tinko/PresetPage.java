package com.example.tinko;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter_LifecycleAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PresetPage extends AppCompatActivity {

    // setting all variables
    private FirebaseUser fbUser;
    private DatabaseReference dbRef;
    private String uID;
    private Button userLogout;
    private Button nfcButton;
    RecyclerView rv;
    ViewManager adapter;
    private TextView test;
    String linkname, getlinkname;
    ClipboardManager myClipboard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preset_page);

        // ADD NEW DATA
        Button addNewData = (Button) findViewById(R.id.addaccount);
        addNewData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AddData.class));
            }
        });

        // DISPLAY DATA - RECYCLERVIEW
        rv = (RecyclerView) findViewById(R.id.recycler);
        rv.setLayoutManager(new LinearLayoutManager(this));

        // FIREBASE USER TO STRING
        fbUser = FirebaseAuth.getInstance().getCurrentUser();
        dbRef = FirebaseDatabase.getInstance().getReference("Users");
        uID = fbUser.getUid();

        // GET DATA FROM FIREBASE
        //https://firebaseopensource.com/projects/firebase/firebaseui-android/database/readme/
        FirebaseRecyclerOptions<Model> options =
                new FirebaseRecyclerOptions.Builder<Model>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Users/" + uID + "/" + "accounts"), Model.class)
                        .build();

        adapter = new ViewManager(options);
        rv.setAdapter(adapter);


        // LOGOUT
        userLogout = findViewById(R.id.button3);
        userLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // when button is clicked log user out
                FirebaseAuth.getInstance().signOut();
                // redirect the user to the login page
                startActivity(new Intent(PresetPage.this, MainActivity.class));
            }
        });

        // NFC BUTTON
        nfcButton = (Button) findViewById(R.id.button7);
        nfcButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToNFC(view);
            }
        });


        // DISPLAYING USER FIRST NAME
        //retrieving data from firebase once again
        fbUser = FirebaseAuth.getInstance().getCurrentUser();
        dbRef = FirebaseDatabase.getInstance().getReference("Users");
        uID = fbUser.getUid();
        // final as we will access them in classes
        final TextView firstnameTextView = (TextView) findViewById(R.id.name3);
        dbRef.child(uID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);
                if(userProfile != null){
                    String name = userProfile.fname;
                    firstnameTextView.setText(name);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(PresetPage.this, "Oops, an issue occurred!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    // --------------- Redirection function
    public void goToNFC(View view) {
        Intent activeIntent = new Intent(this, WritePage.class);
        startActivity(activeIntent);

    }

    // must haves for data to display, otherwise screen is blank
    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}