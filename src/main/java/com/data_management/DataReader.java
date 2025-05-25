package com.data_management;

import java.io.IOException;

public interface DataReader {


    /**
     * starts reading data from the real time data source and stores it in the data storage.
     *
     * @param dataStorage the storage where data will be stored
     * @throws IOException if there is an error reading the data
     */
    void start(DataStorage dataStorage) throws IOException;


    /**
     * stops reading from the data source and closes any open connections.
     *
     * @throws IOException if there is an error reading the data
     */
    void stop() throws IOException;

}
