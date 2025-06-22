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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class MitraCreatemenuActivity extends AppCompatActivity {

    private AutoCompleteTextView dropdownMenuCategory;
    private AutoCompleteTextView dropdownPackageOption;
    private EditText editMenuName, editDescription, editCalories, editPrice, editStoreLocation;
    private Button btnUpload;
    private ImageButton btnBack;
    private ImageView imgAddPhoto;

    private static final int PICK_IMAGE_REQUEST = 101;
    private Uri imageUri;
    private String emailUser;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mitra_createmenu);

        emailUser = getIntent().getStringExtra("email_user");

        // Initialize views
        initViews();
        setupDropdowns();
        setupListeners();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Mengupload menu...");
        progressDialog.setCancelable(false);
    }

    private void initViews() {
        dropdownMenuCategory = findViewById(R.id.dropdownMenuCategory);
        dropdownPackageOption = findViewById(R.id.dropdownPackageOption);
        editMenuName = findViewById(R.id.editMenuName);
        editDescription = findViewById(R.id.editDescription);
        editCalories = findViewById(R.id.editCalories);
        editPrice = findViewById(R.id.editPrice);
        editStoreLocation = findViewById(R.id.editStoreLocation);
        btnUpload = findViewById(R.id.btnUpload);
        btnBack = findViewById(R.id.buttonBack);
        imgAddPhoto = findViewById(R.id.imgAddPhoto);
    }

    private void setupDropdowns() {
        String[] categories = {"Appetizer", "Main Course", "Dessert"};
        String[] packageOptions = {"Small", "Medium", "Large"};

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_dropdown_item_1line, categories);
        dropdownMenuCategory.setAdapter(categoryAdapter);

        ArrayAdapter<String> packageAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_dropdown_item_1line, packageOptions);
        dropdownPackageOption.setAdapter(packageAdapter);
    }

    private void setupListeners() {
        imgAddPhoto.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        btnUpload.setOnClickListener(v -> validateAndUpload());

        btnBack.setOnClickListener(v -> finish());
    }

    private void validateAndUpload() {
        String menuCategory = dropdownMenuCategory.getText().toString().trim();
        String packageOption = dropdownPackageOption.getText().toString().trim();
        String menuName = editMenuName.getText().toString().trim();
        String description = editDescription.getText().toString().trim();
        String caloriesStr = editCalories.getText().toString().trim();
        String priceStr = editPrice.getText().toString().trim();
        String storeLocation = editStoreLocation.getText().toString().trim();

        if (TextUtils.isEmpty(menuCategory)) {
            showToast("Pilih kategori menu");
            return;
        }

        if (TextUtils.isEmpty(packageOption)) {
            showToast("Pilih paket menu");
            return;
        }

        if (TextUtils.isEmpty(menuName)) {
            showToast("Masukkan nama menu");
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

        if (imageUri == null) {
            showToast("Pilih gambar menu terlebih dahulu");
            return;
        }

        try {
            int calories = Integer.parseInt(caloriesStr);
            float price = Float.parseFloat(priceStr);
            uploadMenuData(menuCategory, packageOption, menuName, description, calories, price, storeLocation);
        } catch (NumberFormatException e) {
            showToast("Format angka tidak valid");
        }
    }

    private void uploadMenuData(String menuCategory, String packageOption, String menuName,
                                String description, int calories, float price, String storeLocation) {
        if (!checkNetworkConnection()) {
            showToast("Tidak ada koneksi internet");
            return;
        }

        progressDialog.show();

        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(
                Request.Method.POST,
                DbContract.SERVER_CREATE_MENU,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        progressDialog.dismiss();
                        try {
                            String resultResponse = new String(response.data);
                            Log.d("UPLOAD_RESPONSE", resultResponse);
                            JSONObject jsonObject = new JSONObject(resultResponse);
                            String resp = jsonObject.getString("server_response");

                            if (resp.contains("OK")) {
                                showToast("Menu berhasil diupload");
                                navigateToMitraHome();
                            } else {
                                showToast("Upload menu gagal");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            showToast("Error parsing response");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        String errorMessage = "Error uploading menu";
                        if (error instanceof NetworkError) {
                            errorMessage = "Tidak ada koneksi internet";
                        } else if (error instanceof ServerError) {
                            errorMessage = "Server error";
                        } else if (error instanceof TimeoutError) {
                            errorMessage = "Timeout";
                        }
                        showToast(errorMessage);
                        Log.e("UPLOAD_ERROR", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email_user", emailUser);
                params.put("menu_category", menuCategory);
                params.put("package_option", packageOption);
                params.put("menu_name", menuName);
                params.put("description", description);
                params.put("calories", String.valueOf(calories));
                params.put("price", String.valueOf(price));
                params.put("store_location", storeLocation);
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                try {
                    params.put("image", new DataPart(
                            "menu_" + System.currentTimeMillis() + ".jpg",
                            getFileDataFromUri(imageUri),
                            "image/jpeg"
                    ));
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("IMAGE_ERROR", "Error getting image data");
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

    private void navigateToMitraHome() {
        Intent intent = new Intent(this, MitraHomeActivity.class);
        intent.putExtra("email_user", emailUser);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == PICK_IMAGE_REQUEST) {
                imageUri = data.getData();
                imgAddPhoto.setImageURI(imageUri);
            }
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