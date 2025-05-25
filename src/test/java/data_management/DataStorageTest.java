package data_management;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.data_management.DataStorage;
import com.data_management.PatientRecord;

import java.util.List;

class DataStorageTest {

    @Test
    void testAddAndGetRecords() {
        // Create a new DataStorage instance
        DataStorage storage = new DataStorage();

        // Add two records for the same patient with close timestamps
        storage.addPatientData(1, 100.0, "WhiteBloodCells", 1714376789050L);
        storage.addPatientData(1, 200.0, "WhiteBloodCells", 1714376789051L);

        // Retrieve records between the two timestamps (inclusive)
        List<PatientRecord> records = storage.getRecords(1, 1714376789050L, 1714376789051L);

        // Check if both records are returned
        assertEquals(2, records.size(), "Expected 2 records to be returned");

        // Validate the measurement values of both records
        assertEquals(100.0, records.get(0).getMeasurementValue(), 0.001);
        assertEquals(200.0, records.get(1).getMeasurementValue(), 0.001);
    }
}
