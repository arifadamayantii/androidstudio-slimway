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
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.text.DecimalFormat;
import java.util.List;

public class KateringViewAdapter extends RecyclerView.Adapter<KateringViewAdapter.ViewHolder> {

    private Context context;
    private List<KateringItem> kateringList;
    private String emailUser;
    private String baseImageUrl = "http://192.168.1.14/loginRegister_mysql_volley_api/"; // Update with your server URL

    public KateringViewAdapter(Context context, List<KateringItem> kateringList, String emailUser) {
        this.context = context;
        this.kateringList = kateringList;
        this.emailUser = emailUser;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_katering, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        KateringItem item = kateringList.get(position);

        // Load image using Glide
        if (item.getImagePath() != null && !item.getImagePath().isEmpty()) {
            String imageUrl = baseImageUrl + item.getImagePath();
            Log.d("IMAGE_LOAD", "Loading image from: " + imageUrl);

            Glide.with(context)
                    .load(imageUrl)
                    .placeholder(R.drawable.tuna) // Default image while loading
                    .error(R.drawable.tuna) // Default image if error occurs
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.imageMenu);
        } else {
            holder.imageMenu.setImageResource(R.drawable.tuna);
        }

        holder.MenuName.setText(item.getMenuName());
        holder.description.setText(limitWords(item.getDescription(), 5));

        DecimalFormat df = new DecimalFormat("#,###");
        holder.Price.setText("Rp" + df.format(item.getPrice()));
        holder.kalori.setText(item.getCalories() + " kkal");

        // Selection effect
        holder.itemView.setBackgroundResource(item.isSelected() ? R.drawable.bg_selected : R.drawable.bg_normal);

        holder.itemView.setOnClickListener(v -> {
            item.setSelected(!item.isSelected());
            notifyItemChanged(position);

            // Optional: Open detail activity when clicked
            // openDetailActivity(item);
        });
    }

    @Override
    public int getItemCount() {
        return kateringList.size();
    }

    public List<KateringItem> getSelectedItems() {
        List<KateringItem> selected = new java.util.ArrayList<>();
        for (KateringItem item : kateringList) {
            if (item.isSelected()) {
                selected.add(item);
            }
        }
        return selected;
    }
    private String limitWords(String input, int maxWords) {
        String[] words = input.trim().split("\\s+");
        if (words.length <= maxWords) return input;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < maxWords; i++) {
            sb.append(words[i]).append(" ");
        }
        return sb.toString().trim() + "...";
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageMenu;
        TextView MenuName, description, Price, kalori;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageMenu = itemView.findViewById(R.id.imageMenu);
            MenuName = itemView.findViewById(R.id.textMenuName);
            description = itemView.findViewById(R.id.textDescription);
            Price = itemView.findViewById(R.id.textPrice);
            kalori = itemView.findViewById(R.id.kalori);
        }
    }
}