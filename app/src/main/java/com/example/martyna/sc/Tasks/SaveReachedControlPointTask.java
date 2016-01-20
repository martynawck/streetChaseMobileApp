package com.example.martyna.sc.Tasks;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.martyna.sc.Utilities.ServerUrl;
import com.example.martyna.sc.Utilities.SessionManager;

/**
 * Created by Martyna on 2016-01-19.
 */
public class SaveReachedControlPointTask {

    private final Context mContext;
    SessionManager sessionManager;
    private String[] urls;

    public SaveReachedControlPointTask ( Context context, String[] urls) {
        this.mContext = context;
        sessionManager = new SessionManager(mContext);
        this.urls = urls;
    }

    public void runVolley() {

        RequestQueue queue = Volley.newRequestQueue(mContext);
        String url = ServerUrl.BASE_URL+"mobile/user_reached_point/"+sessionManager.getValueOfUserId()+"/"+urls[0]+"/"+urls[1];
        StringRequest dr = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        // Toast.makeText($this, response, Toast.LENGTH_LONG).show();
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