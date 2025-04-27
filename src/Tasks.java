import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

public class Tasks {
    private final JFrame frame;
    private final JPanel taskPanel;
    private final JTextField taskField;
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

        taskField = new JTextField();
        JButton addButton = new JButton("Add Task");
        JButton saveButton = new JButton("Save Tasks");
        JButton loadButton = new JButton("Load Tasks");
        JButton clearButton = new JButton("Clear Taskboard");

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(taskField, BorderLayout.CENTER);
        inputPanel.add(addButton, BorderLayout.EAST);

        JPanel controlPanel = new JPanel();
        controlPanel.add(saveButton);
        controlPanel.add(loadButton);
        controlPanel.add(clearButton);

        frame.getContentPane().add(inputPanel, BorderLayout.NORTH);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        frame.getContentPane().add(controlPanel, BorderLayout.SOUTH);

        // Add task logic
        addButton.addActionListener(e -> {
            String text = taskField.getText().trim();
            if (!text.isEmpty()) {
                addTask(text, false);
                taskField.setText("");
            }
        });

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
    private void addTask(String text, boolean done) {
        JCheckBox checkBox = new JCheckBox();
        checkBox.setSelected(done);
        JLabel label = new JLabel(text);
        JButton deleteButton = new JButton("Delete");

        JPanel taskItemPanel = new JPanel(new BorderLayout());
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.add(checkBox);
        leftPanel.add(label);
        taskItemPanel.add(leftPanel, BorderLayout.CENTER);
        taskItemPanel.add(deleteButton, BorderLayout.EAST);

        TaskItem newTask = new TaskItem(text, checkBox, taskItemPanel);

        if (done) {
            // Add to the end if already marked as done
            taskPanel.add(taskItemPanel);
            tasks.add(newTask);
        } else {
            // Insert before the first completed task
            int insertIndex = 0;
            for (int i = 0; i < tasks.size(); i++) {
                if (tasks.get(i).checkBox.isSelected()) {
                    break;
                }
                insertIndex++;
            }
            taskPanel.add(taskItemPanel, insertIndex);
            tasks.add(insertIndex, newTask);
        }

        // Toggle strike-through when checkbox is clicked
        checkBox.addActionListener(e -> {
            if (checkBox.isSelected()) {
                label.setText("<html><strike>" + text + "</strike></html>");
            } else {
                label.setText(text);
            }

            // Remove from current position
            taskPanel.remove(taskItemPanel);
            tasks.removeIf(t -> t.panel == taskItemPanel);

            // Re-add at the end if done, or before first done task if not
            if (checkBox.isSelected()) {
                taskPanel.add(taskItemPanel); // add to bottom
                tasks.add(new TaskItem(text, checkBox, taskItemPanel));
            } else {
                // Insert before the first completed task (if any)
                int insertIndex = 0;
                for (int i = 0; i < tasks.size(); i++) {
                    if (tasks.get(i).checkBox.isSelected()) {
                        break;
                    }
                    insertIndex++;
                }
                taskPanel.add(taskItemPanel, insertIndex);
                tasks.add(insertIndex, new TaskItem(text, checkBox, taskItemPanel));
            }

            // Refresh UI
            refreshUI();
        });

        // Delete task
        deleteButton.addActionListener(e -> {
            taskPanel.remove(taskItemPanel);
            tasks.removeIf(t -> t.panel == taskItemPanel);
            refreshUI();
        });

        // Set initial strike-through
        if (done) {
            label.setText("<html><strike>" + text + "</strike></html>");
        }

        refreshUI();
    }

    // Save tasks to file
    private void saveTasks() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (TaskItem task : tasks) {
                boolean isDone = task.checkBox.isSelected();
                String line = (isDone ? "1" : "0") + "|" + task.text;
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
                String[] parts = line.split("\\|", 2);
                if (parts.length == 2) {
                    boolean done = parts[0].equals("1");
                    String text = parts[1];
                    addTask(text, done);
                }
            }
        } catch (IOException e) {
            // File might not exist yet — that's okay
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
        taskPanel.revalidate();
        taskPanel.repaint();
    }

    // Helper class to represent each task item
    static class TaskItem {
        String text;
        JCheckBox checkBox;
        JPanel panel;

        TaskItem(String text, JCheckBox checkBox, JPanel panel) {
            this.text = text;
            this.checkBox = checkBox;
            this.panel = panel;
        }
    }
}
