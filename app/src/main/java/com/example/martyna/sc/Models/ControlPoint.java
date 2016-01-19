package com.example.martyna.sc.Models;

import android.content.Intent;

/**
 * Created by Martyna on 2016-01-18.
 */
public class ControlPoint {


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStreet_game_id() {
        return street_game_id;
    }

    public void setStreet_game_id(int street_game_id) {
        this.street_game_id = street_game_id;
    }

    public int getNext_point_id() {
        return next_point_id;
    }

    public void setNext_point_id(int next_point_id) {
        this.next_point_id = next_point_id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public boolean isStarting_point() {
        return starting_point;
    }

    public void setStarting_point(boolean starting_point) {
        this.starting_point = starting_point;
    }


    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    private String name;
    private int next_point_id;
    private double latitude;
    private double longitude;
    private boolean starting_point;
    private int id;
    private int street_game_id;
    private String hint;

    public void setInitialControlPoint(Intent intent) {
        //ControlPoint controlPoint = new ControlPoint();
       // intent = getIntent(); // gets the previously created intent
        //gameId = intent.getStringExtra("game_id");
        this.setLongitude(intent.getDoubleExtra("control_point_longitude", 0));
        this.setLatitude(intent.getDoubleExtra("control_point_latitude", 0));
        this.setName(intent.getStringExtra("control_point_name"));
        this.setNext_point_id(intent.getIntExtra("control_point_next", 0));
        this.setStreet_game_id(Integer.parseInt(intent.getStringExtra("game_id")));
        this.setId(intent.getIntExtra("id", 0));
        //return controlPoint;
    }

}
