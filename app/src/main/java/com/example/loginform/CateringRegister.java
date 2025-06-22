package com.example.loginform;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CateringRegister extends AppCompatActivity {

    ImageButton buttonBack;
    MaterialButton weeklyPlanButton, monthlyPlanButton;
    Button registerButton;
    TextView coinIcon;

    RecyclerView recyclerView;
    List<KateringItem> kateringList;
    KateringViewAdapter adapter;
    String emailUser;
    int koin;

    // Variabel untuk menyimpan pilihan plan: "weekly" atau "monthly"
    String selectedPlan = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cateringregister);

        // Inisialisasi view
        weeklyPlanButton = findViewById(R.id.weeklyPlanButton);
        monthlyPlanButton = findViewById(R.id.monthlyPlanButton);
        registerButton = findViewById(R.id.registerButton);
        coinIcon = findViewById(R.id.coinIcon);
        buttonBack = findViewById(R.id.buttonBack);
        recyclerView = findViewById(R.id.recyclerViewKatering);


        // Ambil email dari Intent
        emailUser = getIntent().getStringExtra("email_user");
        koin = getIntent().getIntExtra("coin_value", 0);
        coinIcon.setText("🪙 " + koin);

        // Inisialisasi list dan adapter
        kateringList = new ArrayList<>();
        adapter = new KateringViewAdapter(this, kateringList, emailUser);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(adapter);

        // Event tombol back
        buttonBack.setOnClickListener(v -> navigateToCateringActivity());

        // Event tombol plan weekly
        weeklyPlanButton.setOnClickListener(v -> {
            selectedPlan = "weekly";
            Toast.makeText(this, "Weekly Plan dipilih", Toast.LENGTH_SHORT).show();
            updatePlanButtonUI(weeklyPlanButton, monthlyPlanButton);
        });

        // Event tombol plan monthly
        monthlyPlanButton.setOnClickListener(v -> {
            selectedPlan = "monthly";
            Toast.makeText(this, "Monthly Plan dipilih", Toast.LENGTH_SHORT).show();
            updatePlanButtonUI(monthlyPlanButton, weeklyPlanButton);
        });

        // Event tombol register
        registerButton.setOnClickListener(v -> {
            Log.d("CateringRegister", "Register button clicked");
            if (selectedPlan == null) {
                Toast.makeText(this, "Pilih plan terlebih dahulu", Toast.LENGTH_SHORT).show();
                return;
            }
            navigateToShippingActivity();
        });

        // Load data katering
        loadKateringData();
    }

    private void updatePlanButtonUI(MaterialButton selected, MaterialButton unselected) {
        // Highlight tombol yang dipilih
        selected.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
        selected.setTextColor(getResources().getColor(android.R.color.white));

        // Kembalikan tombol yang tidak dipilih ke warna default
        unselected.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        unselected.setTextColor(getResources().getColor(android.R.color.black));
    }

    private void navigateToCateringActivity() {
        Intent intent = new Intent(CateringRegister.this, CateringActivity.class);
        startActivity(intent);
        finish();
    }

    private void navigateToShippingActivity() {
        List<KateringItem> selectedItems = adapter.getSelectedItems();

        if (selectedItems.isEmpty()) {
            Toast.makeText(this, "Pilih minimal satu menu dulu", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(CateringRegister.this, ShippingActivity.class);
        intent.putExtra("email_user", emailUser);
        intent.putExtra("coin_value", koin);

        // Kirim plan yang dipilih ("weekly" atau "monthly")
        intent.putExtra("selected_plan", selectedPlan);

        // Kirim data menu yang dipilih
        intent.putExtra("selected_count", selectedItems.size());
        for (int i = 0; i < selectedItems.size(); i++) {
            KateringItem item = selectedItems.get(i);
            intent.putExtra("menu_id_" + i, item.getId());
            intent.putExtra("menu_name_" + i, item.getMenuName());
            intent.putExtra("menu_price_" + i, item.getPrice());
            intent.putExtra("menu_calories_" + i, item.getCalories());
        }

        startActivity(intent);
        finish();
    }

    private void loadKateringData() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, DbContract.SERVER_GET_KATERINGVIEW_MENU,
                response -> {
                    try {
                        JSONArray array = new JSONArray(response);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);
                            kateringList.add(new KateringItem(
                                    obj.getInt("id"),
                                    obj.getString("menu_category"),
                                    obj.getString("package_option"),
                                    obj.getString("menu_name"),
                                    obj.getString("description"),
                                    obj.getInt("calories"),
                                    obj.getDouble("price"),
                                    obj.getString("image_path"),
                                    obj.getString("store_location")
                            ));
                        }
                        adapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }, error -> error.printStackTrace());

        Volley.newRequestQueue(this).add(stringRequest);
    }
}
