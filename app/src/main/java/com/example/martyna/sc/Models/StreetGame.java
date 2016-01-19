package com.example.martyna.sc.Models;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Martyna on 2015-05-01.
 */
public class StreetGame implements  Comparable<StreetGame> {

    private Date endTime;
    private String ownerId;
    private String imageUrl;
    private String id;
    private String gameName;
    private String description;
    private String placeName;
    private Date startTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String place) {
        this.placeName = place;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String name) {
        this.gameName = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStartPointDescription() {
        return startPointDescription;
    }

    public void setStartPointDescription(String startPointDescription) {
        this.startPointDescription = startPointDescription;
    }

    private String startPointDescription;

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @Override
    public int compareTo(StreetGame o) {
        if (this.getStartTime() == null || o.getStartTime() == null)
            return 0;
        return getStartTime().compareTo(o.getStartTime());
    }
}
