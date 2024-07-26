package com.example.myapplication;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.airbnb.lottie.LottieAnimationView;

public class LottieSplashActivity extends AppCompatActivity {
    private LottieAnimationView lottie_LOTTIE_animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lottie_splash);
        findViews();
        startAnimation(lottie_LOTTIE_animation);
    }

    private void startAnimation(LottieAnimationView view) {
        view.resumeAnimation();
        view.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(@NonNull Animator animation) {
                // pass
            }

            @Override
            public void onAnimationEnd(@NonNull Animator animation) {
                transactToMainActivity();
            }

            @Override
            public void onAnimationCancel(@NonNull Animator animation) {
                // pass
            }

            @Override
            public void onAnimationRepeat(@NonNull Animator animation) {
                // pass
            }
        });
    }

    private void findViews() {
        lottie_LOTTIE_animation = findViewById(R.id.lottie_LOTTIE_animation);
    }

    private void transactToMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

}