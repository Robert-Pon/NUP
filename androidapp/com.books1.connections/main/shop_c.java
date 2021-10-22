package com.books1.connections.main;

import com.books1.books.urls;
import com.books1.connections.connection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class shop_c {
    urls urls = new urls();
    public List<JSONObject> get_products(String data){
        List<JSONObject> json_objects = new ArrayList<>();
        connection connection = new connection();
        JSONArray jsonArray = connection.connection(urls.ip(), urls.get_products(), data);
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                json_objects.add(jsonArray.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return json_objects;
    }
    public List<JSONObject> get_more_products(String data){
        List<JSONObject> json_objects = new ArrayList<>();
        connection connection = new connection();
        JSONArray jsonArray = connection.connection(urls.ip(), urls.get_more_products(), data);
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                json_objects.add(jsonArray.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return json_objects;
    }

    public List<JSONObject> search(String data){
        List<JSONObject> json_objects = new ArrayList<>();
        connection connection = new connection();
        JSONArray jsonArray = connection.connection(urls.ip(), urls.search(), data);
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                json_objects.add(jsonArray.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return json_objects;
    }

public List<JSONObject> search_profiles(String data){
        List<JSONObject> json_objects = new ArrayList<>();
        connection connection = new connection();
        JSONArray jsonArray = connection.connection(urls.ip(), urls.search_profiles(), data);
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                json_objects.add(jsonArray.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return json_objects;
    }



    public String buy(String data){
        JSONObject json_object = new JSONObject();
        String message = "";
        connection connection = new connection();
        try {
            json_object = connection.connection(urls.ip(), urls.buy_book(), data).getJSONObject(0);
            message = json_object.getString("type");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return message;
    }
 public String exchange(String data){
        JSONObject json_object = new JSONObject();
        String message = "";
        connection connection = new connection();
        try {
            json_object = connection.connection(urls.ip(), urls.exchange_book(), data).getJSONObject(0);
            message = json_object.getString("type");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return message;
    }

 public List<JSONObject> get_products_from_map(String data){
     List<JSONObject> json_objects = new ArrayList<>();
     connection connection = new connection();
     JSONArray jsonArray = connection.connection(urls.ip(), urls.products_from_map_selector(), data);
     for (int i = 0; i < jsonArray.length(); i++) {
         try {
             json_objects.add(jsonArray.getJSONObject(i));
         } catch (JSONException e) {
             e.printStackTrace();
         }
     }
     return json_objects;
    }


}
