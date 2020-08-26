package io.dsub.model;

import java.util.UUID;

public class RemoteMessage implements Message {

    private final String data;
    private final UUID uuid;

    public RemoteMessage(String data, UUID uuid) {
        this.data = data;
        this.uuid = uuid;
    }

    @Override
    public String read() {
        return data;
    }

    @Override
    public UUID getUUID() {
        return uuid;
    }
}
