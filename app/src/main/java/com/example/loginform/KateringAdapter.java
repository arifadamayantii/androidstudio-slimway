package com.example.loginform;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KateringAdapter extends RecyclerView.Adapter<KateringAdapter.ViewHolder> {

    private Context context;
    private List<KateringActivityModel> kateringList;
    private String emailUser;

    public KateringAdapter(Context context, List<KateringActivityModel> kateringList, String emailUser) {
        this.context = context;
        this.kateringList = kateringList;
        this.emailUser = emailUser;
    }

    @NonNull
    @Override
    public KateringAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_katering_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KateringAdapter.ViewHolder holder, int position) {
        KateringActivityModel item = kateringList.get(position);

        holder.txtOrderId.setText("Order ID: " + item.getOrderId());
        holder.txtMenus.setText("Menu: " + String.join(", ", item.getMenuNames()));
        holder.txtTotalPrice.setText("Total: Rp" + item.getFinalPrice());

        // Tambahkan listener tombol hapus
        holder.btnDelete.setOnClickListener(v -> {
            new androidx.appcompat.app.AlertDialog.Builder(context)
                    .setTitle("Konfirmasi Hapus")
                    .setMessage("Yakin ingin menghapus pesanan dengan ID \"" + item.getOrderId() + "\"?")
                    .setPositiveButton("Ya", (dialog, which) -> {
                        deleteOrder(item.getOrderId(), position); // Lanjut hapus pesanan
                    })
                    .setNegativeButton("Batal", (dialog, which) -> dialog.dismiss())
                    .show();
        });

    }

    @Override
    public int getItemCount() {
        return kateringList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtOrderId, txtMenus, txtTotalPrice;
        Button btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtOrderId = itemView.findViewById(R.id.txtOrderId);
            txtMenus = itemView.findViewById(R.id.txtMenus);
            txtTotalPrice = itemView.findViewById(R.id.txtTotalPrice);
            btnDelete = itemView.findViewById(R.id.btnDelete); // Pastikan ID-nya sesuai di XML
        }
    }

    private void deleteOrder(String orderId, int posisi) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DbContract.SERVER_DELETE_ORDER_URL,
                response -> {
                    Log.d("RESPONSE_DELETE_ORDER", response);
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        if (responseObject.has("message") && responseObject.getString("message").equals("Pesanan berhasil dihapus")) {
                            Toast.makeText(context, "Pesanan berhasil dihapus", Toast.LENGTH_SHORT).show();
                            kateringList.remove(posisi);
                            notifyItemRemoved(posisi);
                        } else {
                            Toast.makeText(context, "Gagal menghapus pesanan", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(context, "Terjadi kesalahan saat parsing", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Toast.makeText(context, "Gagal terhubung ke server", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email_user", emailUser);
                params.put("order_id", orderId);
                return params;
            }
        };

        Volley.newRequestQueue(context).add(stringRequest);
    }
}
