package com.example.nbiot.Service.Impl;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.nbiot.Entity.tempEntity;
import com.example.nbiot.Service.nbiotService;
import com.example.nbiot.util.tempMapper;
import com.example.nbiot.util.nbiotRec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

@Service // Marks this class as a Spring service for dependency injection
public class nbiotServiceImpl extends ServiceImpl<tempMapper, tempEntity> implements nbiotService<tempEntity> {

    // Autowire the nbiotRec service to handle temperature recording tasks
    @Autowired
    private nbiotRec nbiotrec;

    // Autowire a thread pool task scheduler for scheduling tasks
    @Autowired
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    // Future object to handle scheduled tasks, allowing cancellation of tasks
    private ScheduledFuture<?> future;

    // Define a Runnable task to collect temperature data periodically
    Runnable task = new Runnable() {
        @Override
        public void run() {
            nbiotrec.startTempRecord(); // Start the temperature recording process
            System.out.println("Collected a temperature reading");
        }
    };

    // Start collecting temperature data based on the specified period (in seconds)
    public void start(String period) {
        try {
            // Schedule the task using a Cron expression, based on the provided period
            future = threadPoolTaskScheduler.schedule(task, new CronTrigger("0/" + period + " * * * * * "));
            System.out.println("Temperature data collection started");
        } catch (Exception e) {
            System.out.println("Temperature data collection failed");
        }
    }

    // Stop the scheduled data collection task
    public void stop() {
        try {
            if (future != null) {
                future.cancel(true); // Cancel the scheduled task
                System.out.println("Data collection stopped successfully");
            }
        } catch (Exception e) {
            System.out.println("Failed to stop data collection");
        }
    }

    // Save or update temperature data in the database
    public boolean saveData(tempEntity tempentity) {
        return updateById(tempentity); // Updates the existing record by ID
    }

    // Delete a temperature record by ID
    public boolean deleteById(BigInteger id) {
        return removeById(id); // Removes the record by ID
    }

    // Batch delete multiple temperature records by their IDs
    public boolean deleteByIds(List<BigInteger> ids) {
        return removeByIds(ids); // Removes the records by their IDs
    }

    // Retrieve all temperature records from the database
    public List<tempEntity> find() {
        return list(); // Returns the list of all temperature data
    }

    // Export temperature data to an Excel file and send it to the browser
    public void export(HttpServletResponse response) throws Exception {
        // Query all temperature data from the database
        List<tempEntity> list = find();

        // Create an ExcelWriter in memory to write data to the browser
        ExcelWriter writer = ExcelUtil.getWriter(true);

        // Customize the header for the exported Excel file
        writer.addHeaderAlias("id", "ID");
        writer.addHeaderAlias("senValue", "Sample Value");
        writer.addHeaderAlias("deviceId", "Device ID");
        writer.addHeaderAlias("sensorId", "Sensor ID");
        writer.addHeaderAlias("createDate", "Sample Time");

        // Write the data to the Excel file with default styles and force header output
        writer.write(list, true);

        // Set the response content type and file name for browser download
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("Temperature Data Information", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        // Write the file to the output stream and close resources
        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();
    }
}




