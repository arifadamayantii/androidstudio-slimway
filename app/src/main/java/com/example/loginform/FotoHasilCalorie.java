package com.example.loginform;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class FotoHasilCalorie extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calorie_fotohasil);

        ImageView backButton = findViewById(R.id.imageView36);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Kembali ke CalorieActivity
                Intent intent = new Intent(FotoHasilCalorie.this, CalorieActivity.class);
                startActivity(intent);
                finish(); // Optional: Tutup halaman saat ini
            }
        });
    }
}
