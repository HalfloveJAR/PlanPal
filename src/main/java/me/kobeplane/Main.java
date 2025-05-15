package me.kobeplane;

import io.github.cdimascio.dotenv.Dotenv;
import me.kobeplane.data.UserService;

import javax.swing.*;

public class Main {

    public static Dotenv dotenv;
    static UserService userService;

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
                userService = new UserService("jdbc:sqlite:" + System.getProperty("user.dir") + "/planpal.db");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

}
