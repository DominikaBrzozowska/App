package com.example.one.tas;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class JSONHelper {
    public static final JSONObject initiateJSONUserObject(User user){
        JSONObject JSONUser = new JSONObject();
        try {
        JSONUser.put("name", user.getName());
        JSONUser.put("surname", user.getSurname());
        JSONUser.put("email", user.getEmail());
        JSONUser.put("password", user.getPassword());
        JSONUser.put("address", user.getAddress());
        JSONUser.put("city", user.getCity());
        JSONUser.put("postal_code", user.getPostal_code());
        JSONUser.put("avatar", user.getAvatar());


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return JSONUser;
    }

    public static final JSONObject initiateJSONEmailObject(String email){
        JSONObject JSONEmail = new JSONObject();
        try {
            JSONEmail.put("email",email);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return JSONEmail;
    }

    public static final JSONObject initiateJSONLoginObject(String name, String password){
        JSONObject JSONLogin = new JSONObject();
        try {
            JSONLogin.put("email", name);
            JSONLogin.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return JSONLogin;
    }

    public static final JSONObject initiateJSONOrderObject(String userId, List<String> books){
        JSONObject JSONOrder = new JSONObject();
        try {
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            SimpleDateFormat year = new SimpleDateFormat("yyyy-MM-dd");
            Calendar cal = Calendar.getInstance();
            JSONOrder.put("user_id", userId);
            JSONOrder.put("books", books);
            JSONOrder.put("order_date", year.format(cal.getTime())+"T"+sdf.format(cal.getTime())+"+0100");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return JSONOrder;
    }

    public static final JSONObject JSONvotes(String votes, String rating){
        JSONObject JSONv = new JSONObject();
        try {
            JSONv.put("rating", rating);
            JSONv.put("votes", votes);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return JSONv;
    }
}
