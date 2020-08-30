package io.dsub.service;

import io.dsub.TerminateStatus;
import io.dsub.model.Connection;
import io.dsub.resource.AppConfig;
import io.dsub.resource.AppState;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

public class MessageService implements Service {

    ///////////////////////////////////////////////////////////////////////////
    // global config and resources
    ///////////////////////////////////////////////////////////////////////////
    private final AppConfig appConfig = AppConfig.getInstance();
    private final AppState appState = AppState.getInstance();

    ///////////////////////////////////////////////////////////////////////////
    // fields
    ///////////////////////////////////////////////////////////////////////////
    private final CountDownLatch latch = appState.getLatch();
    private final AtomicBoolean isActive = appState.getIsActive();
    private final Logger logger = Logger.getLogger(MessageService.class.getName());
    private final ExecutorService execService = appState.getExecService();
    private final BlockingQueue<Connection> initConnectionQueue =
            appState.getInitConnectionQueue();

    public MessageService() {}

    @Override
    public void initThenReport() {
        latch.countDown();
        logger.info("finished init");
    }

    @Override
    public TerminateStatus call() throws Exception {
        initThenReport();
        while (this.isActive.get()) {
            initListenTasks();
        }
        return TerminateStatus.OK;
    }

    private void initListenTasks() {
        while (!this.initConnectionQueue.isEmpty()) {
            try {
                Connection conn = this.initConnectionQueue.take();
                InputListener inputListener = new InputListener(conn);
                this.execService.submit(inputListener);
            } catch (InterruptedException e) {
                logger.warning(e.getMessage());
            }
        }
    }

    private void handleInboundMessage() {

    }
}
