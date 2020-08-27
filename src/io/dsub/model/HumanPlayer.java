package io.dsub.model;

import java.net.Socket;

public abstract class HumanPlayer {
    private final Socket socket;

    public HumanPlayer(Socket socket) {
        this.socket = socket;
    }
}
