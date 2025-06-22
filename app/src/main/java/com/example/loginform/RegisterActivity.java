package com.example.loginform;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
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
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private EditText edtFirstName, edtLastName, edtEmail, edtPhone, edtPassword, edtConfirmPassword;
    private Button btnRegister;
    private TextView txtLogin;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Inisialisasi UI
        edtFirstName = findViewById(R.id.edtFirstName);
        edtLastName = findViewById(R.id.edtLastName);
        edtEmail = findViewById(R.id.edtEmail);
        edtPhone = findViewById(R.id.edtPhone);
        edtPassword = findViewById(R.id.edtPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        txtLogin = findViewById(R.id.txtLogin);

        // Event klik tombol register
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        // Event klik teks login
        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Sedang mendaftarkan...");
        progressDialog.setCancelable(false);
    }

    private void registerUser() {
        String firstName = edtFirstName.getText().toString().trim();
        String lastName = edtLastName.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String confirmPassword = edtConfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName) || TextUtils.isEmpty(email)
                || TextUtils.isEmpty(phone) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(this, "Harap isi semua data!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Password tidak cocok!", Toast.LENGTH_SHORT).show();
            return;
        }
        CreateDataToServer(firstName, lastName, email, phone, password);
    }
    public void CreateDataToServer(final String firstName, final String lastName, final String email, final String phone, final String password) {
        if (checkNetworkConnection()){
            progressDialog.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, DbContract.SERVER_REGISTER_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject (response);
                                String resp = jsonObject.getString("server_response");
                                if(resp.equals("[{\"status\":\"OK\"}]")){
                                    Toast.makeText(getApplicationContext(), "Registrasi Berhasil", Toast.LENGTH_SHORT).show();
                                    // Arahkan ke halaman login setelah berhasil
                                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(getApplicationContext(), "Registrasi Gagal", Toast.LENGTH_SHORT).show();
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
                   params.put("first_name", firstName);
                   params.put("last_name", lastName);
                   params.put("phone", phone);
                   return params;

                }

            };

            volleyConnection.getInstance(RegisterActivity.this).addtoRequestQueue((stringRequest));

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
