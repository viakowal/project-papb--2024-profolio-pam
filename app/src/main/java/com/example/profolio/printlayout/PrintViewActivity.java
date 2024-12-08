package com.example.profolio.printlayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.profolio.R;
import com.example.profolio.adapterfragment.AdapterCVKepanitiaan;
import com.example.profolio.adapterfragment.AdapterCVOrganisasi;
import com.example.profolio.adapterfragment.AdapterCVPrestasi;
import com.example.profolio.modelfragment.KepanitiaanModel;
import com.example.profolio.modelfragment.OrganisasiModel;
import com.example.profolio.modelfragment.PrestasiModel;
import com.example.profolio.modelfragment.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PrintViewActivity extends AppCompatActivity {

    TextView firstName, lastName, position, phoneNumber, email, sma, smaPeriod, university, universityPeriod, skills, selfDeskripsi, introName;
    ImageView imgCV;
    AppCompatButton print;
    private static final int REQUEST_CODE = 1232;
    RecyclerView cvRecyclerViewOrganisasi;
    RecyclerView cvRecyclerViewKepanitiaan;
    RecyclerView cvRecyclerViewPrestasi;
    AdapterCVOrganisasi adapterCVOrganisasi;
    AdapterCVKepanitiaan adapterCVKepanitiaan;
    AdapterCVPrestasi adapterCVPrestasi;
    List<OrganisasiModel> organisasiItems;
    List<KepanitiaanModel> kepanitiaanItems;
    List<PrestasiModel> prestasiItems;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_view);

        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        position = findViewById(R.id.positionCV);
        phoneNumber = findViewById(R.id.phoneNumber);
        email = findViewById(R.id.email);
        sma = findViewById(R.id.sma);
        smaPeriod = findViewById(R.id.smaPeriod);
        university = findViewById(R.id.university);
        universityPeriod = findViewById(R.id.universityPeriod);
        skills = findViewById(R.id.skills);
        selfDeskripsi = findViewById(R.id.selfDeskripsi);
        introName = findViewById(R.id.introName);
        imgCV = findViewById(R.id.imgCV);
        print = findViewById(R.id.btn_print);

        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(1000);

        print.setAnimation(animation);

        print.setOnClickListener(v -> {
            createPDF();
        });

        cvRecyclerViewOrganisasi = findViewById(R.id.rvCVOrganisasi);
        RecyclerView.LayoutManager mLayoutOrganisasi = new LinearLayoutManager(PrintViewActivity.this);
        cvRecyclerViewOrganisasi.setLayoutManager(mLayoutOrganisasi);
        cvRecyclerViewOrganisasi.setHasFixedSize(true);

        cvRecyclerViewKepanitiaan = findViewById(R.id.rvCVKepanitiaan);
        RecyclerView.LayoutManager mLayoutKepanitiaan = new LinearLayoutManager(PrintViewActivity.this);
        cvRecyclerViewKepanitiaan.setLayoutManager(mLayoutKepanitiaan);
        cvRecyclerViewKepanitiaan.setHasFixedSize(true);


        cvRecyclerViewPrestasi = findViewById(R.id.rvCVPrestasi);
        RecyclerView.LayoutManager mLayoutPrestasi = new LinearLayoutManager(PrintViewActivity.this);
        cvRecyclerViewPrestasi.setLayoutManager(mLayoutPrestasi);
        cvRecyclerViewPrestasi.setHasFixedSize(true);

        showDataOrganisasi();
        showDataKepanitiaan();
        showDataPrestasi();

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        database.child("Users").child(userId).child("UserData").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    UserModel user = snapshot.getValue(UserModel.class);
                    user.setKey(snapshot.getKey());
                    firstName.setText(user.getFirstName());
                    lastName.setText(user.getLastName());
                    phoneNumber.setText(user.getPhone());
                    email.setText(user.getEmail());
                    sma.setText(user.getSeniorHighSchool());
                    smaPeriod.setText(user.getSeniorHighSchoolPeriod());
                    university.setText(user.getUniversity());
                    universityPeriod.setText(user.getUniversityPeriod());
                    skills.setText(user.getSkills());
                    selfDeskripsi.setText(user.getSelfDescription());
                    introName.setText("Hello, " + user.getUsername());
                    Picasso.get().load(user.getImageProfile()).into(imgCV);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showDataOrganisasi() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        database.child("Users").child(userId).child("Organisasi").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                organisasiItems = new ArrayList<>();
                for (DataSnapshot item : snapshot.getChildren()) {
                    OrganisasiModel organisasi = item.getValue(OrganisasiModel.class);
                    organisasi.setKey(item.getKey());
                    organisasiItems.add(organisasi);
                }
                Collections.reverse(organisasiItems);
                adapterCVOrganisasi = new AdapterCVOrganisasi(organisasiItems, PrintViewActivity.this);
                cvRecyclerViewOrganisasi.setAdapter(adapterCVOrganisasi);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showDataKepanitiaan() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        database.child("Users").child(userId).child("Kepanitiaan").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                kepanitiaanItems = new ArrayList<>();
                for (DataSnapshot item : snapshot.getChildren()) {
                    KepanitiaanModel kepanitiaan = item.getValue(KepanitiaanModel.class);
                    kepanitiaan.setKey(item.getKey());
                    kepanitiaanItems.add(kepanitiaan);
                }
                Collections.reverse(kepanitiaanItems);
                adapterCVKepanitiaan = new AdapterCVKepanitiaan(kepanitiaanItems, PrintViewActivity.this);
                cvRecyclerViewKepanitiaan.setAdapter(adapterCVKepanitiaan);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void showDataPrestasi() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        database.child("Users").child(userId).child("Prestasi").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                prestasiItems = new ArrayList<>();
                for (DataSnapshot item : snapshot.getChildren()) {
                    PrestasiModel prestasi = item.getValue(PrestasiModel.class);
                    prestasi.setKey(item.getKey());
                    prestasiItems.add(prestasi);
                }
                Collections.reverse(prestasiItems);
                adapterCVPrestasi = new AdapterCVPrestasi(prestasiItems, PrintViewActivity.this);
                cvRecyclerViewPrestasi.setAdapter(adapterCVPrestasi);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void createPDF() {
        print.setVisibility(View.INVISIBLE);

        View view = getWindow().getDecorView().getRootView();
        DisplayMetrics displayMetrics = new DisplayMetrics();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            this.getDisplay().getRealMetrics(displayMetrics);
        } else {
            this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        }

        view.measure(View.MeasureSpec.makeMeasureSpec(displayMetrics.widthPixels, View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(displayMetrics.heightPixels, View.MeasureSpec.EXACTLY));

        int viewWidth = view.getMeasuredWidth();
        int viewHeight = view.getMeasuredHeight();

        Bitmap bitmap = Bitmap.createBitmap(viewWidth, viewHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        view.layout(0, 0, viewWidth, viewHeight);
        view.draw(canvas);

        print.setVisibility(View.VISIBLE);

        PdfDocument pdfDocument = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(viewWidth, viewHeight, 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);

        Canvas pageCanvas = page.getCanvas();
        pageCanvas.drawBitmap(bitmap, 0, 0, null);

        pdfDocument.finishPage(page);

        String baseFilename = "CV";
        String filename = generateUniqueFilename(baseFilename, "pdf");
        File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File file = new File(downloadsDir, filename);

        try {
            FileOutputStream fos = new FileOutputStream(file);
            pdfDocument.writeTo(fos);
            pdfDocument.close();
            fos.close();
            Toast.makeText(this, "Export Success", Toast.LENGTH_SHORT).show();
            openPDFFile(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void openPDFFile(File file) {
        Uri uri = FileProvider.getUriForFile(PrintViewActivity.this, getApplicationContext().getPackageName() + ".provider", file);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/pdf");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "No PDF viewer app found", Toast.LENGTH_SHORT).show();
        }
    }

    private String generateUniqueFilename(String baseFilename, String extension) {
        File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        String filename = baseFilename + "." + extension;
        int counter = 1;

        while (new File(downloadsDir, filename).exists()) {
            filename = baseFilename + "(" + counter + ")." + extension;
            counter++;
        }

        return filename;
    }

    private void askPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
    }
}