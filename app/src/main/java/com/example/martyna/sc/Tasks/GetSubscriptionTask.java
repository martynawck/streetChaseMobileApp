package com.example.martyna.sc.Tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.martyna.sc.Utilities.JSONParser;
import com.example.martyna.sc.Utilities.ServerUrl;
import com.example.martyna.sc.Utilities.SessionManager;
import com.example.martyna.sc.Models.Subscription;
import com.example.martyna.sc.Interfaces.OnGetSubscriptionTaskCompleted;
import com.example.martyna.sc.R;
import org.json.JSONObject;

/**
 * Created by Martyna on 2016-01-19.
 */
public class GetSubscriptionTask  {

    private final OnGetSubscriptionTaskCompleted listener;
    private final Context mContext;
    SessionManager sessionManager;
    Subscription subscription;
    String urls;

    public GetSubscriptionTask(Context context, OnGetSubscriptionTaskCompleted listener, String urls) {
        this.listener = listener;
        this.mContext = context;
        sessionManager = new SessionManager(mContext);
        this.urls = urls;
    }

    public void runVolley() {

        RequestQueue queue = Volley.newRequestQueue(mContext);
        String url = ServerUrl.BASE_URL+"mobile/games/subscription/"+sessionManager.getValueOfUserId()+"/"+urls;
        StringRequest dr = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if (!response.equalsIgnoreCase("null")) {
                                JSONObject jsonObject = new JSONObject(response);
                                subscription = JSONParser.JSONToSubscription(jsonObject);
                                listener.onGetSubscription(subscription);
                            }

                        }catch (Exception e) {
                            Toast.makeText(mContext, mContext.getString(R.string.mygames_error), Toast.LENGTH_LONG).show();

                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(mContext, mContext.getString(R.string.mygames_error), Toast.LENGTH_LONG).show();


                    }
                }
        );
        queue.add(dr);
    }
}
