package com.example.loginform;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    Context context;
    List<UserModel> users;
    String emailUser;

    public UserAdapter(Context context, List<UserModel> users, String emailUser) {
        this.context = context;
        this.users = users;
        this.emailUser = emailUser;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false); // Buat layout ini
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        UserModel user = users.get(position);

        // Gunakan getter methods, bukan akses langsung ke field
        holder.namaText.setText(user.getFirstName() + " " + user.getLastName());
        holder.emailText.setText(user.getEmail());
        holder.phoneText.setText(user.getPhone());
        holder.statusText.setText("Status: " + user.getStatusAkun());
        Log.d("DEBUG_USER_STATUS", "User " + user.getEmail() + " status: " + user.getStatus());

        holder.deleteButton.setOnClickListener(v -> {
            new androidx.appcompat.app.AlertDialog.Builder(context)
                    .setTitle("Konfirmasi Hapus")
                    .setMessage("Yakin ingin menghapus user " + user.getFirstName()+" " +user.getLastName()+ "?")
                    .setPositiveButton("Ya", (dialog, which) -> {
                        hapusUser(user.getEmail(), position);
                    })
                    .setNegativeButton("Batal", (dialog, which) -> dialog.dismiss())
                    .show();
        });

        holder.editButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditUserActivity.class);
            intent.putExtra("firstName", user.getFirstName());
            intent.putExtra("lastName", user.getLastName());
            intent.putExtra("email", user.getEmail());
            intent.putExtra("phone", user.getPhone());
            intent.putExtra("statusAkun", user.getStatusAkun());
            intent.putExtra("Password", user.getPassword());
            intent.putExtra("Status", user.getStatus());
            intent.putExtra("email_user", emailUser);
            context.startActivity(intent);
        });
        holder.statusButton.setText(
                user.getStatus().equals("1") ? "NonAktif" : "Aktif"
        );
        holder.statusButton.setOnClickListener(v -> {
            String currentStatus = user.getStatus();
            String newStatus = currentStatus.equals("1") ? "0" : "1";
            String statusText = newStatus.equals("1") ? "mengaktifkan" : "menonaktifkan";

            new androidx.appcompat.app.AlertDialog.Builder(context)
                    .setTitle("Konfirmasi")
                    .setMessage("Yakin ingin " + statusText + " user " + user.getFirstName() + " " + user.getLastName() + "?")
                    .setPositiveButton("Ya", (dialog, which) -> {
                        ubahStatusUser(user.getEmail(), newStatus, position);
                    })
                    .setNegativeButton("Batal", (dialog, which) -> dialog.dismiss())
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView namaText, emailText, phoneText, statusText;
        Button editButton, deleteButton, statusButton;
        public UserViewHolder(View itemView) {
            super(itemView);
            namaText = itemView.findViewById(R.id.txtNama);
            emailText = itemView.findViewById(R.id.txtEmail);
            phoneText = itemView.findViewById(R.id.txtPhone);
            statusText = itemView.findViewById(R.id.txtStatus);
            editButton = itemView.findViewById(R.id.btnEdit);
            deleteButton = itemView.findViewById(R.id.btnDelete);
            statusButton = itemView.findViewById(R.id.btnToggleStatus);
        }
    }
    private void hapusUser(String emailTarget, int position) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DbContract.SERVER_DELETE_USER_URL,
                response -> {
                    Log.d("RESPONSE_DELETE", response);
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        if (responseObject.has("message") && responseObject.getString("message").equals("User berhasil dihapus")) {
                            Toast.makeText(context, "User berhasil dihapus", Toast.LENGTH_SHORT).show();

                            // Hapus dari list dan update RecyclerView
                            users.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, users.size());
                        } else {
                            Toast.makeText(context, "Gagal menghapus user", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(context, "Kesalahan parsing response", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Toast.makeText(context, "Gagal koneksi ke server", Toast.LENGTH_SHORT).show();
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email_target", emailTarget); // Email user yang akan dihapus
                return params;
            }
        };

        // Kirim request dengan Volley
        Volley.newRequestQueue(context).add(stringRequest);
    }
    private void ubahStatusUser(String emailTarget, String newStatus, int position) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DbContract.SERVER_UPDATE_STATUS_URL,
                response -> {
                    Log.d("RESPONSE_STATUS", response);
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        if (jsonResponse.has("message") && jsonResponse.getString("message").equals("Status akun berhasil diperbarui")) {
                            Toast.makeText(context, "Status berhasil diubah", Toast.LENGTH_SHORT).show();
                            users.get(position).setStatus(newStatus); // Update di list
                            notifyItemChanged(position); // Refresh item
                        } else {
                            Toast.makeText(context, "Gagal mengubah status", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(context, "Kesalahan parsing response", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(context, "Gagal koneksi ke server", Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", emailTarget);
                params.put("status", newStatus); // Status: 1 atau 0
                Log.d("DEBUG_PARAMS", "email=" + emailTarget + ", status=" + newStatus);
                return params;
            }
        };

        Volley.newRequestQueue(context).add(stringRequest);
    }




}