package io.dsub.resource;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class AppConfig {

    ///////////////////////////////////////////////////////////////////////////
    // singleton
    ///////////////////////////////////////////////////////////////////////////
    private AppConfig(){}
    private static final AppConfig instance = new AppConfig();
    public static AppConfig getInstance() {
        return instance;
    }

    ///////////////////////////////////////////////////////////////////////////
    // fields
    ///////////////////////////////////////////////////////////////////////////
    private AtomicInteger port = new AtomicInteger(5000);
    private final int latchCount = 2;
    private final int queueSize = 10;
    private final UUID appID = UUID.randomUUID();

    ///////////////////////////////////////////////////////////////////////////
    // getters and setters
    ///////////////////////////////////////////////////////////////////////////
    public int getPort() {
        return port.get();
    }

    // todo: do we need this? seriously?
    public void setPort(int port) {
        this.port.set(port);
    }

    public int getLatchCount() {
        return latchCount;
    }

    public int getQueueSize() {
        return queueSize;
    }

    public UUID getAppID() {
        return appID;
    }

    ///////////////////////////////////////////////////////////////////////////
    // methods
    ///////////////////////////////////////////////////////////////////////////
    public int getNextPort() {
        return port.incrementAndGet();
    }
}
