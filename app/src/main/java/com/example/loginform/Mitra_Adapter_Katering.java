package com.example.loginform;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Mitra_Adapter_Katering extends RecyclerView.Adapter<Mitra_Adapter_Katering.ViewHolder> {

    private Context context;
    private List<KateringModel> kateringList;
    private String email_user;
    private String baseImageUrl = "http://192.168.1.14/loginRegister_mysql_volley_api/"; // Update with your server URL

    public Mitra_Adapter_Katering(Context context, List<KateringModel> kateringList, String email_user) {
        this.context = context;
        this.kateringList = kateringList;
        this.email_user = email_user;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_katering_mitra, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        KateringModel katering = kateringList.get(position);

        // Load image using Glide
        if (katering.getImagePath() != null && !katering.getImagePath().isEmpty()) {
            String imageUrl = baseImageUrl + katering.getImagePath();
            Log.d("IMAGE_LOAD", "Loading katering image from: " + imageUrl);

            Glide.with(context)
                    .load(imageUrl)
                    .placeholder(R.drawable.tuna) // Add a placeholder image
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.imgMenuPhoto);
        } else {
            holder.imgMenuPhoto.setImageResource(R.drawable.tuna);
        }

        holder.txtNamaMenu.setText(katering.getMenuName());
        holder.txtMenuCategory.setText("Kategori: " + katering.getMenuCategory());
        holder.txtCalories.setText("Kalori: " + katering.getCalories() + " kcal");
        holder.txtPrice.setText("Harga: Rp " + String.format("%,.0f", katering.getPrice()));

        // Set click listeners
        holder.btnEdit.setOnClickListener(v -> openEditActivity(katering));
        holder.btnDelete.setOnClickListener(v -> showDeleteConfirmation(katering, position));

        // Add item click listener if needed
        holder.itemView.setOnClickListener(v -> {
            // You can add detail view functionality here if needed
        });
    }

    private void openEditActivity(KateringModel katering) {
        Intent intent = new Intent(context, EditKateringActivity.class);
        intent.putExtra("id_katering", katering.getId());
        intent.putExtra("email_user", katering.getEmailUser());
        intent.putExtra("menu_category", katering.getMenuCategory());
        intent.putExtra("package_option", katering.getPackageOption());
        intent.putExtra("menu_name", katering.getMenuName());
        intent.putExtra("description", katering.getDescription());
        intent.putExtra("calories", katering.getCalories());
        intent.putExtra("price", katering.getPrice());
        intent.putExtra("image_path", katering.getImagePath());
        intent.putExtra("store_location", katering.getStoreLocation());
        context.startActivity(intent);
    }

    private void showDeleteConfirmation(KateringModel katering, int position) {
        new androidx.appcompat.app.AlertDialog.Builder(context)
                .setTitle("Konfirmasi Hapus")
                .setMessage("Yakin ingin menghapus katering \"" + katering.getMenuName() + "\"?")
                .setPositiveButton("Ya", (dialog, which) -> {
                    deleteKatering(String.valueOf(katering.getId()), position);
                })
                .setNegativeButton("Batal", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void deleteKatering(String idKatering, int position) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DbContract.SERVER_DELETE_KATERING_URL,
                response -> {
                    Log.d("DELETE_RESPONSE", response);
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        if (responseObject.has("message") &&
                                responseObject.getString("message").equals("Data katering berhasil dihapus")) {

                            Toast.makeText(context, "Berhasil dihapus", Toast.LENGTH_SHORT).show();
                            kateringList.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, kateringList.size());
                        } else {
                            Toast.makeText(context, "Gagal menghapus katering", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(context, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
                        Log.e("JSON_ERROR", "Error parsing response", e);
                    }
                },
                error -> {
                    Toast.makeText(context, "Gagal menghubungi server", Toast.LENGTH_SHORT).show();
                    Log.e("DELETE_ERROR", "Error deleting katering", error);
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email_user", email_user);
                params.put("id_katering", idKatering);
                return params;
            }
        };

        Volley.newRequestQueue(context).add(stringRequest);
    }

    @Override
    public int getItemCount() {
        return kateringList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgMenuPhoto;
        TextView txtNamaMenu, txtMenuCategory, txtCalories, txtPrice;
        Button btnEdit, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgMenuPhoto = itemView.findViewById(R.id.imgMenuPhoto);
            txtNamaMenu = itemView.findViewById(R.id.txtNamaMenu);
            txtMenuCategory = itemView.findViewById(R.id.txtmenuCategory);
            txtCalories = itemView.findViewById(R.id.txtCalories);
            txtPrice = itemView.findViewById(R.id.txtprice);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

    // Update data method
    public void updateData(List<KateringModel> newList) {
        kateringList.clear();
        kateringList.addAll(newList);
        notifyDataSetChanged();
    }
}