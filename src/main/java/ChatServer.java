import com.google.inject.Guice;
import com.google.inject.Injector;
import core.*;
import db.Database;
import di.ChatModule;

import java.io.*;
import java.net.*;
import java.sql.SQLException;

public class ChatServer {
    private static final int PORT = 1234;

    public static void main(String[] args) throws Exception {
        Injector injector = Guice.createInjector(new ChatModule());
        EventBus bus = injector.getInstance(EventBus.class);
        MessageFactory factory = injector.getInstance(MessageFactory.class);
        Database db = injector.getInstance(Database.class);
        ConnectionManager cm = ConnectionManager.getInstance();

        // Subscribe for message broadcasting
        bus.subscribe((message, user) -> {
            for (PrintWriter writer : cm.getClients()) {
                writer.println(user + ": " + message.format());
                writer.flush();
            }
            try {
                db.saveMessage(user, message.format());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Chat server started on port " + PORT);

        while (true) {
            Socket socket = serverSocket.accept();
            new Thread(new ClientHandler(socket, bus, factory, db, cm)).start();
        }
    }
}

class ClientHandler implements Runnable {
    private Socket socket;
    private EventBus bus;
    private MessageFactory factory;
    private Database db;
    private ConnectionManager cm;
    private PrintWriter out;
    private BufferedReader in;
    private String username;

    public ClientHandler(Socket socket, EventBus bus, MessageFactory factory, Database db, ConnectionManager cm) {
        this.socket = socket;
        this.bus = bus;
        this.factory = factory;
        this.db = db;
        this.cm = cm;
    }

    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            // ======= Authentication Flow =======
            boolean authenticated = false;
            while (!authenticated) {
                out.println("Type 'login' or 'signup':");
                out.flush();
                String choice = in.readLine();
                if (choice == null) return; // client closed

                if (choice.equalsIgnoreCase("signup")) {
                    out.println("Enter new username:");
                    out.flush();
                    String uname = in.readLine();

                    out.println("Enter new password:");
                    out.flush();
                    String pw = in.readLine();

                    if (db.registerUser(uname, pw)) {
                        out.println("✅ Signup successful! You can now log in.");
                        out.flush();
                    } else {
                        out.println("❌ Username already exists. Try again.");
                        out.flush();
                        continue; // back to start
                    }

                } else if (choice.equalsIgnoreCase("login")) {
                    out.println("Enter username:");
                    out.flush();
                    String uname = in.readLine();

                    out.println("Enter password:");
                    out.flush();
                    String pw = in.readLine();

                    if (db.authenticate(uname, pw)) {
                        this.username = uname;
                        authenticated = true;
                        out.println("✅ Auth success! Welcome " + uname);
                        out.flush();
                    } else {
                        out.println("❌ Invalid credentials. Try again.");
                        out.flush();
                    }

                } else {
                    out.println("Invalid choice. Please type 'login' or 'signup'.");
                    out.flush();
                }
            }
            // ======= End Authentication =======

            cm.addClient(out);

            // ======= Chat Loop =======
            String input;
            while ((input = in.readLine()) != null) {
                if (input.equals("/quit")) break;
                Message msg = factory.create(input);
                bus.publish(msg, username);
            }

        } catch (Exception e) {
            System.out.println("Client error: " + e.getMessage());
        } finally {
            cm.removeClient(out);
            try {
                socket.close();
            } catch (IOException ignored) {}
        }
    }
}
