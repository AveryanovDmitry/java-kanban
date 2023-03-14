package test;

import com.praktikum.app.services.inMemoryManager.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;

public class InMemoryTaskManagerTest extends TaskManagerTest {
    @BeforeEach
    public void beforeEach() {
        manager = new InMemoryTaskManager();
    }
}
