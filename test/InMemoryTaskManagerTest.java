import manager.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;

public final class InMemoryTaskManagerTest extends TaskManagerTest {
    @BeforeEach
    public void creationHistory() {
        taskManager = new InMemoryTaskManager();
    }
}
