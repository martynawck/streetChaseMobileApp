package com.example.martyna.sc.Tasks;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.martyna.sc.Activities.MainActivity;
import com.example.martyna.sc.JSONParser;
import com.example.martyna.sc.Models.ServerUrl;
import com.example.martyna.sc.Models.SessionManager;
import com.example.martyna.sc.Models.StreetGame;
import com.example.martyna.sc.OnEventTaskCompleted;
import com.example.martyna.sc.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Martyna on 2016-01-13.
 */

public class SubscribeToGameTask extends AsyncTask<String, Void, String> {

    private final Context mContext;
    private ArrayList<StreetGame> streetGames;
    SessionManager sessionManager;

    public SubscribeToGameTask ( Context context) {
        this.mContext = context;
        sessionManager = new SessionManager(mContext);
    }


    protected String doInBackground(String... urls) {
        try {
            HttpClient httpclient = new DefaultHttpClient();
            // specify the URL you want to post to
            HttpPost httppost = new HttpPost(ServerUrl.BASE_URL+"mobile/games/subscription/"+sessionManager.getValueOfUserId()+"/"+urls[0]);
            try {
                // create a list to store HTTP variables and their values
               // List nameValuePairs = new ArrayList();
                // add an HTTP variable and value pair
               // nameValuePairs.add(new BasicNameValuePair("myHttpData", valueIwantToSend));
               // httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                // send the variable and value, in other words post, to the URL
                HttpResponse response = httpclient.execute(httppost);
                Log.d(":A", response.getStatusLine().toString());

            } catch (ClientProtocolException e) {
                // process execption
            } catch (IOException e) {
                // process execption
            }

        } catch (Exception e) {
            System.out.println("Exception : " + e.getMessage());
        }
        return "-1";
    }

    protected void onPostExecute(String result) {
      }
}

