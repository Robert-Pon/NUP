package com.books1.connections.main;

import com.books1.books.urls;
import com.books1.connections.connection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class book_c {
    public JSONObject get_book_by_id(String data){
        JSONObject message = new JSONObject();
        connection connection = new connection();
        urls urls = new urls();
        try {
            message = connection.connection(urls.ip(), urls.get_book_by_id(), data).getJSONObject(0);

        } catch (JSONException e) {

        }

        return message;
    }

    public String edit_book(String data){
        String message="102";
        connection connection = new connection();
        urls urls = new urls();
        try {
            JSONObject message_json = connection.connection(urls.ip(), urls.edit_book(), data).getJSONObject(0);
            message = message_json.getString("type");
        } catch (JSONException e) {
            message = "100";
        }

        return message;
    }
    public String delete_book(String data){
        String message="102";
        connection connection = new connection();
        urls urls = new urls();
        try {
            JSONObject message_json = connection.connection(urls.ip(), urls.delete_book(), data).getJSONObject(0);
            message = message_json.getString("type");
        } catch (JSONException e) {
            message = "100";
        }

        return message;
    }
    public JSONObject get_localization(String data){
        JSONObject message = new JSONObject();
        connection connection = new connection();
        urls urls = new urls();
        try {
            message = connection.connection(urls.ip(), urls.get_profile_localization(), data).getJSONObject(0);

        } catch (JSONException e) {

        }

        return message;
    }
    urls urls = new urls();
    public JSONObject get_profile_data(String data){
        JSONObject message = new JSONObject();
        connection connection = new connection();
        JSONArray jsonArray = connection.connection(urls.ip(), urls.get_profile_data_1(), data);
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
        JSONArray jsonArray = connection.connection(urls.ip(), urls.get_profile_data_to_product_1(), data);
        try {
            message = jsonArray.getJSONObject(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return message;
    }

    public List<JSONObject> get_products(String data){

        List<JSONObject> json_objects = new ArrayList<>();
        connection connection = new connection();
        JSONArray jsonArray = connection.connection(urls.ip(), urls.get_profile_products(), data);
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
