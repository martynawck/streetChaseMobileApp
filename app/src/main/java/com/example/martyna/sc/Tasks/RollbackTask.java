package com.example.martyna.sc.Tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.martyna.sc.Models.ServerUrl;
import com.example.martyna.sc.Models.SessionManager;
import com.example.martyna.sc.OnRollbackTaskCompleted;
import com.example.martyna.sc.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;

/**
 * Created by Martyna on 2016-01-19.
 */
public class RollbackTask extends AsyncTask<String, Void, String> {

    private final Context mContext;
    SessionManager sessionManager;
    OnRollbackTaskCompleted listener;

    public RollbackTask ( Context context, OnRollbackTaskCompleted listener) {
        this.mContext = context;
        sessionManager = new SessionManager(mContext);
        this.listener = listener;
    }


    protected String doInBackground(String... urls) {
        try {
            HttpClient hc = new DefaultHttpClient();
            HttpDelete httpDelete = new HttpDelete(ServerUrl.BASE_URL+"mobile/games/subscription/played/rollback/"+sessionManager.getValueOfUserId()+"/"+urls[0]);

            try {
                // create a list to store HTTP variables and their values
                // List nameValuePairs = new ArrayList();
                // add an HTTP variable and value pair
                // nameValuePairs.add(new BasicNameValuePair("myHttpData", valueIwantToSend));
                // httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                // send the variable and value, in other words post, to the URL
                HttpResponse response = hc.execute(httpDelete);
                Log.d(":A", response.getStatusLine().toString());

                if (response.getStatusLine().getStatusCode() == 204)
                    return "0";
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
        if (result.equals("0")) {
            listener.onRollback();
        } else {
            Toast.makeText(mContext, mContext.getString(R.string.cannot_rollback), Toast.LENGTH_LONG).show();
        }
    }
}

