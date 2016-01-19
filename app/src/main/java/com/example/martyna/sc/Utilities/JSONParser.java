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
/*
    public static FunMapPlace JSONToFunMapPlace(JSONObject o) {
        try {
            FunMapPlace place = new FunMapPlace();
            if (o.has("id"))
                place.setId(o.getString("id"));
             else
                place.setId("");
            place.setName(o.getString("name"));
            place.setDescription(o.getString("description"));
            place.setCoordinate(new LatLng(Double.parseDouble(o.getString("x_coordinate")),
                    Double.parseDouble(o.getString("y_coordinate"))));
            return place;
        } catch (Exception e){
            return  null;
        }
    }

    public static FunMapCategory JSONToFunMapCategory (JSONObject o) {
        try {
            FunMapCategory category = new FunMapCategory();
            category.setId(o.getString("id"));
            category.setName(o.getString("name"));
            return category;
        } catch (Exception e){
            return  null;
        }
    }

    public static Buddy JSONToBuddy (JSONObject o) {
        try {
            Buddy buddy = new Buddy();
            buddy.setFirstname(o.getString("firstname"));
            buddy.setLastname(o.getString("lastname"));
            buddy.setEmail(o.getString("email"));
            buddy.setSkype(o.getString("skype_id"));
            buddy.setFacebook(o.getString("facebook_id"));
            buddy.setPhone(o.getString("phone_number"));
            buddy.setWhatsapp(o.getString("whatsapp_id"));
            return  buddy;
        } catch (Exception e){
            return  null;
        }
    }

    public static ArrayList<String> JSONToStudentDetails (JSONObject o) {
        try {
            ArrayList<String> details = new ArrayList<>();
            details.add(o.getString("email"));
            details.add(o.getString("phone_number"));
            details.add(o.getString("skype_id"));
            details.add(o.getString("facebook_id"));
            details.add(o.getString("whatsapp_id"));
            return  details;
        } catch (Exception e){
            return  null;
        }
    }

    public static Student JSONToStudent (JSONObject o) {
        try {
            Student student = new Student();
            String faculty = o.getString("faculty");
            String longFaculty = new FacultyNames().returnLongName(faculty);
            student.setFaculty(longFaculty);
            student.setFirstname(o.getString("first_name"));
            student.setLastname(o.getString("last_name"));
            student.setImgUrl(o.getString("image"));
            student.setId(o.getString("id"));
            return student;
        } catch (Exception e){
            return  null;
        }
    }

    public static TodoTask JSONToToDo (JSONObject o) {
        try {
            TodoTask todoTask = new TodoTask();
            todoTask.setValue(o.getInt("value"));
            todoTask.setType(o.getInt("type"));
            todoTask.setDescription(o.getString("task_name"));
            todoTask.setTodo_id(o.getInt("id"));
            return todoTask;
        } catch (Exception e){
            return  null;
        }
    }

    public static Event JSONToEvent (JSONObject o) {
        try {
            Event e = new Event();
            e.setId(o.getString("id"));
            e.setName(o.getString("name"));

            if (o.has("place")) {
                JSONObject placeObject = o.getJSONObject("place");
                e.setPlace(placeObject.getString("name"));
                if (placeObject.has("city")) {
                    JSONObject locationObject = placeObject.getJSONObject("location");
                    e.setWhere(locationObject.getString("city"));
                } else {
                    e.setWhere("");
                }
            } else {
                e.setPlace("");
                e.setWhere("");
            }

            if (o.has("start_time")) {
                String stringStartTime = o.getString("start_time");
                SimpleDateFormat incomingFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
                Date date;
                try {
                    date = incomingFormat.parse(stringStartTime);

                } catch (Exception ex ) {
                    SimpleDateFormat incomingFormat2 = new SimpleDateFormat("yyyy-MM-dd");
                    date = incomingFormat2.parse(stringStartTime);
                }
                e.setStartTime(date);
            }

            if (o.has("owner")) {
                JSONObject ownerObject = o.getJSONObject("owner");
                e.setOwner(ownerObject.getString("name"));
                e.setOwnerId(ownerObject.getString("id"));
            } else {
                e.setOwner("");
            }

            if (o.has("cover")) {
                JSONObject coverObject = o.getJSONObject("cover");
                e.setImageUrl(coverObject.getString("source"));
            } else {
                e.setImageUrl("");
            }
                return e;
        } catch (Exception e){
            return  null;
        }
    }*/
}