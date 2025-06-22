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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class CreateAccountActivity extends AppCompatActivity {
    private EditText edtFirstName, edtLastName, edtEmail, edtPhone, edtPassword, edtConfirmPassword;
    private Button btnRegister;
    private AutoCompleteTextView autoCompleteTextView;
    private ProgressDialog progressDialog;

    String[] status = {"user", "admin", "mitra"};
    String selectedStatus = "";
    private String emailUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.createaccount_admin);
        String emailUser = getIntent().getStringExtra("email_user");
        edtFirstName = findViewById(R.id.edtFirstName);
        edtLastName = findViewById(R.id.edtLastName);
        edtEmail = findViewById(R.id.edtEmail);
        edtPhone = findViewById(R.id.edtPhone);
        edtPassword = findViewById(R.id.edtPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        autoCompleteTextView = findViewById(R.id.auto_complete_txt);

        ArrayAdapter<String> adapterItems = new ArrayAdapter<>(this, R.layout.list_status, status);
        autoCompleteTextView.setAdapter(adapterItems);

        // Inisialisasi tombol back
        ImageView backIcon = findViewById(R.id.imageView25);  // Menemukan tombol back di layout

        // Menangani klik pada tombol back
        backIcon.setOnClickListener(v -> {
            // Membuka MainActivity saat tombol back diklik
            Intent intent = new Intent(CreateAccountActivity.this, HomeAdmin.class);
            intent.putExtra("email_user", emailUser);
            startActivity(intent);
        });

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedStatus = parent.getItemAtPosition(position).toString();
                Toast.makeText(CreateAccountActivity.this, "Status: " + selectedStatus, Toast.LENGTH_SHORT).show();
            }
        });

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Sedang mendaftarkan...");
        progressDialog.setCancelable(false);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser(emailUser);
            }
        });
    }

    private void registerUser(String emailUser) {
        String firstName = edtFirstName.getText().toString().trim();
        String lastName = edtLastName.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String confirmPassword = edtConfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName) || TextUtils.isEmpty(email)
                || TextUtils.isEmpty(phone) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword) || TextUtils.isEmpty(selectedStatus)) {
            Toast.makeText(this, "Harap isi semua data termasuk status!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Password tidak cocok!", Toast.LENGTH_SHORT).show();
            return;
        }

        sendToServer(emailUser, firstName, lastName, email, phone, password, selectedStatus);
    }

    private void sendToServer(final String emailUser, final String firstName, final String lastName, final String email, final String phone, final String password, final String selectedStatus) {
        if (checkNetworkConnection()) {
            progressDialog.show();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, DbContract.SERVER_CREATEUSER_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String resp = jsonObject.getString("server_response");

                                if (resp.equals("[{\"status\":\"OK\"}]")) {
                                    Toast.makeText(getApplicationContext(), "Registrasi Berhasil", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(CreateAccountActivity.this, HomeAdmin.class);
                                    intent.putExtra("email_user", emailUser);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(getApplicationContext(), "Registrasi Gagal", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), "Error parsing response", Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("first_name", firstName);
                    params.put("last_name", lastName);
                    params.put("email", email);
                    params.put("phone", phone);
                    params.put("password", password);
                    params.put("status_akun", selectedStatus); // ⬅ Kirim status di sini
                    return params;
                }
            };

            volleyConnection.getInstance(CreateAccountActivity.this).addtoRequestQueue(stringRequest);

            new Handler().postDelayed(() -> progressDialog.cancel(), 2000);
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
