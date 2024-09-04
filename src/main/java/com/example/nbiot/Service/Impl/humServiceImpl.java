package com.example.nbiot.Service.Impl;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.nbiot.Entity.humEntity;
import com.example.nbiot.Service.nbiotService;
import com.example.nbiot.util.humMapper;
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
public class humServiceImpl extends ServiceImpl<humMapper, humEntity> implements nbiotService<humEntity> {

    // Autowire the nbiotRec service to handle humidity recording tasks
    @Autowired
    private nbiotRec nbiotrec;

    // Autowire a thread pool task scheduler for scheduling tasks
    @Autowired
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    // Future object to handle scheduled tasks, allowing cancellation of tasks
    private ScheduledFuture<?> future;

    // Define a Runnable task to collect humidity data periodically
    Runnable task = new Runnable() {
        @Override
        public void run() {
            nbiotrec.startHumRecord(); // Start the humidity recording process
            System.out.println("Collected a humidity reading");
        }
    };

    // Start collecting humidity data based on the specified period (in seconds)
    public void start(String period) {
        try {
            // Schedule the task using a Cron expression, based on the provided period
            future = threadPoolTaskScheduler.schedule(task, new CronTrigger("0/" + period + " * * * * * "));
            System.out.println("Humidity data collection started");
        } catch (Exception e) {
            System.out.println("Humidity data collection failed");
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

    // Save or update humidity data in the database
    public boolean saveData(humEntity humEntity) {
        return updateById(humEntity); // Updates the existing record by ID
    }

    // Delete a humidity record by ID
    public boolean deleteById(BigInteger id) {
        return removeById(id); // Removes the record by ID
    }

    // Batch delete multiple humidity records by their IDs
    public boolean deleteByIds(List<BigInteger> ids) {
        return removeByIds(ids); // Removes the records by their IDs
    }

    // Retrieve all humidity records from the database
    public List<humEntity> find() {
        return list(); // Returns the list of all humidity data
    }

    // Export humidity data to an Excel file and send it to the browser
    public void export(HttpServletResponse response) throws Exception {
        // Query all humidity data from the database
        List<humEntity> list = find();

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
        String fileName = URLEncoder.encode("Humidity Data Information", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        // Write the file to the output stream and close resources
        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();
    }
}




