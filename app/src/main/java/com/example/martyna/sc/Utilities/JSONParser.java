package com.example.martyna.sc.Utilities;

/**
 * Created by Martyna on 2015-04-19.
 */

import android.util.Log;

import com.example.martyna.sc.Models.ControlPoint;
import com.example.martyna.sc.Models.Question;
import com.example.martyna.sc.Models.StreetGame;
import com.example.martyna.sc.Models.Subscription;

import org.json.JSONObject;
import java.util.Date;

public class JSONParser {

    public static StreetGame JSONToPartner(JSONObject o) {

        try {

            StreetGame streetGame = new StreetGame();
            streetGame.setId(o.getString("id"));
            streetGame.setGameName(o.getString("name"));
            streetGame.setDescription(o.getString("description"));
            streetGame.setOwnerId(o.getString("creatorId"));
            streetGame.setStartPointDescription(o.getString("start_point_description"));
            if (o.getString("start_time") == null)
                streetGame.setStartTime(new Date());
            else
                streetGame.setStartTime(new Date(Long.parseLong(o.getString("start_time"))));
            if (o.getString("end_time") == null)
                streetGame.setEndTime(new Date());
            else
                streetGame.setEndTime(new Date(Long.parseLong(o.getString("end_time"))));

            return  streetGame;

        } catch (Exception e) {
            Log.d("Error",e.getStackTrace().toString());
            return null;
        }
    }

    public static ControlPoint JSONToControlPoint(JSONObject o) {

        try {

            ControlPoint controlPoint = new ControlPoint();
            controlPoint.setId(o.getInt("id"));
            controlPoint.setName(o.getString("name"));
            controlPoint.setNext_point_id(o.getInt("next_point_id"));
            String location = o.getString("location");
            String plainLocation = location.substring(7, location.length() - 1);
            String[] parts = plainLocation.split(" ");
            controlPoint.setLatitude(Double.parseDouble(parts[1]));
            controlPoint.setLongitude(Double.parseDouble(parts[0]));
            if (o.has("hint"))
                controlPoint.setHint(o.getString("hint"));
            else
                controlPoint.setHint("");
            return  controlPoint;

        } catch (Exception e) {
            Log.d("Error", e.getStackTrace().toString());
            return null;
        }
    }

    public static Question JSONToQuestion(JSONObject o) {

        try {

            Question question = new Question();
            question.setId(o.getInt("id"));
            question.setAnswer(o.getString("answer"));
            question.setQuestion(o.getString("question"));
            question.setControlPoint(o.getInt("control_point_id"));

            return  question;

        } catch (Exception e) {
            Log.d("Error",e.getStackTrace().toString());
            return null;
        }
    }

    public static Subscription JSONToSubscription(JSONObject o) {
        try {

            Subscription subscription = new Subscription();
            subscription.setId(o.getInt("id"));
            subscription.setGame(o.getInt("game"));
            subscription.setPlayed(o.getBoolean("played"));
            subscription.setUser(o.getInt("user"));
            if (o.has("game_started"))
                subscription.setGame_started(new Date(Long.parseLong(o.getString("game_started"))));
            else
                subscription.setGame_started(new Date());
            if (o.has("game_finished"))
                subscription.setGame_finished(new Date(Long.parseLong(o.getString("game_finished"))));
            else
                subscription.setGame_finished(new Date());

            return  subscription;

        } catch (Exception e) {
            Log.d("Error",e.getStackTrace().toString());
            return null;
        }
    }
}