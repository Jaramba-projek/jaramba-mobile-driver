package com.example.jarambadriver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.spark.submitbutton.SubmitButton;

import java.math.BigInteger;
import java.sql.Driver;
import java.util.HashMap;

public class LoginPage extends AppCompatActivity {


    private EditText etEmail, etPassword;
    private TextInputLayout txtPass;
    private ImageView img_logo;
    private TextView slogan;
    private SubmitButton btnlogin;


    Animation rightin_anim,top_anim, bottom_anim;
    AwesomeValidation awesomeValidation;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    FirebaseAuth.AuthStateListener mAuthStateListener;

    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.et_email_login);
        etPassword = findViewById(R.id.et_password_login);

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("driver");

        rightin_anim = AnimationUtils.loadAnimation(this,R.anim.right_in);
        top_anim = AnimationUtils.loadAnimation(this,R.anim.splash_top);
        bottom_anim = AnimationUtils.loadAnimation(this,R.anim.splash_bottom);

        img_logo = findViewById(R.id.img_logo);
        slogan = findViewById(R.id.txt_slogan);
        btnlogin = findViewById(R.id.btn_login);
        txtPass = findViewById(R.id.txt_password);

        img_logo.setAnimation(top_anim);
        slogan.setAnimation(top_anim);

        etEmail.setAnimation(rightin_anim);
        etPassword.setAnimation(rightin_anim);

        btnlogin.setAnimation(rightin_anim);
        txtPass.setAnimation(rightin_anim);

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }


    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Konfirmasi keluar aplikasi");
        builder.setIcon(R.drawable.ic_exit_to_app_black_24dp);
        builder.setMessage("Anda yakin ingin keluar aplikasi ? ");
        builder.setCancelable(false);

        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finishAndRemoveTask();
                finish();
            }
        });

        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void login(View view) {

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("driver");

        final String email1 = etEmail.getText().toString().trim();
        final String password = etPassword.getText().toString().trim();


        Query query = databaseReference.orderByChild("email").equalTo(etEmail.getText().toString().trim());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    //get data
                    String email2 = "" + ds.child("email").getValue();
                    String pwd = ""+ds.child("password").getValue();

                    if(email1.equals(email2)) {
                        if(password.equals(pwd)) {

                            Toast.makeText(LoginPage.this, "Berhasil lo ini", Toast.LENGTH_SHORT).show();
                          //  startActivity(new Intent(LoginPage.this, Trip_start.class));
                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

    }

}
