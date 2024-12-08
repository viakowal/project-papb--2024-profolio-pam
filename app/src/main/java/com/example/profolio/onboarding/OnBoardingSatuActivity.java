package com.example.profolio.onboarding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.profolio.R;

public class OnBoardingSatuActivity extends AppCompatActivity {
    private LinearLayout linearLayout;
    private AppCompatButton btn_next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding_satu);
        linearLayout = findViewById(R.id.onBoardingSatuCard);
        btn_next = findViewById(R.id.btn_next);
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

        btn_next.setOnClickListener(v -> {
            Intent next = new Intent(this, OnBoardingDuaActivity.class);
            startActivity(next);
        });
    }
}