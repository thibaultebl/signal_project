package data_management;
import com.data_management.Patient;
import com.data_management.PatientRecord;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.List;

class PatientTest {

    @Test
    void testAddRecordAndGetRecordsInRange() {
        Patient patient = new Patient(1);

        // Add records with different timestamps
        patient.addRecord(120.0, "Systolic", 1000L);
        patient.addRecord(130.0, "Systolic", 2000L);
        patient.addRecord(140.0, "Systolic", 3000L);

        // Retrieve records between 1000 and 2500
        List<PatientRecord> results = patient.getRecords(1000L, 2500L);

        // Expect two records to be returned
        assertEquals(2, results.size());
        assertEquals(120.0, results.get(0).getMeasurementValue(), 0.001);
        assertEquals(130.0, results.get(1).getMeasurementValue(), 0.001);
    }

    @Test
    void testNoRecordInRange() {
        Patient patient = new Patient(1);
        patient.addRecord(120.0, "Systolic", 1000L);

        // Query with a time range that does not include the record
        List<PatientRecord> results = patient.getRecords(2000L, 3000L);

        // Expect an empty list
        assertTrue(results.isEmpty());
    }

    @Test
    void testBoundaryTimestampsAreIncluded() {
        Patient patient = new Patient(1);
        patient.addRecord(110.0, "Systolic", 1500L);

        // Query with exact matching start and end timestamps
        List<PatientRecord> results = patient.getRecords(1500L, 1500L);

        // Expect one record that matches the boundary exactly
        assertEquals(1, results.size());
        assertEquals(110.0, results.get(0).getMeasurementValue(), 0.001);
    }

    @Test
    void testEmptyPatientReturnsEmptyList() {
        Patient patient = new Patient(1);

        // No records added
        List<PatientRecord> results = patient.getRecords(0L, 5000L);

        // Expect empty list for patient with no data
        assertTrue(results.isEmpty());
    }
}
