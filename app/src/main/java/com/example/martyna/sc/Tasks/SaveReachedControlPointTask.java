package com.example.martyna.sc.Tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.martyna.sc.Models.ServerUrl;
import com.example.martyna.sc.Models.SessionManager;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;

/**
 * Created by Martyna on 2016-01-19.
 */
public class SaveReachedControlPointTask extends AsyncTask<String, Void, String> {

    private final Context mContext;
    SessionManager sessionManager;

    public SaveReachedControlPointTask ( Context context) {
        this.mContext = context;
        sessionManager = new SessionManager(mContext);
    }


    protected String doInBackground(String... urls) {
        try {
            HttpClient httpclient = new DefaultHttpClient();
            // specify the URL you want to post to
            HttpPost httppost = new HttpPost(ServerUrl.BASE_URL+"mobile/user_reached_point/"+sessionManager.getValueOfUserId()+"/"+urls[0]+"/"+urls[1]);
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