package com.example.martyna.sc.Tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.martyna.sc.JSONParser;
import com.example.martyna.sc.Models.ServerUrl;
import com.example.martyna.sc.Models.SessionManager;
import com.example.martyna.sc.Models.StreetGame;
import com.example.martyna.sc.OnEventTaskCompleted;
import com.example.martyna.sc.R;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Martyna on 2016-01-12.
 */

public class GetAllGamesFromServerTask extends AsyncTask<Void, Void, String> {

    private final OnEventTaskCompleted listener;
    private final Context mContext;
    private ArrayList<StreetGame> streetGames;
    SessionManager sessionManager;

    public GetAllGamesFromServerTask(Context context, OnEventTaskCompleted listener) {
        this.listener = listener;
        this.mContext = context;
        sessionManager = new SessionManager(mContext);
    }


    protected String doInBackground(Void... urls) {
        try {
            String uri = "mobile/games/othergames/"+sessionManager.getValueOfUserId();
            streetGames = new ArrayList<>();
            HttpParams httpParameters = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters, 5000);
            HttpConnectionParams.setSoTimeout(httpParameters, 10000);
            HttpClient httpclient = new DefaultHttpClient(httpParameters);
            HttpGet httpget = new HttpGet(ServerUrl.BASE_URL + uri);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            final String response = httpclient.execute(httpget, responseHandler);

            if (!response.equalsIgnoreCase("null")) {
                streetGames = new ArrayList<>();
                JSONArray jsonArray = new JSONArray(response);
                JSONObject jsonObject;

                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    StreetGame e = JSONParser.JSONToPartner(jsonObject);//.JSONToEvent(jsonObject);
                    streetGames.add(e);
                }

                Collections.sort(streetGames, new Comparator<StreetGame>() {
                    public int compare(StreetGame o1, StreetGame o2) {
                        if (o1.getStartTime() == null || o2.getStartTime() == null)
                            return 0;
                        return o1.getStartTime().compareTo(o2.getStartTime());
                    }
                });
                return "0";
            }
        } catch (Exception e) {
            System.out.println("Exception : " + e.getMessage());
        }
        return "-1";
    }

    protected void onPostExecute(String result) {

        if (result.equals("0")) {
            listener.onTaskCompleted(streetGames);
        } else {
            Toast.makeText(mContext, mContext.getString(R.string.mygames_error), Toast.LENGTH_LONG).show();
        }
    }
}

