package alerts;

import com.alerts.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FactoryPatternTest {

    @Test
    void testBloodPressureFactory() {
        AlertFactory factory = new BloodPressureAlertFactory();
        Alert alert = factory.createAlert("1234", "CRITICAL_SYSTOLIC", 1000L);

        assertEquals("1234", alert.getPatientId());
        assertEquals("CRITICAL_SYSTOLIC", alert.getCondition());
        assertEquals(1000L, alert.getTimestamp());
    }

    @Test
    void testBloodOxygenFactory() {
        AlertFactory factory = new OxygenAlertFactory();
        Alert alert = factory.createAlert("5678", "LOW_SATURATION", 2000L);

        assertEquals("5678", alert.getPatientId());
        assertEquals("LOW_SATURATION", alert.getCondition());
    }

    @Test
    void testECGFactory() {
        AlertFactory factory = new ECGAlertFactory();
        Alert alert = factory.createAlert("9999", "ECG_SPIKE", 3000L);

        assertEquals("9999", alert.getPatientId());
        assertEquals("ECG_SPIKE", alert.getCondition());
    }
}
