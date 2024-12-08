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

import com.example.profolio.modelfragment.KepanitiaanModel;
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

public class AddKepanitiaanActivity extends AppCompatActivity {
    EditText edtNamaKepanitiaan, edtDeskripsiKepanitiaan, edtJabatanKepanitiaan, edtTahunKepanitiaan;
    AppCompatButton btnAddKepanitiaan, btnUploadSertif;

    ImageView ivKepanitiaan;

    Uri imageUri = null;

    FirebaseStorage mStorage;

    private static final int galleryCode = 1;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_kepanitiaan);

        edtNamaKepanitiaan = findViewById(R.id.edtNamaKepanitiaan);
        edtDeskripsiKepanitiaan = findViewById(R.id.edtDeskripsiKepanitiaan);
        edtJabatanKepanitiaan = findViewById(R.id.edtJabatanKepanitiaan);
        edtTahunKepanitiaan = findViewById(R.id.edtTahunKepanitiaan);
        ivKepanitiaan = findViewById(R.id.iv_Kepanitiaan);
        btnUploadSertif = findViewById(R.id.btn_UploadSertif);
        btnAddKepanitiaan = findViewById(R.id.btnAddKepanitiaan);

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
            ivKepanitiaan.setImageURI(imageUri);
        }

        btnAddKepanitiaan.setOnClickListener(v -> {
            String getNamaKepanitiaan = edtNamaKepanitiaan.getText().toString();
            String getJabatanKepanitiaan = edtDeskripsiKepanitiaan.getText().toString();
            String getDeskripsiKepanitiaan = edtJabatanKepanitiaan.getText().toString();
            String getTahunKepanitiaan = edtTahunKepanitiaan.getText().toString();


            if (getNamaKepanitiaan.isEmpty()) {
                edtNamaKepanitiaan.setError("Entry Kepanitiaan Name");
            } else if (getDeskripsiKepanitiaan.isEmpty()) {
                edtDeskripsiKepanitiaan.setError("Entry Kepanitiaan Description");
            } else if (getJabatanKepanitiaan.isEmpty()) {
                edtJabatanKepanitiaan.setError("Entry Jabatan Status");
            } else if (getTahunKepanitiaan.isEmpty()) {
                edtTahunKepanitiaan.setError("Entry Tahun Mulai");
            } else {
                StorageReference filePath = mStorage.getReference().child("imagePostKepanitiaan").child(imageUri.getLastPathSegment());
                filePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> downloadUrl = taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                String getSertifOrganisasi = task.getResult().toString();

                                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                database.child("Users").child(userId).child("Kepanitiaan").push().setValue(new KepanitiaanModel(getNamaKepanitiaan, getJabatanKepanitiaan, getDeskripsiKepanitiaan, getTahunKepanitiaan, getSertifOrganisasi)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        startActivity(new Intent(AddKepanitiaanActivity.this, HomePageActivity.class));
                                    }
                                });
                            }
                        });
                        Toast.makeText(AddKepanitiaanActivity.this, "Data has been added", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddKepanitiaanActivity.this, "Data failed to add", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}