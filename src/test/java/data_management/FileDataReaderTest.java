package data_management;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import com.data_management.Patient;
import com.data_management.PatientRecord;
import com.data_management.DataStorage;
import com.data_management.FileDataReader;
import java.util.List;

class FileDataReaderTest {

    @Test
    void testReadValidFileFromManualFolder() throws Exception {
        // Path to the manually created test_input/file_data.txt
        String testFolder = "test_input";

        // Create FileDataReader with this path
        FileDataReader reader = new FileDataReader(testFolder);

        // Prepare storage and read the file
        DataStorage storage = new DataStorage();
        reader.readData(storage);

        // Retrieve all records for patient 1
        List<PatientRecord> records = storage.getRecords(1, 0, System.currentTimeMillis());

        // Assert the file contains exactly 2 records
        assertEquals(2, records.size());

        // Validate the first record
        assertEquals("HeartRate", records.get(0).getRecordType());
        assertEquals(85.0, records.get(0).getMeasurementValue(), 0.001);

        // Validate the second record
        assertEquals("Systolic", records.get(1).getRecordType());
        assertEquals(120.0, records.get(1).getMeasurementValue(), 0.001);
    }
}
