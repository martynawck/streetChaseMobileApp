package com.example.martyna.sc.Tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;
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
public class GetControlPointTask extends AsyncTask<String, Void, String> {

    private final OnControlPointTaskCompleted listener;
    private final Context mContext;
    ControlPoint controlPoint;
    SessionManager sessionManager;

    public GetControlPointTask(Context context, OnControlPointTaskCompleted listener) {
        this.listener = listener;
        this.mContext = context;
        sessionManager = new SessionManager(mContext);
    }


    protected String doInBackground(String... urls) {
        try {
            String uri = "mobile/control_point/"+urls[0];
            HttpParams httpParameters = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters, 5000);
            HttpConnectionParams.setSoTimeout(httpParameters, 10000);
            HttpClient httpclient = new DefaultHttpClient(httpParameters);
            HttpGet httpget = new HttpGet(ServerUrl.BASE_URL + uri);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            final String response = httpclient.execute(httpget, responseHandler);

            if (!response.equalsIgnoreCase("null")) {
                JSONObject jsonObject = new JSONObject(response);
                controlPoint = JSONParser.JSONToControlPoint(jsonObject);

                return "0";
            }
        } catch (Exception e) {
            System.out.println("Exception : " + e.getMessage());
        }
        return "-1";
    }

    protected void onPostExecute(String result) {

        if (result.equals("0")) {
            listener.onControlPointReturned(controlPoint);
        } else {
            Toast.makeText(mContext, mContext.getString(R.string.mygames_error), Toast.LENGTH_LONG).show();
        }
    }
}