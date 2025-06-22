package com.example.loginform;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.gridlayout.widget.GridLayout;
import android.graphics.Bitmap;
import android.provider.MediaStore;

import androidx.appcompat.app.AppCompatActivity;

public class CalorieActivity extends AppCompatActivity {

    ImageView navHome, navRecipe, navCatering, navCalorie, navProgress;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageView scanButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calorieactivity); // Pastikan nama XML-nya sesuai

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
            Intent intent = new Intent(CalorieActivity.this, HomeActivity.class);
            intent.putExtra("email_user", emailUser);
            startActivity(intent);
        });

        // Tombol Resep
        navRecipe.setOnClickListener(view -> {
            Intent intent = new Intent(CalorieActivity.this, ResepActivity.class);
            intent.putExtra("email_user", emailUser);
            startActivity(intent);
        });

        // Tombol Catering
        navCatering.setOnClickListener(view -> {
            Intent intent = new Intent(CalorieActivity.this, CateringActivity.class);
            intent.putExtra("email_user", emailUser);
            startActivity(intent);
        });

        // Tombol Kalori
        navProgress.setOnClickListener(view -> {
            Intent intent = new Intent(CalorieActivity.this, Progress_activity.class);
            intent.putExtra("email_user", emailUser);
            startActivity(intent);
        });

        scanButton = findViewById(R.id.imageView29);

        // Ketika imageView scanbutton diklik, buka kamera
        scanButton.setOnClickListener(v -> {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            // Kirim bitmap ke activity baru
            Intent intent = new Intent(this, FotoHasilCalorie.class);
            // Kirim bitmap lewat singleton atau ubah ke byte array
            startActivity(intent);
        }
    }

}
