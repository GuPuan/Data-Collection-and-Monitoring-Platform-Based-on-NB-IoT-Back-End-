package com.example.nbiot.Entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigInteger;
import java.util.Date;

@Data // Lombok annotation to automatically generate getters, setters, toString, equals, and hashCode methods
@TableName("nbiot_temp") // Specifies the corresponding table name in the database
public class tempEntity {

    // The primary key of the table, with an auto-increment strategy
    @TableId(value = "id", type = IdType.AUTO)
    private BigInteger id;

    // Field to store the sensor value (temperature in this case)
    private Float senValue;

    // Field to store the device ID associated with this sensor
    private String deviceId;

    // Field to store the sensor ID
    private String sensorId;

    // Field to store the date and time when the data was created
    private Date createDate;
}

