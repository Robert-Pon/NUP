package com.books1.connections.main;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.books1.connections.connection;
import com.books1.books.urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class add_book_c {
    public JSONObject add_book(String data){
        JSONObject message= new JSONObject();
        connection connection = new connection();
        urls urls = new urls();
        try {
            JSONObject message_json = connection.connection(urls.ip(), urls.add_book(), data).getJSONObject(0);
            message = message_json;
        } catch (JSONException e) {
            try {
                message.put("type", "102");
            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
            }
        }

        return message;
    }

    public Bitmap get_photo(String data){
        Bitmap bitmap = null;
        connection connection = new connection();
        urls urls = new urls();
        try {
            bitmap = BitmapFactory.decodeStream(connection.connection_stream(urls.ip(), urls.get_photo(), data));
        } catch (Exception e) {

        }

        return bitmap;
    }

    public String upload_photo(Uri uri, Context context, String data, List<String> names, List<String> values){
        String message = "-";
        connection connection = new connection();
        urls urls = new urls();
        try {
            JSONObject message_json = connection.upload_image(urls.ip(), urls.upload_image(), uri, "name.jpg", context,  names, values).getJSONObject(0);
            message = message_json.getString("type");
        } catch (Exception e) {

                message = "101";

        }

        return message;
    }
    public String upload_compress_photo(Uri uri, Context context, String data, List<String> names, List<String> values){
        String message = "-";
        connection connection = new connection();
        urls urls = new urls();
        try {
            JSONObject message_json = connection.upload_compress_image(urls.ip(), urls.upload_compress_image(), uri, "name.jpg", context,  names, values).getJSONObject(0);
            message = message_json.getString("type");
        } catch (Exception e) {

                message = "101";

        }

        return message;
    }
    public String upload_named_compress_photo(Uri uri, Context context, String name, List<String> names, List<String> values){
            String message = "-";
            connection connection = new connection();
            urls urls = new urls();
            try {
                JSONObject message_json = connection.upload_compress_image(urls.ip(), urls.upload_named_compress_image(), uri, name, context,  names, values).getJSONObject(0);
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
            JSONObject message_json = connection.upload_image(urls.ip(), urls.upload_named_image(), uri, name, context,  names, values).getJSONObject(0);
            message = message_json.getString("type");
        } catch (Exception e) {

            message = "101";

        }

        return message;
    }

    public List<JSONObject> get_tag(String data){
        List<JSONObject> json_objects = new ArrayList<>();
        connection connection = new connection();
        urls urls = new urls();
        JSONArray jsonArray = connection.connection(urls.ip(), urls.get_tag(), data);
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                json_objects.add(jsonArray.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return json_objects;
    }
    public List<JSONObject> search_tag(String data){
        List<JSONObject> json_objects = new ArrayList<>();
        connection connection = new connection();
        urls urls = new urls();
        JSONArray jsonArray = connection.connection(urls.ip(), urls.search_tag(), data);
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                json_objects.add(jsonArray.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return json_objects;
    }
    public JSONObject create_tag(String data){
        JSONObject jsonObject = new JSONObject();
        connection connection = new connection();
        urls urls = new urls();
        JSONArray jsonArray = connection.connection(urls.ip(), urls.create_tag(), data);
        try {
            jsonObject = jsonArray.getJSONObject(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

}
