package com.example.loginform;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class StrukActivity extends AppCompatActivity {

    private TextView tvNama, tvAlamat, tvTelepon, tvProgram, tvDelivery, tvStartDate,
            tvMetodePembayaran, tvBank, tvCardNumber, tvTotalHarga, tvKoin, tvHargaAkhir, tvDate;
    private LinearLayout layoutMenu;
    private ProgressDialog progressDialog;
    private Button btnKembali;
    private LinearLayout layoutBank, layoutCardNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.struk_activity);

        // Inisialisasi view
        tvNama = findViewById(R.id.tvNama);
        tvAlamat = findViewById(R.id.tvAlamat);
        tvTelepon = findViewById(R.id.tvTelepon);
        tvProgram = findViewById(R.id.tvProgram);
        tvDelivery = findViewById(R.id.tvDelivery);
        tvStartDate = findViewById(R.id.tvStartDate);
        tvMetodePembayaran = findViewById(R.id.tvMetodePembayaran);
        tvBank = findViewById(R.id.tvBank);
        tvCardNumber = findViewById(R.id.tvCardNumber);
        tvTotalHarga = findViewById(R.id.tvTotalHarga);
        tvKoin = findViewById(R.id.tvKoin);
        tvHargaAkhir = findViewById(R.id.tvHargaAkhir);
        layoutMenu = findViewById(R.id.layoutMenu);
        tvDate = findViewById(R.id.tvDate);
        btnKembali = findViewById(R.id.btnKembali);
        layoutBank = findViewById(R.id.layoutBank);
        layoutCardNumber = findViewById(R.id.layoutCardNumber);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Menyimpan pesanan...");

        // Tanggal hari ini
        String emailUser = getIntent().getStringExtra("email_user");
        String dayName = new SimpleDateFormat("EEEE", new Locale("id", "ID")).format(new Date());
        String currentDate = new SimpleDateFormat("dd MMM", Locale.getDefault()).format(new Date());
        tvDate.setText(dayName + ", " + currentDate);

        // Ambil data dari intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            tvNama.setText(extras.getString("name", ""));
            tvAlamat.setText(extras.getString("address", "") + ", " + extras.getString("city", "") + ", " +
                    extras.getString("province", "") + " " + extras.getString("postalCode", ""));
            tvTelepon.setText(extras.getString("phoneNumber", ""));
            tvProgram.setText(extras.getString("selected_plan", ""));
            tvDelivery.setText(extras.getString("deliveryTime", ""));
            tvStartDate.setText(extras.getString("startDate", ""));
            tvMetodePembayaran.setText(extras.getString("payment_method", ""));
            String bank = extras.getString("selected_bank", "-");
            String cardNumber = extras.getString("card_number", "-");

            if (bank.equals("-") || bank.trim().isEmpty()) {
                layoutBank.setVisibility(LinearLayout.GONE);
            } else {
                tvBank.setText(bank);
            }

            if (cardNumber.equals("-") || cardNumber.trim().isEmpty()) {
                layoutCardNumber.setVisibility(LinearLayout.GONE);
            } else {
                tvCardNumber.setText(cardNumber);
            }
            int coinValue = extras.getInt("coin_value", 0);
            double originalPrice = extras.getDouble("original_price", 0);
            double finalPrice = extras.getDouble("final_price", 0);
            Boolean useCoins = extras.getBoolean("useCoins", false);
            Log.d("Response SERVER", String.valueOf(useCoins));

            tvKoin.setText(coinValue + " koin");
            tvTotalHarga.setText("Rp " + String.format("%,.0f", originalPrice));
            tvHargaAkhir.setText("Rp " + String.format("%,.0f", finalPrice));

            ArrayList<HashMap<String, Object>> menuItems = new ArrayList<>();

            int count = extras.getInt("selected_count", 0);
            for (int i = 0; i < count; i++) {
                int menuId = extras.getInt("menu_id_" + i);
                String menuName = extras.getString("menu_name_" + i);
                double price = extras.getDouble("menu_price_" + i, 0);
                int calories = extras.getInt("menu_calories_" + i, 0);

                TextView tvMenu = new TextView(this);
                tvMenu.setText("- " + menuName + " | Rp " + String.format("%,.0f", price) + " | " + calories + " Kalori");
                layoutMenu.addView(tvMenu);

                HashMap<String, Object> item = new HashMap<>();
                item.put("menu_id", menuId);
                Log.d("CEK_MENU_ID", "menu_id_" + i + ": " + menuId);
                item.put("menu_name", menuName);
                item.put("price", price);
                item.put("calories", calories);
                menuItems.add(item);
            }

            // Panggil fungsi SimpanOrder
            SimpanOrder(
                    emailUser,
                    extras.getString("name", ""),
                    extras.getString("address", ""),
                    extras.getString("city", ""),
                    extras.getString("province", ""),
                    extras.getString("postalCode", ""),
                    extras.getString("phoneNumber", ""),
                    extras.getString("selected_plan", ""),
                    extras.getString("deliveryTime", ""),
                    extras.getString("startDate", ""),
                    extras.getString("payment_method", ""),
                    bank,
                    cardNumber,
                    String.valueOf(useCoins),
                    coinValue,
                    originalPrice,
                    finalPrice,
                    menuItems
            );

            btnKembali.setOnClickListener(view -> {
                Intent intent = new Intent(StrukActivity.this, CateringActivity.class);
                intent.putExtra("email_user", emailUser);
                Log.d("Response SERVER", emailUser);
                startActivity(intent);
            });
        }
    }

    public void SimpanOrder(
            final String emailUser, final String name, final String address, final String city, final String province,
            final String postalCode, final String phoneNumber, final String selectedPlan,
            final String deliveryTime, final String startDate, final String paymentMethod,
            final String selectedBank, final String cardNumber, final String useCoins,
            final int coinValue, final double originalPrice, final double finalPrice,
            final ArrayList<HashMap<String, Object>> menuItems
    ) {
        if (checkNetworkConnection()) {
            progressDialog.show();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, DbContract.SERVER_ORDER_URL,
                    response -> {
                        Log.d("Response SERVER", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            if (status.equals("success")) {
                                Toast.makeText(getApplicationContext(), "Pemesanan berhasil disimpan", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Gagal menyimpan pesanan", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    },
                    error -> {
                        Toast.makeText(getApplicationContext(), "Terjadi kesalahan koneksi", Toast.LENGTH_SHORT).show();
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("emailUser", emailUser);
                    params.put("name", name);
                    params.put("address", address);
                    params.put("city", city);
                    params.put("province", province);
                    params.put("postalCode", postalCode);
                    params.put("phoneNumber", phoneNumber);
                    params.put("selected_plan", selectedPlan);
                    params.put("deliveryTime", deliveryTime);
                    params.put("startDate", startDate);
                    params.put("payment_method", paymentMethod);
                    params.put("selected_bank", selectedBank);
                    params.put("card_number", cardNumber);
                    params.put("coin_value", String.valueOf(coinValue));
                    params.put("original_price", String.valueOf(originalPrice));
                    params.put("final_price", String.valueOf(finalPrice));
                    params.put("useCoins", useCoins); // ✅ Ditambahkan
                    Log.d("Response SERVER", useCoins);


                    for (int i = 0; i < menuItems.size(); i++) {
                        HashMap<String, Object> item = menuItems.get(i);
                        params.put("menu_id_" + i, String.valueOf(item.get("menu_id")));
                        params.put("menu_name_" + i, item.get("menu_name").toString());
                        params.put("menu_price_" + i, String.valueOf(item.get("price")));
                        params.put("menu_calories_" + i, String.valueOf(item.get("calories")));
                    }
                    params.put("selected_count", String.valueOf(menuItems.size()));
                    return params;
                }
            };

            volleyConnection.getInstance(this).addtoRequestQueue(stringRequest);
            new Handler().postDelayed(() -> progressDialog.dismiss(), 2000);
        } else {
            Toast.makeText(getApplicationContext(), "Tidak ada koneksi internet", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean checkNetworkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}
