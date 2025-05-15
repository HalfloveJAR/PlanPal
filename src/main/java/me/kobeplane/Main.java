package me.kobeplane;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import io.github.cdimascio.dotenv.Dotenv;
import me.kobeplane.data.TaskboardsService;
import me.kobeplane.data.TasksData;
import me.kobeplane.data.TasksService;
import me.kobeplane.data.UserService;

import javax.swing.*;

public class Main {

    public static Dotenv dotenv;
    static UserService userService;
    static TaskboardsService taskboardsService;
    static TasksService tasksService;

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

}
