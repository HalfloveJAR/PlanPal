import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginPage extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public LoginPage() {
        setTitle("PlanPal - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(426, 240);
        setLocationRelativeTo(null); // Center on screen

        JPanel contentPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        usernameField = new JTextField();
        passwordField = new JPasswordField();
        loginButton = new JButton("Login");

        contentPanel.add(new JLabel("Username:"));
        contentPanel.add(usernameField);
        contentPanel.add(new JLabel("Password:"));
        contentPanel.add(passwordField);
        contentPanel.add(loginButton);

        add(contentPanel);

        loginButton.addActionListener(e -> {
            String user = usernameField.getText();
            String pass = new String(passwordField.getPassword());

            // Placeholder login logic
            // Hook up firebase login here
            if (user.equals("admin") && pass.equals("password")) {
                showLoginSuccess();
            } else {
                showLoginError();
            }
        });

        setVisible(true);
    }

    private void showLoginSuccess() {
        JOptionPane.showMessageDialog(this, "Login successful!");
        dispose(); // Close login window
        SwingUtilities.invokeLater(TaskBoard::new); // Open taskboard after closing login page
    }

    private void showLoginError() {
        JOptionPane.showMessageDialog(this, "Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginPage::new);
    }
}
