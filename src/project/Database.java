package project;

import java.security.MessageDigest;
import java.sql.*;

public class Database
{

    public static final String ADMIN_USER = "admin";
    public static final String ADMIN_PASS = "admin123";

    public static Connection connect()
    {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:issues.db");
            Statement st = conn.createStatement();

            st.execute("CREATE TABLE IF NOT EXISTS users (username TEXT, password TEXT)");

            st.execute("CREATE TABLE IF NOT EXISTS complaints (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "title TEXT, " +
                    "description TEXT, " +
                    "zone TEXT, " +
                    "status TEXT DEFAULT 'Pending', " +
                    "submitted_by TEXT)");

            // add columns to existing db if they dont exist yet
            try { st.execute("ALTER TABLE complaints ADD COLUMN status TEXT DEFAULT 'Pending'"); }
            catch (Exception ignored) {}

            try { st.execute("ALTER TABLE complaints ADD COLUMN submitted_by TEXT"); }
            catch (Exception ignored) {}

        } catch (Exception e) {
            System.out.println("Database error: " + e.getMessage());
        }
        return conn;
    }

    public static String hashPassword(String password)
    {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes)
            {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            return password;
        }
    }

    public static boolean validateAdmin(String user, String pass)
    {
        return user.equals(ADMIN_USER) && hashPassword(pass).equals(hashPassword(ADMIN_PASS));
    }

    public static int getCount(String status)
    {
        try {
            Connection conn = connect();
            PreparedStatement ps;
            if (status.equals("Total"))
            {
                ps = conn.prepareStatement("SELECT COUNT(*) FROM complaints");
            }
            else
            {
                ps = conn.prepareStatement("SELECT COUNT(*) FROM complaints WHERE status=?");
                ps.setString(1, status);
            }
            ResultSet rs = ps.executeQuery();
            int count = rs.next() ? rs.getInt(1) : 0;
            conn.close();
            return count;
        } catch (Exception e) {
            return 0;
        }
    }

    public static boolean updateStatus(int id, String status)
    {
        try {
            Connection conn = connect();
            PreparedStatement ps = conn.prepareStatement(
                    "UPDATE complaints SET status=? WHERE id=?");
            ps.setString(1, status);
            ps.setInt(2, id);
            ps.executeUpdate();
            conn.close();
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}