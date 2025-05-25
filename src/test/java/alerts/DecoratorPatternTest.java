package alerts;

import com.alerts.Alert;
import com.alerts.RepeatedAlertDecorator;
import com.alerts.PriorityAlertDecorator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DecoratorPatternTest {

    @Test
    void testRepeatedAlertDecorator() {
        Alert baseAlert = new Alert("123", "LOW_SATURATION", 1000L);
        RepeatedAlertDecorator decorated = new RepeatedAlertDecorator(baseAlert, 2);

        assertEquals("123", decorated.getPatientId());
        assertEquals("LOW_SATURATION_REPEATED_2", decorated.getCondition());
        assertEquals(1000L, decorated.getTimestamp());
    }

    @Test
    void testPriorityAlertDecorator() {
        Alert baseAlert = new Alert("456", "ECG_SPIKE", 2000L);
        PriorityAlertDecorator decorated = new PriorityAlertDecorator(baseAlert, "HIGH");

        assertEquals("456", decorated.getPatientId());
        assertEquals("ECG_SPIKE_PRIORITY_HIGH", decorated.getCondition());
        assertEquals(2000L, decorated.getTimestamp());
    }
}
