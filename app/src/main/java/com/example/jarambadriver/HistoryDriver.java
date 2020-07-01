package com.example.jarambadriver;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class HistoryDriver extends AppCompatActivity {
    private ImageView rate;
    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;
    private DatabaseReference database;
    FirebaseAuth firebaseAuth;

    private ArrayList<HistoryData> historyData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_driver);

        recyclerView = findViewById(R.id.rv_dtlist);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        database = FirebaseDatabase.getInstance().getReference();
        //firebaseAuth = FirebaseAuth.getInstance();
        //FirebaseUser user = firebaseAuth.getCurrentUser();
        //String uid = user.getUid();

        database.child("Mobile_Apps").child("Driver").child("Uji_Coba").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                historyData.clear();

                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    HistoryData data = new HistoryData();
                    data = noteDataSnapshot.getValue(HistoryData.class);
                    data.setKey(noteDataSnapshot.getKey());

                    historyData.add(data);
                }

                Collections.reverse(historyData);
                recyclerAdapter = new RecyclerAdapter(historyData);
                recyclerView.setAdapter(recyclerAdapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

/*
        rate = findViewById(R.id.rate);

        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(HistoryDriver.this);
                dialog.setContentView(R.layout.history_rate);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                Button Submit = dialog.findViewById(R.id.submit_rate);
                final RatingBar Rating = dialog.findViewById(R.id.ratingBar);
                final EditText Komentar = dialog.findViewById(R.id.comment_rate);

                Submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String text = "Rating Bus : " + Rating.getRating() + "\nKomentar : " + Komentar.getText();
                        Toast.makeText(HistoryDriver.this, text, Toast.LENGTH_SHORT).show();

                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        */
    }
}
