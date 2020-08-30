package io.dsub.model;

import java.util.UUID;

public class UserMessage implements Message {

    private final UUID dest;
    private final UUID source;
    private final String data;

    public UserMessage(UUID dest, UUID source, String data) {
        this.dest = dest;
        this.source = source;
        this.data = data;
    }

    @Override
    public UUID getDest() {
        return this.dest;
    }

    @Override
    public UUID getSource() {
        return this.source;
    }

    @Override
    public String getData() {
        return this.data;
    }
}
