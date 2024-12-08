package com.example.profolio.login;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.profolio.R;
import com.example.profolio.homepage.HomePageActivity;
import com.example.profolio.loading.LoadingActivity;
import com.example.profolio.modelfragment.UserModel;
import com.example.profolio.register.RegisterPageActivity;
import com.example.profolio.userdata.UserDataActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginPageActivity extends AppCompatActivity {
    private LinearLayout linearLayout;
    private EditText etEmail, etPassword;
    private TextView signUp;
    private Button btnLogin;
    private FirebaseAuth mAuth;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        FirebaseApp.initializeApp(this);

        signUp = findViewById(R.id.signUp);
        linearLayout = findViewById(R.id.loginPage);
        btnLogin = findViewById(R.id.btn_login);
        etEmail = findViewById(R.id.et_Lemail);
        etPassword = findViewById(R.id.et_Lpassword);

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

        signUp.setOnClickListener(v -> {
            Intent next = new Intent(this, RegisterPageActivity.class);
            startActivity(next);
        });

        btnLogin.setOnClickListener(v -> {
            signIn(etEmail.getText().toString(), etPassword.getText().toString());
//            finish();
        });
    }

    @Override
    public void onStart(){
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    public void updateUI(FirebaseUser user){
        if (user != null){
            Intent loginNext = new Intent(LoginPageActivity.this, LoadingActivity.class);
            startActivity(loginNext);

        } else {
            Toast.makeText(this, "Sign In First", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateForm(){
        boolean result = true;
        String password = etPassword.getText().toString();
        if (TextUtils.isEmpty(etEmail.getText().toString())){
            Toast.makeText(this, "Please fill the email form", Toast.LENGTH_SHORT).show();
            result = false;
        }

        if (TextUtils.isEmpty(etPassword.getText().toString())){
            Toast.makeText(this, "Please fill the password form", Toast.LENGTH_SHORT).show();
            result = false;
        }

//        if (!TextUtils.equals(password, )){
//            Toast.makeText(this, "Password wrong", Toast.LENGTH_SHORT).show();
//        }

        return result;
    }

    public void signIn(String email, String password){
        if (!validateForm()){
            return;
        }
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            Toast.makeText(LoginPageActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }
}