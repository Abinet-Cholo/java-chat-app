package core;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventBus {
    private final List<Subscriber> subscribers = new CopyOnWriteArrayList<>();

    public interface Subscriber {
        void onMessage(Message message, String user);
    }

    public void subscribe(Subscriber s) {
        subscribers.add(s);
    }

    public void publish(Message message, String user) {
        for (Subscriber s : subscribers) {
            s.onMessage(message, user);
        }
    }
}

