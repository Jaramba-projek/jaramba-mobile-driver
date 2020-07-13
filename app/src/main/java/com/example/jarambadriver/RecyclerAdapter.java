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
import java.util.HashMap;

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

        if (isi.getStatus().contains("done")) {
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

                                    myRef.child("Mobile_Apps").child("Driver").child(isi.getId_driver()).child("History_Trip_Driver").child(isi.getKey()).setValue(new HistoryData(Rating.getRating(), Komentar.getText().toString(), isi.getTrayek(), isi.getPlate_number(), isi.getTanggal(), isi.getStart_time(), isi.getEnd_time(), "done", isi.getId_key(), isi.getId_driver(), isi.getStatus()));

                                    HashMap<String, Object> rating = new HashMap<>();
                                    rating.put("rating", Rating.getRating());
                                    myRef.child("history_trip_dashboard").child(isi.getId_key()).updateChildren(rating);

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

            holder.info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog dialog1 = new Dialog(context);
                    dialog1.setContentView(R.layout.activity_history_driver_detail);
                    dialog1.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                    final TextView trayek = dialog1.findViewById(R.id.tv_detail_destinasi);
                    final TextView plat = dialog1.findViewById(R.id.tv_detail_plat_damri);
                    final TextView tanggal = dialog1.findViewById(R.id.tv_detail_tanggal);
                    final TextView waktu = dialog1.findViewById(R.id.tv_detail_waktu);

                    trayek.setText(isi.getTrayek());
                    plat.setText(isi.getPlate_number());
                    tanggal.setText(isi.getTanggal());
                    String time = isi.getStart_time() + " - " + isi.getEnd_time();
                    waktu.setText(time);

                    dialog1.show();
                }
            });
        } else {
            holder.rating.setVisibility(View.GONE);

            holder.info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog dialog1 = new Dialog(context);
                    dialog1.setContentView(R.layout.activity_history_driver_detail);
                    dialog1.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                    final TextView trayek = dialog1.findViewById(R.id.tv_detail_destinasi);
                    final TextView plat = dialog1.findViewById(R.id.tv_detail_plat_damri);
                    final TextView tanggal = dialog1.findViewById(R.id.tv_detail_tanggal);
                    final TextView waktu = dialog1.findViewById(R.id.tv_detail_waktu);

                    trayek.setText(isi.getTrayek());
                    plat.setText(isi.getPlate_number());
                    tanggal.setText(isi.getTanggal());
                    String time = isi.getStart_time() + " - Trip Belum Selesai!";
                    waktu.setText(time);

                    dialog1.show();
                }
            });
        }



        holder.trayek.setText(isi.getTrayek());
        holder.plat.setText(isi.getPlate_number());
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