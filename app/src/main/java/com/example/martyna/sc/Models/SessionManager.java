package com.example.martyna.sc.Models;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Martyna on 2015-03-20.
 */
public class SessionManager {
    private final SharedPreferences _sharedPreferences;
    private final SharedPreferences.Editor _editor;
    private static final String SESSION_NAME = "STREET_CHASE";
    private static final String KEY_USERNAME = "LOGIN";
    private static final String KEY_PASSWORD = "PASSWORD";
    private static final String KEY_USER_ID = "USER_ID";
    private static final String KEY_NAME = "NAME";


    public SessionManager(Context context){
        Context _context = context;
        _sharedPreferences = _context.getSharedPreferences(SESSION_NAME, Context.MODE_PRIVATE);
        _editor =_sharedPreferences.edit();
    }

    public void createSession(String username, String password, Integer id) {
        _editor.putString(KEY_USERNAME,username);
        _editor.putString(KEY_PASSWORD,password);
        _editor.putString(KEY_USER_ID,id.toString());
        _editor.commit();
    }

    public String getValueOfLogin() {
        return _sharedPreferences.getString(KEY_USERNAME, "");
    }

    public String getValueOfPassword() {
        return _sharedPreferences.getString(KEY_PASSWORD, "");
    }

    public String getValueOfUserId() { return _sharedPreferences.getString(KEY_USER_ID, ""); }

    public String getValueOfFirstName() { return _sharedPreferences.getString(KEY_NAME, ""); }


    public void setValueOfFirstName(String s) {
        SharedPreferences.Editor editor = _sharedPreferences.edit();
        editor.putString(KEY_NAME,s);
        editor.apply();
    }

    public void setValueOfPassword(String s) {
        SharedPreferences.Editor editor = _sharedPreferences.edit();
        editor.putString(KEY_PASSWORD,s);
        editor.apply();
    }

    public boolean resumeSession(){
        return _sharedPreferences.contains(KEY_USERNAME) && _sharedPreferences.contains(KEY_PASSWORD);
    }

    public void destroySession() {
        _editor.clear();
        _editor.commit();
    }
}
