package com.example.martyna.sc.Tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.martyna.sc.JSONParser;
import com.example.martyna.sc.Models.Question;
import com.example.martyna.sc.Models.ServerUrl;
import com.example.martyna.sc.Models.SessionManager;
import com.example.martyna.sc.OnAnswerTaskCompleted;
import com.example.martyna.sc.OnQuestionTaskCompleted;
import com.example.martyna.sc.R;

import org.apache.http.HttpResponse;
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
public class GetAnswerTask extends AsyncTask<String, Void, String> {

    private final OnAnswerTaskCompleted listener;
    private final Context mContext;
    SessionManager sessionManager;
    Question question;

    public GetAnswerTask(Context context, OnAnswerTaskCompleted listener) {
        this.listener = listener;
        this.mContext = context;
        sessionManager = new SessionManager(mContext);
    }


    protected String doInBackground(String... urls) {
        try {
            String uri = "mobile/question/answer/"+urls[0]+"/"+urls[1];
            HttpParams httpParameters = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters, 5000);
            HttpConnectionParams.setSoTimeout(httpParameters, 10000);
            HttpClient httpclient = new DefaultHttpClient(httpParameters);
            HttpGet httpget = new HttpGet(ServerUrl.BASE_URL + uri);
            HttpResponse response = httpclient.execute(httpget);

            if (response.getStatusLine().getStatusCode() == 200) {
                return "0";
            } else {
                return "1";
            }
        } catch (Exception e) {
            System.out.println("Exception : " + e.getMessage());
        }
        return "-1";
    }

    protected void onPostExecute(String result) {

        listener.onAnswerReturned(result);
    }
}
