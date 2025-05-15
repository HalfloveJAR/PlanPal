package me.kobeplane;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import io.github.cdimascio.dotenv.Dotenv;
import me.kobeplane.data.*;

import javax.swing.*;
import java.sql.SQLException;

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
            String address = "localhost";
            String user = "KMP-MAIN\\SQLEXPRESS";
            String pass = "";
            String database = "";
            try {
                //new UserService("jdbc:mysql://localhost;databaseName=planpal;integratedSecurity=true");
                //userService = new UserService("jdbc:mysql://" + user + ":" + pass + "@" + address + "/" + database);
                ConnectionSource connectionSource = new JdbcConnectionSource("jdbc:sqlite:" + System.getProperty("user.dir") + "/planpal.db");
                userService = new UserService(connectionSource);
                taskboardsService = new TaskboardsService(connectionSource);
                tasksService = new TasksService(connectionSource);
            } catch (Exception ex) {
                ex.printStackTrace();
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

    /*static TaskboardsData getValidTaskboardData(String ID) throws SQLException {
        TaskboardsData taskboardsData = taskboardsService.getTaskboardData(ID);
        if (taskboardsData == null) {
            taskboardsService.add
        }
    }*/

}
