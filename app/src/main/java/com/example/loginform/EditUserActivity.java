package com.example.loginform;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.util.HashMap;
import java.util.Map;

public class EditUserActivity extends AppCompatActivity {

    EditText edtFirstName, edtLastName, edtEmail, edtPhone, edtPassword, edtConfirmPassword;
    AutoCompleteTextView autoCompleteStatus;
    Button btnRegister;

    String emailLama;

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
        autoCompleteStatus = findViewById(R.id.auto_complete_txt);
        btnRegister = findViewById(R.id.btnRegister);
        ImageView backIcon = findViewById(R.id.imageView25);


        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish(); // atau bisa pakai Intent ke ResepActivity
            }
        });

        Intent intent = getIntent();
        if (intent != null) {
            edtFirstName.setText(intent.getStringExtra("firstName"));
            edtLastName.setText(intent.getStringExtra("lastName"));
            edtEmail.setText(intent.getStringExtra("email"));
            edtPhone.setText(intent.getStringExtra("phone"));
            autoCompleteStatus.setText(intent.getStringExtra("statusAkun"));
            emailLama = intent.getStringExtra("email");
            edtPassword.setText(intent.getStringExtra("Password"));
            edtConfirmPassword.setText(intent.getStringExtra("Password"));
            String[] statusList = {"admin", "user", "mitra"};
            ArrayAdapter<String> adapterStatus = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, statusList);
            autoCompleteStatus.setAdapter(adapterStatus);
            autoCompleteStatus.setThreshold(1); // Tampilkan dropdown setelah 1 huruf diketik
            autoCompleteStatus.setOnClickListener(v -> autoCompleteStatus.showDropDown()); //
        }

        btnRegister.setText("Update");
        btnRegister.setOnClickListener(v -> updateUser(emailUser));
    }

    private void updateUser(final String emailUser) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DbContract.SERVER_UPDATE_USER_URL,
                response -> {
                    try {
                        // Parsing JSON response
                        org.json.JSONObject jsonObject = new org.json.JSONObject(response);
                        String message = jsonObject.getString("message");
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

                        if (message.toLowerCase().contains("berhasil")) {
                            setResult(Activity.RESULT_OK);
                            Intent intent = new Intent(EditUserActivity.this, HomeAdmin.class);
                            intent.putExtra("email_user", emailUser);
                            startActivity(intent);
                        }
                    } catch (Exception e) {
                        Toast.makeText(this, "Error saat memproses respon: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                },
                error -> Toast.makeText(this, "Gagal update user: " + error.getMessage(), Toast.LENGTH_SHORT).show()) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email_lama", emailLama);
                params.put("first_name", edtFirstName.getText().toString());
                params.put("last_name", edtLastName.getText().toString());
                params.put("email", edtEmail.getText().toString());
                params.put("phone", edtPhone.getText().toString());
                params.put("password", edtPassword.getText().toString());
                params.put("status_akun", autoCompleteStatus.getText().toString());
                return params;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);
    }
}
