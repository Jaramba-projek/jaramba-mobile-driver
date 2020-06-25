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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HistoryDriver extends AppCompatActivity {
    private ImageView rate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_driver);

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
    }
}
