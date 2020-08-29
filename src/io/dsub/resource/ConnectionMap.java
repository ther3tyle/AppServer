package io.dsub.resource;

import io.dsub.model.Connection;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionMap extends ConcurrentHashMap<UUID, Connection> {

    private ConnectionMap(){}
    private static final ConnectionMap instance = new ConnectionMap();
    public static ConnectionMap getInstance() {
        return instance;
    }

    public void put(Connection connection) {
        this.put(connection.getUuid(), connection);
    }

    @Override
    public Connection put(UUID key, Connection value) {
        if (value != null) {
            return super.put(key, value);
        }
        return null;
    }
}
