package com.example.nbiot.util;
import com.alibaba.fastjson.JSONObject;
import com.example.nbiot.Entity.humEntity;
import com.example.nbiot.Entity.tempEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Component // Marks this class as a Spring component for dependency injection
public class nbiotRec {

    // Access token for authentication purposes
    String accessToken = "2eb63881-15ed-44d0-a056-fdef391b5da0";

    // Autowire the tempMapper for temperature-related database operations
    @Autowired
    private tempMapper tempMapper;

    // Autowire the humMapper for humidity-related database operations
    @Autowired
    private humMapper humMapper;

    // Constructs the request header for the API call, including user and sensor information
    public String reqHeader(String userId, String userNum, String sensorId, String sensorNum) {
        JSONObject obj = new JSONObject();
        obj.put(userId, userNum); // Adds user information to the request
        obj.put(sensorId, sensorNum); // Adds sensor information to the request
        return obj.toString(); // Returns the request header as a JSON string
    }

    // Receives the response from the API and returns it as a string
    public String recReturn(HttpURLConnection conn, String parm) {
        String result = "";
        BufferedReader in = null;
        PrintWriter out = null;
        try {
            out = new PrintWriter(conn.getOutputStream()); // Send the request data
            out.print(parm);
            out.flush();

            // Read the response from the server
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Close the streams
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    // Converts the temperature data from JSON to a tempEntity object and saves it to the database
    public void tranTempEntity(String result) {
        JSONObject jsonObject = JSONObject.parseObject(result);
        try {
            String deviceId = jsonObject.getString("deviceId");
            String sensorId = jsonObject.getString("sensorId");
            Float senvalue = jsonObject.getFloatValue("value");
            String updateDate = jsonObject.getString("updateDate");

            // Create a new tempEntity object and set its fields
            tempEntity tempentity = new tempEntity();
            tempentity.setDeviceId(deviceId);
            tempentity.setSensorId(sensorId);
            tempentity.setSenValue(senvalue);

            // Convert the date string to a Date object
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            tempentity.setCreateDate(sdf.parse(updateDate));

            // Save the entity to the database
            tempMapper.insert(tempentity);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    // Converts the humidity data from JSON to a humEntity object and saves it to the database
    public void tranHumEntity(String result) {
        JSONObject jsonObject = JSONObject.parseObject(result);
        try {
            String deviceId = jsonObject.getString("deviceId");
            String sensorId = jsonObject.getString("sensorId");
            Float senvalue = jsonObject.getFloatValue("value");
            String updateDate = jsonObject.getString("updateDate");

            // Create a new humEntity object and set its fields
            humEntity humentity = new humEntity();
            humentity.setDeviceId(deviceId);
            humentity.setSensorId(sensorId);
            humentity.setSenValue(senvalue);

            // Convert the date string to a Date object
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            humentity.setCreateDate(sdf.parse(updateDate));

            // Save the entity to the database
            humMapper.insert(humentity);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    // Method to start collecting temperature data
    public void startTempRecord() {
        try {
            // Establish a connection to the temperature sensor API
            URL realUrl = new URL("http://api.tlink.io/api/device/getSingleSensorDatas");
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Content-Type", "text/plain;charset=utf-8");
            conn.addRequestProperty("tlinkAppId", "1431e537129c44bc92614358243863db");
            conn.addRequestProperty("Cookie", "JSESSIONID=D6271C285CFB03DA5D1806BA599E8998");
            conn.addRequestProperty("Authorization", "Bearer" + accessToken);
            conn.setDoOutput(true);
            conn.setDoInput(true);

            // Create the request parameters
            String parm = reqHeader("userId", "200036250", "sensorId", "200599442");
            // Get the response from the API and convert it to a tempEntity
            String result = recReturn(conn, parm);
            tranTempEntity(result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to start collecting humidity data
    public void startHumRecord() {
        try {
            // Establish a connection to the humidity sensor API
            URL realUrl = new URL("http://api.tlink.io/api/device/getSingleSensorDatas");
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Content-Type", "text/plain;charset=utf-8");
            conn.addRequestProperty("tlinkAppId", "1431e537129c44bc92614358243863db");
            conn.addRequestProperty("Cookie", "JSESSIONID=D6271C285CFB03DA5D1806BA599E8998");
            conn.addRequestProperty("Authorization", "Bearer" + accessToken);
            conn.setDoOutput(true);
            conn.setDoInput(true);

            // Create the request parameters
            String parm = reqHeader("userId", "200036250", "sensorId", "200599440");
            // Get the response from the API and convert it to a humEntity
            String result = recReturn(conn, parm);
            tranHumEntity(result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

