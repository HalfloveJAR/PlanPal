package me.kobeplane;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import io.github.cdimascio.dotenv.Dotenv;
import me.kobeplane.data.*;

import javax.swing.*;
import java.sql.SQLException;
import java.util.stream.Stream;

public class Main {

    public static Dotenv dotenv;
    static UserService userService;
    static TaskboardsService taskboardsService;
    static TasksService tasksService;

    static UserData userData;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginPage::new);
        dotenv = Dotenv.configure().load();
        setupDatabase();
    }

    private static void setupDatabase() {
        boolean useMysql = true;
        if (useMysql) {
            String address = dotenv.get("SQL_ADDRESS");
            String user = dotenv.get("SQL_USER");
            String pass = dotenv.get("SQL_PASS");
            String database = "planpal";
            try {
                String connectionUrl;
                ConnectionSource connectionSource;
                if (Stream.of(address, user, pass, database).anyMatch(s -> s == null || s.trim().isEmpty())) {
                    connectionUrl = "jdbc:sqlite:" + System.getProperty("user.dir") + "/planpal.db";
                    connectionSource = new JdbcConnectionSource(connectionUrl);
                } else {
                    connectionUrl = "jdbc:mysql://" + address + ":3306/" + database;
                    connectionSource = new JdbcConnectionSource(connectionUrl, user, pass);
                }
                userService = new UserService(connectionSource);
                taskboardsService = new TaskboardsService(connectionSource);
                tasksService = new TasksService(connectionSource);
            } catch (Exception ex) {
                System.out.println("Connection to database failed, you might have incorrectly defined database credentials in your .env file");
                System.out.println("Error: " + ex.getMessage());
            }
        }
    }

    static UserData getValidUserData(String email) throws SQLException {
        UserData userData = userService.getUserData(email);
        if (userData == null) {
            userService.addUser(email);
            userData = userService.getUserData(email);
        }
        return userData;
    }

    static void logout(JFrame frame) {
        frame.dispose();
        userData = null;
        TaskManager.getInstance().activeTaskBoard = null;
        TaskManager.getInstance().tasks.clear();
        SwingUtilities.invokeLater(LoginPage::new);
    }

}
