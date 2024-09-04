package com.example.nbiot.Service;
import javax.servlet.http.HttpServletResponse;
import java.math.BigInteger;
import java.util.List;

// A generic interface for handling NBIoT services, where T is a generic entity type
public interface nbiotService<T>  {

    // Method to start collecting data with a specified period
    void start(String period);

    // Method to stop collecting data
    void stop();

    // Method to save or update data for a specific entity
    boolean saveData(T entity);

    // Method to retrieve all data records for the entity
    List<T> find();

    // Method to delete a data record by its ID
    boolean deleteById(BigInteger id);

    // Method to delete multiple data records by their IDs
    boolean deleteByIds(List<BigInteger> ids);

    // Method to export data to a file and send it via HTTP response
    void export(HttpServletResponse response) throws Exception;

}

