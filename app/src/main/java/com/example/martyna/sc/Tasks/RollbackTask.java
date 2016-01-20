package com.example.martyna.sc.Tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.martyna.sc.Utilities.ServerUrl;
import com.example.martyna.sc.Utilities.SessionManager;
import com.example.martyna.sc.Interfaces.OnRollbackTaskCompleted;
import com.example.martyna.sc.R;

/**
 * Created by Martyna on 2016-01-19.
 */
public class RollbackTask {

    private final Context mContext;
    SessionManager sessionManager;
    OnRollbackTaskCompleted listener;
    private String urls;

    public RollbackTask ( Context context, OnRollbackTaskCompleted listener, String urls) {
        this.mContext = context;
        sessionManager = new SessionManager(mContext);
        this.listener = listener;
        this.urls = urls;
    }

    public void runVolley() {

        RequestQueue queue = Volley.newRequestQueue(mContext);
        String url = ServerUrl.BASE_URL+"mobile/games/subscription/played/rollback/"+sessionManager.getValueOfUserId()+"/"+urls;
        StringRequest dr = new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {

                        listener.onRollback();

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error.
                        Toast.makeText(mContext, mContext.getString(R.string.cannot_rollback), Toast.LENGTH_LONG).show();
                    }
                }
        );
        queue.add(dr);
    }
}

