package com.example.profolio.edit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.profolio.R;
import com.example.profolio.modelfragment.UserModel;
import com.example.profolio.profile.ProfilePageFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class EditProfileActivity extends AppCompatActivity {

    EditText edtProfileUsername, edtProfileFirstName, edtProfileLastName, edtProfilePhone, edtProfileEmail, edtProfileSMA, edtProfileSMAPeriod, edtProfileUniversity, edtProfileUniversityPeriod, edtProfileSkills, edtProfileDeskripsi;
    TextView jumlahOrganisasi, jumlahKepanitiaan, jumlahPrestasi;
    ShapeableImageView ivFotoProfile;
    AppCompatButton btn_save;
    FloatingActionButton btnEditProfile;
    Uri imageUri = null;
    FirebaseStorage mStorage;
    private static final int galleryCode = 1;

    List<UserModel> userItems;

    DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        edtProfileUsername = findViewById(R.id.edtProfileUsername);
        edtProfileFirstName = findViewById(R.id.edtProfileFirstName);
        edtProfileLastName = findViewById(R.id.edtProfileLastName);
        edtProfilePhone = findViewById(R.id.edtProfilePhone);
        edtProfileEmail = findViewById(R.id.edtProfileEmail);
        edtProfileSMA = findViewById(R.id.edtProfileSMA);
        edtProfileSMAPeriod = findViewById(R.id.edtProfileSMAPeriod);
        edtProfileUniversity = findViewById(R.id.edtProfileUniversity);
        edtProfileUniversityPeriod = findViewById(R.id.edtProfileUniversityPeriod);
        edtProfileSkills = findViewById(R.id.edtProfileSkills);
        edtProfileDeskripsi = findViewById(R.id.edtProfileDeskripsi);
        btn_save = findViewById(R.id.btnEditProfile);
        jumlahOrganisasi = findViewById(R.id.jumlahOrganisasi);
        jumlahKepanitiaan = findViewById(R.id.jumlahKepanitiaan);
        jumlahPrestasi = findViewById(R.id.jumlahPrestasi);
        ivFotoProfile = findViewById(R.id.ivProfile);

        btnEditProfile = findViewById(R.id.btnEditImageProfile);

        mStorage = FirebaseStorage.getInstance();

        Intent getData = getIntent();
        String username = getData.getStringExtra("username");
        String firstname = getData.getStringExtra("firstname");
        String lastname = getData.getStringExtra("lastname");
        String phone = getData.getStringExtra("phone");
        String email = getData.getStringExtra("email");
        String sma = getData.getStringExtra("sma");
        String smaperiod = getData.getStringExtra("smaperiod");
        String university = getData.getStringExtra("university");
        String universityperiod = getData.getStringExtra("universityperiod");
        String skills = getData.getStringExtra("skills");
        String deskripsi = getData.getStringExtra("deskripsi");
        String jmlhOrganisasi = getData.getStringExtra("jumlahorganisasi");
        String jmlhKepanitiaan = getData.getStringExtra("jumlahkepanitiaan");
        String jmlhPrestasi = getData.getStringExtra("jumlahprestasi");
        String fotoProfile = getData.getStringExtra("fotoProfile");
        Picasso.get().load(fotoProfile).into(ivFotoProfile);

        edtProfileUsername.setText(username);
        edtProfileFirstName.setText(firstname);
        edtProfileLastName.setText(lastname);
        edtProfilePhone.setText(phone);
        edtProfileEmail.setText(email);
        edtProfileSMA.setText(sma);
        edtProfileSMAPeriod.setText(smaperiod);
        edtProfileUniversity.setText(university);
        edtProfileUniversityPeriod.setText(universityperiod);
        edtProfileSkills.setText(skills);
        edtProfileDeskripsi.setText(deskripsi);
        jumlahOrganisasi.setText(jmlhOrganisasi);
        jumlahKepanitiaan.setText(jmlhKepanitiaan);
        jumlahPrestasi.setText(jmlhPrestasi);

        btnEditProfile.setOnClickListener(v -> {
            Intent addProfile = new Intent(Intent.ACTION_GET_CONTENT);
            addProfile.setType("image/*");
            startActivityForResult(addProfile, galleryCode);
        });

        btn_save.setOnClickListener(v -> saveUserData());
    }

    private void saveUserData() {
        // Retrieve the values from EditText fields
        String username = edtProfileUsername.getText().toString();
        String firstName = edtProfileFirstName.getText().toString();
        String lastName = edtProfileLastName.getText().toString();
        String phone = edtProfilePhone.getText().toString();
        String email = edtProfileEmail.getText().toString();
        String seniorHighSchool = edtProfileSMA.getText().toString();
        String seniorHighSchoolPeriod = edtProfileSMAPeriod.getText().toString();
        String university = edtProfileUniversity.getText().toString();
        String universityPeriod = edtProfileUniversityPeriod.getText().toString();
        String skills = edtProfileSkills.getText().toString();
        String selfDescription = edtProfileDeskripsi.getText().toString();

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        database.child("Users").child(userId).child("UserData").setValue(new UserModel(username, firstName, lastName, phone, email,
                        seniorHighSchool, seniorHighSchoolPeriod, university,
                        universityPeriod, skills, selfDescription, ""))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(EditProfileActivity.this, "Update Data Successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditProfileActivity.this, "Update Data Failed", Toast.LENGTH_SHORT).show();
                    }
                });
//        StorageReference filePath = mStorage.getReference().child("imageProfile").child(imageUri.getLastPathSegment());
//        filePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                Task<Uri> downloadUrl = taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Uri> task) {
//                        String getFotoProfile = task.getResult().toString();
//
//                        database.child("Users").child(userId).child("UserData").setValue(new UserModel(username, firstName, lastName, phone, email,
//                                seniorHighSchool, seniorHighSchoolPeriod, university,
//                                universityPeriod, skills, selfDescription, getFotoProfile)).addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void unused) {
//                                Toast.makeText(EditProfileActivity.this, "Update Data Successfully", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                    }
//                });
//
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(EditProfileActivity.this, "Update Data Failed", Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == galleryCode && resultCode == RESULT_OK) {
            imageUri = data.getData();
            ivFotoProfile.setImageURI(imageUri);
        }
    }
}