package io.dsub.model;

import java.util.UUID;

public class Message {
    private final UUID origin;
    private final UUID dest;
    private final String data;

    public Message(UUID origin, UUID dest, String data) {
        this.origin = origin;
        this.dest = dest;
        this.data = data;
    }

    public UUID getOrigin() {
        return origin;
    }

    public UUID getDest() {
        return dest;
    }

    public String getData() {
        return data;
    }
}
