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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

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

public class AddResepActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 101;
    private static final int PICK_VIDEO_REQUEST = 102;
    private ImageView imgAddPhoto;
    private Uri imageUri;
    private VideoView videoPreview;
    private Uri videoUri;


    private EditText etRecipeName, etDescription, etProtein, etFat, etCarbs, etCalories, etIngredient, etInstruction;
    private CheckBox cbBreakfast, cbLunch, cbDinner;

    ProgressDialog progressDialog;
    private Button btnSave, btnUpload;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_resep_activity);

        imgAddPhoto = findViewById(R.id.imgAddPhoto);

        imgAddPhoto.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });
        String emailUser = getIntent().getStringExtra("email_user");
        ImageView ImgAddPhoto = findViewById(R.id.imgAddPhoto);
        btnSave = findViewById(R.id.btnSave);
        btnUpload = findViewById(R.id.btnUpload);
        btnBack = findViewById(R.id.btnBack);
        etRecipeName = findViewById(R.id.etRecipeName);
        etDescription = findViewById(R.id.etDescription);
        etProtein = findViewById(R.id.etProtein);
        etFat = findViewById(R.id.etFat);
        etCarbs = findViewById(R.id.etCarbs);
        etCalories = findViewById(R.id.etCalories);
        etIngredient = findViewById(R.id.etIngredient);
        etInstruction = findViewById(R.id.etInstruction);
        cbBreakfast = findViewById(R.id.cbBreakfast);
        cbLunch = findViewById(R.id.cbLunch);
        cbDinner = findViewById(R.id.cbDinner);


        ImgAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Pilih Foto"), PICK_IMAGE_REQUEST);
            }
        });


        // Kembali ke halaman sebelumnya saat tombol back diklik
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish(); // atau bisa pakai Intent ke ResepActivity
            }
        });

        // Simpan resep dan kembali ke halaman ResepActivity
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveRecipe(emailUser);
            }
        });

        // Upload resep dan kembali ke halaman ResepActivity
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadRecipe(emailUser);
            }
        });
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Menyimpan resep...");
        progressDialog.setCancelable(false);
    }
    private void saveRecipe(final String emailUser) {
        float breakfastValue = cbBreakfast.isChecked() ? 1 : 0;
        float lunchValue = cbLunch.isChecked() ? 1 : 0;
        float dinnerValue = cbDinner.isChecked() ? 1 : 0;
        String recipeName = etRecipeName.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String protein = etProtein.getText().toString().trim();
        String fat = etFat.getText().toString().trim();
        String carbs = etCarbs.getText().toString().trim();
        String calories = etCalories.getText().toString().trim();
        String ingredient = etIngredient.getText().toString().trim();
        String instruction = etInstruction.getText().toString().trim();

        if (TextUtils.isEmpty(recipeName) || TextUtils.isEmpty(description) || TextUtils.isEmpty(protein) ||
                TextUtils.isEmpty(fat) || TextUtils.isEmpty(carbs) || TextUtils.isEmpty(calories) ||
                TextUtils.isEmpty(ingredient) || TextUtils.isEmpty(instruction)) {
            Toast.makeText(AddResepActivity.this, "Harap isi semua data resep!", Toast.LENGTH_SHORT).show();
            return;
        }
        // Cek apakah pengguna memilih salah satu CheckBox
        if (breakfastValue == 0 && lunchValue == 0 && dinnerValue == 0) {
            Toast.makeText(AddResepActivity.this, "Harap pilih kategori makanan (Breakfast, Lunch, or Dinner)!", Toast.LENGTH_SHORT).show();
            return;
        }
        createResepToServer(emailUser, recipeName, description, protein, fat, carbs, calories, breakfastValue, lunchValue, dinnerValue, ingredient, instruction);
    }
    public void createResepToServer(String email_user, String recipeName, String description, String protein, String fat, String carbs,
                                    String calories, float breakfast, float lunch, float dinner, String ingredients, String instructions){
        if (checkNetworkConnection()) {
            progressDialog.show();

            VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, DbContract.SERVER_ADD_RECIPE_URL,
                    new Response.Listener<NetworkResponse>() {
                        @Override
                        public void onResponse(NetworkResponse response) {
                            try {
                                String jsonStr = new String(response.data);
                                Log.d("Response", jsonStr);

                                JSONObject jsonObject = new JSONObject(jsonStr);
                                String resp = jsonObject.getString("server_response");

                                if (resp.equals("[{\"status\":\"OK\"}]")) {
                                    Toast.makeText(getApplicationContext(), "Simpan resep Berhasil", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(AddResepActivity.this, ResepActivity.class);
                                    intent.putExtra("email_user", email_user);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(getApplicationContext(), "Simpan Resep Gagal", Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), "Parsing JSON gagal", Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            String errorMessage = "Terjadi kesalahan jaringan"; // Default message
                            Log.e("VolleyError", "Error: " + error.toString());
                            if (error instanceof NetworkError) {
                                errorMessage = "Tidak ada koneksi internet";
                            } else if (error instanceof ServerError) {
                                errorMessage = "Kesalahan di server";
                            } else if (error instanceof TimeoutError) {
                                errorMessage = "Waktu tunggu habis";
                            } else {
                                errorMessage = "Kesalahan tidak terduga";
                            }
                            // Menampilkan pesan error di UI
                            Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("email_user", email_user);
                    params.put("recipe_name", recipeName);
                    params.put("description", description);
                    params.put("protein", protein);
                    params.put("fat", fat);
                    params.put("carbs", carbs);
                    params.put("calories", calories);
                    params.put("is_breakfast", String.valueOf(breakfast));
                    params.put("is_lunch", String.valueOf(lunch));
                    params.put("is_dinner", String.valueOf(dinner));
                    params.put("ingredients", ingredients);
                    params.put("instructions", instructions);
                    params.put("video_path", "default.mp4");
                    return params;
                }
                @Override
                protected Map<String, VolleyMultipartRequest.DataPart> getByteData() {
                    Map<String, VolleyMultipartRequest.DataPart> params = new HashMap<>();
                    try {
                        params.put("photo", new VolleyMultipartRequest.DataPart(
                                "recipe_" + System.currentTimeMillis() + ".jpg",
                                getFileDataFromUri(imageUri),
                                "image/jpeg"
                        ));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return params;
                }

            };

            volleyConnection.getInstance(AddResepActivity.this).addtoRequestQueue(multipartRequest);
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
    private void uploadRecipe(final String emailUser) {
        float breakfastValue = cbBreakfast.isChecked() ? 1 : 0;
        float lunchValue = cbLunch.isChecked() ? 1 : 0;
        float dinnerValue = cbDinner.isChecked() ? 1 : 0;
        String recipeName = etRecipeName.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String protein = etProtein.getText().toString().trim();
        String fat = etFat.getText().toString().trim();
        String carbs = etCarbs.getText().toString().trim();
        String calories = etCalories.getText().toString().trim();
        String ingredient = etIngredient.getText().toString().trim();
        String instruction = etInstruction.getText().toString().trim();

        if (TextUtils.isEmpty(recipeName) || TextUtils.isEmpty(description) || TextUtils.isEmpty(protein) ||
                TextUtils.isEmpty(fat) || TextUtils.isEmpty(carbs) || TextUtils.isEmpty(calories) ||
                TextUtils.isEmpty(ingredient) || TextUtils.isEmpty(instruction)) {
            Toast.makeText(AddResepActivity.this, "Harap isi semua data resep!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (breakfastValue == 0 && lunchValue == 0 && dinnerValue == 0) {
            Toast.makeText(AddResepActivity.this, "Pilih kategori makanan (Breakfast, Lunch, atau Dinner)!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (imageUri == null) {
            Toast.makeText(this, "Harap pilih foto resep terlebih dahulu", Toast.LENGTH_SHORT).show();
            return;
        }
        if (checkNetworkConnection()) {
            progressDialog.setMessage("Mengupload resep...");
            progressDialog.show();

            // Buat Volley Multipart Request
            VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(
                    Request.Method.POST,
                    DbContract.SERVER_UPLOAD_RECIPE_URL,
                    new Response.Listener<NetworkResponse>() {
                        @Override
                        public void onResponse(NetworkResponse response) {
                            progressDialog.dismiss();
                            try {
                                String resultResponse = new String(response.data);
                                Log.d("RESP_JSON", "Raw response: " + resultResponse);
                                JSONObject jsonObject = new JSONObject(resultResponse);
                                String resp = jsonObject.getString("server_response");

                                if (resp.equals("[{\"status\":\"OK\"}]")) {
                                    Toast.makeText(getApplicationContext(), "Upload resep Berhasil", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(AddResepActivity.this, RewardActivity.class);
                                    intent.putExtra("email_user", emailUser);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(getApplicationContext(), "Upload Resep Gagal", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            String errorMessage = "Terjadi kesalahan jaringan";
                            Log.e("UploadVolleyError", "Error: " + error.toString());
                            if (error instanceof NetworkError) {
                                errorMessage = "Tidak ada koneksi internet";
                            } else if (error instanceof ServerError) {
                                errorMessage = "Kesalahan di server";
                            } else if (error instanceof TimeoutError) {
                                errorMessage = "Waktu tunggu habis";
                            } else {
                                errorMessage = "Kesalahan tidak terduga";
                            }
                            Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    Log.d("PARAMS_UPLOAD", new JSONObject(params).toString());
                    params.put("email_user", emailUser);
                    Log.d("DebugEmail", "Email user: " + emailUser);
                    params.put("recipe_name", etRecipeName.getText().toString().trim());
                    params.put("description", description);
                    params.put("protein", protein);
                    params.put("fat", fat);
                    params.put("carbs", carbs);
                    params.put("calories", calories);
                    params.put("is_breakfast", cbBreakfast.isChecked() ? "1" : "0");
                    params.put("is_lunch", cbLunch.isChecked() ? "1" : "0");
                    params.put("is_dinner", cbDinner.isChecked() ? "1" : "0");
                    params.put("ingredients", ingredient);
                    params.put("instructions", instruction);
                    params.put("video_path", "default.mp4");
                    params.put("status", "upload");
                    params.put("point_increment", "5");
                    return params;
                }
                @Override
                protected Map<String, DataPart> getByteData() {
                    Map<String, DataPart> params = new HashMap<>();
                    try {
                        params.put("photo", new DataPart(
                                "recipe_" + System.currentTimeMillis() + ".jpg",
                                getFileDataFromUri(imageUri),
                                "image/jpeg"
                        ));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return params;
                }
            };

            volleyConnection.getInstance(AddResepActivity.this).addtoRequestQueue(multipartRequest);
            new Handler().postDelayed(() -> progressDialog.cancel(), 2000);
        } else {
            Toast.makeText(getApplicationContext(), "Tidak ada koneksi internet", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            Uri selectedUri = data.getData();

            if (requestCode == PICK_IMAGE_REQUEST) {
                imageUri = selectedUri;
                imgAddPhoto.setImageURI(imageUri);
                Toast.makeText(this, "Gambar dipilih", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public boolean checkNetworkConnection(){
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    private byte[] getFileDataFromUri(Uri uri) throws IOException {
        InputStream inputStream = getContentResolver().openInputStream(uri);
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }

        return byteBuffer.toByteArray();
    }
}