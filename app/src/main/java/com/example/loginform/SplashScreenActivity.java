package com.example.loginform;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class SplashScreenActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen); // Pastikan ada file activity_main.xml

        // Splash screen selama 3 detik, lalu pindah ke halaman login
        new Handler(getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(SplashScreenActivity.this, WelcomeActivity.class);
            startActivity(intent);
            finish();
        }, 3000);

    }
}
