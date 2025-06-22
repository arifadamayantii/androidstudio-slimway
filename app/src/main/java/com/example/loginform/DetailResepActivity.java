package com.example.loginform;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class DetailResepActivity extends AppCompatActivity {

    ImageView imageRecipe;
    Button backButton;
    TextView title, rating, desc, bahan, howToMake, step1;
    TextView nut1_value, nut2_value, nut3_value, nut4_value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_resep_activity);

        // Inisialisasi UI
        backButton = findViewById(R.id.buttonBack);
        imageRecipe = findViewById(R.id.imageRecipe);
        title = findViewById(R.id.title);
        rating = findViewById(R.id.rating);
        desc = findViewById(R.id.desc);
        bahan = findViewById(R.id.bahan);
        howToMake = findViewById(R.id.howToMake);
        step1 = findViewById(R.id.step1);
        nut1_value = findViewById(R.id.nut1_value);
        nut2_value = findViewById(R.id.nut2_value);
        nut3_value = findViewById(R.id.nut3_value);
        nut4_value = findViewById(R.id.nut4_value);

        // Terima data dari intent
        Intent intent = getIntent();

        String namaResep = intent.getStringExtra("namaResep");
        String kategori = intent.getStringExtra("kategori");
        String kalori = intent.getStringExtra("kalori");
        String deskripsi = intent.getStringExtra("deskripsi");
        String foto = intent.getStringExtra("photo");
        String bahanStr = intent.getStringExtra("bahan");
        String langkah = intent.getStringExtra("langkah");
        String video = intent.getStringExtra("video");
        String status = intent.getStringExtra("status");
        String protein = intent.getStringExtra("protein");
        String fat = intent.getStringExtra("fat");
        String carbs = intent.getStringExtra("carbs");
        String emailUser = intent.getStringExtra("emailUser");
        double ratingValue = intent.getDoubleExtra("rating", 0.0);

        // Set data ke UI
        title.setText(namaResep != null ? namaResep : "No Title");
        desc.setText(deskripsi != null ? deskripsi : "-");
        bahan.setText(bahanStr != null ? bahanStr : "-");
        step1.setText(langkah != null ? langkah : "-");
        rating.setText("★ " + ratingValue);
        nut1_value.setText(protein != null ? protein : "0g");
        nut2_value.setText(fat != null ? fat : "0g");
        nut3_value.setText(carbs != null ? carbs : "0g");
        nut4_value.setText(kalori != null ? kalori : "0");

        // Load gambar menggunakan Glide
        if (foto != null && !foto.isEmpty()) {
            String imageUrl;
            if (foto.contains("uploads/recipes/")) {
                imageUrl = "http://192.168.1.14/loginRegister_mysql_volley_api/" + foto;
            } else {
                imageUrl = "http://192.168.1.14/loginRegister_mysql_volley_api/uploads/recipes/" + foto;
            }

            Log.d("DetailImageURL", "Image URL: " + imageUrl); // Debug

            Glide.with(this)
                    .load(imageUrl)
                    .placeholder(R.drawable.tunainput)
                    .error(R.drawable.tunainput)
                    .into(imageRecipe);
        } else {
            imageRecipe.setImageResource(R.drawable.tunainput);
        }

        // Tombol back
        backButton.setOnClickListener(v -> onBackPressed());
    }
}
