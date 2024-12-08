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

import com.example.profolio.adapterfragment.AdapterPrestasi;
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
import com.squareup.picasso.Picasso;

public class EditPrestasiActivity extends AppCompatActivity {
    TextView edtNamaPrestasi2, edtDeskripsiPrestasi2,
            edtJabatanPrestasi2, edtTahunPrestasi2;
    ImageView ivPrestasi2;
    Uri imageUri = null;
    FirebaseStorage mStorage;
    private static final int galleryCode = 1;
    AppCompatButton btnEditPrestasi, btnUploadSertif2;

    AdapterPrestasi adapterPrestasi;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_prestasi);

        edtNamaPrestasi2 = findViewById(R.id.edtNamaPrestasi2);
        edtDeskripsiPrestasi2 = findViewById(R.id.edtDeskripsiPrestasi2);
        edtJabatanPrestasi2 = findViewById(R.id.edtJabatanPrestasi2);
        edtTahunPrestasi2 = findViewById(R.id.edtTahunPrestasi2);
        ivPrestasi2 = findViewById(R.id.iv_Prestasi2);

        btnUploadSertif2 = findViewById(R.id.btn_UploadSertif2);
        btnEditPrestasi = findViewById(R.id.btnEditPrestasi);

        mStorage = FirebaseStorage.getInstance();

        Intent getData = getIntent();
        String nama = getData.getStringExtra("nama");
        String deskripsi = getData.getStringExtra("deskripsi");
        String jabatan = getData.getStringExtra("jabatan");
        String tahun = getData.getStringExtra("tahun");
        String sertifikat = getData.getStringExtra("sertifikat");
        Picasso.get().load(sertifikat).into(ivPrestasi2);

        edtNamaPrestasi2.setText(nama);
        edtDeskripsiPrestasi2.setText(deskripsi);
        edtJabatanPrestasi2.setText(jabatan);
        edtTahunPrestasi2.setText(tahun);

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
            ivPrestasi2.setImageURI(imageUri);
        }

        Intent getData2 = getIntent();
        String key = getData2.getStringExtra("key");

        btnEditPrestasi.setOnClickListener(v -> {
            String getNamaPrestasi = edtNamaPrestasi2.getText().toString();
            String getDeskripsiPrestasi = edtDeskripsiPrestasi2.getText().toString();
            String getJabatanPrestasi = edtJabatanPrestasi2.getText().toString();
            String getTahunPrestasi = edtTahunPrestasi2.getText().toString();

            if (getNamaPrestasi.isEmpty()) {
                edtNamaPrestasi2.setError("Entry Organisasi Name");
            } else if (getDeskripsiPrestasi.isEmpty()) {
                edtDeskripsiPrestasi2.setError("Entry Organisasi Description");
            } else if (getJabatanPrestasi.isEmpty()) {
                edtJabatanPrestasi2.setError("Entry Jabatan Status");
            } else if (getTahunPrestasi.isEmpty()) {
                edtTahunPrestasi2.setError("Entry Tahun Mulai");
            } else  {
                Dialog popUp = new Dialog(EditPrestasiActivity.this);
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
                        StorageReference filePath = mStorage.getReference().child("imagePostPrestasi").child(imageUri.getLastPathSegment());
                        filePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Task<Uri> downloadUrl = taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        String getSertifikatPrestasi = task.getResult().toString();
                                        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                                        database.child("Users").child(userId).child("Prestasi").child(key).setValue(new PrestasiModel(getNamaPrestasi, getJabatanPrestasi,
                                                getDeskripsiPrestasi, getTahunPrestasi, getSertifikatPrestasi)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Intent back = new Intent(EditPrestasiActivity.this, HomePageActivity.class);
                                                startActivity(back);
                                            }
                                        });
                                    }
                                });
                                Toast.makeText(EditPrestasiActivity.this, "Update Data Succesfully", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(EditPrestasiActivity.this, "Update Data Failed", Toast.LENGTH_SHORT).show();
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