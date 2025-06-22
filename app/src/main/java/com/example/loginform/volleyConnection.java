package com.example.loginform;
import android.content.Context;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class volleyConnection {
    private static volleyConnection vInstance;
    private RequestQueue requestQueue;
    private static Context vCtx;
    private volleyConnection (Context context){
        vCtx = context;
        requestQueue = getRequestQueue();
    }
    private RequestQueue getRequestQueue() {
        if (requestQueue == null){
            requestQueue = Volley.newRequestQueue(vCtx.getApplicationContext());
        }
        return requestQueue;
    }

    public static synchronized volleyConnection getInstance(Context context){
        if (vInstance == null){
            vInstance = new volleyConnection(context);
        }
        return vInstance;
    }

    public <T> void addtoRequestQueue (Request<T> request){
        getRequestQueue().add(request);
    }
}
