package io.dsub.service;

import io.dsub.TerminateStatus;
import io.dsub.resource.AppConfig;
import io.dsub.resource.AppState;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class AppService implements Service {

    private final AppConfig appConfig = AppConfig.getInstance();
    private final AppState appState = AppState.getInstance();
    private final AtomicBoolean isActive = appState.getIsActive();
    private final CountDownLatch initLatch = appState.getLatch();
    private final ExecutorService execService = appState.getExecService();

    public AppService() {}

    @Override
    public void initThenReport() {
        execService.submit(ConnectionService.getInstance());
        execService.submit(new MessageService());
    }

    @Override
    public TerminateStatus call() throws Exception {
        initThenReport();
        initLatch.await();
        while (isActive.get()) {
        }
        return null;
    }
}
