package com.example.profolio.loading;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.IntentCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.airbnb.lottie.LottieAnimationView;
import com.example.profolio.R;
import com.example.profolio.homepage.HomePageActivity;

public class LoadingActivity extends AppCompatActivity {
    LottieAnimationView loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.activity_loading);

        loading = findViewById(R.id.loading);

        getWindow().setFeatureInt(Window.FEATURE_PROGRESS, Window.PROGRESS_VISIBILITY_ON);

        // Set up your LottieAnimationView
        loading.setAnimation(R.raw.loading); // Replace with your animation file
        loading.playAnimation();

        // Simulate loading completion after a delay (for demonstration purposes)
        loading.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start the next activity here
                Intent intent = new Intent(LoadingActivity.this, HomePageActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000); // Replace with your desired delay in milliseconds
    }
}