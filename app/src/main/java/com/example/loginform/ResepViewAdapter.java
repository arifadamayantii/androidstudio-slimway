package com.example.loginform;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

public class ResepViewAdapter extends RecyclerView.Adapter<ResepViewAdapter.ResepViewHolder> {
    private Context context;
    private List<Resep> resepList;
    private String emailUser;
    private RequestManager glide;
    public ResepViewAdapter(Context context, List<Resep> resepList, String emailUser) {
        this.context = context;
        this.resepList = resepList;
        this.emailUser = emailUser;
        this.glide = Glide.with(context);
    }

    @NonNull
    @Override
    public ResepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.resep_card_item, parent, false);
        return new ResepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResepViewHolder holder, int position) {
        Resep resep = resepList.get(position);

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
        holder.tvKategoriKalori.setText(resep.getDeskripsi());
        holder.tvKalori.setText(resep.getKalori() + " Kkal");
        // Logic pengacakan rating
        double minRating = 3.5;
        double maxRating = 5.0;
        double randomRating = minRating + (Math.random() * (maxRating - minRating));
        double roundedRating = Math.round(randomRating * 10.0) / 10.0;

        int minReview = 100;
        int maxReview = 2000;
        int randomReview = minReview + (int)(Math.random() * ((maxReview - minReview) + 1));

        String ratingText = "★ " + roundedRating + " (" + randomReview + ")";
        holder.ratingResep.setText(ratingText);

        // Tambahkan click listener di sini untuk membuka DetailResepActivity
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailResepActivity.class);
            intent.putExtra("namaResep", resep.getNamaResep());
            intent.putExtra("kategori", resep.getKategori());
            intent.putExtra("kalori", resep.getKalori());
            intent.putExtra("deskripsi", resep.getDeskripsi());
            intent.putExtra("bahan", resep.getIngredients());
            intent.putExtra("langkah", resep.getInstructions());
            intent.putExtra("photo", resep.getPhotoPath());
            Log.d("DEBUG_LOG", "Photo path: " + resep.getPhotoPath());
            intent.putExtra("video", resep.getVideoPath());
            intent.putExtra("status", resep.getStatus());
            intent.putExtra("protein", resep.getProtein());
            intent.putExtra("fat", resep.getFat());
            intent.putExtra("carbs", resep.getCarbs());
            intent.putExtra("emailUser", emailUser);
            intent.putExtra("rating", roundedRating);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return resepList.size();
    }

    public static class ResepViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public ImageView gambarResep;
        TextView tvNamaResep, tvKategoriKalori, tvKalori, ratingResep; // tambahkan ratingResep

        public ResepViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNamaResep = itemView.findViewById(R.id.namaResep);
            tvKategoriKalori = itemView.findViewById(R.id.kategoriResep);
            tvKalori = itemView.findViewById(R.id.kaloriResep);
            ratingResep = itemView.findViewById(R.id.ratingResep); // inisialisasi ratingResep
            gambarResep = itemView.findViewById(R.id.gambarResep);
        }
    }

}
