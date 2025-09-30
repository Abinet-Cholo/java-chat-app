import org.junit.jupiter.api.*;
import java.io.*;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

public class ChatServerTest {
    private static Thread serverThread;

    @BeforeAll
    static void startServer() {
        // Start the server in a background thread
        serverThread = new Thread(() -> {
            try {
                ChatServer.main(new String[]{});
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        serverThread.setDaemon(true); // won't block JVM exit
        serverThread.start();

        // Give server a moment to start
        try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
    }

    @Test
    void testClientSignupLoginAndMessage() throws Exception {
        try (Socket socket = new Socket("localhost", 1234)) {
            assertTrue(socket.isConnected(), "Client should connect to server");

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Step 1: See login/signup prompt
            String prompt = in.readLine();
            assertTrue(prompt.toLowerCase().contains("login") && prompt.toLowerCase().contains("signup"));

            // Step 2: Signup a new user
            out.println("signup");
            assertTrue(in.readLine().toLowerCase().contains("username"));
            out.println("testuser");
            assertTrue(in.readLine().toLowerCase().contains("password"));
            out.println("secret123");

            String signupResponse = in.readLine();
            assertTrue(signupResponse.contains("Signup"), "Should confirm signup");

            // Step 3: Back at login/signup prompt
            String prompt2 = in.readLine();
            assertTrue(prompt2.toLowerCase().contains("login"));

            // Step 4: Login with that user
            out.println("login");
            assertTrue(in.readLine().toLowerCase().contains("username"));
            out.println("testuser");
            assertTrue(in.readLine().toLowerCase().contains("password"));
            out.println("secret123");

            String loginResponse = in.readLine();
            assertTrue(loginResponse.contains("Auth success"), "Login should succeed");

            // Step 5: Send a chat message
            out.println("Hello world!");
            String response = in.readLine();
            assertNotNull(response, "Server should echo a message back");
            assertTrue(response.contains("Hello world!"), "Message should be broadcast");
        }
    }
}
