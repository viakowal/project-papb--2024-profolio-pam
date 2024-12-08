package com.example.profolio.edit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.profolio.adapterfragment.AdapterOrganisasi;
import com.example.profolio.modelfragment.OrganisasiModel;
import com.example.profolio.R;
import com.example.profolio.homepage.HomePageActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.List;

public class EditOrganisasiActivity extends AppCompatActivity {
    TextView edtNamaOrganisasi2, edtDeskripsiOrganisasi2,
            edtJabatanOrganisasi2, edtTahunMulaiOrganisasi2, edtTahunSelesaiOrganisasi2;
    ImageView ivOrganisasi2;
    Uri imageUri = null;
    FirebaseStorage mStorage;
    private static final int galleryCode = 1;
    AppCompatButton btnEditOrganisasi, btnUploadSertif2;

    AdapterOrganisasi adapterOrganisasi;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_organisasi);

        edtNamaOrganisasi2 = findViewById(R.id.edtNamaOrganisasi2);
        edtDeskripsiOrganisasi2 = findViewById(R.id.edtDeskripsiOrganisasi2);
        edtJabatanOrganisasi2 = findViewById(R.id.edtJabatanOrganisasi2);
        edtTahunMulaiOrganisasi2 = findViewById(R.id.edtTahunMulaiOrganisasi2);
        edtTahunSelesaiOrganisasi2 = findViewById(R.id.edtTahunSelesaiOrganisasi2);
        ivOrganisasi2 = findViewById(R.id.iv_Organisasi2);
        btnUploadSertif2 = findViewById(R.id.btn_UploadSertif2);

        mStorage = FirebaseStorage.getInstance();

        Intent getData = getIntent();
//        String key = getData.getStringExtra("key");
        String nama = getData.getStringExtra("nama");
        String deskripsi = getData.getStringExtra("deskripsi");
        String jabatan = getData.getStringExtra("jabatan");
        String tahunMulai = getData.getStringExtra("tahunMulai");
        String tahunSelesai = getData.getStringExtra("tahunSelesai");
        String sertifikat = getData.getStringExtra("sertifikat");
        Picasso.get().load(sertifikat).into(ivOrganisasi2);

        edtNamaOrganisasi2.setText(nama);
        edtDeskripsiOrganisasi2.setText(deskripsi);
        edtJabatanOrganisasi2.setText(jabatan);
        edtTahunMulaiOrganisasi2.setText(tahunMulai);
        edtTahunSelesaiOrganisasi2.setText(tahunSelesai);

        btnUploadSertif2.setOnClickListener(v -> {
            Intent addImg = new Intent(Intent.ACTION_GET_CONTENT);
            addImg.setType("image/*");
            startActivityForResult(addImg, galleryCode);
        });

        btnEditOrganisasi = findViewById(R.id.btnEditOrganisasi);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == galleryCode && resultCode == RESULT_OK){
            imageUri = data.getData();
            ivOrganisasi2.setImageURI(imageUri);
        }

        Intent getData2 = getIntent();
        String key = getData2.getStringExtra("key");

        btnEditOrganisasi.setOnClickListener(v -> {
            String getNamaOrganisasi = edtNamaOrganisasi2.getText().toString();
            String getDeskripsiOrganisasi = edtDeskripsiOrganisasi2.getText().toString();
            String getJabatanOrganisasi = edtJabatanOrganisasi2.getText().toString();
            String getTahunMulaiOrganisasi = edtTahunMulaiOrganisasi2.getText().toString();
            String getTahunSelesaiOrganisasi = edtTahunSelesaiOrganisasi2.getText().toString();


            if (getNamaOrganisasi.isEmpty()) {
                edtNamaOrganisasi2.setError("Entry Organisasi Name");
            } else if (getDeskripsiOrganisasi.isEmpty()) {
                edtDeskripsiOrganisasi2.setError("Entry Organisasi Description");
            } else if (getJabatanOrganisasi.isEmpty()) {
                edtJabatanOrganisasi2.setError("Entry Jabatan Status");
            } else if (getTahunMulaiOrganisasi.isEmpty()) {
                edtTahunMulaiOrganisasi2.setError("Entry Tahun Mulai");
            } else if (getTahunSelesaiOrganisasi.isEmpty()) {
                edtTahunSelesaiOrganisasi2.setError("Entry Tahun Selesai");
            } else  {
                Dialog popUp = new Dialog(EditOrganisasiActivity.this);
                popUp.setContentView(R.layout.popup1_edit);
                Window window = popUp.getWindow();
                WindowManager.LayoutParams layoutParams = window.getAttributes();
                layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;

                AppCompatButton confirm = popUp.findViewById(R.id.btnEditConfirm);
                AppCompatButton cancel = popUp.findViewById(R.id.btnEditCancel);

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popUp.dismiss();
                    }
                });

                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        StorageReference filePath = mStorage.getReference().child("imagePost").child(imageUri.getLastPathSegment());
                        filePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Task<Uri> downloadUrl = taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        String getSertifOrganisasi = task.getResult().toString();
                                        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                                        database.child("Users").child(userId).child("Organisasi").child(key).setValue(new OrganisasiModel(getNamaOrganisasi, getJabatanOrganisasi,
                                                getTahunMulaiOrganisasi, getTahunSelesaiOrganisasi, getDeskripsiOrganisasi, getSertifOrganisasi)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                startActivity(new Intent(EditOrganisasiActivity.this, HomePageActivity.class));
                                            }
                                        });
                                    }
                                });
                                Toast.makeText(EditOrganisasiActivity.this, "Update Data Succesfully", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(EditOrganisasiActivity.this, "Update Data Failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                        popUp.dismiss();
                    }
                });
                popUp.show();
            }
        });
    }
}