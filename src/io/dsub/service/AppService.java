package io.dsub.service;

import io.dsub.TerminateStatus;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class AppService implements Service {

    private final AtomicBoolean isActive;
    private final CountDownLatch initLatch;
    private final ExecutorService executorService;
    private final BlockingQueue<Service> serviceQueue;

    public AppService() {
        this.isActive = new AtomicBoolean(true);
        this.initLatch = new CountDownLatch(2);
        this.executorService = Executors.newFixedThreadPool(4);
        this.serviceQueue = new ArrayBlockingQueue<>(50);
    }

    @Override
    public void initThenReport() {
        executorService.submit(ConnectionService.getInstance());
        executorService.submit(new MessageService(this.isActive, initLatch));
    }

    @Override
    public TerminateStatus call() throws Exception {
        initThenReport();
        initLatch.await();
        while (isActive.get()) {
            if (!serviceQueue.isEmpty()) {
                this.executorService.submit(serviceQueue.take());
            }
        }
        return null;
    }
}
