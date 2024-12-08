package com.example.profolio.add;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddOrganisasiActivity extends AppCompatActivity {
    private static final int galleryCode = 1;
    EditText edtNamaOrganisasi, edtDeskripsiOrganisasi, edtJabatanOrganisasi, edtTahunMulaiOrganisasi, edtTahunSelesaiOrganisasi;
    AppCompatButton btnAddOrganisasi, btnUploadSertif;
    ImageView ivOrganisasi;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    Uri imageUri = null;
    FirebaseStorage mStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_organisasi);

        edtNamaOrganisasi = findViewById(R.id.edtNamaOrganisasi);
        edtDeskripsiOrganisasi = findViewById(R.id.edtDeskripsiOrganisasi);
        edtJabatanOrganisasi = findViewById(R.id.edtJabatanOrganisasi);
        edtTahunMulaiOrganisasi = findViewById(R.id.edtTahunMulaiOrganisasi);
        edtTahunSelesaiOrganisasi = findViewById(R.id.edtTahunSelesaiOrganisasi);

        btnAddOrganisasi = findViewById(R.id.btnAddOrganisasi);
        btnUploadSertif = findViewById(R.id.btn_UploadSertif);
        ivOrganisasi = findViewById(R.id.iv_Organisasi);

        mStorage = FirebaseStorage.getInstance();

        btnUploadSertif.setOnClickListener(v -> {
            Intent addImg = new Intent(Intent.ACTION_GET_CONTENT);
            addImg.setType("image/*");
            startActivityForResult(addImg, galleryCode);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == galleryCode && resultCode == RESULT_OK) {
            imageUri = data.getData();
            ivOrganisasi.setImageURI(imageUri);
        }

        btnAddOrganisasi.setOnClickListener(v -> {
            String getNamaOrganisasi = edtNamaOrganisasi.getText().toString();
            String getJabatanOrganisasi = edtJabatanOrganisasi.getText().toString();
            String getDeskripsiOrganisasi = edtDeskripsiOrganisasi.getText().toString();
            String getTahunMulaiOrganisasi = edtTahunMulaiOrganisasi.getText().toString();
            String getTahunSelesaiOrganisasi = edtTahunSelesaiOrganisasi.getText().toString();

            if (getNamaOrganisasi.isEmpty()) {
                edtNamaOrganisasi.setError("Entry Organisasi Name");
            } else if (getDeskripsiOrganisasi.isEmpty()) {
                edtDeskripsiOrganisasi.setError("Entry Organisasi Description");
            } else if (getJabatanOrganisasi.isEmpty()) {
                edtJabatanOrganisasi.setError("Entry Jabatan Status");
            } else if (getTahunMulaiOrganisasi.isEmpty()) {
                edtTahunMulaiOrganisasi.setError("Entry Tahun Mulai");
            } else if (getTahunSelesaiOrganisasi.isEmpty()) {
                edtTahunSelesaiOrganisasi.setError("Entry Tahun Selesai");
            } else {
                StorageReference filePath = mStorage.getReference().child("imagePost").child(imageUri.getLastPathSegment());
                filePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> downloadUrl = taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                String getSertifOrganisasi = task.getResult().toString();
                                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                                database.child("Users").child(userId).child("Organisasi").push().setValue(new OrganisasiModel(getNamaOrganisasi, getJabatanOrganisasi, getTahunMulaiOrganisasi, getTahunSelesaiOrganisasi, getDeskripsiOrganisasi, getSertifOrganisasi)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        startActivity(new Intent(AddOrganisasiActivity.this, HomePageActivity.class));
                                    }
                                });
                            }
                        });
                        Toast.makeText(AddOrganisasiActivity.this, "Data has been added", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddOrganisasiActivity.this, "Data failed to add", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

}