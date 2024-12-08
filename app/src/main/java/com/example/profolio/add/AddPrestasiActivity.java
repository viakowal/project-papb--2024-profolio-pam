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

import com.example.profolio.modelfragment.PrestasiModel;
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

public class AddPrestasiActivity extends AppCompatActivity {
    EditText edtNamaPrestasi, edtDeskripsiPrestasi, edtJabatanPrestasi, edtTahunPrestasi;
    ImageView ivPrestasi;
    AppCompatButton btnAddPrestasi, btnUploadSertif;
    Uri imageUri = null;
    private static final int galleryCode = 1;
    FirebaseStorage mStorage;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_prestasi);

        edtNamaPrestasi = findViewById(R.id.edtNamaPrestasi);
        edtDeskripsiPrestasi = findViewById(R.id.edtDeskripsiPrestasi);
        edtJabatanPrestasi = findViewById(R.id.edtJabatanPrestasi);
        edtTahunPrestasi = findViewById(R.id.edtTahunPrestasi);
        ivPrestasi = findViewById(R.id.iv_Prestasi);

        btnUploadSertif = findViewById(R.id.btn_UploadSertif);
        btnAddPrestasi = findViewById(R.id.btnAddPrestasi);

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

        if (requestCode == galleryCode && resultCode == RESULT_OK){
            imageUri = data.getData();
            ivPrestasi.setImageURI(imageUri);
        }

        btnAddPrestasi.setOnClickListener(v -> {
            String getNamaPrestasi = edtNamaPrestasi.getText().toString();
            String getJabatanPrestasi = edtDeskripsiPrestasi.getText().toString();
            String getDeskripsiPrestasi = edtJabatanPrestasi.getText().toString();
            String getTahunPrestasi = edtTahunPrestasi.getText().toString();


            if (getNamaPrestasi.isEmpty()) {
                edtNamaPrestasi.setError("Entry Prestasi Name");
            } else if (getDeskripsiPrestasi.isEmpty()) {
                edtDeskripsiPrestasi.setError("Entry Prestasi Description");
            } else if (getJabatanPrestasi.isEmpty()) {
                edtJabatanPrestasi.setError("Entry Jabatan Status");
            } else if (getTahunPrestasi.isEmpty()) {
                edtTahunPrestasi.setError("Entry Tahun Mulai");
            } else {
                StorageReference filePath = mStorage.getReference().child("imagePostPrestasi").child(imageUri.getLastPathSegment());
                filePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> downloadUrl = taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                String getSertifikatPrestasi = task.getResult().toString();
                                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                                database.child("Users").child(userId).child("Prestasi").push().setValue(new PrestasiModel(getNamaPrestasi,
                                        getJabatanPrestasi, getDeskripsiPrestasi, getTahunPrestasi, getSertifikatPrestasi)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        startActivity(new Intent(AddPrestasiActivity.this, HomePageActivity.class));
                                    }
                                });
                            }
                        });
                        Toast.makeText(AddPrestasiActivity.this, "Data has been added", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddPrestasiActivity.this, "Data failed to add", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}