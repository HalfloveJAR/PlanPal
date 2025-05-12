import org.junit.Test;
import static org.junit.Assert.*;

import java.time.LocalDate;

public class TaskManagerTest {

    @Test
    public void testAddTask() {
        TaskManager manager = TaskManager.getInstance();
        manager.clearTasks(); // Reset before test

        manager.addTask("Test Task", Priority.MEDIUM, LocalDate.now(), false);

        assertEquals(1, manager.getTasks().size());
        assertEquals("Test Task", manager.getTasks().get(0).getText());
    }

    @Test
    public void testClearTasks() {
        TaskManager manager = TaskManager.getInstance();
        manager.addTask("Clear Me", Priority.HIGH, null, false);

        manager.clearTasks();
        assertEquals(0, manager.getTasks().size());
    }
}
