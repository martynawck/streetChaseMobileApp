package com.example.martyna.sc.Tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.martyna.sc.Utilities.ServerUrl;
import com.example.martyna.sc.Utilities.SessionManager;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Martyna on 2016-01-18.
 */

public class SendUserLocationTask extends AsyncTask<String, Void, String> {

    private final Context mContext;
    SessionManager sessionManager;

    public SendUserLocationTask ( Context context) {
        this.mContext = context;
        sessionManager = new SessionManager(mContext);
    }


    protected String doInBackground(String... urls) {
        try {
            HttpClient httpclient = new DefaultHttpClient();
            // specify the URL you want to post to
            HttpPost httppost = new HttpPost(ServerUrl.BASE_URL+"mobile/user_location");
            try {
                // create a list to store HTTP variables and their values
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
                // add an HTTP variable and value pair
                nameValuePairs.add(new BasicNameValuePair("id", sessionManager.getValueOfUserId()));
                nameValuePairs.add(new BasicNameValuePair("timestamp", urls[0]));
                nameValuePairs.add(new BasicNameValuePair("game", urls[1]));
                nameValuePairs.add(new BasicNameValuePair("y", urls[2]));
                nameValuePairs.add(new BasicNameValuePair("x", urls[3]));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
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
