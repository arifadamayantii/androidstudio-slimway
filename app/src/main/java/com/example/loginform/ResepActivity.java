package com.example.loginform;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResepActivity extends AppCompatActivity {

    Button btnGetReward;
    ImageView navHome, navProgress;
    ImageView navCatering, navCalorie;
    TextView coinTextView;
    List<Resep> resepList;
    ResepViewAdapter adapter;
    RecyclerView recyclerView;
    int coin_value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resep_activity);

        // Dapatkan email user dari intent
        String emailUser = getIntent().getStringExtra("email_user");
        recyclerView = findViewById(R.id.recyclerViewResep);

        // Pakai GridLayoutManager dengan 2 kolom
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        // Misal kamu sudah punya adapter yang diinisialisasi sebelumnya
        resepList = new ArrayList<>();

        btnGetReward = findViewById(R.id.btnGetReward);
        navHome = findViewById(R.id.nav_resep_home);
        navProgress = findViewById(R.id.nav_resep_progres);
        navCatering = findViewById(R.id.nav_resep_catering);
        navCalorie = findViewById(R.id.nav_resep_calorie);
        coinTextView = findViewById(R.id.coinIcon);  // Pastikan id ini ada di layout XML

        // Load points dari server saat activity dibuat
        loadPointsFromServer(emailUser);

        // Tombol Get Reward buka AddResepActivity dengan membawa emailUser
        btnGetReward.setOnClickListener(view -> {
            Intent intent = new Intent(ResepActivity.this, AddResepActivity.class);
            intent.putExtra("email_user", emailUser);
            startActivity(intent);
        });

        // Navbar home klik balik ke HomeActivity dengan emailUser
        navHome.setOnClickListener(view -> {
            Intent intent = new Intent(ResepActivity.this, HomeActivity.class);
            intent.putExtra("email_user", emailUser);
            startActivity(intent);
            finish();
        });

        navCatering.setOnClickListener(view -> {
            Intent intent = new Intent(ResepActivity.this, CateringActivity.class);
            intent.putExtra("email_user", emailUser);
            startActivity(intent);
            finish();
        });
        navProgress.setOnClickListener(view -> {
            Intent intent = new Intent(ResepActivity.this, Progress_activity.class);
            intent.putExtra("email_user", emailUser);
            startActivity(intent);
            finish();
        });
        navCalorie.setOnClickListener(view -> {
            Intent intent = new Intent(ResepActivity.this, CalorieActivity.class);
            intent.putExtra("email_user", emailUser);
            startActivity(intent);
            finish();
        });
        ambilDataResep(emailUser);
        // CardView klik buka DetailResepActivity
    }
    private void ambilDataResep(String email) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, DbContract.SERVER_GET_VIEW_RECIPE,
                response -> {
                    Log.d("RESEP_ACTIVITY", "Respon dari server: " + response);
                    try {
                        JSONArray array = new JSONArray(response);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);
                            int isBreakfast = obj.getInt("is_breakfast");
                            int isLunch = obj.getInt("is_lunch");
                            int isDinner = obj.getInt("is_dinner");

                            List<String> kategoriList = new ArrayList<>();
                            if (isBreakfast == 1) kategoriList.add("Breakfast");
                            if (isLunch == 1) kategoriList.add("Lunch");
                            if (isDinner == 1) kategoriList.add("Dinner");

                            String kategori = kategoriList.isEmpty() ? "Uncategorized" : String.join(" & ", kategoriList);

                            Resep resep = new Resep(
                                    obj.getString("id"),
                                    obj.getString("recipe_name"),
                                    kategori,
                                    obj.getString("calories"),
                                    obj.optString("description", ""),
                                    obj.optString("photo_path", ""),
                                    obj.optString("ingredients", ""),
                                    obj.optString("instructions", ""),
                                    obj.optString("video_path", ""),
                                    obj.optString("status", "draft"),
                                    obj.optString("protein", "0"),
                                    obj.optString("fat", "0"),
                                    obj.optString("carbs", "0"),
                                    isBreakfast,
                                    isLunch,
                                    isDinner
                            );
                            resepList.add(resep);
                        }
                        adapter = new ResepViewAdapter(ResepActivity.this, resepList, email);
                        recyclerView.setAdapter(adapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Toast.makeText(ResepActivity.this, "Gagal ambil data", Toast.LENGTH_SHORT).show();
                }) {
        };

        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void loadPointsFromServer(String emailUser) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DbContract.SERVER_GET_REWARD_URL,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getBoolean("success")) {
                            int points = jsonObject.getInt("points");
                            coinTextView.setText("🪙 " + points);
                            coin_value = points;
                        } else {
                            Toast.makeText(ResepActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ResepActivity.this, "Parsing error", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(ResepActivity.this, "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", emailUser); // kirim email user dari intent
                return params;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);


    }
}
