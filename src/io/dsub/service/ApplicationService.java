package io.dsub.service;

import io.dsub.config.AppConfig;
import io.dsub.model.Status;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class ApplicationService implements Service {

    private final CountDownLatch latch = new CountDownLatch(2);
    private final ExecutorService execService = Executors.newFixedThreadPool(4);
    private final AtomicBoolean isActive = new AtomicBoolean(true);

    private Future<Status> msgServiceStatus;
    private Future<Status> connServiceStatus;

    @Override
    public void initThenReport() throws InterruptedException {
        print("initializing...");
        connServiceStatus = execService.submit(new ConnectionService(5000, this.isActive, latch));
        msgServiceStatus = execService.submit(new MessageService(this.isActive, latch));
        this.latch.await();
    }

    @Override
    public Status call() throws Exception {
        initThenReport();
        print("initialized...");
        while(AppConfig.getIsActive().get()) {
            Thread.sleep(TimeUnit.SECONDS.toMillis(1));
            print("running");
        }
        msgServiceStatus.get(5, TimeUnit.SECONDS);
        connServiceStatus.get(5, TimeUnit.SECONDS);
        return Status.OK;
    }

    private void print(String s) {
        System.out.println(getClass().getSimpleName() + ": " + s);
    }
}
