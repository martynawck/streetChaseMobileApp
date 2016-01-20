package com.example.martyna.sc.Tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.martyna.sc.Utilities.ServerUrl;
import com.example.martyna.sc.Utilities.SessionManager;
import com.example.martyna.sc.Models.StreetGame;
import java.util.ArrayList;

/**
 * Created by Martyna on 2016-01-13.
 */

public class SubscribeToGameTask {

    private final Context mContext;
    SessionManager sessionManager;
    private String urls;

    public SubscribeToGameTask ( Context context, String urls) {
        this.mContext = context;
        sessionManager = new SessionManager(mContext);
        this.urls = urls;
    }

    public void runVolley() {

        RequestQueue queue = Volley.newRequestQueue(mContext);
        String url = ServerUrl.BASE_URL+"mobile/games/subscription/"+sessionManager.getValueOfUserId()+"/"+urls;
        StringRequest dr = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }
        );
        queue.add(dr);
    }
}

