package com.example.loginform;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class pembayaran_activity extends AppCompatActivity {

    private CardView cardList, cardCash;
    private LinearLayout layoutCreditTitle, card1, card2;
    private ImageView ivToggle;
    private Button btnPaymentNow;
    private EditText etCardNumber;

    // Data intent lama
    private String address, program, deliveryTime, startDate, name, phoneNumber, city, province, postalCode, emailUser;
    private boolean useCoins;
    private int coinValue;
    private double originalPrice, finalPrice;

    private int[] menuIds;
    private String[] menuNames;
    private double[] menuPrices;
    private int[] menuCalories;

    // Pilihan metode pembayaran dan bank
    private String paymentMethod = ""; // "COD" atau "CreditCard"
    private String selectedBank = "";  // "BCA", "HDFC", dll

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pembayaran);

        // Inisialisasi view
        cardList = findViewById(R.id.cardList);
        cardCash = findViewById(R.id.cardCash);
        layoutCreditTitle = findViewById(R.id.layoutCreditTitle);
        ivToggle = findViewById(R.id.ivToggle);
        btnPaymentNow = findViewById(R.id.btnPaymentNow);
        etCardNumber = findViewById(R.id.etCardNumber);

        card1 = findViewById(R.id.card1);
        card2 = findViewById(R.id.card2);

        // Toggle visibilitas Card List
        layoutCreditTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cardList.getVisibility() == View.VISIBLE) {
                    cardList.setVisibility(View.GONE);
                    ivToggle.setImageResource(R.drawable.ic_expand_more);
                } else {
                    cardList.setVisibility(View.VISIBLE);
                    ivToggle.setImageResource(R.drawable.ic_expand_less);
                }
            }
        });

        // Ambil data dari intent
        getDataFromIntent();

        // Pilih COD
        cardCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                paymentMethod = "COD";
                selectedBank = "";

                etCardNumber.setVisibility(View.GONE);
                cardCash.setBackgroundColor(getResources().getColor(R.color.pilih_bg));
                card1.setBackgroundColor(getResources().getColor(R.color.default_bg));
                card2.setBackgroundColor(getResources().getColor(R.color.default_bg));
            }
        });

        // Pilih kartu 1 (misal BCA)
        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                paymentMethod = "CreditCard";
                selectedBank = "BCA";

                etCardNumber.setVisibility(View.VISIBLE);
                cardCash.setBackgroundColor(getResources().getColor(R.color.default_bg));
                card1.setBackgroundColor(getResources().getColor(R.color.pilih_bg));
                card2.setBackgroundColor(getResources().getColor(R.color.default_bg));
            }
        });

        // Pilih kartu 2 (misal HDFC)
        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                paymentMethod = "CreditCard";
                selectedBank = "HDFC Bank";

                etCardNumber.setVisibility(View.VISIBLE);
                cardCash.setBackgroundColor(getResources().getColor(R.color.default_bg));
                card1.setBackgroundColor(getResources().getColor(R.color.default_bg));
                card2.setBackgroundColor(getResources().getColor(R.color.pilih_bg));
            }
        });

        // Tombol bayar
        btnPaymentNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(pembayaran_activity.this, StrukActivity.class);

                // Kirim data lama
                intent.putExtra("address", address);
                intent.putExtra("selected_plan", program);
                intent.putExtra("deliveryTime", deliveryTime);
                intent.putExtra("startDate", startDate);
                intent.putExtra("name", name);
                intent.putExtra("phoneNumber", phoneNumber);
                intent.putExtra("city", city);
                intent.putExtra("province", province);
                intent.putExtra("postalCode", postalCode);
                intent.putExtra("email_user", emailUser);
                Log.d("Response SERVER", emailUser);
                intent.putExtra("useCoins", useCoins);
                Log.d("Response SERVER", String.valueOf(useCoins));
                intent.putExtra("coin_value", coinValue);
                intent.putExtra("original_price", originalPrice);
                intent.putExtra("final_price", finalPrice);

                int count = menuNames.length;
                intent.putExtra("selected_count", count);
                for (int i = 0; i < count; i++) {
                    intent.putExtra("menu_id_" + i, menuIds[i]);
                    intent.putExtra("menu_name_" + i, menuNames[i]);
                    intent.putExtra("menu_price_" + i, menuPrices[i]);
                    intent.putExtra("menu_calories_" + i, menuCalories[i]);
                }

                // Kirim data metode pembayaran
                intent.putExtra("payment_method", paymentMethod);
                intent.putExtra("selected_bank", selectedBank);
                intent.putExtra("card_number", etCardNumber.getText().toString());

                startActivity(intent);
            }
        });

        // Set default selection ke COD (misal)
        cardCash.performClick();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        address = intent.getStringExtra("address");
        program = intent.getStringExtra("selected_plan");
        deliveryTime = intent.getStringExtra("deliveryTime");
        startDate = intent.getStringExtra("startDate");
        name = intent.getStringExtra("name");
        phoneNumber = intent.getStringExtra("phoneNumber");
        city = intent.getStringExtra("city");
        province = intent.getStringExtra("province");
        postalCode = intent.getStringExtra("postalCode");
        emailUser = intent.getStringExtra("email_user");

        useCoins = intent.getBooleanExtra("useCoins", false);
        coinValue = intent.getIntExtra("coin_value", 0);
        originalPrice = intent.getDoubleExtra("original_price", 0);
        finalPrice = intent.getDoubleExtra("final_price", 0);

        int count = intent.getIntExtra("selected_count", 0);
        menuIds = new int[count];
        menuNames = new String[count];
        menuPrices = new double[count];
        menuCalories = new int[count];

        for (int i = 0; i < count; i++) {
            menuIds[i] = intent.getIntExtra("menu_id_" + i,0);
            menuNames[i] = intent.getStringExtra("menu_name_" + i);
            menuPrices[i] = intent.getDoubleExtra("menu_price_" + i, 0);
            menuCalories[i] = intent.getIntExtra("menu_calories_" + i, 0);
        }
    }
}
