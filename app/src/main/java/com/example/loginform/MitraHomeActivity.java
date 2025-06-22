package com.example.loginform;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MitraHomeActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;  // DrawerLayout to handle sidebar
    private ImageView menuIcon;  // ImageView for the menu icon (to open sidebar)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mitrahome); // Ensure you are using the correct layout file
        String emailUser = getIntent().getStringExtra("email_user");
        View sidebar = findViewById(R.id.sidebar_layout);

        // Call method loadUserData to fill sidebar with data
        SideBarActivity.loadUserData(emailUser, sidebar, this);
        // Initialize DrawerLayout and ImageView for the menu
        drawerLayout = findViewById(R.id.drawer_layout);
        menuIcon = findViewById(R.id.imageView10);  // This is the ImageView with the menu icon

        // Set greeting and date

        // Logout functionality
        ImageView imageViewLogout = findViewById(R.id.imageView22);
        imageViewLogout.setOnClickListener(v -> {
            Intent intent = new Intent(MitraHomeActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish(); // Close MitraHomeActivity
        });

        // Handle the home icon click



        // Set padding to handle window insets (system bars, etc.)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set click listener for the menu icon to open the sidebar
        menuIcon.setOnClickListener(v -> {
            Log.d("MitraHome", "Menu icon diklik");
            // Open the sidebar by sliding the drawer
            drawerLayout.openDrawer(findViewById(R.id.sidebar_layout));
        });

        // Close the sidebar when the back icon is clicked in the sidebar
        ImageView backIcon = findViewById(R.id.ic_back);  // Assuming you have a back icon in sidebar.xml
        backIcon.setOnClickListener(v -> {
            // Close the sidebar
            drawerLayout.closeDrawer(findViewById(R.id.sidebar_layout));
        });

        // Handle MyActivity icon click
        ImageView myActivityIcon = findViewById(R.id.imageView20);
        myActivityIcon.setOnClickListener(v -> {
            Intent intent = new Intent(MitraHomeActivity.this, MitraActivity.class);
            intent.putExtra("email_user", emailUser);
            startActivity(intent);
        });

        Button createMenuButton = findViewById(R.id.createMenuBtn); // Assuming you have the correct ID for the button
        createMenuButton.setOnClickListener(v -> {
            Intent intent = new Intent(MitraHomeActivity.this, MitraCreatemenuActivity.class);
            intent.putExtra("email_user", emailUser); // Pass the email_user or any required data
            startActivity(intent);
        });
    }
}
