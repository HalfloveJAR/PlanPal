package me.kobeplane;

import io.github.cdimascio.dotenv.Dotenv;

import javax.swing.*;

public class Main {

    public static Dotenv dotenv;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginPage::new);
        dotenv = Dotenv.configure().load();
    }

}
