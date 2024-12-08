package com.example.profolio.onboarding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import com.example.profolio.R;

public class OnBoardingDuaActivity extends AppCompatActivity {
    private LinearLayout linearLayout;
    private AppCompatButton btn_next2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding_dua);
        linearLayout = findViewById(R.id.onBoardingDuaCard);
        btn_next2 = findViewById(R.id.btn_next2);
        linearLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                linearLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                float startPosition = linearLayout.getY() + linearLayout.getHeight();
                float endPosition = linearLayout.getY();
                ValueAnimator animator = ValueAnimator.ofFloat(startPosition, endPosition);
                animator.setDuration(750);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float animatedValue = (float) animation.getAnimatedValue();
                        linearLayout.setY(animatedValue);
                    }
                });
                animator.start();
            }
        });

        btn_next2.setOnClickListener(v -> {
            Intent next = new Intent(this, OnBoardingTigaActivity.class);
            startActivity(next);
        });
    }
}