package project;

import java.sql.*;

public class Database {

    public static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:issues.db");
            Statement st = conn.createStatement();
            st.execute("CREATE TABLE IF NOT EXISTS users (username TEXT, password TEXT)");
            st.execute("CREATE TABLE IF NOT EXISTS complaints (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, description TEXT, zone TEXT)");
        } catch (Exception e) {
            System.out.println("Database error: " + e.getMessage());
        }
        return conn;
    }
}