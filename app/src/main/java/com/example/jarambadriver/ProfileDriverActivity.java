package com.example.jarambadriver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.HashMap;

public class ProfileDriverActivity extends AppCompatActivity {


    FirebaseDatabase database;
    DatabaseReference databaseReference;

    //storage
    StorageReference storageReference;
    //path where image will be created
    String storagePath = "Driver_Profile_Imgs/";

    ProgressDialog progressDialog;

    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 200;

    private static final int IMAGE_PICK_GALLERY_CODE = 300;
    private static final int IMAGE_PICK_CAMERA_CODE = 400;

    private TextView nameTv, emailTv, phoneTv;
    private ImageView avatarIv, bgDynamic, icSetting;

    String[] cameraPermission;
    String[] storagePermission;

    private String uid;

    //uri of picked image
    Uri image_uri;

    //for checking profile picture
    String profile;

    BottomNavigationView bottomNavigationView;
    ImageView greetImg;

    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_driver);

        //firebase component init
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("driver");
        storageReference = FirebaseStorage.getInstance().getReference();

        //init array of permission
        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        //bottom nav init
        bottomNavigationView = findViewById(R.id.menu_navigasi);
        bottomNavigationView.setSelectedItemId(R.id.profile);

        //dynamic bg init
        greetImg = findViewById(R.id.greeting_img_profile);



        //view init
        avatarIv = findViewById(R.id.img_profile_page);
        nameTv = findViewById(R.id.tv_profil_username);
        emailTv = findViewById(R.id.tv_profil_email);
        phoneTv = findViewById(R.id.tv_profil_phone);

        setTitle("Profile Driver");

        //init progres dialog
        progressDialog = new ProgressDialog(ProfileDriverActivity.this);

        setOnNavigationSelectedListener();
        greeting();

        //progress dialog shown to wait retrieve data
        progressDialog();

        Query query = databaseReference.orderByChild("email").equalTo("nardiyansah@gmail.com");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    //get children from firebase
                    String email = ""+ds.child("email").getValue();
                    String name = ""+ds.child("nama").getValue();
                    String phone = ""+ds.child("no_telp").getValue();
                    String image = ""+ds.child("image").getValue();
                    key = ""+ds.child("key").getValue();

                    //set data
                    nameTv.setText(name);
                    emailTv.setText(email);
                    phoneTv.setText(phone);


                    //Picasso.get().load(R.drawable.ic_face_black_24dp).into(avatarIv);
                    try {
                        //if image is received then set
                        Glide.with(ProfileDriverActivity.this).load(image).into(avatarIv);
                    } catch (Exception e) {
                        //if there is any exception while getting image then set default
                        Picasso.get().load(R.drawable.ic_face_black_24dp).into(avatarIv);
                    }

                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    private void progressDialog() {
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    private void setOnNavigationSelectedListener() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.history:
                        startActivity(new Intent(ProfileDriverActivity.this, HistoryDriverDetail.class));
                        overridePendingTransition(0,0);
                        finish();
                        break;

                    case R.id.nav_home:
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        finish();
                        break;

                    case R.id.trip:
                        startActivity(new Intent(getApplicationContext(), Trip_start.class));
                        finish();
                        break;

                }

                return false;
            }
        });
    }

    public void logout(View view) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("Konfirmasi Logout aplikasi");
        builder.setIcon(R.drawable.ic_exit_to_app_black_24dp);
        builder.setMessage("Anda yakin ingin Logout ? ");
        builder.setCancelable(false);

        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                FirebaseAuth.getInstance().signOut();
