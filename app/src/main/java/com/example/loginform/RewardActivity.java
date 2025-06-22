package com.example.loginform;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class RewardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reward_activity);
        String emailUser = getIntent().getStringExtra("email_user");
        // Inisialisasi komponen UI
        ImageView backButton = findViewById(R.id.backButton);
        Button seePointsButton = findViewById(R.id.seePointsButton);

        // Aksi saat tombol back ditekan
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RewardActivity.this, ResepActivity.class);
                intent.putExtra("email_user", emailUser);
                startActivity(intent);
                finish();
            }
        });

        // Aksi saat tombol "See my points" ditekan
        seePointsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mengarahkan ke ResepActivity
                Intent intent = new Intent(RewardActivity.this, ResepActivity.class);
                intent.putExtra("email_user", emailUser);
                startActivity(intent);
                finish(); // Jika ingin menutup RewardActivity setelah berpindah
            }
        });
    }
}

