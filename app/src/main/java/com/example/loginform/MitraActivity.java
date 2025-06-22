package com.example.loginform;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
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

public class MitraActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<KateringModel> listKatering;
    Mitra_Adapter_Katering adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mitraactivity);
        String emailUser = getIntent().getStringExtra("email_user");
        if (emailUser == null || emailUser.isEmpty()) {
            Toast.makeText(this, "Email user kosong!", Toast.LENGTH_SHORT).show();
            return;
        }

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        listKatering = new ArrayList<>();

        ambilDataKatering(emailUser);
        TextView tvToOrder = findViewById(R.id.tvToOrder);
        tvToOrder.setOnClickListener(v -> {
            Intent intent = new Intent(MitraActivity.this, MitraActivity_Katering.class);
            intent.putExtra("email_user", emailUser); // teruskan email user ke activity baru
            startActivity(intent);
        });

        // Inisialisasi tombol back
        ImageView backIcon = findViewById(R.id.imageView5);

        // Menangani klik pada tombol back
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Membuka HomeActivity setelah klik tombol back
                Intent intent = new Intent(MitraActivity.this, MitraHomeActivity.class);
                intent.putExtra("email_user", emailUser); // Sesuaikan nama kelas dengan yang ada
                startActivity(intent);
            }
        });
    }

    private void ambilDataKatering(String email) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DbContract.SERVER_GET_KATERING_URL,
                response -> {
                    Log.d("KATERING_ACTIVITY", "Respon dari server: " + response);
                    try {
                        JSONArray array = new JSONArray(response);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);

                            KateringModel katering = new KateringModel(
                                    obj.getInt("id"),
                                    obj.getString("email_user"),
                                    obj.getString("menu_category"),
                                    obj.getString("package_option"),
                                    obj.getString("menu_name"),
                                    obj.optString("description", ""),
                                    obj.optInt("calories", 0),
                                    obj.optDouble("price", 0.0),
                                    obj.optString("image_path", ""),
                                    obj.optString("store_location", "")
                            );
                            listKatering.add(katering);
                        }

                        adapter = new Mitra_Adapter_Katering(MitraActivity.this, listKatering, email);
                        recyclerView.setAdapter(adapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(MitraActivity.this, "Gagal parsing data", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(MitraActivity.this, "Gagal ambil data", Toast.LENGTH_SHORT).show()
        ) {
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


