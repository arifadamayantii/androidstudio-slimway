package com.example.loginform;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CateringActivity extends AppCompatActivity {

    ImageView navHome, navRecipe, navCatering, navCalorie, navProgress, menuIcon;
    Button btnregister;
    DrawerLayout drawerLayout;
    TextView coinTextView;

    int coin_value = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.catering_activity);

        navHome = findViewById(R.id.nav_resep_home);
        navRecipe = findViewById(R.id.nav_home_recipe);
        navCatering = findViewById(R.id.nav_resep_catering);
        navCalorie = findViewById(R.id.nav_resep_calorie);
        navProgress = findViewById(R.id.nav_resep_progres);
        btnregister = findViewById(R.id.createcatering);
        menuIcon = findViewById(R.id.menuIcon);
        drawerLayout = findViewById(R.id.drawer_layout);
        // Pastikan TextView ini ada di layout dengan ID yang sesuai

        // Ambil email dari intent
        String emailUser = getIntent().getStringExtra("email_user");
        View sidebar = findViewById(R.id.sidebar_layout);
        // Call method loadUserData to fill sidebar with data
        SideBarActivity.loadUserData(emailUser, sidebar, this);

        // Ambil poin dari server
        loadPointsFromServer(emailUser);

        btnregister.setOnClickListener(view -> {
            Intent intent = new Intent(CateringActivity.this, CateringRegister.class);
            intent.putExtra("email_user", emailUser);
            intent.putExtra("coin_value", coin_value); // kirim koin ke halaman berikutnya
            startActivity(intent);
        });

        navHome.setOnClickListener(view -> navigateToHome(emailUser));
        navRecipe.setOnClickListener(view -> navigateToRecipe(emailUser));
        navCatering.setOnClickListener(view -> navigateToHome(emailUser));
        navCalorie.setOnClickListener(view -> navigateToCalorie(emailUser));
        navProgress.setOnClickListener(view -> navigateToProgress(emailUser));

        menuIcon.setOnClickListener(v -> {
            drawerLayout.openDrawer(findViewById(R.id.sidebar_layout));
        });
        ImageView backIcon = findViewById(R.id.ic_back);  // Assuming you have a back icon in sidebar.xml
        backIcon.setOnClickListener(v -> {
            // Close the sidebar
            drawerLayout.closeDrawer(findViewById(R.id.sidebar_layout));
        });
        ImageView myActivityIcon = findViewById(R.id.imageView20);
        myActivityIcon.setOnClickListener(v -> {
            Intent intent = new Intent(CateringActivity.this, MyActivity.class);
            intent.putExtra("email_user", emailUser);
            startActivity(intent);
        });
        ImageView imageViewLogout = findViewById(R.id.imageView22);
        imageViewLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CateringActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish(); // Tutup HomeAdmin
            }
        });
    }

    private void navigateToHome(String emailUser) {
        Intent intent = new Intent(CateringActivity.this, HomeActivity.class);
        intent.putExtra("email_user", emailUser);
        startActivity(intent);
    }

    private void navigateToRecipe(String emailUser) {
        Intent intent = new Intent(CateringActivity.this, ResepActivity.class);
        intent.putExtra("email_user", emailUser);
        startActivity(intent);
    }
    private void navigateToCalorie(String emailUser) {
        Intent intent = new Intent(CateringActivity.this, CalorieActivity.class);
        intent.putExtra("email_user", emailUser);
        startActivity(intent);
    }
    private void navigateToProgress(String emailUser) {
        Intent intent = new Intent(CateringActivity.this, Progress_activity.class);
        intent.putExtra("email_user", emailUser);
        startActivity(intent);
    }

    private void loadPointsFromServer(String emailUser) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DbContract.SERVER_GET_REWARD_URL,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getBoolean("success")) {
                            int points = jsonObject.getInt("points");
                            coin_value = points;
                        } else {
                            Toast.makeText(CateringActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(CateringActivity.this, "Parsing error", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(CateringActivity.this, "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show()) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", emailUser);
                return params;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }
}
