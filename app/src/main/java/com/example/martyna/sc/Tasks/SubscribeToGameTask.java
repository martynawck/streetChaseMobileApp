package com.example.martyna.sc.Tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import com.example.martyna.sc.Utilities.ServerUrl;
import com.example.martyna.sc.Utilities.SessionManager;
import com.example.martyna.sc.Models.StreetGame;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import java.io.IOException;
import java.util.ArrayList;

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
            HttpPost httppost = new HttpPost(ServerUrl.BASE_URL+"mobile/games/subscription/"+sessionManager.getValueOfUserId()+"/"+urls[0]);
            try {
                HttpResponse response = httpclient.execute(httppost);

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

