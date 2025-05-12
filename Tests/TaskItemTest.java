import org.junit.Test;
import static org.junit.Assert.*;

import javax.swing.*;
import java.time.LocalDate;

public class TaskItemTest {

    @Test
    public void testTaskItemInitialization() {
        String taskText = "Finish Unit Test";
        Priority priority = Priority.HIGH;
        LocalDate dueDate = LocalDate.of(2025, 5, 10);
        JCheckBox checkBox = new JCheckBox();
        JLabel label = new JLabel(taskText);
        JLabel priorityLabel = new JLabel("High");
        JPanel panel = new JPanel();

        TaskItem task = new TaskItem(taskText, priority, dueDate, checkBox, label, priorityLabel, panel);

        assertEquals("Finish Unit Test", task.getText());
        assertEquals(Priority.HIGH, task.getPriority());
        assertEquals(LocalDate.of(2025, 5, 10), task.getDueDate());
        assertFalse(task.isCompleted()); // checkbox should be unchecked
    }

    @Test
    public void testCompletionStatus() {
        JCheckBox checkBox = new JCheckBox();
        TaskItem task = new TaskItem("Test Completion", Priority.MEDIUM, null, checkBox, new JLabel(), new JLabel(), new JPanel());

        checkBox.setSelected(true); // simulate task completion

        assertTrue(task.isCompleted());
    }
}
