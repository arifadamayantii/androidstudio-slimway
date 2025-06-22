package com.example.loginform;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class Progress_activity extends AppCompatActivity {

    ImageView navHome, navRecipe, navCatering, navCalorie, navProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progres_activity); // Pastikan nama XML-nya sesuai

        // Ambil data email dari intent
        String emailUser = getIntent().getStringExtra("email_user");

        // Inisialisasi navbar
        navHome = findViewById(R.id.nav_resep_home);
        navRecipe = findViewById(R.id.nav_resep_RECIPE);
        navCatering = findViewById(R.id.nav_resep_catering);
        navCalorie = findViewById(R.id.nav_resep_calorie);
        navProgress = findViewById(R.id.nav_resep_progres);

        // Tombol Home
        navHome.setOnClickListener(view -> {
            Intent intent = new Intent(Progress_activity.this, HomeActivity.class);
            intent.putExtra("email_user", emailUser);
            startActivity(intent);
        });

        // Tombol Resep
        navRecipe.setOnClickListener(view -> {
            Intent intent = new Intent(Progress_activity.this, ResepActivity.class);
            intent.putExtra("email_user", emailUser);
            startActivity(intent);
        });

        // Tombol Catering
        navCatering.setOnClickListener(view -> {
            Intent intent = new Intent(Progress_activity.this, CateringActivity.class);
            intent.putExtra("email_user", emailUser);
            startActivity(intent);
        });

        // Tombol Kalori
        navCalorie.setOnClickListener(view -> {
            Intent intent = new Intent(Progress_activity.this, CalorieActivity.class);
            intent.putExtra("email_user", emailUser);
            startActivity(intent);
        });

    }
}
