package data_management;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import com.data_management.Patient;
import com.data_management.PatientRecord;
import com.data_management.DataStorage;
import com.data_management.FileDataReader;
import java.util.List;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.Path;
import java.util.List;




class FileDataReaderTest {

    @Test
    void testReadFromSimpleFileAtProjectRoot() throws Exception {
        // Define the output path: create test_data_file.txt at the root of the project
        String fileName = "test_data_file.txt";

        // Write test content into the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("1;1714376789050;HeartRate;85.0\n");
            writer.write("1;1714376789051;Systolic;120.0\n");
        }

        // Read using FileDataReader (looking at the project root folder)
        FileDataReader reader = new FileDataReader(".");
        DataStorage storage = new DataStorage();
        reader.readData(storage);

        // Validate the content
        List<PatientRecord> records = storage.getRecords(1, 0, System.currentTimeMillis());
        assertEquals(2, records.size());

        assertEquals("HeartRate", records.get(0).getRecordType());
        assertEquals(85.0, records.get(0).getMeasurementValue(), 0.001);
        assertEquals("Systolic", records.get(1).getRecordType());
        assertEquals(120.0, records.get(1).getMeasurementValue(), 0.001);
    }
}
