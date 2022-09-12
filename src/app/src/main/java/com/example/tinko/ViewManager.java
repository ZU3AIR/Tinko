package com.example.tinko;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

// adapter class that serves as a bridge between UI and data source
public class ViewManager extends FirebaseRecyclerAdapter<Model, ViewManager.allViews> {

    private FirebaseUser fbUser;
    private DatabaseReference dbRef;
    private String uID;

    // constructor matching super
    public ViewManager(@NonNull FirebaseRecyclerOptions<Model> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull allViews holder, @SuppressLint("RecyclerView") final int position, @NonNull Model model) {
        //setting the text
        holder.name.setText(model.getName());
        holder.username.setText(model.getUsername());
        holder.link.setText(model.getLink());
        //using the glide image library to set image
        Glide.with(holder.image.getContext())
                .load(model.getTurl())
                .placeholder(R.drawable.brokenimage)
                .error(R.drawable.brokenimage)
                .into(holder.image);

        // getting user id once again
        fbUser = FirebaseAuth.getInstance().getCurrentUser();
        dbRef = FirebaseDatabase.getInstance().getReference("Users");
        uID = fbUser.getUid();



        // making the delete button erase the data from firebase
        holder.delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference().child("Users/" + uID + "/" + "accounts").child(getRef(position).getKey()).removeValue();
            }
        });

        holder.sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager myClipboard = (ClipboardManager) view.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                String linkname = model.getLink();
                ClipData clip = ClipData.newPlainText("Edit Text", linkname);
                myClipboard.setPrimaryClip(clip);

                clip.getDescription();

                Toast.makeText(view.getContext(), "Copied", Toast.LENGTH_SHORT).show();
            }
        });
    }


    // binding this to RecyclerView
    @NonNull
    @Override
    public allViews onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_main, parent, false);
        return new allViews(item);
    }

    class allViews extends RecyclerView.ViewHolder{

        // setting variables
        TextView link, name, username, url;
        ImageView image;
        ImageButton delBtn, sendBtn;

        public allViews(@NonNull View view) {
            super(view);

            image = (ImageView)view.findViewById(R.id.accountimage);
            name = (TextView) view.findViewById(R.id.account);
            username = (TextView) view.findViewById(R.id.username_id);
            link = (TextView) view.findViewById(R.id.link);
            delBtn = (ImageButton) view.findViewById(R.id.deleteButton);
            sendBtn = (ImageButton) view.findViewById(R.id.shareButtonNFC);


        }
    }
}
