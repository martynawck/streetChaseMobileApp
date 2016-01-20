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
import com.example.martyna.sc.Models.ControlPoint;
import com.example.martyna.sc.Utilities.ServerUrl;
import com.example.martyna.sc.Utilities.SessionManager;
import com.example.martyna.sc.Interfaces.OnControlPointTaskCompleted;
import com.example.martyna.sc.R;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

/**
 * Created by Martyna on 2016-01-18.
 */
public class GetInitialControlPoint {

    private final OnControlPointTaskCompleted listener;
    private final Context mContext;
    ControlPoint controlPoint;
    SessionManager sessionManager;
    String urls;

    public GetInitialControlPoint(Context context, OnControlPointTaskCompleted listener, String urls) {
        this.listener = listener;
        this.mContext = context;
        sessionManager = new SessionManager(mContext);
        this.urls = urls;
    }

    public void runVolley() {

        RequestQueue queue = Volley.newRequestQueue(mContext);
        String url = ServerUrl.BASE_URL+"mobile/control_point/initial/"+urls;
        StringRequest dr = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if (!response.equalsIgnoreCase("null")) {
                                JSONObject jsonObject = new JSONObject(response);
                                controlPoint = JSONParser.JSONToControlPoint(jsonObject);
                                listener.onControlPointReturned(controlPoint);
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
                        // error.
                        Toast.makeText(mContext, mContext.getString(R.string.mygames_error), Toast.LENGTH_LONG).show();


                    }
                }
        );
        queue.add(dr);
    }
}