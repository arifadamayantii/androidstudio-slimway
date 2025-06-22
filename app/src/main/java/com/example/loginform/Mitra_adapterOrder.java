package com.example.loginform;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Mitra_adapterOrder extends RecyclerView.Adapter<Mitra_adapterOrder.ViewHolder> {

    private Context context;
    private List<PesananKatering_Model> pesananList;

    public Mitra_adapterOrder(Context context, List<PesananKatering_Model> pesananList) {
        this.context = context;
        this.pesananList = pesananList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_pesanan_katering, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PesananKatering_Model pesanan = pesananList.get(position);

        holder.tvOrderId.setText("ID Pesanan: " + pesanan.getOrderId());
        holder.tvNama.setText("Nama: " + pesanan.getName());
        holder.tvAlamat.setText("Alamat: " + pesanan.getAddress() + ", " +
                pesanan.getCity() + ", " + pesanan.getProvince() + ", " + pesanan.getPostalCode());

        // Clear container dulu biar tidak duplikat saat di-recycle
        holder.menuContainer.removeAllViews();

        for (MenuModel menu : pesanan.getMenuList()) {
            TextView tvMenu = new TextView(context);
            tvMenu.setText("- " + menu.getMenuName());
            holder.menuContainer.addView(tvMenu);
        }
    }

    @Override
    public int getItemCount() {
        return pesananList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId, tvNama, tvAlamat;
        LinearLayout menuContainer;

        public ViewHolder(View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvNama = itemView.findViewById(R.id.tvNama);
            tvAlamat = itemView.findViewById(R.id.tvAlamat);
            menuContainer = itemView.findViewById(R.id.menuContainer);
        }
    }
}
