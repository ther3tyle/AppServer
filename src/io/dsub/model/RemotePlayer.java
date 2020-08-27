package io.dsub.model;

import java.net.Socket;

public class RemotePlayer extends HumanPlayer {
    public RemotePlayer(Socket socket) {
        super(socket);
    }
}
