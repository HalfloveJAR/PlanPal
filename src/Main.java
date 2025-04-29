import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.json.JSONObject;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Firebase Login");
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        // Center the window on the screen
        frame.setLocationRelativeTo(null);

        JTextField emailField = new JTextField();
        emailField.setBounds(50, 30, 200, 25);
        emailField.setToolTipText("Email");

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(50, 70, 200, 25);
        passwordField.setToolTipText("Password");

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(100, 110, 100, 25);

        frame.add(emailField);
        frame.add(passwordField);
        frame.add(loginButton);

        frame.setVisible(true);

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());

                try {
                    // Call FirebaseAuthHelper to sign in
                    JSONObject result = FirebaseAuthHelper.signIn(email, password);
                    String idToken = result.getString("idToken");
                    String uid = result.getString("localId");

                    // Close the login window and open the ToDo window
                    frame.dispose();
                    new ToDoWindow(uid, idToken);

                    // Clear password field
                    passwordField.setText("");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "Login failed:\n" + ex.getMessage());
                }
            }
        });
    }
}
