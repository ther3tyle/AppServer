package io.dsub.model;

import io.dsub.resource.AppConfig;

import java.util.UUID;

public class AppMessage implements Message {

    private final UUID dest;
    private final String data;

    public AppMessage(UUID dest, String data) {
        this.dest = dest;
        this.data = data;
    }

    @Override
    public UUID getDest() {
        return this.dest;
    }

    @Override
    public UUID getSource() {
        return AppConfig.getInstance().getAppID();
    }

    @Override
    public String getData() {
        return this.data;
    }
}
