package com.ntropia.filmico.utilities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiRequester {

    public Bitmap getImage(String urlAddress) {
        try {
            URL endpoint = new URL(urlAddress);
            HttpURLConnection connection = (HttpURLConnection) endpoint.openConnection();
            connection.setConnectTimeout(5000);
            connection.setDoInput(true);
            try {
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap newImage = BitmapFactory.decodeStream(input);
                return newImage;
            }
            finally {
                connection.disconnect();
            }
        } catch (Exception ex) {
            Log.e("Error", ex.getMessage(), ex);
            return null;
        }
    }

    public String get(String urlAddress) {
        try {
            URL apiEndpoint = new URL(urlAddress);
            HttpURLConnection connection = (HttpURLConnection) apiEndpoint.openConnection();
            connection.setConnectTimeout(5000);
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                br.close();
                return sb.toString();
            }
            finally {
                connection.disconnect();
            }
        } catch (Exception ex) {
            Log.e("Error", ex.getMessage(), ex);
            return null;
        }
    }

}
