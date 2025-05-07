import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;
import org.json.JSONArray;

public class ToDoWindow extends JFrame {
    private String idToken;
    private String uid;
    private DefaultListModel<String> listModel;
    private JList<String> todoList;
    private JTextField inputField;

    public ToDoWindow(String uid, String idToken) {
        this.uid = uid;
        this.idToken = idToken;

        setTitle("To-Do List");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        listModel = new DefaultListModel<>();
        todoList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(todoList);
        scrollPane.setBounds(20, 20, 250, 180);
        add(scrollPane);

        inputField = new JTextField();
        inputField.setBounds(20, 210, 180, 25);
        add(inputField);

        JButton addButton = new JButton("Add");
        addButton.setBounds(210, 210, 60, 25);
        add(addButton);

        addButton.addActionListener(e -> {
            String task = inputField.getText().trim();
            if (!task.isEmpty()) {
                listModel.addElement(task);
                inputField.setText("");
                saveToFirebase();
            }
        });

        loadFromFirebase(); // Load existing tasks

        setVisible(true);
    }

    private void saveToFirebase() {
        try {
            JSONArray tasks = new JSONArray();
            for (int i = 0; i < listModel.size(); i++) {
                tasks.put(listModel.get(i));
            }

            String urlStr = "https://it488unit6newauthvers-default-rtdb.firebaseio.com/todos/" + uid + ".json?auth=" + idToken;
            //updated the FireBase API Key as referred to from last time
            HttpURLConnection conn = (HttpURLConnection) new URL(urlStr).openConnection();
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(tasks.toString().getBytes("UTF-8"));
            }

            conn.getInputStream().close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error saving to-do list:\n" + e.getMessage());
        }
    }

    private void loadFromFirebase() {
        try {
            String urlStr = "https://it488unit6newauthvers-default-rtdb.firebaseio.com/todos/" + uid + ".json?auth=" + idToken;
            //updated the FireBase API Key as referred to from last time
            HttpURLConnection conn = (HttpURLConnection) new URL(urlStr).openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String input;
            while ((input = in.readLine()) != null) {
                response.append(input);
            }
            in.close();

            JSONArray tasks = new JSONArray(response.toString());
            for (int i = 0; i < tasks.length(); i++) {
                listModel.addElement(tasks.getString(i));
            }
        } catch (Exception e) {
            // No to-dos yet or JSON is empty — skip error
        }
    }
}
