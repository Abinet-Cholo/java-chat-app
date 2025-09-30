package db;

import java.sql.*;

public class Database {
    private final Connection conn;

    public Database(String dbFile) throws SQLException {
        conn = DriverManager.getConnection("jdbc:sqlite:" + dbFile);
        try (Statement st = conn.createStatement()) {
            st.execute("CREATE TABLE IF NOT EXISTS messages (id INTEGER PRIMARY KEY AUTOINCREMENT, user TEXT, content TEXT, ts TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");
            st.execute("CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT UNIQUE, password TEXT)");
        }
    }

    public void saveMessage(String user, String content) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO messages(user, content) VALUES (?, ?)")) {
            ps.setString(1, user);
            ps.setString(2, content);
            ps.executeUpdate();
        }
    }

    public boolean registerUser(String username, String password) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO users(username, password) VALUES (?, ?)")) {
            ps.setString(1, username);
            ps.setString(2, password);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false; // username already exists
        }
    }

    public boolean authenticate(String username, String password) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE username=? AND password=?")) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
    }
}