//                startActivity(new Intent(ProfileDriverActivity.this, LoginPage.class));
//                finish();
            }
        });

        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        androidx.appcompat.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    @SuppressLint("SetTextI18n")
    private void greeting() {
        Calendar calendar = Calendar.getInstance();
        int timeOfDay = calendar.get(Calendar.HOUR_OF_DAY);

        if (timeOfDay > 0 && timeOfDay < 18) {
            greetImg.setImageResource(R.drawable.img_default_half_morning);
            Glide.with(ProfileDriverActivity.this).load(R.drawable.img_default_half_morning).into(greetImg);
        }else if (timeOfDay >= 18 && timeOfDay < 23) {
            Glide.with(ProfileDriverActivity.this).load(R.drawable.img_default_half_night).into(greetImg);
            greetImg.setImageResource(R.drawable.img_default_half_night);
        }

    }


    public void changeProfilePicture(View view) {
        profile = "image";
        String option[] = {"Kamera","Galeri"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pilih mode pengambilan gambar");
        builder.setItems(option, new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (which == 0) {
                    //kamera clicked
                    if(!checkCameraPermission()){
                        requestCameraPermission();
                    } else {
                        pickFromCamera();
                    }
                } else if (which == 1) {
                    //galeri clicked
                    if(!checkStoragePermission()){
                        requsetStoragePermission();
                    } else {
                        pickFromGaleri();
                    }
                }
            }
        });
        builder.create().show();
    }

    private void pickFromGaleri() {
        Intent galeryIntent = new Intent(Intent.ACTION_PICK);
        galeryIntent.setType("image/*");
        startActivityForResult(galeryIntent, IMAGE_PICK_GALLERY_CODE);
    }

    private void pickFromCamera() {
        ContentValues cv = new ContentValues();
        cv.put(MediaStore.Images.Media.TITLE, "Temp Pict");
        cv.put(MediaStore.Images.Media.DESCRIPTION, "Temp Description");

        //put image uri
        image_uri = ProfileDriverActivity.this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                cv);

        //intent to start camera
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CODE);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requsetStoragePermission() {
        requestPermissions(storagePermission, STORAGE_REQUEST_CODE);
    }

    private boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestCameraPermission() {
        requestPermissions(cameraPermission, CAMERA_REQUEST_CODE);
    }

    private boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return  result && result1;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case  CAMERA_REQUEST_CODE: {
                if(grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if(cameraAccepted && writeStorageAccepted) {
                        pickFromCamera();
                    } else {
                        Toast.makeText(ProfileDriverActivity.this, "Mohon setujui permission Kamera & Penyimpanan", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;

            case STORAGE_REQUEST_CODE: {
                if(grantResults.length > 0) {
                    boolean writeStorageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if(writeStorageAccepted) {
                        pickFromGaleri();
                    } else {
                        Toast.makeText(ProfileDriverActivity.this, "Mohon setujui permission Penyimpanan", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK) {
            if(requestCode == IMAGE_PICK_GALLERY_CODE) {
                image_uri = data.getData();
                uploadProfileDriverPhoto(image_uri);
            }

            else if(requestCode == IMAGE_PICK_CAMERA_CODE) {
                uploadProfileDriverPhoto(image_uri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadProfileDriverPhoto(final Uri uri) {
        progressDialog();

        String filePathandName = storagePath + ""+ profile + "_" + key;

        StorageReference storageReference2nd = storageReference.child(filePathandName);
        storageReference2nd.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();

                        while (!uriTask.isSuccessful());
                        final Uri downloadUri = uriTask.getResult();

                        if(uriTask.isSuccessful()) {
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put(profile, downloadUri.toString());

                            databaseReference.child(key).updateChildren(hashMap)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            progressDialog.dismiss();
                                            Toast.makeText(getApplicationContext(), "Gambar berhasil diperbarui", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "Maaf, upload tidak berhasil", Toast.LENGTH_SHORT).show();
                                }
                            });


                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Maaf, terdapat gangguan ketika upload", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void editProfile (View view) {
        String []options = {"Ubah email pengguna", "Ubah nama pengguna", "Ubah nomor handphone"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pilihan");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == 0) {
                    Toast.makeText(ProfileDriverActivity.this, "Mengubah email anda...", Toast.LENGTH_SHORT).show();
                } else if(which == 1) {
                    showNamePhoneUpdateDialog("nama");
                }else {
                    showNamePhoneUpdateDialog("no_telp");
                }
            }
        });
        builder.create().show();
    }


    private void showNamePhoneUpdateDialog(final String key_ki) {
        final String keys;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if(key_ki.equals("nama")){
            keys = "nama pengguna";
            builder.setIcon(R.drawable.ic_person_black_24dp);
        } else {
            keys = "nomor handphone";
            builder.setIcon(R.drawable.ic_smartphone_black_24dp);
        }
        builder.setTitle("Memperbarui " + keys);

        LinearLayout  linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(10,10,10,10);

        final EditText editText = new EditText(this);
        if(key_ki.equals("nama")){
            editText.setHint("Masukkan " + keys);
            editText.setInputType(InputType.TYPE_CLASS_TEXT);
        } else {
            editText.setHint("Masukkan " + keys);
            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        }

        linearLayout.addView(editText);
        builder.setView(linearLayout);

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                    String value = editText.getText().toString().trim();
                    if(!TextUtils.isEmpty(value)) {
                        progressDialog();

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put(key_ki, value);

                        databaseReference.child(key).updateChildren(hashMap)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        progressDialog.dismiss();
                                        Toast.makeText(ProfileDriverActivity.this, keys + " diperbarui...",Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(ProfileDriverActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(ProfileDriverActivity.this, keys +  " tidak boleh kosong...", Toast.LENGTH_SHORT ).show();
                    }
            }
        });

        builder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                progressDialog.dismiss();
            }
        });
        builder.create().show();


    }
}
