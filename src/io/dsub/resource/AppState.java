package io.dsub.resource;

import io.dsub.model.Connection;
import io.dsub.model.Message;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class AppState {

    ///////////////////////////////////////////////////////////////////////////
    // singleton
    ///////////////////////////////////////////////////////////////////////////
    private AppState() {}
    private static final AppState instance = new AppState();
    public static AppState getInstance() {
        return instance;
    }

    ///////////////////////////////////////////////////////////////////////////
    // instance fields
    ///////////////////////////////////////////////////////////////////////////

    private final AppConfig appConfig = AppConfig.getInstance();
    private final AtomicBoolean isActive = new AtomicBoolean(true);
    private final CountDownLatch latch = new CountDownLatch(AppConfig.getInstance().getLatchCount());
    private final ExecutorService execService = Executors.newFixedThreadPool(16);
    private final ConcurrentMap<UUID, Connection> connMap = new ConcurrentHashMap<>();

    private final BlockingQueue<UUID> closeConnectionQueue =
            new ArrayBlockingQueue<>(appConfig.getQueueSize());
    private final BlockingQueue<Connection> initConnectionQueue =
            new ArrayBlockingQueue<>(appConfig.getQueueSize());
    private final BlockingQueue<Message> inboundMessageQueue =
            new ArrayBlockingQueue<>(appConfig.getQueueSize());

    ///////////////////////////////////////////////////////////////////////////
    // getters
    ///////////////////////////////////////////////////////////////////////////
    public AtomicBoolean getIsActive() {
        return isActive;
    }

    public CountDownLatch getLatch() {
        return latch;
    }

    public ExecutorService getExecService() {
        return execService;
    }

    public ConcurrentMap<UUID, Connection> getConnMap() {
        return connMap;
    }

    public BlockingQueue<UUID> getCloseConnectionQueue() {
        return closeConnectionQueue;
    }

    public BlockingQueue<Connection> getInitConnectionQueue() {
        return initConnectionQueue;
    }

    public AppConfig getAppConfig() {
        return appConfig;
    }

    public BlockingQueue<Message> getInboundMessageQueue() {
        return inboundMessageQueue;
    }

    ///////////////////////////////////////////////////////////////////////////
    // instance methods
    ///////////////////////////////////////////////////////////////////////////
    public void closeApp() {
        this.isActive.set(false);
    }
}
