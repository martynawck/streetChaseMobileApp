package com.example.martyna.sc.Tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.martyna.sc.Activities.MainActivity;
import com.example.martyna.sc.Models.ServerUrl;
import com.example.martyna.sc.Models.SessionManager;
import com.example.martyna.sc.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Martyna on 2016-01-12.
 */
public class AuthenticationTask {

    private final Context mContext;
    private final String login;
    private final ProgressDialog progressDialog;
    private final String password;
    private final Activity activity;

    public AuthenticationTask(Context context, Activity activity, ProgressDialog progressDialog, String login, String password) {
        mContext = context;
        this.login = login;
        this.progressDialog = progressDialog;
        this.password = password;
        this.activity = activity;
    }

    public void runVolley() {

        RequestQueue queue = Volley.newRequestQueue(mContext);

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.POST, ServerUrl.BASE_URL+"mobile/login/"+login+"/"+password, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(!response.equals("null")) {
                                SessionManager sessionManager = new SessionManager(mContext);
                                sessionManager.createSession(login, password, Integer.parseInt(response.getString("id")));
                                sessionManager.setValueOfFirstName(response.getString("name"));
                                progressDialog.dismiss();

                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(mContext, mContext.getString(R.string.login_error),
                                        Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(mContext, mContext.getString(R.string.wrong_login_data),
                                    Toast.LENGTH_SHORT).show();
                        }

                        activity.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(mContext, mContext.getString(R.string.login_success), Toast.LENGTH_SHORT).show();
                            }
                        });

                        Intent i = new Intent(mContext, MainActivity.class);
                        activity.startActivity(i);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(mContext, mContext.getString(R.string.login_error), Toast.LENGTH_LONG).show();
                    }
                }

        )
        {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put("username",login);
                params.put("password",password);

                return params;
            }
        };
        queue.add(getRequest);

    }
}