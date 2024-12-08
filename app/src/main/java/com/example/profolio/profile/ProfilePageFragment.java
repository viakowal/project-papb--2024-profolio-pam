package com.example.profolio.profile;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.profolio.R;
import com.example.profolio.adapterfragment.AdapterKepanitiaan;
import com.example.profolio.adapterfragment.AdapterOrganisasi;
import com.example.profolio.adapterfragment.AdapterPrestasi;
import com.example.profolio.edit.EditProfileActivity;
import com.example.profolio.login.LoginPageActivity;
import com.example.profolio.modelfragment.KepanitiaanModel;
import com.example.profolio.modelfragment.OrganisasiModel;
import com.example.profolio.modelfragment.PrestasiModel;
import com.example.profolio.modelfragment.UserModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfilePageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfilePageFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters

    DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    List<UserModel> userItems;

    ExtendedFloatingActionButton btn_edit_profile;
    TextView tvProfileUsername, tvProfileFirstName, tvProfileLastName, tvProfilePhone,
    tvProfileEmail, tvProfileSMA, tvProfileSMAPeriod, tvProfileUniversity, tvProfileUniversityPeriod,
            tvProfileSkills, tvProfileDeskripsi;

    TextView jumlahOrganisasi, jumlahKepanitiaan, jumlahPrestasi, btnLogOut;
    ShapeableImageView ivFotoProfile;
    Uri imageUri = null;
    FirebaseStorage mStorage;
    private FirebaseAuth mAuth;
    private String mParam1;
    private String mParam2;

    public ProfilePageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfilePageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfilePageFragment newInstance(String param1, String param2) {
        ProfilePageFragment fragment = new ProfilePageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_page, container, false);

        userItems = new ArrayList<>();

        tvProfileUsername = view.findViewById(R.id.tvProfileUsername);
        tvProfileFirstName = view.findViewById(R.id.tvProfileFirstName);
        tvProfileLastName = view.findViewById(R.id.tvProfileLastName);
        tvProfilePhone = view.findViewById(R.id.tvProfilePhone);
        tvProfileEmail = view.findViewById(R.id.tvProfileEmail);
        tvProfileSMA = view.findViewById(R.id.tvProfileSMA);
        tvProfileSMAPeriod = view.findViewById(R.id.tvProfileSMAPeriod);
        tvProfileUniversity = view.findViewById(R.id.tvProfileUniversity);
        tvProfileUniversityPeriod = view.findViewById(R.id.tvProfileUniversityPeriod);
        tvProfileSkills = view.findViewById(R.id.tvProfileSkills);
        tvProfileDeskripsi = view.findViewById(R.id.tvProfileDeskripsi);
        ivFotoProfile = view.findViewById(R.id.ivFotoProfile);
        btnLogOut = view.findViewById(R.id.btn_logout);

        mStorage = FirebaseStorage.getInstance();

        jumlahOrganisasi = view.findViewById(R.id.jmlhOrganisasi);
        jumlahKepanitiaan = view.findViewById(R.id.jmlhKepanitiaan);
        jumlahPrestasi = view.findViewById(R.id.jmlhPrestasi);

        btn_edit_profile = view.findViewById(R.id.btn_edit_profile);

        mAuth = FirebaseAuth.getInstance();

        String userKey = FirebaseAuth.getInstance().getCurrentUser().getUid();

        database.child("Users").child(userKey).child("Organisasi").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long childOrganisasi = snapshot.getChildrenCount();
                jumlahOrganisasi.setText(String.valueOf(childOrganisasi));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        database.child("Users").child(userKey).child("Kepanitiaan").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long chidKepanitiaan = snapshot.getChildrenCount();
                jumlahKepanitiaan.setText(String.valueOf(chidKepanitiaan));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        database.child("Users").child(userKey).child("Prestasi").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long childPrestasi = snapshot.getChildrenCount();
                jumlahPrestasi.setText(String.valueOf(childPrestasi));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        showData();

        btnLogOut.setOnClickListener(v -> {
            mAuth.signOut();
            Intent logout = new Intent(getContext(), LoginPageActivity.class);
            logout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(logout);
        });

        btn_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                database.child("Users").child(userId).child("UserData").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            UserModel user = snapshot.getValue(UserModel.class);
                            if (user != null) {
                                user.setKey(snapshot.getKey());

                                Intent sendData = new Intent(getContext(), EditProfileActivity.class);

                                sendData.putExtra("username", user.getUsername());
                                sendData.putExtra("firstname", user.getFirstName());
                                sendData.putExtra("lastname", user.getLastName());
                                sendData.putExtra("phone", user.getPhone());
                                sendData.putExtra("email", user.getEmail());
                                sendData.putExtra("sma", user.getSeniorHighSchool());
                                sendData.putExtra("smaperiod", user.getSeniorHighSchoolPeriod());
                                sendData.putExtra("university", user.getUniversity());
                                sendData.putExtra("universityperiod", user.getUniversityPeriod());
                                sendData.putExtra("skills", user.getSkills());
                                sendData.putExtra("deskripsi", user.getSelfDescription());
                                sendData.putExtra("jumlahorganisasi", jumlahOrganisasi.getText().toString());
                                sendData.putExtra("jumlahkepanitiaan", jumlahKepanitiaan.getText().toString());
                                sendData.putExtra("jumlahprestasi", jumlahPrestasi.getText().toString());
//                                sendData.putExtra("fotoProfile", user.getImageProfile());

                                startActivity(sendData);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        });

        return view;
    }

    private void showData() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        database.child("Users").child(userId).child("UserData").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    UserModel user = snapshot.getValue(UserModel.class);
                    if (user != null) {
                        user.setKey(snapshot.getKey());
                        // Update your UI with the retrieved user data
                        tvProfileUsername.setText(user.getUsername());
                        tvProfileFirstName.setText(user.getFirstName());
                        tvProfileLastName.setText(user.getLastName());
                        tvProfilePhone.setText(user.getPhone());
                        tvProfileEmail.setText(user.getEmail());
                        tvProfileSMA.setText(user.getSeniorHighSchool());
                        tvProfileSMAPeriod.setText(user.getSeniorHighSchoolPeriod());
                        tvProfileUniversity.setText(user.getUniversity());
                        tvProfileUniversityPeriod.setText(user.getUniversityPeriod());
                        tvProfileSkills.setText(user.getSkills());
                        tvProfileDeskripsi.setText(user.getSelfDescription());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error
            }
        });

    }
}