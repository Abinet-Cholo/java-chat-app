import db.Database;
import org.junit.jupiter.api.*;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseTest {
    private static Database db;

    @BeforeAll
    static void setup() throws SQLException {
        // Use a separate test DB so you don't pollute chat.db
        db = new Database("test.db");
    }

    @Test
    void testUserSignupAndLogin() throws SQLException {
        String username = "alice";
        String password = "secret";

        // Sign up
        boolean registered = db.registerUser(username, password);
        assertTrue(registered);

        // Duplicate signup should fail
        boolean registeredAgain = db.registerUser(username, password);
        assertFalse(registeredAgain);

        // Login with correct password
        assertTrue(db.authenticate(username, password));

        // Login with wrong password
        assertFalse(db.authenticate(username, "wrongpw"));
    }

    @Test
    void testSaveMessage() throws SQLException {
        db.saveMessage("alice", "Hello world!");
        // If no exception, we consider it success
        assertTrue(true);
    }
}

