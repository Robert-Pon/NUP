package com.books1.connections.main;

import com.books1.books.urls;
import com.books1.connections.connection;

import org.json.JSONException;
import org.json.JSONObject;

public class eco_c {
    urls urls = new urls();

    public JSONObject get_eco(String data){
        JSONObject message = new JSONObject();
        connection connection = new connection();
        urls urls = new urls();
        try {
            message = connection.connection(urls.ip(), urls.get_eco(), data).getJSONObject(0);

        } catch (JSONException e) {

        }

        return message;
    }
}
