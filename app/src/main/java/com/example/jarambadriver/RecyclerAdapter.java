package com.example.jarambadriver;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ListViewHolder> {
    private ArrayList<HistoryData> rvData;

    private Context context;

    public RecyclerAdapter(ArrayList<HistoryData> inputData) {
        this.rvData = inputData;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_list_item, viewGroup, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ListViewHolder holder, int position) {
        final HistoryData isi = rvData.get(position);

        if (isi.getRate_status().contains("done")) {
            holder.cl.setBackgroundResource(R.drawable.border_black);
            holder.info.setImageResource(R.drawable.ic_baseline_info_black_24);
            holder.rating.setColorFilter(Color.rgb(255,204,0));
            holder.rating.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "Anda Telah Memberikan Rating dan Komentar", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            holder.rating.setColorFilter(Color.rgb(128,128,128));

            holder.rating.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.history_rate);
                    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                    Button Submit = dialog.findViewById(R.id.submit_rate);
                    final RatingBar Rating = dialog.findViewById(R.id.ratingBar);
                    final EditText Komentar = dialog.findViewById(R.id.comment_rate);

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    final DatabaseReference myRef = database.getReference();

                    Submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (Rating.getRating()!=0 && !Komentar.getText().toString().equals("")) {
                                Toast.makeText(context, "Terimakasih Telah Memberikan Rating!", Toast.LENGTH_SHORT).show();

                                myRef.child("Mobile_Apps").child("Driver").child("Uji_Coba").child("History_Trip_Driver").child(isi.getKey()).setValue(new HistoryData(Rating.getRating(), Komentar.getText().toString(), isi.getTrayek(), isi.getPlat(), isi.getTanggal(), isi.getWaktu(), "done"));

                                dialog.dismiss();
                            } else {
                                if (Rating.getRating()==0) {
                                    Toast.makeText(context, "Anda Belum Memberi Rating!", Toast.LENGTH_SHORT).show();
                                } else if (Komentar.getText().toString().equals("")) {
                                    Toast.makeText(context, "Anda Belum Mengisi Komentar!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
                    dialog.show();
                }
            });
        }

        holder.trayek.setText(isi.getTrayek());
        holder.plat.setText(isi.getPlat());

        holder.info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Ini adalah info detail trip", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return rvData.size();
    }

    class ListViewHolder extends RecyclerView.ViewHolder {
        TextView trayek, plat;
        ImageView rating, info;
        ConstraintLayout cl;

        ListViewHolder(View itemView) {
            super(itemView);
            trayek = itemView.findViewById(R.id.trayek);
            plat = itemView.findViewById(R.id.plat);
            rating = itemView.findViewById(R.id.rate);
            info = itemView.findViewById(R.id.iconinfo2);
            cl = itemView.findViewById(R.id.item_history);

            context = itemView.getContext();
        }
    }
}