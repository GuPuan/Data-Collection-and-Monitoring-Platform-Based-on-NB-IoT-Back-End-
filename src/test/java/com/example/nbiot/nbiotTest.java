package com.example.nbiot;

import com.alibaba.fastjson.JSONObject;
import com.example.nbiot.Entity.tempEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;

@Component // This annotation marks the class as a Spring-managed component for dependency injection
public class nbiotTest {

//    @Autowired
//    private nbiotMapper nbiotMapper;  // Mapper for database operations (commented out in this case)

    // Method to construct the request header for the API call
    public String reqHeader(String userId, String userNum, String sensorId, String sensorNum) {
        JSONObject obj = new JSONObject();
        obj.put(userId, userNum);  // Adds user information
        obj.put(sensorId, sensorNum);  // Adds sensor information
        return obj.toString();  // Returns the JSON string
    }

    // Placeholder method for handling API response (currently unused)
    public void recReturn() {
        // Placeholder for future functionality
    }

    // Test method to simulate data collection from the API
    @Test
    public void startRecord() {
        // Construct the JSON request body with user and sensor IDs
        JSONObject obj = new JSONObject();
        obj.put("userId", "200036250");
        obj.put("sensorId", "200599442");

        String parm = obj.toString();  // Convert the request to a string
        String result = "";  // To store the response from the API

        PrintWriter out = null;
        BufferedReader in = null;
        try {
            // Establish a connection to the API
            URL realUrl = new URL("http://api.tlink.io/api/device/getSingleSensorDatas");
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
            conn.setRequestMethod("POST");  // Set the request method to POST

            // Set common request properties
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Content-Type", "text/plain;charset=utf-8");
            conn.addRequestProperty("tlinkAppId", "1431e537129c44bc92614358243863db");
            conn.addRequestProperty("Cookie", "JSESSIONID=D6271C285CFB03DA5D1806BA599E8998");
            conn.addRequestProperty("Authorization", "Bearer c7b96626-0a43-45d3-8e56-a78a7656af22");

            // Enable output and input for the connection
            conn.setDoOutput(true);
            conn.setDoInput(true);

            // Send the request
            out = new PrintWriter(conn.getOutputStream());
            out.print(parm);  // JSON request body
            out.flush();  // Flush the stream

            // Read the response from the server
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;  // Append each line of the response
            }

            // Convert the JSON response to a tempEntity (commented out for now)
//            JSONObject jsonObject = JSONObject.parseObject(result);
//            String deviceId = jsonObject.getString("deviceId");
//            String sensorId = jsonObject.getString("sensorId");
//            Float senvalue = jsonObject.getFloatValue("value");
//            String updateDate = jsonObject.getString("updateDate");
//
//            tempEntity tempentity = new tempEntity();
//            tempentity.setDeviceId(deviceId);
//            tempentity.setSensorId(sensorId);
//            tempentity.setSenValue(senvalue);
//
//            // Convert the date string to a Date object
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            tempentity.setCreateDate(sdf.parse(updateDate));
//
//            // Insert the entity into the database (commented out)
//            // nbiotMapper.insert(tempentity);

        } catch (Exception e) {
            e.printStackTrace();  // Handle any exceptions
        } finally {
            // Ensure the streams are closed after the operation
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
    }
}
