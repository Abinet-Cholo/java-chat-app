package di;

import com.google.inject.AbstractModule;
import core.EventBus;
import core.MessageFactory;
import db.Database;

import java.sql.SQLException;

public class ChatModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(EventBus.class).asEagerSingleton();
        bind(MessageFactory.class).asEagerSingleton();
        try {
            bind(Database.class).toInstance(new Database("chat.db"));
        } catch (SQLException e) {
            throw new RuntimeException("Failed to init DB", e);
        }
    }
}

