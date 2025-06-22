package com.example.loginform;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class MyActivityKatering extends AppCompatActivity {

    RecyclerView recyclerView;
    List<KateringActivityModel> listKatering;
    KateringAdapter adapter;  // Asumsikan kamu buat adapter ini mirip ResepAdapter

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myactivitykatering);

        String emailUser = getIntent().getStringExtra("email_user");
        if (emailUser == null || emailUser.isEmpty()) {
            Toast.makeText(this, "Email user kosong!", Toast.LENGTH_SHORT).show();
            return;
        }

        recyclerView = findViewById(R.id.recyclerView);  // pastikan di my_activity_katering.xml juga ada RecyclerView dengan id yang sama
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        listKatering = new ArrayList<KateringActivityModel>();
        ambilDataKatering(emailUser);
        TextView tvToCatering = findViewById(R.id.tvResep);
        tvToCatering.setOnClickListener(v -> {
            Intent intent = new Intent(MyActivityKatering.this, MyActivity.class);
            intent.putExtra("email_user", emailUser); // teruskan email user ke activity baru
            startActivity(intent);
        });
        // Tombol back (gunakan id yang sama atau sesuaikan di layout)
        ImageView backIcon = findViewById(R.id.imageView5);
        if (backIcon != null) {
            backIcon.setOnClickListener(v -> {
                Intent intent = new Intent(MyActivityKatering.this, HomeActivity.class);
                intent.putExtra("email_user", emailUser);
                startActivity(intent);
            });
        }
    }

    private void ambilDataKatering(String email) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DbContract.SERVER_GET_ORDER_URL,
                response -> {
                    Log.d("MY_ACTIVITY_KATERING", "Respon dari server: " + response);
                    try {
                        JSONArray array = new JSONArray(response);
                        listKatering.clear();

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);

                            String orderId = obj.getString("order_id");
                            String finalPrice = obj.getString("final_price");

                            JSONArray menusJson = obj.getJSONArray("menus");
                            List<String> menus = new ArrayList<>();
                            for (int j = 0; j < menusJson.length(); j++) {
                                menus.add(menusJson.getString(j));
                            }

                            KateringActivityModel katering = new KateringActivityModel( orderId, menus, finalPrice);
                            listKatering.add(katering);
                        }

                        adapter = new KateringAdapter(MyActivityKatering.this, listKatering, email);
                        recyclerView.setAdapter(adapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(MyActivityKatering.this, "Gagal parsing data katering", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Toast.makeText(MyActivityKatering.this, "Gagal ambil data katering", Toast.LENGTH_SHORT).show();
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
