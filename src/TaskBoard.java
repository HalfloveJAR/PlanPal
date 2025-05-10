import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class TaskBoard {
    public final JFrame frame;
    private final JPanel taskPanel;
    private final JButton addButton, saveButton, loadButton, clearButton, newBoardButton;
    private final JScrollPane scrollPane;
    private final JPanel controlPanel, inputPanel;

    public TaskBoard() {
        frame = new JFrame("PlanPal");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);

        taskPanel = new JPanel();
        taskPanel.setLayout(new BoxLayout(taskPanel, BoxLayout.Y_AXIS));
        scrollPane = new JScrollPane(taskPanel);

        addButton = new JButton("Add Task");
        saveButton = new JButton("Save Tasks");
        loadButton = new JButton("Load Tasks");
        clearButton = new JButton("Clear Taskboard");
        newBoardButton = new JButton("New Taskboard");


        inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(addButton, BorderLayout.CENTER);

        controlPanel = new JPanel();
        controlPanel.add(saveButton);
        controlPanel.add(loadButton);
        controlPanel.add(clearButton);
        controlPanel.add(newBoardButton);

        frame.getContentPane().add(inputPanel, BorderLayout.NORTH);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        frame.getContentPane().add(controlPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> showAddTaskDialog());
        saveButton.addActionListener(e -> TaskManager.getInstance().saveTasks());
        loadButton.addActionListener(e -> {
            TaskManager.getInstance().clearTasks();
            taskPanel.removeAll();
            TaskManager.getInstance().loadTasks();
            refreshUI();
        });
        clearButton.addActionListener(e -> {
            TaskManager.getInstance().clearTasks();
            refreshUI();
        });
        newBoardButton.addActionListener(e -> TaskManager.getInstance().createNewTaskboard());


        TaskManager.getInstance().setTaskPanel(taskPanel);
        TaskManager.getInstance().loadTasks();

        refreshUI();

        frame.setVisible(true);

        for (TaskItem task : TaskManager.getInstance().tasks) {
            if (!task.isCompleted() && task.getDueDate() != null) {
                if (TaskManager.getInstance().isDueWithin24Hours(task.getDueDate())) {
                    JOptionPane.showMessageDialog(frame,
                            "Reminder: Task \"" + task.getText() + "\" is due soon!",
                            "Upcoming Task Due",
                            JOptionPane.WARNING_MESSAGE);
                }
                if (TaskManager.getInstance().isOverdue(task.getDueDate())) {
                    JOptionPane.showMessageDialog(frame,
                            "Reminder: Task \"" + task.getText() + "\" is overdue!",
                            "Task Overdue!",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }
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
            String dueDateStr = dueDateField.getText().trim();
            Priority priority = Priority.valueOf(((String) priorityBox.getSelectedItem()).toUpperCase());

            if (!text.isEmpty()) {
                try {
                    LocalDate dueDate = (dueDateStr.isEmpty() || dueDateStr.equals("yyyy-MM-dd"))
                            ? null
                            : LocalDate.parse(dueDateStr);
                    TaskManager.getInstance().addTask(text, priority, dueDate, false);
                    refreshUI();
                } catch (DateTimeParseException ex) {
                    JOptionPane.showMessageDialog(frame, "Invalid due date format. Use yyyy-MM-dd.");
                }
            }
        }
    }

    public void refreshUI() {
        taskPanel.removeAll();
        TaskManager.getInstance().sortTasks();

        for (TaskItem task : TaskManager.getInstance().getTasks()) {
            taskPanel.add(task.panel);
        }

        taskPanel.revalidate();
        taskPanel.repaint();
    }
}
