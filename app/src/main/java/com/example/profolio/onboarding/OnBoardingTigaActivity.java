package com.example.profolio.onboarding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import com.example.profolio.R;
import com.example.profolio.login.LoginPageActivity;

public class OnBoardingTigaActivity extends AppCompatActivity {
    private LinearLayout cardView3;
    private AppCompatButton btn_next3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding_tiga);
        cardView3 = findViewById(R.id.cardViewTiga);
        btn_next3 = findViewById(R.id.btn_next3);
        cardView3.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                cardView3.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                float startPosition = cardView3.getY() + cardView3.getHeight();
                float endPosition = cardView3.getY();
                ValueAnimator animator = ValueAnimator.ofFloat(startPosition, endPosition);
                animator.setDuration(750);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float animatedValue = (float) animation.getAnimatedValue();
                        cardView3.setY(animatedValue);
                    }
                });
                animator.start();
            }
        });

        btn_next3.setOnClickListener(v -> {
            Intent next = new Intent(this, LoginPageActivity.class);
            startActivity(next);
        });
    }
}