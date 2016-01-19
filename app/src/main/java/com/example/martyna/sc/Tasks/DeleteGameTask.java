package com.example.martyna.sc.Tasks;

/**
 * Created by Martyna on 2016-01-13.
 */

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.martyna.sc.Utilities.ServerUrl;
import com.example.martyna.sc.Utilities.SessionManager;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;


public class DeleteGameTask extends AsyncTask<String, Void, String> {

    private final Context mContext;
    SessionManager sessionManager;

    public DeleteGameTask ( Context context) {
        this.mContext = context;
        sessionManager = new SessionManager(mContext);
    }

    protected String doInBackground(String... urls) {
        try {
            HttpClient hc = new DefaultHttpClient();
            HttpDelete httpDelete = new HttpDelete(ServerUrl.BASE_URL+"mobile/games/mygames/delete/"+urls[0]);

            try {
                HttpResponse response = hc.execute(httpDelete);
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

