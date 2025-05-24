package data_management;

import com.data_management.DataReader;
import com.data_management.DataStorage;

import java.io.IOException;

public class MockDataReader implements DataReader {

    @Override
    public void readData(DataStorage dataStorage) throws IOException {
        // Add mock records to simulate data reading
        dataStorage.addPatientData(1, 100.0, "WhiteBloodCells", 1714376789050L);
        dataStorage.addPatientData(1, 200.0, "WhiteBloodCells", 1714376789051L);
    }
}
