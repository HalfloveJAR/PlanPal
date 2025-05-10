import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class TaskManager {
    private static final TaskManager instance = new TaskManager();
    public final ArrayList<TaskItem> tasks = new ArrayList<>();
    private JPanel taskPanel;

    public TaskBoard activeTaskBoard = null;

    private TaskManager() {}

    public static TaskManager getInstance() {
        return instance;
    }

    public void setTaskPanel(JPanel panel) {
        this.taskPanel = panel;
    }

    public ArrayList<TaskItem> getTasks() {
        return tasks;
    }

    private String FILE_NAME = "tasks.txt";

    public void addTask(String text, Priority priority, LocalDate dueDate, boolean done) {
        JCheckBox checkBox = new JCheckBox();
        checkBox.setSelected(done);
        JLabel label = new JLabel(formatLabelText(text, dueDate));
        JLabel priorityLabel = new JLabel(priority.toString());
        JButton deleteButton = new JButton("Delete");

        if (done) {
            applyCompletedStyle(label, priorityLabel);
        } else {
            applyActiveStyle(label, priorityLabel, priority);
        }

        JPanel taskItemPanel = new JPanel(new BorderLayout());
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.add(checkBox);
        leftPanel.add(label);
        leftPanel.add(priorityLabel);
        leftPanel.add(Box.createRigidArea(new Dimension(8, 0)));

        taskItemPanel.add(leftPanel, BorderLayout.CENTER);
        taskItemPanel.add(deleteButton, BorderLayout.EAST);
        taskItemPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        taskItemPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        TaskItem newTask = new TaskItem(text, priority, dueDate, checkBox, label, priorityLabel, taskItemPanel);
        tasks.add(newTask);

        checkBox.addActionListener(e -> {
            toggleTaskState(newTask);
            sortTasks();
            refreshPanel();
        });

        deleteButton.addActionListener(e -> {
            tasks.remove(newTask);
            refreshPanel();
        });

        sortTasks();
        refreshPanel();
    }

    private void toggleTaskState(TaskItem task) {
        if (task.checkBox.isSelected()) {
            applyCompletedStyle(task.label, task.priorityLabel);
        } else {
            applyActiveStyle(task.label, task.priorityLabel, task.priority);
        }
        sortTasks();
        refreshPanel();
    }

    private void applyCompletedStyle(JLabel label, JLabel priorityLabel) {
        label.setText("<html><strike>" + label.getText() + "</strike></html>");
        label.setForeground(Color.GRAY);
        label.setFont(label.getFont().deriveFont(Font.ITALIC));

        priorityLabel.setText("<html><strike>" + priorityLabel.getText() + "</strike></html>");
        priorityLabel.setForeground(Color.GRAY);
        priorityLabel.setFont(priorityLabel.getFont().deriveFont(Font.ITALIC));
    }

    private void applyActiveStyle(JLabel label, JLabel priorityLabel, Priority priority) {
        label.setForeground(Color.BLACK);
        label.setFont(label.getFont().deriveFont(Font.PLAIN));
        priorityLabel.setText(priority.toString());
        priorityLabel.setForeground(getPriorityColor(priority));
        priorityLabel.setFont(priorityLabel.getFont().deriveFont(Font.PLAIN));
    }

    private String formatLabelText(String text, LocalDate dueDate) {
        return text + " (" + (dueDate != null ? dueDate.toString() : "No due date") + ")";
    }

    private Color getPriorityColor(Priority priority) {
        return switch (priority) {
            case HIGH -> Color.RED;
            case MEDIUM -> Color.ORANGE;
            case LOW -> Color.GREEN;
        };
    }

    public void saveTasks() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (TaskItem task : tasks) {
                boolean isDone = task.checkBox.isSelected();
                String dueDateStr = (task.dueDate != null) ? task.dueDate.toString() : "";
                writer.write((isDone ? "1" : "0") + "|" + task.priority + "|" + dueDateStr + "|" + task.text);
                writer.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving tasks: " + e.getMessage());
        }
    }

    public void loadTasks() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|", 4);
                if (parts.length == 4) {
                    boolean done = parts[0].equals("1");
                    Priority priority = Priority.valueOf(parts[1].toUpperCase());
                    LocalDate dueDate = parts[2].isEmpty() ? null : LocalDate.parse(parts[2]);
                    String text = parts[3];
                    addTask(text, priority, dueDate, done);
                }
            }
        } catch (IOException e) {
            // File might not exist; no action needed
        }
    }

    public void clearTasks() {
        tasks.clear();
        if (taskPanel != null) {
            taskPanel.removeAll();
            taskPanel.revalidate();
            taskPanel.repaint();
        }
    }

    public void sortTasks() {
        ArrayList<TaskItem> incomplete = new ArrayList<>();
        ArrayList<TaskItem> complete = new ArrayList<>();

        for (TaskItem task : tasks) {
            if (task.checkBox.isSelected()) {
                complete.add(task);
            } else {
                incomplete.add(task);
            }
        }

        incomplete.sort((a, b) -> {
            if (a.dueDate == null && b.dueDate != null) return 1;
            if (a.dueDate != null && b.dueDate == null) return -1;
            if (a.dueDate != null && b.dueDate != null) {
                int compare = a.dueDate.compareTo(b.dueDate);
                if (compare != 0) return compare;
            }
            return Integer.compare(priorityValue(a.priority), priorityValue(b.priority));
        });

        tasks.clear();
        tasks.addAll(incomplete);
        tasks.addAll(complete);
    }

    public void createNewTaskboard() {
        int index = 2;
        while (true) {
            String fileName = "tasks" + index + ".txt";
            File file = new File(fileName);
            if (!file.exists()) {
                FILE_NAME = fileName;
                tasks.clear();
                taskPanel.removeAll();
                saveTasks(); // create the new file immediately
                TaskManager.getInstance().activeTaskBoard.refreshUI();
                JOptionPane.showMessageDialog(TaskManager.getInstance().activeTaskBoard.frame, "New taskboard created: " + FILE_NAME);
                break;
            }
            index++;
        }
    }

    private int priorityValue(Priority priority) {
        return switch (priority) {
            case HIGH -> 0;
            case MEDIUM -> 1;
            case LOW -> 2;
        };
    }

    private void refreshPanel() {
        if (taskPanel != null) {
            taskPanel.removeAll();
            for (TaskItem task : tasks) {
                taskPanel.add(task.panel);
            }
            taskPanel.revalidate();
            taskPanel.repaint();
        }
    }

    boolean isDueWithin24Hours(LocalDate dueDate) {
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);
        return (dueDate.isAfter(today.minusDays(1)) && !dueDate.isAfter(tomorrow));
    }

    boolean isOverdue(LocalDate dueDate) {
        return dueDate.isBefore(LocalDate.now());
    }
}
