package data_management;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import com.data_management.PatientRecord;
import com.data_management.DataStorage;
import com.data_management.FileDataReader;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

class FileDataReaderTest {

    @Test
    void testReadFromSimpleFileAtProjectRoot() throws Exception {
        // Create a temp directory for the test
        Path tempDir = Files.createTempDirectory("test_data_dir");
        String fileName = tempDir.resolve("test_data_file.txt").toString();

        // Write test data
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("1;1714376789050;HeartRate;85.0\n");
            writer.write("1;1714376789051;Systolic;120.0\n");
        }

        // Use FileDataReader with the temp directory
        FileDataReader reader = new FileDataReader(tempDir.toString());
        DataStorage storage = DataStorage.getInstance(); 
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
