package me.kobeplane;

import org.json.JSONObject;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginPage extends JFrame {

    private final JTextField emailField;
    private final JPasswordField passwordField;
    private final JButton loginButton;

    public LoginPage() {
        setTitle("PlanPal - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(426, 240);
        setLocationRelativeTo(null); // Center on screen
        JPanel contentPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        emailField = new JTextField();
        passwordField = new JPasswordField();
        loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register"); // ** UNIT 8 NEW **

        contentPanel.add(new JLabel("Email:"));
        contentPanel.add(emailField);
        contentPanel.add(new JLabel("Password:"));
        contentPanel.add(passwordField);
        contentPanel.add(loginButton);
        contentPanel.add(registerButton); // ** UNIT 8 NEW: register button  **
        add(contentPanel);

        // Existing login logic
        loginButton.addActionListener(e -> {
            String email = emailField.getText();
            String pass = new String(passwordField.getPassword());

            // Attempt to login using Firebase
            try {
                // Call FirebaseAuthHelper to sign in
                JSONObject result = FirebaseAuthHelper.signIn(email, pass);
                String idToken = result.getString("idToken");
                String uid = result.getString("localId");

                // Clear password field
                passwordField.setText("");

                // If no errors occurred, login was successful
                showLoginSuccess();
            } catch (Exception ex) {
                // If we run into an error, the login was not successful
                ex.printStackTrace();
                showLoginError();
            }
        });

        // **UNIT 8 NEW: Register logic**
        registerButton.addActionListener(e -> {
            String email = emailField.getText();
            String pass = new String(passwordField.getPassword());

            try {
                JSONObject result = FirebaseAuthHelper.signUp(email, pass);
                String uid = result.getString("localId");

                JOptionPane.showMessageDialog(this, "Registration successful! You can now log in.");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Registration failed: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        setVisible(true);
    }

    private void showLoginSuccess() {
        JOptionPane.showMessageDialog(this, "Login successful!");
        dispose(); // Close login window
        SwingUtilities.invokeLater(TaskBoardList::new);
    }

    private void showLoginError() {
        JOptionPane.showMessageDialog(this, "Invalid email or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
    }

}