package com.example.nbiot;
import com.alibaba.fastjson.JSONObject;
import com.example.nbiot.Entity.tempEntity;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Component;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Component // Marks this class as a Spring-managed component
public class nbiotRec {

//    @Autowired
//    private nbiotMapper nbiotMapper;  // The mapper for database operations (commented out in this case)

    // Method to construct the request header JSON for the API call
    public String reqHeader(String userId, String userNum, String sensorId, String sensorNum) {
        JSONObject obj = new JSONObject();
        obj.put(userId, userNum);  // Adds user ID and user number to the request
        obj.put(sensorId, sensorNum);  // Adds sensor ID and sensor number to the request
        return obj.toString();  // Returns the request header as a JSON string
    }

    // Method to handle the response from the API and return the result as a string
    public String recReturn(HttpURLConnection conn, String parm) {
        String result = "";
        BufferedReader in = null;
        PrintWriter out = null;
        try {
            // Sending the request parameters to the server
            out = new PrintWriter(conn.getOutputStream());
            out.print(parm);
            out.flush();

            // Reading the response from the server
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Closing the input/output streams
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

    // Method to convert the API response to a tempEntity and save it to the database
    public void tranEntity(String result) {
        JSONObject jsonObject = JSONObject.parseObject(result);
        try {
            String deviceId = jsonObject.getString("deviceId");  // Get device ID from the response
            String sensorId = jsonObject.getString("sensorId");  // Get sensor ID from the response
            Float senvalue = jsonObject.getFloatValue("value");  // Get sensor value
            String updateDate = jsonObject.getString("updateDate");  // Get the update timestamp

            // Create a new tempEntity object and set its fields
            tempEntity tempentity = new tempEntity();
            tempentity.setDeviceId(deviceId);
            tempentity.setSensorId(sensorId);
            tempentity.setSenValue(senvalue);

            // Convert the string date into a Date object
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            tempentity.setCreateDate(sdf.parse(updateDate));

            // Print the tempEntity object to console
            System.out.println(tempentity);

            // Insert the entity into the database (the line is commented out here)
            // nbiotMapper.insert(tempentity);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    // Test method to simulate data collection from the API
    @Test
    public void startRecord() {
        try {
            // Establish a connection to the API
            URL realUrl = new URL("http://api.tlink.io/api/device/getSingleSensorDatas");
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Content-Type", "text/plain;charset=utf-8");
            conn.addRequestProperty("tlinkAppId", "1431e537129c44bc92614358243863db");
            conn.addRequestProperty("Cookie", "JSESSIONID=D6271C285CFB03DA5D1806BA599E8998");
            conn.addRequestProperty("Authorization", "Bearer 2eb63881-15ed-44d0-a056-fdef391b5da0");
            conn.setDoOutput(true);
            conn.setDoInput(true);

            // Create the request parameters
            String parm = reqHeader("userId", "200036250", "sensorId", "200599442");

            // Get the response from the API and process it
            String result = recReturn(conn, parm);
            tranEntity(result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

