package core;

import java.io.PrintWriter;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    private static final ConnectionManager instance = new ConnectionManager();
    private final Set<PrintWriter> clients = ConcurrentHashMap.newKeySet();

    private ConnectionManager() {}

    public static ConnectionManager getInstance() {
        return instance;
    }

    public void addClient(PrintWriter writer) {
        clients.add(writer);
    }

    public void removeClient(PrintWriter writer) {
        clients.remove(writer);
    }

    public Set<PrintWriter> getClients() {
        return clients;
    }
}

