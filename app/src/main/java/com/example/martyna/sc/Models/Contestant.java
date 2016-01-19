package com.example.martyna.sc.Models;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Martyna on 2016-01-10.
 */
public class Contestant {

    private String id;
    private String nickname;

    public void setNickname ( String nickname) { this.nickname = nickname; }
    public void setId (String id) { this.id = id; }
    public String getNickname () { return this.nickname; }
    public String getId () { return this.id; }

}
