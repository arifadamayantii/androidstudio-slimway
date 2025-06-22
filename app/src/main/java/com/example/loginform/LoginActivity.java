package com.example.loginform;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin, btnGoogle;
    private TextView tvForgetPassword, tvSignup;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inisialisasi komponen
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnGoogle = findViewById(R.id.btnGoogle);
        tvForgetPassword = findViewById(R.id.tvForgetPassword);
        tvSignup = findViewById(R.id.tvSignup);
        progressDialog = new ProgressDialog(this);

        // Event untuk tombol login
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please enter email and password!", Toast.LENGTH_SHORT).show();
                } else {
                        CheckLogin(email,password);
                    }

                }
            }
        );
        // Event untuk tombol Google
        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "Google Login Clicked!", Toast.LENGTH_SHORT).show();
            }
        });

        // Event untuk lupa password
        tvForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "Forgot Password Clicked!", Toast.LENGTH_SHORT).show();
            }
        });

        // Event untuk pindah ke halaman sign up
        tvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

    }
    public void CheckLogin(final String email, final String password) {
        if (checkNetworkConnection()){
            progressDialog.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, DbContract.SERVER_LOGIN_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray serverResponse = jsonObject.getJSONArray("server_response");
                                JSONObject resObj = serverResponse.getJSONObject(0);
                                String status = resObj.getString("status");

                                if (status.equals("OK")) {
                                    String statusAkun = resObj.getString("status_akun");
                                    Toast.makeText(getApplicationContext(), "Login Berhasil", Toast.LENGTH_SHORT).show();
                                    Intent intent;
                                    switch (statusAkun) {
                                        case "admin":
                                            intent = new Intent(LoginActivity.this, HomeAdmin.class);
                                            break;
                                        case "mitra":
                                            intent = new Intent(LoginActivity.this, MitraHomeActivity.class);
                                            break;
                                        case "user":
                                        default:
                                            intent = new Intent(LoginActivity.this, HomeActivity.class);
                                            break;
                                    }
                                    intent.putExtra("email_user", email); // email adalah variabel yang kamu dapat dari EditText login
                                    startActivity(intent);
                                } else {
                                    String message = resObj.has("message") ? resObj.getString("message") : "Login gagal";
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map <String,String> params = new HashMap<>();
                    params.put("email", email);
                    params.put("password", password);
                    return params;
                }
            };

            volleyConnection.getInstance(LoginActivity.this).addtoRequestQueue((stringRequest));

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressDialog.cancel();
                }
            }, 2000);
            } else {
            Toast.makeText(getApplicationContext(), "Tidak ada koneksi internet", Toast.LENGTH_SHORT).show();
        }
    }
    public boolean checkNetworkConnection(){
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}
