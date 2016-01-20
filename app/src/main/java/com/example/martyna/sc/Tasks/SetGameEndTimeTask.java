package com.example.martyna.sc.Tasks;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.martyna.sc.Interfaces.OnEndTimeSet;
import com.example.martyna.sc.Utilities.ServerUrl;
import com.example.martyna.sc.Utilities.SessionManager;

/**
 * Created by Martyna on 2016-01-19.
 */
public class SetGameEndTimeTask /*extends AsyncTask<String, Void, String> */{

    private final Context mContext;
    SessionManager sessionManager;
    String[] urls;
    OnEndTimeSet listener;

    public SetGameEndTimeTask ( Context context, String[] urls, OnEndTimeSet listener) {
        this.mContext = context;
        sessionManager = new SessionManager(mContext);
        this.urls = urls;
        this.listener = listener;
    }

    public void runVolley() {

        RequestQueue queue = Volley.newRequestQueue(mContext);
        String url = ServerUrl.BASE_URL+"mobile/games/subscription/played/end/"+sessionManager.getValueOfUserId()+"/"+urls[0]+"/"+urls[1];
        StringRequest dr = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        listener.onEndTime();
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error.

                    }
                }
        );
        queue.add(dr);
    }
}