package com.books1.connections.main;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;

import com.books1.books.urls;
import com.books1.connections.connection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class profile_c {
    urls urls = new urls();
    public JSONObject get_profile_data(String data){
        JSONObject message = new JSONObject();
        connection connection = new connection();
        JSONArray jsonArray = connection.connection(urls.ip(), urls.get_profile_data(), data);
        try {
            message = jsonArray.getJSONObject(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return message;
    }
     public JSONObject get_profile_data_to_product(String data){
            JSONObject message = new JSONObject();
            connection connection = new connection();
            JSONArray jsonArray = connection.connection(urls.ip(), urls.get_profile_data_to_product(), data);
            try {
                message = jsonArray.getJSONObject(0);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return message;
        }

    public List<JSONObject> get_my_products(String data){
        List<JSONObject> json_objects = new ArrayList<>();
        connection connection = new connection();
        JSONArray jsonArray = connection.connection(urls.ip(), urls.get_my_products(), data);
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                json_objects.add(jsonArray.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return json_objects;
    }

    public String see_notifications(String data){
        JSONObject json_object = new JSONObject();
        String message = "";
        connection connection = new connection();
        try {
            json_object = connection.connection(urls.ip(), urls.see_notifications(), data).getJSONObject(0);
            message = json_object.getString("type");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return message;
    }
    public List<JSONObject> get_notifications(String data){
        List<JSONObject> json_objects = new ArrayList<>();
        connection connection = new connection();
        JSONArray jsonArray = connection.connection(urls.ip(), urls.get_notifications(), data);
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                json_objects.add(jsonArray.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return json_objects;
    }

    public List<JSONObject> get_my_shop(String data){
        List<JSONObject> json_objects = new ArrayList<>();
        connection connection = new connection();
        JSONArray jsonArray = connection.connection(urls.ip(), urls.get_my_shop(), data);
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                json_objects.add(jsonArray.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return json_objects;
    }



    public String accept_sold(String data){
        JSONObject json_object = new JSONObject();
        String message = "er";
        connection connection = new connection();
        try {
            json_object = connection.connection(urls.ip(), urls.accept_sold(), data).getJSONObject(0);
            if(json_object.getString("type").equals("101")) {
                message = json_object.getString("nups");
            }else{
                message = "er";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return message;
    }

    public String cancel_sold(String data){
        JSONObject json_object = new JSONObject();
        String message = "er";
        connection connection = new connection();
        try {
            json_object = connection.connection(urls.ip(), urls.cancel_sold(), data).getJSONObject(0);
            if(json_object.getString("type").equals("101")) {
                message = json_object.getString("nups");
            }else{
                message = "er";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return message;
    }

    public JSONObject profile_setter(String data, StringBuilder school_data, String name, String login, String password, Context context){
        connection connection = new connection();
        JSONObject message = new JSONObject();
        JSONArray jsonArray = connection.connection(urls.ip(), urls.profile_setter(), data);
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

    public String password_changer(String data){
        JSONObject json_object = new JSONObject();
        String message = "er";
        connection connection = new connection();
        try {
            json_object = connection.connection(urls.ip(), urls.password_changer(), data).getJSONObject(0);
            if(json_object.getString("type").equals("101")) {
                message = json_object.getString("nups");
            }else{
                message = "er";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return message;
    }
    public JSONObject get_school(String data){
        JSONObject json_object = new JSONObject();
        String message = "er";
        connection connection = new connection();
        try {
            json_object = connection.connection(urls.ip(), urls.get_school(), data).getJSONObject(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json_object;
    }

    public String upload_named_compress_photo(Uri uri, Context context, String name, List<String> names, List<String> values){
        String message = "-";
        connection connection = new connection();
        urls urls = new urls();
        try {
            JSONObject message_json = connection.upload_compress_image(urls.ip(), urls.upload_named_compress_image_profile(), uri, name, context,  names, values).getJSONObject(0);
            message = message_json.getString("type");
        } catch (Exception e) {

            message = "101";

        }

        return message;
    }

    public String upload_named_photo(Uri uri, Context context, String name, List<String> names, List<String> values){
        String message = "-";
        connection connection = new connection();
        urls urls = new urls();
        try {
            JSONObject message_json = connection.upload_image(urls.ip(), urls.upload_named_image_profile(), uri, name, context,  names, values).getJSONObject(0);
            message = message_json.getString("type");
        } catch (Exception e) {

            message = "101";

        }

        return message;
    }

}
