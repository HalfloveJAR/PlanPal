package me.kobeplane;

import javax.swing.*;
import java.time.LocalDate;

public class TaskItem {
    int id;
    String text;
    Priority priority;
    LocalDate dueDate;
    JCheckBox checkBox;
    JPanel panel;
    JLabel label;
    JLabel priorityLabel;

    TaskItem(int id, String text, Priority priority, LocalDate dueDate, JCheckBox checkBox, JLabel label, JLabel priorityLabel, JPanel panel) {
        this.id = id;
        this.text = text;
        this.priority = priority;
        this.dueDate = dueDate;
        this.checkBox = checkBox;
        this.label = label;
        this.priorityLabel = priorityLabel;
        this.panel = panel;
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public Priority getPriority() {
        return priority;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public boolean isCompleted() {
        return checkBox.isSelected();
    }

    public JCheckBox getCheckBox() {
        return checkBox;
    }

    public JPanel getPanel() {
        return panel;
    }

    public JLabel getLabel() {
        return label;
    }

    public JLabel getPriorityLabel() {
        return priorityLabel;
    }

}
