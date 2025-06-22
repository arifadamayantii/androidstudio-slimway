package com.example.loginform;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class totalan_activity extends AppCompatActivity {

    TextView deliveryAddress, totalPrice, coinsRemaining;
    CheckBox checkboxUseCoins;
    LinearLayout orderSummaryDetails, priceDetails;
    Button btnProceed;
    ImageView icBack;

    // Variabel untuk simpan total harga aktual
    double basePrice = 0;
    double OriginalPrice = 0;
    int coinsLeft;
    int discountCoins = 0; // diskon dinamis dari koin
    boolean useCoin = false;
    boolean useCoins = false;
    int coin_value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.totalan_katering);

        // Inisialisasi view
        deliveryAddress = findViewById(R.id.delivery_address);
        totalPrice = findViewById(R.id.total_price);
        coinsRemaining = findViewById(R.id.coins_remaining);
        checkboxUseCoins = findViewById(R.id.checkbox_use_coins);
        orderSummaryDetails = findViewById(R.id.order_summary_details);
        btnProceed = findViewById(R.id.btn_proceed);
        icBack = findViewById(R.id.ic_back);

        // Ambil data dari Intent
        String address = getIntent().getStringExtra("address");
        String program = getIntent().getStringExtra("selected_plan");
        String deliveryTime = getIntent().getStringExtra("deliveryTime");
        String startDate = getIntent().getStringExtra("startDate");
        String name = getIntent().getStringExtra("name");
        String phoneNumber = getIntent().getStringExtra("phoneNumber");
        String city = getIntent().getStringExtra("city");
        String province = getIntent().getStringExtra("province");
        String postalCode = getIntent().getStringExtra("postalCode");
        String emailUser = getIntent().getStringExtra("email_user");
        coin_value = getIntent().getIntExtra("coin_value",0);

        int count = getIntent().getIntExtra("selected_count", 0);
        int[] menuIds = new int[count];
        String[] selectedMenus = new String[count];
        double[] menuPrices = new double[count];
        int[] menuCalories = new int[count];

        for (int i = 0; i < count; i++) {
            menuIds[i] = getIntent().getIntExtra("menu_id_" + i,0);
            selectedMenus[i] = getIntent().getStringExtra("menu_name_" + i);
            menuPrices[i] = getIntent().getDoubleExtra("menu_price_" + i, 0);
            menuCalories[i] = getIntent().getIntExtra("menu_calories_" + i, 0);
        }

        basePrice = getIntent().getDoubleExtra("total_price", 0);
        OriginalPrice = basePrice;


        // Konversi coin_value dari String ke int
        Log.d("TOTALAN", "coin_value = " + coin_value);
        coinsLeft = coin_value;
        // Set nilai diskon maksimal (Rp6.000 atau sesuai koin)

        // Tampilkan alamat pengiriman
        if (address != null) {
            deliveryAddress.setText(address);
        }

        // Tampilkan koin tersisa
        coinsRemaining.setText("(Your Remaining Coins: " + coinsLeft + ")");

        // Nonaktifkan checkbox jika tidak ada koin
        checkboxUseCoins.setEnabled(coin_value > 0);

        // Checkbox diskon koin
        checkboxUseCoins.setOnCheckedChangeListener((buttonView, isChecked) -> {
            useCoin = isChecked;
            updateTotalPrice();
        });

        // Bersihkan dan isi ulang order summary
        orderSummaryDetails.removeAllViews();
        addTextViewToLayout(orderSummaryDetails, "Program: " + (program != null ? program : "-"));
        addTextViewToLayout(orderSummaryDetails, "Selected Menu:");
        if (selectedMenus != null && selectedMenus.length > 0) {
            for (String menu : selectedMenus) {
                addTextViewToLayout(orderSummaryDetails, "• " + menu);
            }
        } else {
            addTextViewToLayout(orderSummaryDetails, "-");
        }
        addTextViewToLayout(orderSummaryDetails, "Delivery Time: " + (deliveryTime != null ? deliveryTime : "-"));
        addTextViewToLayout(orderSummaryDetails, "Start Date: " + (startDate != null ? startDate : "-"));
        addTextViewToLayout(orderSummaryDetails, "Name: " + (name != null ? name : "-"));
        addTextViewToLayout(orderSummaryDetails, "Phone: " + (phoneNumber != null ? phoneNumber : "-"));
        addTextViewToLayout(orderSummaryDetails, "City: " + (city != null ? city : "-"));
        addTextViewToLayout(orderSummaryDetails, "Province: " + (province != null ? province : "-"));
        addTextViewToLayout(orderSummaryDetails, "Postal Code: " + (postalCode != null ? postalCode : "-"));

        // Hitung total awal
        updateTotalPrice();

        // Tombol kembali
        icBack.setOnClickListener(v -> finish());

        // Tombol lanjut (sementara belum diproses)
        btnProceed.setOnClickListener(v -> {
            Intent intent = new Intent(totalan_activity.this, pembayaran_activity.class);

            // Kirim data alamat & info user
            intent.putExtra("address", deliveryAddress.getText().toString());
            intent.putExtra("selected_plan", getIntent().getStringExtra("selected_plan"));
            intent.putExtra("deliveryTime", getIntent().getStringExtra("deliveryTime"));
            intent.putExtra("startDate", getIntent().getStringExtra("startDate"));
            intent.putExtra("name", getIntent().getStringExtra("name"));
            intent.putExtra("phoneNumber", getIntent().getStringExtra("phoneNumber"));
            intent.putExtra("city", getIntent().getStringExtra("city"));
            intent.putExtra("province", getIntent().getStringExtra("province"));
            intent.putExtra("postalCode", getIntent().getStringExtra("postalCode"));
            intent.putExtra("email_user", emailUser);
            Log.d("Response SERVER", emailUser);
            // Kirim data koin
            intent.putExtra("coin_value", coin_value);
            intent.putExtra("useCoins", useCoins);
            Log.d("Response SERVER", String.valueOf(useCoins));

            // Kirim data menu
            int counts = getIntent().getIntExtra("selected_count", 0);
            intent.putExtra("selected_count", counts);

            for (int i = 0; i < counts; i++) {
                intent.putExtra("menu_id_" + i, getIntent().getIntExtra("menu_id_" + i,0));
                intent.putExtra("menu_name_" + i, getIntent().getStringExtra("menu_name_" + i));
                intent.putExtra("menu_price_" + i, getIntent().getDoubleExtra("menu_price_" + i, 0));
                intent.putExtra("menu_calories_" + i, getIntent().getIntExtra("menu_calories_" + i, 0));
            }

            // Kirim harga sebelum diskon sebagai double
            intent.putExtra("original_price", OriginalPrice);

            // Kirim harga final setelah diskon seperti sebelumnya
            intent.putExtra("final_price", basePrice);

            startActivity(intent);
        });

    }

    private void updateTotalPrice() {
        double discountedPrice = OriginalPrice;
        int usedCoins;

        if (useCoin) {
            usedCoins = coinsLeft;
            int maxDiscount = usedCoins * 2000;
            discountedPrice -= maxDiscount;

            if (discountedPrice < 0) discountedPrice = 0;
        }

        String totalFormatted = String.format("Rp %.0f", discountedPrice);
        totalPrice.setText(totalFormatted);

        // Update basePrice agar nilai terbaru dikirim ke intent
        basePrice = discountedPrice;

        // Update tampilan koin
        if (useCoin) {
            useCoins = true;
            coinsRemaining.setText("(Your Remaining Coins: 0)");
        } else {
            useCoins = false;
            coinsRemaining.setText("(Your Remaining Coins: " + coinsLeft + ")");
        }
    }


    private void addTextViewToLayout(LinearLayout layout, String text) {
        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setTextSize(14);
        layout.addView(tv);
    }
}
