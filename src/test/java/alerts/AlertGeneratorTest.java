package alerts;

import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;
import com.alerts.AlertGenerator;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AlertGeneratorTest {

    @Test
    void testCriticalSystolicTriggersAlert() {
        // Create a DataStorage and a patient with high systolic pressure
        DataStorage storage = new DataStorage();
        int patientId = 1;
        storage.addPatientData(patientId, 190.0, "Systolic", System.currentTimeMillis());

        // Set up the AlertGenerator
        AlertGenerator generator = new AlertGenerator(storage);

        // Redirect console output temporarily to capture alert message
        java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(out));

        // Evaluate the data
        Patient patient = storage.getAllPatients().get(0);
        generator.evaluateData(patient);

        // Check output
        String consoleOutput = out.toString();
        assertTrue(consoleOutput.contains("CRITICAL_SYSTOLIC"), "Expected CRITICAL_SYSTOLIC alert to be printed");

        // Reset System.out
        System.setOut(System.out);
    }

    @Test
    void testLowSaturationTriggersAlert() {
        DataStorage storage = new DataStorage();
        int patientId = 2;
        storage.addPatientData(patientId, 90.0, "Saturation", System.currentTimeMillis());

        AlertGenerator generator = new AlertGenerator(storage);

        java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(out));

        Patient patient = storage.getAllPatients().get(0);
        generator.evaluateData(patient);

        String consoleOutput = out.toString();
        assertTrue(consoleOutput.contains("LOW_SATURATION"), "Expected LOW_SATURATION alert");

        System.setOut(System.out);
    }
}