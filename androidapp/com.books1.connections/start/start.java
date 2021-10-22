package com.books1.connections.start;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.books1.connections.connection;
import com.books1.books.urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class start {
    urls urls = new urls();
    public JSONObject register(String data, StringBuilder school_data, String name, String login, String password, Context context){
        connection connection = new connection();
        JSONObject message = new JSONObject();
        JSONArray jsonArray = connection.connection(urls.ip(), urls.register(), data);
        try {
            Geocoder geocoder = new Geocoder(context);
            List<Address> addresses = geocoder.getFromLocationName(name, 1);
            school_data.append("&x="+String.valueOf(addresses.get(0).getLatitude()));
            school_data.append("&y="+String.valueOf(addresses.get(0).getLongitude()));
            connection.connection(urls.ip(), urls.add_school(), school_data.toString());


        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            message = jsonArray.getJSONObject(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return message;
    }

    public JSONObject login(String data){
        connection connection = new connection();
        JSONObject message = new JSONObject();
        JSONArray jsonArray = connection.connection(urls.ip(), urls.login(), data);
        try {
            message = jsonArray.getJSONObject(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return message;
    }

    public JSONObject login_checking(String data){
        connection connection = new connection();
        JSONObject message = new JSONObject();
        JSONArray jsonArray = connection.connection(urls.ip(), urls.login_checking(), data);
        try {
            message = jsonArray.getJSONObject(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return message;
    }

    public void delete_reservation(String data){
        connection connection = new connection();
        connection.connection(urls.ip(), urls.delete_reservation(), data);
    }

    public void account_back(String data){
        connection connection = new connection();
        connection.connection(urls.ip(), urls.account_back(), data);
    }






}
