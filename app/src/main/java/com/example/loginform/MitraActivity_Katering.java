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

public class MitraActivity_Katering extends AppCompatActivity {

    RecyclerView recyclerView;
    List<PesananKatering_Model> listPesanan;
    Mitra_adapterOrder adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mitra_activity_order);

        String emailUser = getIntent().getStringExtra("email_user");
        if (emailUser == null || emailUser.isEmpty()) {
            Toast.makeText(this, "Email user kosong!", Toast.LENGTH_SHORT).show();
            return;
        }

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        listPesanan = new ArrayList<>();

        ambilDataPesanan(emailUser);

        TextView tvMyMenu = findViewById(R.id.tvMyMenu);
        tvMyMenu.setOnClickListener(v -> {
            Intent intent = new Intent(MitraActivity_Katering.this, MitraActivity.class);
            intent.putExtra("email_user", emailUser);
            startActivity(intent);
        });

        ImageView backIcon = findViewById(R.id.imageView5);
        backIcon.setOnClickListener(v -> {
            Intent intent = new Intent(MitraActivity_Katering.this, MitraHomeActivity.class);
            intent.putExtra("email_user", emailUser);
            startActivity(intent);
        });
    }

    private void ambilDataPesanan(String emailUser) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DbContract.SERVER_GET_PESANAN_KATERING_URL,
                response -> {
                    Log.e("RESPONSE_DEBUG", response);

                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        if (jsonResponse.getString("status").equals("success")) {
                            JSONArray dataArray = jsonResponse.getJSONArray("data");

                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject obj = dataArray.getJSONObject(i);
                                int orderId = obj.getInt("order_id");

                                // Ambil array menus
                                JSONArray menuArray = obj.getJSONArray("menus");
                                List<MenuModel> menuList = new ArrayList<>();

                                for (int j = 0; j < menuArray.length(); j++) {
                                    JSONObject menuObj = menuArray.getJSONObject(j);
                                    MenuModel menu = new MenuModel(
                                            menuObj.getString("menu_name"),
                                            menuObj.getDouble("price"),
                                            menuObj.getInt("calories")
                                    );
                                    menuList.add(menu);
                                }

                                // Buat objek pesanan
                                PesananKatering_Model pesanan = new PesananKatering_Model(
                                        orderId,
                                        obj.getString("name"),
                                        obj.getString("address"),
                                        obj.getString("city"),
                                        obj.getString("province"),
                                        obj.getString("postalCode"),
                                        "", // startDate masih kosong (belum ditambahkan di API)
                                        menuList
                                );

                                listPesanan.add(pesanan);
                            }

                            adapter = new Mitra_adapterOrder(this, listPesanan);
                            recyclerView.setAdapter(adapter);
                        } else {
                            Toast.makeText(this, "Data tidak ditemukan", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Gagal parsing data", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("emailUser", emailUser);
                return params;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);
    }
}
