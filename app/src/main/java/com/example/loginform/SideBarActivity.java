package com.example.loginform;
import android.view.View;
import android.widget.TextView;
import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject;

public class SideBarActivity {

        public static void loadUserData(String email, View sidebar, Context context) {
            TextView textViewName = sidebar.findViewById(R.id.textViewName);
            TextView textViewNumber = sidebar.findViewById(R.id.textViewNumber);

            String url = DbContract.SERVER_SETTINGNAMESIDEBAR_URL + "?email=" + email;

            RequestQueue queue = Volley.newRequestQueue(context);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    response -> {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String firstName = jsonObject.getString("first_name");
                            String lastName = jsonObject.getString("last_name");
                            String phone = jsonObject.getString("phone");

                            String fullName = firstName + " " + lastName;

                            textViewName.setText(fullName);
                            textViewNumber.setText(phone);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    },
                    error -> error.printStackTrace()
            );
            queue.add(stringRequest);
        }


}
