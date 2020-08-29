package io.dsub.model;

import java.net.Socket;
import java.util.UUID;

public class Connection {
    private final UUID uuid;
    private final Socket socket;

    public Connection(UUID uuid, Socket socket) {
        this.uuid = uuid;
        this.socket = socket;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Socket getSocket() {
        return socket;
    }
}
