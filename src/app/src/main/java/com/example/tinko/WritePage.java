package com.example.tinko;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.nfc.tech.NdefFormatable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.app.PendingIntent;
import android.content.IntentFilter;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Parcelable;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.Arrays;

public class WritePage extends AppCompatActivity{

    //Fields
    private TextView textView;
    private PendingIntent pendingIntent;
    private IntentFilter[] readFilters;
    private EditText inputView;
    private NdefMessage messageToWrite;
    private IntentFilter[] writeFilters;
    private String[][] writeTechList;

    //firebase
    private FirebaseUser User;
    private DatabaseReference Reference;
    private String id;
    private String linkedId;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_page);
        textView = (TextView) findViewById(R.id.text);
        inputView = (EditText) findViewById(R.id.input);

        //Here we create Intent Filters to utilize NFC technology
        try {
            Intent intent = new Intent(this, getClass());
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            IntentFilter azizaFilter = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
            azizaFilter.addDataScheme("http");
            IntentFilter textFilter = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED, "text/plain");

            readFilters = new IntentFilter[]{azizaFilter, textFilter};
            writeFilters = new IntentFilter[]{};
            writeTechList = new String[][]{
                    {Ndef.class.getName()},
                    {NdefFormatable.class.getName()}
            };
        } catch (IntentFilter.MalformedMimeTypeException e){
            e.printStackTrace();
        }

        processNFC(getIntent());

        User = FirebaseAuth.getInstance().getCurrentUser();
        Reference = FirebaseDatabase.getInstance().getReference("Users");
        id = User.getUid();

        Query query = Reference.orderByChild("link");

        /*
        //https://stackoverflow.com/questions/43293935/how-to-get-child-of-child-value-from-firebase-in-android
        Reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    linkedId = snapshot.child("link").getValue().toString();
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

         */

    }

    //This method writes to the tag
    private void enableWrite() {
        NfcAdapter.getDefaultAdapter(this).enableForegroundDispatch(this, pendingIntent, writeFilters, writeTechList);
    }

    //This method allows tags to be read
    private void enableRead() {
        NfcAdapter.getDefaultAdapter(this).enableForegroundDispatch(this, pendingIntent, readFilters, null);
    }

    //This method disables reading once a tag has been read
    private void disableRead() {
        NfcAdapter.getDefaultAdapter(this).disableForegroundDispatch(this);
    }

    @Override
    protected void onPause(){
        super.onPause();
        disableRead();
    }

    @Override
    protected void onResume(){
        super.onResume();
        enableRead();
    }

    @Override
    protected void onNewIntent(Intent intent){
        super.onNewIntent(intent);
        processNFC(intent);
    }


    private void processNFC(Intent intent) {
        if (messageToWrite != null) {
            writetoTag(intent);
        } else {
            readTag(intent);
        }
    }

    //Here we are writing the URI
    private void writetoTag(Intent intent) {
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if (tag != null) {
            try {
                Ndef ndef = Ndef.get(tag);
                if (ndef == null) {
                    NdefFormatable ndefFormatable = NdefFormatable.get(tag);
                    if (ndefFormatable != null) {
                        ndefFormatable.connect();
                        ndefFormatable.format(messageToWrite);
                        ndefFormatable.close();
                    }
                } else {
                    ndef.connect();
                    ndef.writeNdefMessage(messageToWrite);
                    ndef.close();
                }
            } catch (FormatException | IOException e) {
                throw new RuntimeException(e);

            } finally {
                messageToWrite = null;
            }
        }
    }

    //Here we are reading the NFC tag
    private void readTag(Intent intent) {
        Parcelable[] messages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        textView.setText("");
        if (messages != null) {
            for(Parcelable message : messages) {
                NdefMessage ndefMessage = (NdefMessage) message;
                for(NdefRecord record : ndefMessage.getRecords()) {
                     switch(record.getTnf()) {
                        case NdefRecord.TNF_WELL_KNOWN:
                            textView.append("WELL KNOWN: ");
                            if (Arrays.equals(record.getType(), NdefRecord.RTD_URI)) {
                                textView.append("URI: ");
                                textView.append(new String(record.getPayload()));
                                textView.append("\n");
                            }
                    }
                }
            }
        }
    }

    //Sending written URI into the NFC tag
    public void onWriteUri(View view) {
        if (inputView.getText().toString().matches("")) {
            Toast.makeText(this, "Cannot be empty", Toast.LENGTH_SHORT).show();
        }
        else {
            NdefRecord record = NdefRecord.createUri(inputView.getText().toString());
            messageToWrite = new NdefMessage(new NdefRecord[]{record});
            textView.setText("Hit button and tap tag to write url x2");
            enableWrite();
        }
    }
}

