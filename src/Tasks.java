import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;

public class Tasks {
    private final JFrame frame;
    private final JPanel taskPanel;
    private final ArrayList<TaskItem> tasks = new ArrayList<>();

    // File path for saving/loading
    private final String FILE_NAME = "tasks.txt";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Tasks::new);
    }

    public Tasks() {
        frame = new JFrame("PlanPal");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);

        // Task panel with vertical layout
        taskPanel = new JPanel();
        taskPanel.setLayout(new BoxLayout(taskPanel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(taskPanel);

        JButton addButton = new JButton("Add Task");
        JButton saveButton = new JButton("Save Tasks");
        JButton loadButton = new JButton("Load Tasks");
        JButton clearButton = new JButton("Clear Taskboard");

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(addButton, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel();
        controlPanel.add(saveButton);
        controlPanel.add(loadButton);
        controlPanel.add(clearButton);

        frame.getContentPane().add(inputPanel, BorderLayout.NORTH);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        frame.getContentPane().add(controlPanel, BorderLayout.SOUTH);

        // Add task logic
        addButton.addActionListener(e -> showAddTaskDialog());

        // Save tasks to file
        saveButton.addActionListener(e -> saveTasks());

        // Load tasks from file
        loadButton.addActionListener(e -> {
            tasks.clear();
            taskPanel.removeAll();
            loadTasks();
            taskPanel.revalidate();
            taskPanel.repaint();
        });

        loadTasks(); // Load tasks on startup

        // Clear taskboard button listener
        clearButton.addActionListener(e -> clearTasks());

        frame.setVisible(true);
    }

    // Add a task item to the panel and list
    private void addTask(String text, Priority priority, LocalDate dueDate, boolean done) {
        JCheckBox checkBox = new JCheckBox();
        checkBox.setSelected(done);
        JLabel label = new JLabel(text + " (" + (dueDate != null ? dueDate.toString() : "No due date") + ")");
        JButton deleteButton = new JButton("Delete");

        // Create colored priority label
        JLabel priorityLabel = new JLabel(String.valueOf(priority));

        if (done) {
            label.setText("<html><strike>" + text + " (" + (dueDate != null ? dueDate.toString() : "No due date") + ")" + "</strike></html>");
            priorityLabel.setText("<html><strike>" +  String.valueOf(priority) + "</strike></html>");
            priorityLabel.setForeground(Color.GRAY);
            priorityLabel.setFont(priorityLabel.getFont().deriveFont(Font.ITALIC));
            label.setForeground(Color.GRAY);
            label.setFont(label.getFont().deriveFont(Font.ITALIC));
        } else {
            label.setForeground(Color.BLACK);
            label.setFont(label.getFont().deriveFont(Font.PLAIN));
            priorityLabel.setText(String.valueOf(priority));
            priorityLabel.setForeground(getPriorityColor(priority)); // <-- set color
        }

        JPanel taskItemPanel = new JPanel(new BorderLayout());
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.add(checkBox);
        leftPanel.add(label);
        taskItemPanel.add(leftPanel, BorderLayout.CENTER);
        taskItemPanel.add(deleteButton, BorderLayout.EAST);
        taskItemPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60)); // 60px height
        taskItemPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // Nice padding

        leftPanel.add(priorityLabel);
        leftPanel.add(Box.createRigidArea(new Dimension(8, 0))); // spacer

        TaskItem newTask = new TaskItem(text, priority, dueDate, checkBox, taskItemPanel);

        tasks.add(newTask);

        // Checkbox logic
        checkBox.addActionListener(e -> {
            completeTask(checkBox, label, priorityLabel, text, taskItemPanel, priority, dueDate);
            refreshUI();
        });

        // Delete logic
        deleteButton.addActionListener(e -> {
            tasks.removeIf(t -> t.panel == taskItemPanel);
            refreshUI();
        });

        refreshUI();
    }

    // Save tasks to file
    private void saveTasks() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (TaskItem task : tasks) {
                boolean isDone = task.checkBox.isSelected();
                String dueDateStr = (task.dueDate != null) ? task.dueDate.toString() : "";
                String line = (isDone ? "1" : "0") + "|" + task.priority + "|" + dueDateStr + "|" + task.text;
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error saving tasks: " + e.getMessage());
        }
    }

    // Load tasks from file
    private void loadTasks() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|", 4);
                if (parts.length == 4) {
                    boolean done = parts[0].equals("1");
                    String priority = parts[1].toUpperCase();
                    LocalDate dueDate = parts[2].isEmpty() ? null : LocalDate.parse(parts[2]);
                    String text = parts[3];
                    addTask(text, Priority.valueOf(priority), dueDate, done);
                }
            }
        } catch (IOException e) {
            // File might not exist yet
        }
    }

    // Clear all tasks from current taskboard
    private void clearTasks() {
        tasks.clear();
        taskPanel.removeAll();
        refreshUI();
    }

    // Refreshes the UI / taskboard
    private void refreshUI() {
        taskPanel.removeAll();

        // Separate completed and incomplete tasks
        ArrayList<TaskItem> incompleteTasks = new ArrayList<>();
        ArrayList<TaskItem> completedTasks = new ArrayList<>();

        for (TaskItem task : tasks) {
            if (task.checkBox.isSelected()) {
                completedTasks.add(task);
            } else {
                incompleteTasks.add(task);
            }
        }

        // Sort incomplete tasks by due date first, then priority
        incompleteTasks.sort((t1, t2) -> {
            // First: if one task has no due date, it goes after the one that does
            if (t1.dueDate == null && t2.dueDate != null) return 1;
            if (t1.dueDate != null && t2.dueDate == null) return -1;

            // If both have dates, compare dates
            if (t1.dueDate != null && t2.dueDate != null) {
                int dateCompare = t1.dueDate.compareTo(t2.dueDate);
                if (dateCompare != 0) {
                    return dateCompare;
                }
            }

            // Same due date or both null → compare priority
            return Integer.compare(priorityValue(t1.priority), priorityValue(t2.priority));
        });

        // Add incomplete tasks first
        for (TaskItem task : incompleteTasks) {
            taskPanel.add(task.panel);
        }

        // Then add completed tasks
        for (TaskItem task : completedTasks) {
            taskPanel.add(task.panel);
        }

        taskPanel.revalidate();
        taskPanel.repaint();
    }

    private void showAddTaskDialog() {
        JTextField taskNameField = new JTextField();
        String[] priorities = {"High", "Medium", "Low"};
        JComboBox<String> priorityBox = new JComboBox<>(priorities);
        JTextField dueDateField = new JTextField("yyyy-MM-dd");

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Task Name:"));
        panel.add(taskNameField);
        panel.add(new JLabel("Priority:"));
        panel.add(priorityBox);
        panel.add(new JLabel("Due Date (optional, yyyy-MM-dd):"));
        panel.add(dueDateField);

        int result = JOptionPane.showConfirmDialog(frame, panel, "Add Task",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String text = taskNameField.getText().trim();
            Priority priority = Priority.valueOf(((String) priorityBox.getSelectedItem()).toUpperCase());
            String dueDateStr = dueDateField.getText().trim();

            if (!text.isEmpty()) { // only task name is required now
                try {
                    boolean emptyDate = dueDateStr.isEmpty() || dueDateStr.equals("yyyy-MM-dd");
                    LocalDate dueDate = emptyDate ? null : LocalDate.parse(dueDateStr);
                    addTask(text, priority, dueDate, false);
                } catch (DateTimeParseException ex) {
                    JOptionPane.showMessageDialog(frame, "Invalid due date format. Use yyyy-MM-dd.");
                }
            }
        }
    }

    private void completeTask(JCheckBox checkBox, JLabel label, JLabel priorityLabel, String text, JPanel taskItemPanel, Priority priority, LocalDate dueDate) {
        if (checkBox.isSelected()) {
            label.setText("<html><strike>" + text + " (" + (dueDate != null ? dueDate.toString() : "No due date") + ")" + "</strike></html>");
            label.setForeground(Color.GRAY);
            label.setFont(label.getFont().deriveFont(Font.ITALIC));

            priorityLabel.setText("<html><strike>" + priority + "</strike></html>");
            priorityLabel.setForeground(Color.GRAY);
            priorityLabel.setFont(priorityLabel.getFont().deriveFont(Font.ITALIC));
        } else {
            label.setText(text + " (" + dueDate.toString() + ")");
            label.setForeground(Color.BLACK);
            label.setFont(label.getFont().deriveFont(Font.PLAIN));

            priorityLabel.setText(priority.toString());
            priorityLabel.setForeground(getPriorityColor(priority));
            priorityLabel.setFont(priorityLabel.getFont().deriveFont(Font.PLAIN));
        }

        // Move task to correct position (you already have this part)
        taskPanel.remove(taskItemPanel);
        tasks.removeIf(t -> t.panel == taskItemPanel);

        if (checkBox.isSelected()) {
            taskPanel.add(taskItemPanel); // Move to bottom
            tasks.add(new TaskItem(text, priority, dueDate, checkBox, taskItemPanel));
        } else {
            int insertIndex = 0;
            for (int i = 0; i < tasks.size(); i++) {
                if (tasks.get(i).checkBox.isSelected()) {
                    break;
                }
                insertIndex++;
            }
            taskPanel.add(taskItemPanel, insertIndex);
            tasks.add(insertIndex, new TaskItem(text, priority, dueDate, checkBox, taskItemPanel));
        }
    }

    private int priorityValue(Priority priority) {
        switch (priority) {
            case HIGH: return 0;
            case MEDIUM: return 1;
            case LOW: return 2;
            default: return 3;
        }
    }

    private Color getPriorityColor(Priority priority) {
        return switch (priority) {
            case HIGH -> Color.RED;
            case MEDIUM -> Color.ORANGE;
            case LOW -> Color.GREEN;
        };
    }

    // Helper class to represent each task item
    static class TaskItem {
        String text;
        Priority priority;
        LocalDate dueDate; // LocalDate import needed!
        JCheckBox checkBox;
        JPanel panel;

        TaskItem(String text, Priority priority, LocalDate dueDate, JCheckBox checkBox, JPanel panel) {
            this.text = text;
            this.priority = priority;
            this.dueDate = dueDate;
            this.checkBox = checkBox;
            this.panel = panel;
        }
    }

    enum Priority {
        HIGH, MEDIUM, LOW
    }

}
