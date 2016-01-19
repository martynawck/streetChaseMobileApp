package com.example.martyna.sc.Models;

import java.util.Date;

/**
 * Created by Martyna on 2016-01-19.
 */
public class Subscription {

    private int id;
    private int game;
    private int user;
    private boolean played;
    private Date game_started;
    private Date game_finished;

    public Date getGame_finished() {
        return game_finished;
    }

    public void setGame_finished(Date game_finished) {
        this.game_finished = game_finished;
    }

    public Date getGame_started() {
        return game_started;
    }

    public void setGame_started(Date game_started) {
        this.game_started = game_started;
    }

    public boolean isPlayed() {
        return played;
    }

    public void setPlayed(boolean played) {
        this.played = played;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public int getGame() {
        return game;
    }

    public void setGame(int game) {
        this.game = game;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
