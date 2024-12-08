package com.example.profolio.register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.profolio.R;
import com.example.profolio.login.LoginPageActivity;
import com.example.profolio.modelfragment.UserModel;
import com.example.profolio.userdata.UserDataActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

public class RegisterPageActivity extends AppCompatActivity {
    private LinearLayout linearLayout;
    private TextView signIn;
    private EditText etEmail, etPassword, etConfPassword;
    private Button btnRegister;
    private FirebaseAuth mAuth;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        signIn = findViewById(R.id.signIn);
        linearLayout = findViewById(R.id.registerPage);
        btnRegister = findViewById(R.id.btn_register);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        etConfPassword = findViewById(R.id.et_confirmPassword);

        mAuth = FirebaseAuth.getInstance();
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
        signIn.setOnClickListener(v -> {
            Intent next = new Intent(this, LoginPageActivity.class);
            startActivity(next);
        });

        btnRegister.setOnClickListener(v -> {
            signUp(etEmail.getText().toString(), etPassword.getText().toString());
        });
    }

//    @Override
//    public void onStart(){
//        super.onStart();
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        updateUI(currentUser);
//    }

    public void updateUI(FirebaseUser user){
        if (user != null){
//            Intent loginNext = new Intent(RegisterPageActivity.this, LoginPageActivity.class);
            Intent loginNext = new Intent(RegisterPageActivity.this, UserDataActivity.class);
            startActivity(loginNext);
        } else {
            Toast.makeText(RegisterPageActivity.this, "Sign Up First", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateForm(){
        boolean result = true;
        if (TextUtils.isEmpty(etEmail.getText().toString())){
            Toast.makeText(this, "Please fill the email form", Toast.LENGTH_SHORT).show();
            result = false;
        }

        if (TextUtils.isEmpty(etPassword.getText().toString())){
            Toast.makeText(this, "Please fill the password form", Toast.LENGTH_SHORT).show();
            result = false;
        }

        if (TextUtils.isEmpty(etConfPassword.getText().toString())){
            Toast.makeText(this, "Please fill the password confirmation form", Toast.LENGTH_SHORT).show();
            result = false;
        } else if (!TextUtils.isEmpty(etConfPassword.getText().toString()) && !etConfPassword.getText().toString().equals(etPassword.getText().toString())){
            Toast.makeText(this, "Your password is not same", Toast.LENGTH_SHORT).show();
            result = false;
        }

        return result;
    }

    public void signUp(String email, String password){
        if (!validateForm()){
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            Toast.makeText(RegisterPageActivity.this, "Password should be at least 6 characters", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }
}