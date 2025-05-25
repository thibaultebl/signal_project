package alerts;

import com.data_management.Patient;

import org.junit.jupiter.api.Test;
import com.alerts.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class StrategyPatternTest {

    @Test
    void testBloodPressureStrategy_criticalOnly() {
        Patient patient = new Patient(1);

        // CRITICAL systolic
        patient.addRecord(200.0, "Systolic", 1000L);
        // CRITICAL diastolic
        patient.addRecord(50.0, "Diastolic", 2000L);
        // Normal value (should not trigger alert)
        patient.addRecord(120.0, "Systolic", 3000L);

        AlertStrategy strategy = new BloodPressureStrategy();
        List<Alert> alerts = strategy.checkAlert(patient);

        assertEquals(2, alerts.size());
        assertTrue(alerts.stream().anyMatch(a -> a.getCondition().equals("CRITICAL_SYSTOLIC")));
        assertTrue(alerts.stream().anyMatch(a -> a.getCondition().equals("CRITICAL_DIASTOLIC")));
    }

    @Test
    void testHeartRateStrategy_ecgSpike() {
        Patient patient = new Patient(2);
        for (int i = 0; i < 10; i++) {
            patient.addRecord(60 + i, "ECG", 1000L + i * 1000); // smooth values
        }
        // Spike
        patient.addRecord(200.0, "ECG", 20000L);

        AlertStrategy strategy = new HeartRateStrategy();
        List<Alert> alerts = strategy.checkAlert(patient);

        assertFalse(alerts.isEmpty());
        assertTrue(alerts.stream().anyMatch(a -> a.getCondition().equals("ECG_SPIKE")));
    }

    @Test
    void testOxygenSaturationStrategy_drop() {
        Patient patient = new Patient(3);
        patient.addRecord(97.0, "Saturation", 1000L);
        patient.addRecord(91.0, "Saturation", 1500L); // drop > 5% in <10min

        AlertStrategy strategy = new OxygenSaturationStrategy();
        List<Alert> alerts = strategy.checkAlert(patient);

        assertTrue(alerts.stream().anyMatch(a -> a.getCondition().equals("RAPID_SATURATION_DROP")));
    }
}