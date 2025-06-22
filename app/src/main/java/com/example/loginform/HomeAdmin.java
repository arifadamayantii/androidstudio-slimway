package com.example.loginform;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeAdmin extends AppCompatActivity {
    private static final int REQUEST_EDIT_USER = 1001;
    private DrawerLayout drawerLayout;
    private ImageView menuIcon;
    RecyclerView recyclerView;
    UserAdapter userAdapter;
    List<UserModel> userList;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_admin); // Layout khusus admin
        String emailUser = getIntent().getStringExtra("email_user");
        View sidebar = findViewById(R.id.sidebar_layout);
        SideBarActivity.loadUserData(emailUser, sidebar, this);
        TextView textHelloName = findViewById(R.id.tvGreeting);
        TextView textTodayDate = findViewById(R.id.tvDate);

// Set tanggal hari ini
        String dayName = new java.text.SimpleDateFormat("EEEE", new java.util.Locale("id", "ID")).format(new java.util.Date());
        String currentDate = new java.text.SimpleDateFormat("dd MMM", java.util.Locale.getDefault()).format(new java.util.Date());
        textTodayDate.setText(dayName + ", " + currentDate);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userList = new ArrayList<>();

        userAdapter = new UserAdapter(this, userList, emailUser);
        recyclerView.setAdapter(userAdapter);

        requestQueue = Volley.newRequestQueue(this);
        loadUsers(emailUser);
        String url = DbContract.SERVER_SETTINGWELCOME_URL + "?email=" + emailUser;

        RequestQueue queue = Volley.newRequestQueue(HomeAdmin.this);
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
        // Inisialisasi drawer & ikon menu
        drawerLayout = findViewById(R.id.drawer_layout);
        menuIcon = findViewById(R.id.imageView10); // ikon menu (burger icon)

        // Atur padding untuk insets sistem (status bar, nav bar, dll)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Klik untuk membuka sidebar
        menuIcon.setOnClickListener(v -> {
            drawerLayout.openDrawer(findViewById(R.id.sidebar_layout));
        });

        // Tombol kembali di sidebar
        ImageView backIcon = findViewById(R.id.ic_back);
        backIcon.setOnClickListener(v -> {
            drawerLayout.closeDrawer(findViewById(R.id.sidebar_layout));
        });

        ImageView CreateAccountActivityIcon = findViewById(R.id.imageView20);
        CreateAccountActivityIcon.setOnClickListener(v -> {
            Intent intent = new Intent(HomeAdmin.this, CreateAccountActivity.class);
            intent.putExtra("email_user", emailUser);
            startActivity(intent);
        });

        // Tombol Logout
        ImageView imageViewLogout = findViewById(R.id.imageView22);
        imageViewLogout.setOnClickListener(v -> {
            Intent intent = new Intent(HomeAdmin.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish(); // Tutup HomeAdmin
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("HomeAdmin", "onActivityResult called with requestCode=" + requestCode + ", resultCode=" + resultCode);
        if (requestCode == REQUEST_EDIT_USER && resultCode == RESULT_OK) {
            Log.d("HomeAdmin", "Reloading user list after edit");
            loadUsers(null);
        }
    }
    private void loadUsers(String emailUser) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, DbContract.SERVER_GET_USERS_URL,
                response -> {
                    Log.d("LIST_ACCOUNT", "Respon dari server: " + response);
                    try {
                        JSONArray array = new JSONArray(response); // parsing dari String ke JSONArray
                        userList.clear();

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject userObject = array.getJSONObject(i); // akses JSONObject dari array

                            UserModel user = new UserModel(
                                    userObject.optString("id", ""),
                                    userObject.optString("status_akun", ""),
                                    userObject.optString("first_name", ""),
                                    userObject.optString("last_name", ""),
                                    userObject.optString("email", ""),
                                    userObject.optString("phone", ""),
                                    userObject.optString("password", ""),
                                    userObject.optString("status", "")

                            );
                            userList.add(user);
                        }
                        for (UserModel u : userList) {
                            Log.d("DEBUG_PARSED_USER", "email=" + u.getEmail() + ", status=" + u.getStatus());
                        }
                        userAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        Log.e("JSON_ERROR", "Parsing error: " + e.getMessage());
                        Toast.makeText(this, "Data format error", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e("VOLLEY_ERROR", "Error: " + error.toString());
                    if (error.networkResponse != null) {
                        Log.e("SERVER_ERROR", new String(error.networkResponse.data));
                    }
                    Toast.makeText(this, "Connection error", Toast.LENGTH_SHORT).show();
                }
        );

        requestQueue.add(stringRequest);
    }
}
