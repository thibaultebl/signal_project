package alerts;

import com.cardio_generator.HealthDataSimulator;
import com.data_management.DataStorage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SingletonPatternTest {

    @Test
    public void testHealthDataSimulatorSingleton() {
        HealthDataSimulator instance1 = HealthDataSimulator.getInstance();
        HealthDataSimulator instance2 = HealthDataSimulator.getInstance();

        assertNotNull(instance1, "HealthDataSimulator instance should not be null");
        assertSame(instance1, instance2, "Both instances should be the same (singleton)");
    }

    @Test
    public void testDataStorageSingleton() {
        DataStorage storage1 = DataStorage.getInstance();
        DataStorage storage2 = DataStorage.getInstance();

        assertNotNull(storage1, "DataStorage instance should not be null");
        assertSame(storage1, storage2, "Both instances should be the same (singleton)");
    }
}
