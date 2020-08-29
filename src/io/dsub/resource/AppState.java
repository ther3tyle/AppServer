package io.dsub.resource;

import io.dsub.model.Connection;

import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class AppState {
    private AppState() {}
    private static final AppState instance = new AppState();
    public static AppState getInstance() {
        return instance;
    }

    private final AtomicBoolean isActive = new AtomicBoolean(true);
    private final CountDownLatch latch = new CountDownLatch(3);
    private final ExecutorService execService = Executors.newFixedThreadPool(4);
    private final ConcurrentMap<UUID, Connection> connMap = new ConcurrentHashMap<>();
    private final BlockingQueue<UUID> closeConnQueue = new ArrayBlockingQueue<>(20);

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

    public BlockingQueue<UUID> getCloseConnQueue() {
        return closeConnQueue;
    }
}
