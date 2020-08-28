package io.dsub.model;

import java.net.Socket;
import java.util.UUID;

public class Connection {
    private final UUID uuid;
    private final Socket socket;

    public Connection(Socket socket) {
        this.socket = socket;
        this.uuid = UUID.randomUUID();
    }

    public UUID getUuid() {
        return uuid;
    }

    public Socket getSocket() {
        return socket;
    }
}
