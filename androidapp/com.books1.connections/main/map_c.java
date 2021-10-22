package com.books1.connections.main;

import com.books1.books.urls;
import com.books1.connections.connection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class map_c {
    public List<JSONObject> get_map(String data){
        List<JSONObject> json_objects = new ArrayList<>();
        connection connection = new connection();
        urls urls = new urls();
        JSONArray jsonArray = connection.connection(urls.ip(), urls.map_selector(), data);
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
