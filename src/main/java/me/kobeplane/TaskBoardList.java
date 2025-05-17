package me.kobeplane;

import me.kobeplane.data.TaskboardsData;

import javax.swing.*;
import java.awt.*;

public class TaskBoardList {

    JFrame frame;

    public TaskBoardList() {
        frame = new JFrame("PlanPal - Available Taskboards");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setLocationRelativeTo(null); // Center on screen

        // Main layout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Top panel with the title (pinned)
        JPanel topPanel = new JPanel();
        JLabel title = new JLabel("Select a Taskboard");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        topPanel.add(title);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // List panel (scrollable)
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));

        // Populate taskboard buttons
        try {
            java.util.List<TaskboardsData> boards = Main.taskboardsService.getTaskboardsForUser(Main.userData);
            for (TaskboardsData board : boards) {
                JButton boardButton = new JButton(board.getName());
                boardButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                boardButton.addActionListener(e -> {
                    TaskManager.getInstance().openTaskboard(frame, board);
                });
                listPanel.add(boardButton);
                listPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setBorder(null);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Bottom panel with "Create New Taskboard" button
        JPanel bottomPanel = new JPanel();
        JButton newBoardButton = new JButton("Create New Taskboard");
        JButton logoutButton = new JButton("Logout");
        newBoardButton.addActionListener(e -> {
            TaskManager.getInstance().createNewTaskboardFromList(frame);
        });
        logoutButton.addActionListener(e -> Main.logout(frame));
        bottomPanel.add(newBoardButton);
        bottomPanel.add(logoutButton);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Show frame
        frame.setContentPane(mainPanel);
        frame.setVisible(true);
    }
}
