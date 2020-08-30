package io.dsub.model;

import java.net.Socket;
import java.util.UUID;

public class User extends Connection {
    public User(UUID uuid, Socket socket) {
        super(uuid, socket);
    }


}
