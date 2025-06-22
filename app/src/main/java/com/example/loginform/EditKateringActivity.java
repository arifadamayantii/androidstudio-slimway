package com.example.loginform;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class EditKateringActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 101;

    private AutoCompleteTextView editPaket, editKategori;
    private EditText editNama, editDeskripsi, editKalori, editHarga, editLokasi;
    private ImageView imgAddPhoto;
    private Button btnUpdate;
    private ImageButton btnBack;

    private Uri imageUri;
    private ProgressDialog progressDialog;
    private int idKatering;
    private String emailUser;
    private String existingImagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mitra_createmenu);

        // Initialize views
        initViews();

        // Get data from intent
        getIntentData();

        // Setup dropdowns
        setupDropdowns();

        // Setup click listeners
        setupListeners();

        // Initialize progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Memperbarui menu...");
        progressDialog.setCancelable(false);
    }

    private void initViews() {
        editNama = findViewById(R.id.editMenuName);
        editKategori = findViewById(R.id.dropdownMenuCategory);
        editDeskripsi = findViewById(R.id.editDescription);
        editPaket = findViewById(R.id.dropdownPackageOption);
        editKalori = findViewById(R.id.editCalories);
        editHarga = findViewById(R.id.editPrice);
        editLokasi = findViewById(R.id.editStoreLocation);
        imgAddPhoto = findViewById(R.id.imgAddPhoto);
        btnUpdate = findViewById(R.id.btnUpload);
        btnBack = findViewById(R.id.buttonBack);
    }

    private void getIntentData() {
        Intent intent = getIntent();
        idKatering = intent.getIntExtra("id_katering", -1);
        emailUser = intent.getStringExtra("email_user");
        existingImagePath = intent.getStringExtra("image_path");

        editNama.setText(intent.getStringExtra("menu_name"));
        editDeskripsi.setText(intent.getStringExtra("description"));
        editKalori.setText(String.valueOf(intent.getIntExtra("calories", 0)));
        editHarga.setText(String.valueOf(intent.getDoubleExtra("price", 0.0)));
        editLokasi.setText(intent.getStringExtra("store_location"));
    }

    private void setupDropdowns() {
        String[] categories = {"Appetizer", "Main Course", "Dessert"};
        String[] packageOptions = {"Small", "Medium", "Large"};

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_dropdown_item_1line, categories);
        editKategori.setAdapter(categoryAdapter);
        editKategori.setThreshold(0);

        ArrayAdapter<String> packageAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_dropdown_item_1line, packageOptions);
        editPaket.setAdapter(packageAdapter);
        editPaket.setThreshold(0);

        // Set dropdown values from intent
        String menuCategory = getIntent().getStringExtra("menu_category");
        if (menuCategory != null) {
            editKategori.setText(menuCategory, false);
        }

        String packageOption = getIntent().getStringExtra("package_option");
        if (packageOption != null) {
            editPaket.setText(packageOption, false);
        }
    }

    private void setupListeners() {
        imgAddPhoto.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        btnUpdate.setOnClickListener(v -> validateAndUpdate());

        btnBack.setOnClickListener(v -> finish());
    }

    private void validateAndUpdate() {
        String menuName = editNama.getText().toString().trim();
        String menuCategory = editKategori.getText().toString().trim();
        String description = editDeskripsi.getText().toString().trim();
        String packageOption = editPaket.getText().toString().trim();
        String caloriesStr = editKalori.getText().toString().trim();
        String priceStr = editHarga.getText().toString().trim();
        String storeLocation = editLokasi.getText().toString().trim();

        if (TextUtils.isEmpty(menuName)) {
            showToast("Masukkan nama menu");
            return;
        }

        if (TextUtils.isEmpty(menuCategory)) {
            showToast("Pilih kategori menu");
            return;
        }

        if (TextUtils.isEmpty(packageOption)) {
            showToast("Pilih paket menu");
            return;
        }

        if (TextUtils.isEmpty(caloriesStr)) {
            showToast("Masukkan jumlah kalori");
            return;
        }

        if (TextUtils.isEmpty(priceStr)) {
            showToast("Masukkan harga menu");
            return;
        }

        if (TextUtils.isEmpty(storeLocation)) {
            showToast("Masukkan lokasi toko");
            return;
        }

        try {
            int calories = Integer.parseInt(caloriesStr);
            float price = Float.parseFloat(priceStr);

            if (checkNetworkConnection()) {
                updateKatering(menuName, menuCategory, description, packageOption,
                        calories, price, storeLocation);
            } else {
                showToast("Tidak ada koneksi internet");
            }
        } catch (NumberFormatException e) {
            showToast("Format angka tidak valid");
        }
    }

    private void updateKatering(String menuName, String menuCategory, String description,
                                String packageOption, int calories, float price, String storeLocation) {
        progressDialog.show();

        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(
                Request.Method.POST,
                DbContract.SERVER_UPDATE_KATERING_URL,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        progressDialog.dismiss();
                        try {
                            String resultResponse = new String(response.data);
                            Log.d("UPDATE_RESPONSE", resultResponse);
                            JSONObject jsonObject = new JSONObject(resultResponse);
                            String resp = jsonObject.getString("message");

                            if (resp.equalsIgnoreCase("Data katering berhasil diperbarui")) {
                                showToast("Menu berhasil diperbarui");
                                setResult(RESULT_OK);
                                Intent intent = new Intent(EditKateringActivity.this, MitraActivity.class);
                                intent.putExtra("email_user", emailUser);
                                startActivity(intent);
                            } else {
                                showToast("Gagal memperbarui menu");
                            }
                        } catch (JSONException e) {
                            showToast("Error parsing response");
                            Log.e("JSON_ERROR", "Error parsing response", e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        String errorMessage = "Error updating menu";
                        if (error instanceof NetworkError) {
                            errorMessage = "Tidak ada koneksi internet";
                        } else if (error instanceof ServerError) {
                            errorMessage = "Server error";
                        } else if (error instanceof TimeoutError) {
                            errorMessage = "Timeout";
                        }
                        showToast(errorMessage);
                        Log.e("UPDATE_ERROR", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_katering", String.valueOf(idKatering));
                params.put("email_user", emailUser);
                params.put("menu_name", menuName);
                params.put("menu_category", menuCategory);
                params.put("description", description);
                params.put("package_option", packageOption);
                params.put("calories", String.valueOf(calories));
                params.put("price", String.valueOf(price));
                params.put("store_location", storeLocation);

                // Include existing image path if no new image is selected
                if (existingImagePath != null && !existingImagePath.isEmpty() && imageUri == null) {
                    params.put("existing_image_path", existingImagePath);
                }

                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                try {
                    if (imageUri != null) {
                        params.put("image", new DataPart(
                                "menu_" + System.currentTimeMillis() + ".jpg",
                                getFileDataFromUri(imageUri),
                                "image/jpeg"
                        ));
                    }
                } catch (IOException e) {
                    Log.e("IMAGE_ERROR", "Error getting image data", e);
                }
                return params;
            }
        };

        volleyConnection.getInstance(this).addtoRequestQueue(multipartRequest);

        // Auto-dismiss progress after timeout
        new Handler().postDelayed(() -> {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }, 10000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null && requestCode == PICK_IMAGE_REQUEST) {
            imageUri = data.getData();
            imgAddPhoto.setImageURI(imageUri);
        }
    }

    private boolean checkNetworkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    private byte[] getFileDataFromUri(Uri uri) throws IOException {
        InputStream inputStream = getContentResolver().openInputStream(uri);
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}