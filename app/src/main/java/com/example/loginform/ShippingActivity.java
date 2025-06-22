package com.example.loginform;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ShippingActivity extends AppCompatActivity {

    ImageButton buttonBack;
    TextView tvTotalPrice;

    // EditText untuk data diri
    EditText etName, etStartDate, etPhoneNumber, etAddress, etCity, etProvince, etPostalCode, etDeliveryTime;
    Button nextButton;
    double totalPrice = 0;


    // Simpan selectedPlan tapi tidak ditampilkan di UI
    String selectedPlan;
    String emailUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shippingactivity);

        tvTotalPrice = findViewById(R.id.totalPriceValue);

        etName = findViewById(R.id.etName);
        etStartDate = findViewById(R.id.etStartDate);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etAddress = findViewById(R.id.etAddress);
        etCity = findViewById(R.id.etCity);
        etProvince = findViewById(R.id.etProvince);
        etPostalCode = findViewById(R.id.etPostalCode);
        nextButton = findViewById(R.id.nextButton);
        etDeliveryTime = findViewById(R.id.etDeliveryTime);

        Intent intent = getIntent();
        String emailUser = intent.getStringExtra("email_user");
        int coinValue = intent.getIntExtra("coin_value", 0);
        int count = intent.getIntExtra("selected_count", 0);
        selectedPlan = intent.getStringExtra("selected_plan"); // Ambil tapi tidak tampilkan

        List<String> selectedMenuNames = new ArrayList<>();
        List<Double> selectedMenuPrices = new ArrayList<>();
        List<Integer> selectedMenuCalories = new ArrayList<>();
        List<Integer> selectedMenuId = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            int menuId = intent.getIntExtra("menu_id_" + i, 0);
            selectedMenuId.add(menuId);
            selectedMenuNames.add(intent.getStringExtra("menu_name_" + i));
            double price = intent.getDoubleExtra("menu_price_" + i, 0);
            selectedMenuPrices.add(price);
            selectedMenuCalories.add(intent.getIntExtra("menu_calories_" + i, 0));
            totalPrice += price;
        }

        tvTotalPrice.setText(String.format("Total: Rp %.0f", totalPrice));

        buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(v -> finish());

        nextButton.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String startDate = etStartDate.getText().toString().trim();
            String phoneNumber = etPhoneNumber.getText().toString().trim();
            String address = etAddress.getText().toString().trim();
            String city = etCity.getText().toString().trim();
            String province = etProvince.getText().toString().trim();
            String postalCode = etPostalCode.getText().toString().trim();
            String deliveryTime = etDeliveryTime.getText().toString().trim();

            Intent totalanIntent = new Intent(ShippingActivity.this, totalan_activity.class);

            totalanIntent.putExtra("name", name);
            totalanIntent.putExtra("startDate", startDate);
            totalanIntent.putExtra("phoneNumber", phoneNumber);
            totalanIntent.putExtra("address", address);
            totalanIntent.putExtra("city", city);
            totalanIntent.putExtra("province", province);
            totalanIntent.putExtra("postalCode", postalCode);
            totalanIntent.putExtra("deliveryTime", deliveryTime);
            totalanIntent.putExtra("selected_plan", selectedPlan);
            totalanIntent.putExtra("total_price", totalPrice);
            totalanIntent.putExtra("email_user", emailUser);
            totalanIntent.putExtra("coin_value",coinValue);
            Log.d("Response SERVER", emailUser);

            // Kirim data menu (loop sesuai jumlah menu)
            totalanIntent.putExtra("selected_count", count);
            for (int i = 0; i < count; i++) {
                totalanIntent.putExtra("menu_id_" + i, selectedMenuId.get(i));
                Log.d("Response SERVER", "menu_id_" + i + ": " + selectedMenuId.get(i));
                totalanIntent.putExtra("menu_name_" + i, selectedMenuNames.get(i));
                totalanIntent.putExtra("menu_price_" + i, selectedMenuPrices.get(i));
                totalanIntent.putExtra("menu_calories_" + i, selectedMenuCalories.get(i));
            }

            startActivity(totalanIntent);
        });

    }
}
