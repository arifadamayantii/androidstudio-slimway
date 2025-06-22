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
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResepAdapter extends RecyclerView.Adapter<ResepAdapter.ResepViewHolder> {
    private Context context;
    private List<ResepModel> resepList;
    private String emailUser;
    private RequestManager glide;

    public ResepAdapter(Context context, List<ResepModel> resepList, String emailUser) {
        this.context = context;
        this.resepList = resepList;
        this.emailUser = emailUser;
        this.glide = Glide.with(context);
    }

    @NonNull

    @Override
    public ResepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_resep, parent, false);
        return new ResepViewHolder(view);
        // Cek status upload dan nonaktifkan tombol Edit jika sudah di-upload

    }

    @Override
    public void onBindViewHolder(@NonNull ResepViewHolder holder, int position) {
        ResepModel resep = resepList.get(position);
        String imageUrl;
        if (resep.getPhotoPath() != null && !resep.getPhotoPath().isEmpty()) {
            // Gunakan path relatif dari server
            imageUrl = "http://192.168.1.14/loginRegister_mysql_volley_api/" + resep.getPhotoPath();

            // 2. Debugging - tampilkan URL di log
            Log.d("IMAGE_LOAD", "Loading image from: " + imageUrl);

            // 3. Load gambar dengan Glide
            Glide.with(holder.itemView.getContext())
                    .load(imageUrl)
                    .placeholder(R.drawable.tuna) // gambar default saat loading
                    .diskCacheStrategy(DiskCacheStrategy.ALL) // caching
                    .into(holder.gambarResep);
        } else {
            // Jika photo_path null/kosong, gunakan gambar default
            holder.gambarResep.setImageResource(R.drawable.tuna);
        }
        holder.tvNamaResep.setText(resep.getNamaResep());
        holder.tvKategoriKalori.setText(resep.getKategori() + " | " + resep.getKalori() + " kkal");
        holder.btnEdit.setOnClickListener(v -> {
            if ("upload".equalsIgnoreCase(resep.getStatus())) {  // Atau gunakan kondisi lain sesuai status upload
                holder.btnEdit.setEnabled(false);  // Nonaktifkan tombol Edit
                Toast.makeText(context, "Resep sudah diupload, tidak bisa diedit", Toast.LENGTH_SHORT).show();
            } else {
                holder.btnEdit.setEnabled(true);
                Intent intent = new Intent(context, EditResepActivity.class);
                intent.putExtra("id", resep.getId());
                intent.putExtra("nama", resep.getNamaResep());
                intent.putExtra("kategori", resep.getKategori());
                intent.putExtra("kalori", resep.getKalori());
                intent.putExtra("deskripsi", resep.getDeskripsi());
                intent.putExtra("foto", resep.getPhotoPath());
                intent.putExtra("bahan", resep.getIngredients());
                intent.putExtra("langkah", resep.getInstructions());
                intent.putExtra("video", resep.getVideoPath());
                intent.putExtra("status", resep.getStatus());
                intent.putExtra("protein", resep.getProtein());
                intent.putExtra("fat", resep.getFat());
                intent.putExtra("carbs", resep.getCarbs());
                intent.putExtra("isBreakfast", resep.getIsBreakfast());
                intent.putExtra("isLunch", resep.getIsLunch());
                intent.putExtra("isDinner", resep.getIsDinner());
                intent.putExtra("email_user", emailUser);
                context.startActivity(intent);// Aktifkan tombol Edit jika belum di-upload
            }

        });
        holder.btnHapus.setOnClickListener(v -> {
            new androidx.appcompat.app.AlertDialog.Builder(context)
                    .setTitle("Konfirmasi Hapus")
                    .setMessage("Yakin ingin menghapus resep \"" + resep.getNamaResep() + "\"?")
                    .setPositiveButton("Ya", (dialog, which) -> {
                        hapusResep(resep.getId(), position); // Lanjut hapus resep
                    })
                    .setNegativeButton("Batal", (dialog, which) -> dialog.dismiss())
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return resepList.size();
    }

    public class ResepViewHolder extends RecyclerView.ViewHolder {
        TextView tvNamaResep, tvKategoriKalori;
        Button btnEdit, btnHapus;
        public ImageView gambarResep;

        public ResepViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNamaResep = itemView.findViewById(R.id.tvNamaResep);
            tvKategoriKalori = itemView.findViewById(R.id.tvKategoriKalori);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnHapus = itemView.findViewById(R.id.btnHapus);
            gambarResep = itemView.findViewById(R.id.imageResep);

        }
    }

    private void hapusResep(String idResep, int posisi) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DbContract.SERVER_DELETE_RECIPE_URL,
                response -> {
                    Log.d("RESPONSE_DELETE", response);
                    try {
                        // Parse response JSON dari server
                        JSONObject responseObject = new JSONObject(response);

                        // Mengecek jika resep berhasil dihapus
                        if (responseObject.has("message") && responseObject.getString("message").equals("Resep berhasil dihapus")) {
                            // Menampilkan pesan berhasil
                            Toast.makeText(context, "Berhasil dihapus", Toast.LENGTH_SHORT).show();

                            // Menghapus resep dari list (sesuai posisi item)
                            resepList.remove(posisi);
                            notifyItemRemoved(posisi);
                        } else {
                            // Jika ada error
                            Toast.makeText(context, "Gagal menghapus resep", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        // Menangani jika JSON parsing gagal
                        Toast.makeText(context, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    // Menangani error jika terjadi
                    Toast.makeText(context, "Gagal hapus", Toast.LENGTH_SHORT).show();
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email_user", emailUser);  // Mengirimkan email_user
                params.put("id_resep", idResep);     // Mengirimkan id_resep untuk menghapus resep yang dipilih
                return params;
            }
        };

        // Menambahkan request ke dalam Volley queue untuk dieksekusi
        Volley.newRequestQueue(context).add(stringRequest);
    }

}

