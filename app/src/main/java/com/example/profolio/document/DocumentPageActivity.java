package com.example.profolio.document;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.profolio.R;
import com.example.profolio.printlayout.PrintViewActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class DocumentPageActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1232;
    AppCompatButton btn_generate;
    LinearLayout card, header_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_document_page); // Pastikan layout ini sesuai dengan kebutuhan

        btn_generate = findViewById(R.id.btn_generate);
        card = findViewById(R.id.cardCreateCV);
        header_title = findViewById(R.id.header_title);

        askPermissions();

        btn_generate.setOnClickListener(v -> {
            Intent next = new Intent(DocumentPageActivity.this, PrintViewActivity.class);
            startActivity(next);
        });
    }

    private void createPDF() {
        View view = getLayoutInflater().inflate(R.layout.activity_print_view, null);
        DisplayMetrics displayMetrics = new DisplayMetrics();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            getDisplay().getRealMetrics(displayMetrics);
        } else {
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        }

        view.measure(View.MeasureSpec.makeMeasureSpec(displayMetrics.widthPixels, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(displayMetrics.heightPixels, View.MeasureSpec.EXACTLY));

        int viewWidth = view.getMeasuredWidth();
        int viewHeight = view.getMeasuredHeight();

        Bitmap bitmap = Bitmap.createBitmap(viewWidth, viewHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        view.layout(0, 0, viewWidth, viewHeight);
        view.draw(canvas);

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
            Toast.makeText(DocumentPageActivity.this, "Export Success", Toast.LENGTH_SHORT).show();
            openPDFFile(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void openPDFFile(File file) {
        Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".provider", file);

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
