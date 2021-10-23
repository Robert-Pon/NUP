package com.books1.connections;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class connection {
    public JSONArray connection(String ip, String address, String data){
        JSONArray jsonArray = new JSONArray();
        try {
            URL url = new URL(ip+address);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod("POST");
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(httpURLConnection.getOutputStream());
            outputStreamWriter.write(data);
            outputStreamWriter.close();
            httpURLConnection.getResponseCode();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String i = "";
            while ((i = bufferedReader.readLine()) != null) {
                jsonArray = new JSONArray(i);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonArray;

    }
    public InputStream connection_stream(String ip, String address, String data){
        InputStream inputStream = null;
        try {
            URL url = new URL(ip+address);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod("POST");
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(httpURLConnection.getOutputStream());
            outputStreamWriter.write(data);
            outputStreamWriter.close();
            httpURLConnection.getResponseCode();
            inputStream = httpURLConnection.getInputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return inputStream;

    }
    public JSONArray upload_image(String ip, String address, Uri uri, String name, Context context, List<String> names, List<String> values){
        JSONArray jsonArray = null;
        try {

            int bytesAvailable, bufferSize;
            int maxBufferSize = 1 * 4000 * 3000;
            FileInputStream fileInputStream = new FileInputStream(context.getContentResolver().openFileDescriptor(uri, "r").getFileDescriptor());
            URL url = new URL(ip+address);
            HttpURLConnection httpURLConnection =(HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            httpURLConnection.setRequestProperty("ENCTYPE", "multipart/form-data");
            httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=*****");
            httpURLConnection.setRequestProperty("image", name);
            OutputStream outputStream = httpURLConnection.getOutputStream();

            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

            for(int i = 0; i < names.size(); i++){
                dataOutputStream.writeBytes("--*****\r\n");
                dataOutputStream.writeBytes("Content-Disposition: form-data; name="+names.get(i)+"\r\n");
                dataOutputStream.writeBytes("\r\n");
                dataOutputStream.writeBytes(values.get(i));
                dataOutputStream.writeBytes("\r\n");
                dataOutputStream.writeBytes("--*****--\r\n");
            }

            dataOutputStream.writeBytes("--*****\r\n");
            dataOutputStream.writeBytes("Content-Disposition: form-data; name=image;filename="+name+"\r\n");
            dataOutputStream.writeBytes("\r\n");
            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            byte[] bytes = new byte[bufferSize];
            fileInputStream.read(bytes, 0, bufferSize);
            dataOutputStream.write(bytes, 0, bufferSize);
            dataOutputStream.writeBytes("\r\n");
            dataOutputStream.writeBytes("--*****--\r\n");

            httpURLConnection.getResponseCode();
            outputStream.close();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String  i = "";
            while ((i = bufferedReader.readLine()) != null) {
                //jsonArray = new JSONArray(i);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonArray;

    }
    public JSONArray upload_compress_image(String ip, String address, Uri uri, String name, Context context, List<String> names, List<String> values){
        JSONArray jsonArray = null;
        try {

            int bytesAvailable, bufferSize;
            int maxBufferSize = 1 * 4000 * 3000;
            InputStream fileInputStream = new FileInputStream(context.getContentResolver().openFileDescriptor(uri, "r").getFileDescriptor());
            URL url = new URL(ip+address);
            HttpURLConnection httpURLConnection =(HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            httpURLConnection.setRequestProperty("ENCTYPE", "multipart/form-data");
            httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=*****");
            httpURLConnection.setRequestProperty("image", name);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

            for(int i = 0; i < names.size(); i++){
                dataOutputStream.writeBytes("--*****\r\n");
                dataOutputStream.writeBytes("Content-Disposition: form-data; name="+names.get(i)+"\r\n");
                dataOutputStream.writeBytes("\r\n");
                dataOutputStream.writeBytes(values.get(i));
                dataOutputStream.writeBytes("\r\n");
                dataOutputStream.writeBytes("--*****--\r\n");
            }

            dataOutputStream.writeBytes("--*****\r\n");
            dataOutputStream.writeBytes("Content-Disposition: form-data; name=image;filename="+name+"\r\n");
            dataOutputStream.writeBytes("\r\n");
            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            Bitmap bitmap = BitmapFactory.decodeStream(fileInputStream);
            double height_prop = 400/(double)bitmap.getHeight();
            double width = (double) bitmap.getWidth()*height_prop;
            bitmap = Bitmap.createScaledBitmap(bitmap,(int) width, 400, true);
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            Bitmap rotate = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            rotate.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
            byte[] bytes = byteArrayOutputStream.toByteArray();
            dataOutputStream.write(bytes, 0, byteArrayOutputStream.size());
            dataOutputStream.writeBytes("\r\n");
            dataOutputStream.writeBytes("--*****--\r\n");

            httpURLConnection.getResponseCode();
            outputStream.close();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String  i = "";
            while ((i = bufferedReader.readLine()) != null) {
                //jsonArray = new JSONArray(i);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonArray;

    }
    public JSONArray connection2(String ip, String address, String data){
        JSONArray jsonArray = new JSONArray();
        try {
            URL url = new URL(ip+address);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod("POST");
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(httpURLConnection.getOutputStream());
            outputStreamWriter.write(data);
            outputStreamWriter.close();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String i = "";
            while ((i = bufferedReader.readLine()) != null) {
                //jsonArray = new JSONArray(i);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonArray;

    }


}
