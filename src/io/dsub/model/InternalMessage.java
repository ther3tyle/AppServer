package io.dsub.model;

import io.dsub.resource.AppConfig;

import java.util.UUID;

public class InternalMessage implements Message {

    public InternalMessage(String data) {
        this.data = data;
    }

    private final String data;

    @Override
    public UUID getDest() {
        return AppConfig.getInstance().getAppID();
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
