package me.kobeplane;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Arrays;

public class TaskBoardList {

    JPanel panel;
    JFrame frame;

    public TaskBoardList() {
        frame = new JFrame("PlanPal - Available Taskboards");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setLocationRelativeTo(null); // Center on screen

        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // padding

        // Title
        JLabel title = new JLabel("Select a Taskboard");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(title);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        // List existing taskboards
        File dir = new File("taskboards");
        if (!dir.exists()) dir.mkdirs();
        File[] files = dir.listFiles((d, name) -> name.endsWith(".txt"));
        if (files != null) {
            Arrays.sort(files);
            for (File file : files) {
                JButton boardButton = new JButton(file.getName());
                boardButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                boardButton.addActionListener(e -> {
                    TaskManager.getInstance().openTaskboard(file.getName(), frame);
                });
                panel.add(boardButton);
                panel.add(Box.createRigidArea(new Dimension(0, 5)));
            }
        }

        // Add "Create New Taskboard" button
        JButton newBoardButton = new JButton("Create New Taskboard");
        newBoardButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        newBoardButton.addActionListener(e -> {
            TaskManager.getInstance().createNewTaskboardFromList(frame);
        });
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(newBoardButton);

        frame.setContentPane(panel);
        frame.setVisible(true);
    }
}