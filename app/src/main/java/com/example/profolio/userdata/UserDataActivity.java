package com.example.profolio.userdata;

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

import com.example.profolio.R;
import com.example.profolio.homepage.HomePageActivity;
import com.example.profolio.login.LoginPageActivity;
import com.example.profolio.modelfragment.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class UserDataActivity extends AppCompatActivity {

    EditText edtUserDataUsername, edtUserDataFirstName, edtUserDataLastName, edtUserDataPhone, edtUserDataEmail, edtUserDataSMA, edtUserDataSMAPeriod,
            edtUserDataUniversity, edtUserDataUniversityPeriod, edtUserDataSkills, edtUserDataDeskripsi;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    ShapeableImageView ivFotoProfile;
    Uri imageUri = null;
    private static final int galleryCode = 1;
    FirebaseStorage mStorage;

    FirebaseAuth mAuth;
    AppCompatButton btn_add_user_data;
    FloatingActionButton btnAddProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_data);

        edtUserDataUsername = findViewById(R.id.edtUserDataUsername);
        edtUserDataFirstName = findViewById(R.id.edtUserDataFirstName);
        edtUserDataLastName = findViewById(R.id.edtUserDataLastName);
        edtUserDataPhone = findViewById(R.id.edtUserDataPhone);
        edtUserDataEmail = findViewById(R.id.edtUserDataEmail);
        edtUserDataSMA = findViewById(R.id.edtUserDataSMA);
        edtUserDataSMAPeriod = findViewById(R.id.edtUserDataSMAPeriod);
        edtUserDataUniversity = findViewById(R.id.edtUserDataUniversity);
        edtUserDataUniversityPeriod = findViewById(R.id.edtUserDataUniversityPeriod);
        edtUserDataSkills = findViewById(R.id.edtUserDataSkills);
        edtUserDataDeskripsi = findViewById(R.id.edtUserDataDeskripsi);
        ivFotoProfile = findViewById(R.id.ivProfile);

        btnAddProfile = findViewById(R.id.btnAddImageProfile);

        mStorage = FirebaseStorage.getInstance();

        btn_add_user_data = findViewById(R.id.btnAddUserData);

        btnAddProfile.setOnClickListener(v -> {
            Intent addProfile = new Intent(Intent.ACTION_GET_CONTENT);
            addProfile.setType("image/*");
            startActivityForResult(addProfile, galleryCode);
        });
    }


    private void defaultData() {

        String username = edtUserDataUsername.getText().toString();
        String firstName = edtUserDataFirstName.getText().toString();
        String lastName = edtUserDataLastName.getText().toString();
        String phone = edtUserDataPhone.getText().toString();
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String sma = edtUserDataSMA.getText().toString();
        String smaPeriod = edtUserDataSMAPeriod.getText().toString();
        String university = edtUserDataUniversity.getText().toString();
        String universityPeriod = edtUserDataUniversityPeriod.getText().toString();
        String skills = edtUserDataSkills.getText().toString();
        String selfDescription = edtUserDataDeskripsi.getText().toString();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        StorageReference filePath = mStorage.getReference().child("imageProfile").child(imageUri.getLastPathSegment());
        filePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> downloadUrl = taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        String getFotoProfile = task.getResult().toString();

                        database.child("Users").child(userId).child("UserData").setValue(new UserModel(username, firstName, lastName,
                                phone, email, sma, smaPeriod, university, universityPeriod,
                                skills, selfDescription, getFotoProfile)).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Intent next = new Intent(UserDataActivity.this, LoginPageActivity.class);
                                startActivity(next);
                            }
                        });
                    }
                });
                Toast.makeText(UserDataActivity.this, "Thank you for adding", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UserDataActivity.this, "Failed adding", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == galleryCode && resultCode == RESULT_OK) {
            imageUri = data.getData();
            ivFotoProfile.setImageURI(imageUri);
        }

        btn_add_user_data.setOnClickListener(v -> {
            defaultData();
        });
    }
}