package com.example.one.tas;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class CurrentUser {
    private static User user = new User();

    public static void set(User logedIn){
        user=logedIn;
    }

    public static void set(String logedIn){
        try {
            JSONObject JSONUser= new JSONObject(logedIn);
            user=new User(JSONUser.getString("id"),JSONUser.getString("name"),JSONUser.getString("surname"),JSONUser.getString("email"),"password",JSONUser.getString("address"),JSONUser.getString("city"),JSONUser.getString("postal_code"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public static boolean isLogedIn(){
        if (user.getEmail().equals("guest"))
        return false;
        else
        return true;
    }

    public static User get(){
        return user;
    }
}
