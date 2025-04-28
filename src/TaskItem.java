import javax.swing.*;
import java.time.LocalDate;

public class TaskItem {
    String text;
    Priority priority;
    LocalDate dueDate;
    JCheckBox checkBox;
    JPanel panel;
    JLabel label;
    JLabel priorityLabel;

    TaskItem(String text, Priority priority, LocalDate dueDate, JCheckBox checkBox, JPanel panel) {
        this.text = text;
        this.priority = priority;
        this.dueDate = dueDate;
        this.checkBox = checkBox;
        this.panel = panel;

        this.label = (JLabel) ((JPanel) panel.getComponent(0)).getComponent(1);
        this.priorityLabel = (JLabel) ((JPanel) panel.getComponent(0)).getComponent(2);
    }
}
