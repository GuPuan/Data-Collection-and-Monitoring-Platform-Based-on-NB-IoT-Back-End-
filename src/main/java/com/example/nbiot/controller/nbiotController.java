package com.example.nbiot.controller;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.nbiot.Entity.humEntity;
import com.example.nbiot.Entity.tempEntity;
import com.example.nbiot.Service.Impl.humServiceImpl;
import com.example.nbiot.Service.Impl.nbiotServiceImpl;
import com.example.nbiot.Service.nbiotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping("/data") // Base URL for all endpoints in this controller
public class nbiotController {

    // Autowired service for handling temperature-related operations
    @Autowired
    private nbiotService<tempEntity> tempservice;

    // Autowired service for handling humidity-related operations
    @Autowired
    private nbiotService<humEntity> humservice;

    // Autowired implementation of temperature service
    @Autowired
    private nbiotServiceImpl tempserviceimpl;

    // Autowired implementation of humidity service
    @Autowired
    private humServiceImpl humserviceimpl;

    // Endpoint to start data recording, based on a key (1 for temperature, 2 for humidity)
    @RequestMapping("/start")
    public void startRec(@RequestParam String period, @RequestParam Integer key) {
        switch (key) {
            case 1:
                tempservice.start(period); // Start temperature data recording
                break;
            case 2:
                humservice.start(period); // Start humidity data recording
                break;
        }
    }

    // Endpoint to stop data recording, based on the key (1 for temperature, 2 for humidity)
    @RequestMapping("/stop")
    public void stop(@RequestParam Integer key) {
        switch (key) {
            case 1:
                tempservice.stop(); // Stop temperature data recording
                break;
            case 2:
                humservice.stop(); // Stop humidity data recording
                break;
        }
    }

    // Endpoint to save temperature data
    @RequestMapping("/saveTemp")
    public boolean saveTemp(@RequestBody tempEntity nbiotentity) {
        return tempservice.saveData(nbiotentity);
    }

    // Endpoint to save humidity data
    @RequestMapping("/saveHum")
    public boolean saveHum(@RequestBody humEntity humentity) {
        return humservice.saveData(humentity);
    }

    // Endpoint to retrieve all temperature data
    @RequestMapping("/find")
    public List<tempEntity> findall() {
        return tempservice.find();
    }

    // Endpoint to delete data by ID, based on the key (1 for temperature, 2 for humidity)
    @DeleteMapping("/delete/{id}")
    public boolean delete(@PathVariable BigInteger id, @RequestParam Integer key) {
        switch (key) {
            case 1:
                return tempservice.deleteById(id); // Delete temperature data by ID
            case 2:
                return humservice.deleteById(id); // Delete humidity data by ID
        }
        return false;
    }

    // Endpoint to delete multiple records by IDs in a batch, based on the key
    @PostMapping("/delete/batch")
    public boolean deleteBatch(@RequestBody List<BigInteger> ids, @RequestParam Integer key) {
        switch (key) {
            case 1:
                return tempservice.deleteByIds(ids); // Batch delete temperature data
            case 2:
                return humservice.deleteByIds(ids); // Batch delete humidity data
        }
        return false;
    }

    // Paginated endpoint to retrieve temperature data with filtering options
    @RequestMapping("/page/temp")
    public IPage<tempEntity> findTempPage(@RequestParam Integer pageNum,
                                          @RequestParam Integer pageSize,
                                          @RequestParam(defaultValue = "") String deviceId,
                                          @RequestParam(defaultValue = "") String sensorId,
                                          @RequestParam(defaultValue = "") String createDate) {
        IPage<tempEntity> page = new Page<>(pageNum, pageSize);
        QueryWrapper<tempEntity> queryWrapper = new QueryWrapper<>();

        // Apply filtering if deviceId, sensorId, or createDate is provided
        if (!"".equals(deviceId)) {
            queryWrapper.like("device_id", deviceId);
        }
        if (!"".equals(sensorId)) {
            queryWrapper.like("sensor_id", sensorId);
        }
        if (!"".equals(createDate)) {
            queryWrapper.like("create_date", createDate);
        }

        // Order the results by create_date in descending order
        queryWrapper.orderByDesc("create_date");

        return tempserviceimpl.page(page, queryWrapper); // Return paginated temperature data
    }

    // Paginated endpoint to retrieve humidity data with filtering options
    @RequestMapping("/page/hum")
    public IPage<humEntity> findHumPage(@RequestParam Integer pageNum,
                                        @RequestParam Integer pageSize,
                                        @RequestParam(defaultValue = "") String deviceId,
                                        @RequestParam(defaultValue = "") String sensorId,
                                        @RequestParam(defaultValue = "") String createDate) {
        IPage<humEntity> page = new Page<>(pageNum, pageSize);
        QueryWrapper<humEntity> queryWrapper = new QueryWrapper<>();

        // Apply filtering if deviceId, sensorId, or createDate is provided
        if (!"".equals(deviceId)) {
            queryWrapper.like("device_id", deviceId);
        }
        if (!"".equals(sensorId)) {
            queryWrapper.like("sensor_id", sensorId);
        }
        if (!"".equals(createDate)) {
            queryWrapper.like("create_date", createDate);
        }

        // Order the results by create_date in descending order
        queryWrapper.orderByDesc("create_date");

        return humserviceimpl.page(page, queryWrapper); // Return paginated humidity data
    }

    // Endpoint to export temperature data to a file
    @GetMapping("/tempexport")
    public void tmepexport(HttpServletResponse response) throws Exception {
        tempservice.export(response); // Export temperature data
    }

    // Endpoint to export humidity data to a file
    @GetMapping("/humexport")
    public void humexport(HttpServletResponse response) throws Exception {
        humservice.export(response); // Export humidity data
    }
}




