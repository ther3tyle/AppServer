package io.dsub.resource;

import io.dsub.model.Connection;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class ConnectionMap extends ConcurrentHashMap<UUID, Connection> {

    ///////////////////////////////////////////////////////////////////////////
    // singleton
    ///////////////////////////////////////////////////////////////////////////
    private ConnectionMap(){}
    private static final ConnectionMap instance = new ConnectionMap();
    public static ConnectionMap getInstance() {
        return instance;
    }

    private static final Logger logger = Logger.getLogger(ConnectionMap.class.getName());

    ///////////////////////////////////////////////////////////////////////////
    // methods
    ///////////////////////////////////////////////////////////////////////////
    @Override
    public Connection put(UUID key, Connection value) {
        logger.info("input...");
        if (value != null) {
            return super.put(key, value);
        }
        logger.info("found null!");
        return null;
    }
}
