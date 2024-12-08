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

import com.example.profolio.adapterfragment.AdapterKepanitiaan;
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
import com.squareup.picasso.Picasso;

public class EditKepanitiaanActivity extends AppCompatActivity {
    TextView edtNamaKepanitiaan2, edtDeskripsiKepanitiaan2,
            edtJabatanKepanitiaan2, edtTahunKepanitiaan2;
    ImageView ivKepanitiaan2;
    Uri imageUri = null;
    FirebaseStorage mStorage;
    private static final int galleryCode = 1;
    AppCompatButton btnEditKepanitiaan, btnUploadSertif2;

    AdapterKepanitiaan adapterKepanitiaan;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_kepanitiaan);

        edtNamaKepanitiaan2 = findViewById(R.id.edtNamaKepanitiaan2);
        edtDeskripsiKepanitiaan2 = findViewById(R.id.edtDeskripsiKepanitiaan2);
        edtJabatanKepanitiaan2 = findViewById(R.id.edtJabatanKepanitiaan2);
        edtTahunKepanitiaan2 = findViewById(R.id.edtTahunKepanitiaan2);
        ivKepanitiaan2 = findViewById(R.id.iv_Kepanitiaan2);
        btnUploadSertif2 = findViewById(R.id.btn_UploadSertif2);

        mStorage = FirebaseStorage.getInstance();
        btnEditKepanitiaan = findViewById(R.id.btnEditKepanitiaan);

        Intent getData = getIntent();
        String nama = getData.getStringExtra("nama");
        String deskripsi = getData.getStringExtra("deskripsi");
        String jabatan = getData.getStringExtra("jabatan");
        String tahun = getData.getStringExtra("tahun");
        String sertifikat = getData.getStringExtra("sertifikat");
        Picasso.get().load(sertifikat).into(ivKepanitiaan2);

        edtNamaKepanitiaan2.setText(nama);
        edtDeskripsiKepanitiaan2.setText(deskripsi);
        edtJabatanKepanitiaan2.setText(jabatan);
        edtTahunKepanitiaan2.setText(tahun);

        btnUploadSertif2.setOnClickListener(v -> {
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
            ivKepanitiaan2.setImageURI(imageUri);
        }

        Intent getData2 = getIntent();
        String key = getData2.getStringExtra("key");

        btnEditKepanitiaan.setOnClickListener(v -> {
            String getNamaKepanitiaan = edtNamaKepanitiaan2.getText().toString();
            String getDeskripsiKepanitiaan = edtDeskripsiKepanitiaan2.getText().toString();
            String getJabatanKepanitiaan = edtJabatanKepanitiaan2.getText().toString();
            String getTahunKepanitiaan = edtTahunKepanitiaan2.getText().toString();

            if (getNamaKepanitiaan.isEmpty()) {
                edtNamaKepanitiaan2.setError("Entry Organisasi Name");
            } else if (getDeskripsiKepanitiaan.isEmpty()) {
                edtDeskripsiKepanitiaan2.setError("Entry Organisasi Description");
            } else if (getJabatanKepanitiaan.isEmpty()) {
                edtJabatanKepanitiaan2.setError("Entry Jabatan Status");
            } else if (getTahunKepanitiaan.isEmpty()) {
                edtTahunKepanitiaan2.setError("Entry Tahun Mulai");
            } else  {
                Dialog popUp = new Dialog(EditKepanitiaanActivity.this);
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
                        StorageReference filePath = mStorage.getReference().child("imagePostKepanitiaan").child(imageUri.getLastPathSegment());
                        filePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Task<Uri> downloadUrl = taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        String getSertifKepanitiaan = task.getResult().toString();
                                        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                                        database.child("Users").child(userId).child("Kepanitiaan").child(key).setValue(new KepanitiaanModel(getNamaKepanitiaan, getJabatanKepanitiaan,
                                                getDeskripsiKepanitiaan, getTahunKepanitiaan, getSertifKepanitiaan)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Intent back = new Intent(EditKepanitiaanActivity.this, HomePageActivity.class);
                                                startActivity(back);
                                            }
                                        });
                                    }
                                });
                                Toast.makeText(EditKepanitiaanActivity.this, "Update Data Succesfully", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(EditKepanitiaanActivity.this, "Update Data Failed", Toast.LENGTH_SHORT).show();
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