package io.dsub.model;

import io.dsub.config.AppConfig;

import java.util.UUID;

public class LocalMessage implements Message {
    private final String data;
    private final UUID target;

    public LocalMessage(String data) {
        this.data = data;
        this.target = AppConfig.APP_UUID;
    }

    @Override
    public String read() {
        return this.data;
    }

    @Override
    public UUID getUUID() {
        return null;
    }
}
