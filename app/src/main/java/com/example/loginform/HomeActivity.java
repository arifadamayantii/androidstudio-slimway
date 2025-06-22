package com.example.loginform;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class HomeActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;  // DrawerLayout to handle sidebar
    private ImageView menuIcon;  // ImageView for the menu icon (to open sidebar)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_main);
        String emailUser = getIntent().getStringExtra("email_user");
        View sidebar = findViewById(R.id.sidebar_layout);

        // Panggil method loadUserData untuk mengisi data sidebar
        SideBarActivity.loadUserData(emailUser, sidebar, this);
        TextView textHelloName = findViewById(R.id.tvGreeting);
        TextView textTodayDate = findViewById(R.id.tvDate);



// Set tanggal hari ini
        String dayName = new SimpleDateFormat("EEEE", new Locale("id", "ID")).format(new Date());
        String currentDate = new SimpleDateFormat("dd MMM", Locale.getDefault()).format(new Date());
        textTodayDate.setText(dayName+", " + currentDate);


// Ambil nama dari API pakai email
        String url = DbContract.SERVER_SETTINGWELCOME_URL + "?email=" + emailUser;

        RequestQueue queue = Volley.newRequestQueue(HomeActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String firstName = jsonObject.getString("first_name");
                        textHelloName.setText("Hello, " + firstName);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    error.printStackTrace();
                });

        queue.add(stringRequest);
        ImageView imageViewLogout = findViewById(R.id.imageView22);
        imageViewLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish(); // Tutup HomeAdmin
            }
        });
        // Find the home icon ImageView
        ImageView homeIcon = findViewById(R.id.nav_home_recipe);
        ImageView navCatering = findViewById(R.id.nav_home_catering);
        ImageView navCalorie = findViewById(R.id.nav_home_calorie);
        ImageView navProgress = findViewById(R.id.nav_home_progres);

        // Set an OnClickListener for the home icon
        homeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to start the ResepActivity
                Intent intent = new Intent(HomeActivity.this, ResepActivity.class);
                intent.putExtra("email_user", emailUser);
                startActivity(intent);
            }
        });
        navCatering.setOnClickListener(view -> {
            Intent intent = new Intent(HomeActivity.this, CateringActivity.class);
            intent.putExtra("email_user", emailUser);
            startActivity(intent);
            finish();
        });
        navCalorie.setOnClickListener(view -> {
            Intent intent = new Intent(HomeActivity.this, CalorieActivity.class);
            intent.putExtra("email_user", emailUser);
            startActivity(intent);
            finish();
            });
        navProgress.setOnClickListener(view -> {
            Intent intent = new Intent(HomeActivity.this, Progress_activity.class);
            intent.putExtra("email_user", emailUser);
            startActivity(intent);
            finish();
        });

            // Initialize the DrawerLayout and ImageView
            drawerLayout = findViewById(R.id.drawer_layout);
            menuIcon = findViewById(R.id.imageView10);  // This is the ImageView with the menu icon

            // Set the padding to handle window insets (system bars, etc.)
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });

            // Set click listener for the menu icon to open the sidebar
            menuIcon.setOnClickListener(v -> {
                // Open the sidebar by sliding the drawer
                drawerLayout.openDrawer(findViewById(R.id.sidebar_layout));
            });

            // You can also handle closing the sidebar by clicking the back icon in the sidebar
            ImageView backIcon = findViewById(R.id.ic_back);  // Assuming you have a back icon in sidebar.xml
            backIcon.setOnClickListener(v -> {
                // Close the sidebar when the back icon is clicked
                drawerLayout.closeDrawer(findViewById(R.id.sidebar_layout));
            });

            ImageView myActivityIcon = findViewById(R.id.imageView20);
            myActivityIcon.setOnClickListener(v -> {
                Intent intent = new Intent(HomeActivity.this, MyActivity.class);
                intent.putExtra("email_user", emailUser);
                startActivity(intent);
            });
    }
}

