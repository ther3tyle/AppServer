package io.dsub.config;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class AppConfig {
    private AppConfig(){}
    private static final AppConfig instance = new AppConfig();
    public AppConfig getInstance() {
        return instance;
    }

    private static final UUID APPLICATION_ID = UUID.randomUUID();
    private static final ExecutorService execService = Executors.newFixedThreadPool(4);
    private static final AtomicBoolean isActive = new AtomicBoolean(true);
    private static final CountDownLatch latch = new CountDownLatch(3);

    public static UUID getApplicationId() {
        return APPLICATION_ID;
    }

    public static ExecutorService getExecService() {
        return execService;
    }

    public static AtomicBoolean getIsActive() {
        return isActive;
    }

    public static CountDownLatch getLatch() {
        return latch;
    }
}
