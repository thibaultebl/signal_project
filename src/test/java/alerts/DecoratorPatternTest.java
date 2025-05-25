package alerts;
import com.alerts.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DecoratorPatternTest {

    @Test
    void testPriorityDecorator() {
        Alert base = new Alert("1234", "LOW_SATURATION", 999L);
        PriorityAlertDecorator decorated = new PriorityAlertDecorator(base, "HIGH");

        String message = decorated.getMessage();
        assertTrue(message.contains("[PRIORITY: HIGH]"));
        assertTrue(message.contains("LOW_SATURATION"));
    }

    @Test
    void testRepeatedDecorator() {
        Alert base = new Alert("5678", "CRITICAL_SYSTOLIC", 888L);
        RepeatedAlertDecorator decorated = new RepeatedAlertDecorator(base, 3);

        String message = decorated.getMessage();
        assertTrue(message.contains("REPEATED 3 times"));
    }
}
