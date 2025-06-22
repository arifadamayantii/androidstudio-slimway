package com.example.loginform;

import android.util.Log;
import android.widget.ImageView;
import android.view.View;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;

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

public class MyActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    List<ResepModel> listResep;
    ResepAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myactivity);

        String emailUser = getIntent().getStringExtra("email_user");
        if (emailUser == null || emailUser.isEmpty()) {
            Toast.makeText(this, "Email user kosong!", Toast.LENGTH_SHORT).show();
            return;
        }

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        listResep = new ArrayList<>();

        // Panggil data resep saja, tanpa load points
        ambilDataResep(emailUser);

        // Inisialisasi tombol back
        ImageView backIcon = findViewById(R.id.imageView5);
        backIcon.setOnClickListener(v -> {
            Intent intent = new Intent(MyActivity.this, HomeActivity.class);
            intent.putExtra("email_user", emailUser);
            startActivity(intent);
        });
        TextView tvToCatering = findViewById(R.id.tvToCatering);
        tvToCatering.setOnClickListener(v -> {
            Intent intent = new Intent(MyActivity.this, MyActivityKatering.class);
            intent.putExtra("email_user", emailUser); // teruskan email user ke activity baru
            startActivity(intent);
        });

    }

    private void ambilDataResep(String email) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DbContract.SERVER_GET_RECIPE_URL,
                response -> {
                    Log.d("MY_ACTIVITY", "Respon dari server: " + response);
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

                            ResepModel resep = new ResepModel(
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
                            listResep.add(resep);
                        }
                        adapter = new ResepAdapter(MyActivity.this, listResep, email);
                        recyclerView.setAdapter(adapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Toast.makeText(MyActivity.this, "Gagal ambil data", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email_user", email);
                return params;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);
    }


}
