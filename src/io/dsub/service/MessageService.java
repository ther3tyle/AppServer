package io.dsub.service;

import io.dsub.TerminateStatus;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class MessageService implements Service {

    private final CountDownLatch initLatch;
    private final AtomicBoolean isActive;

    public MessageService(AtomicBoolean isActive, CountDownLatch initLatch) {
        this.initLatch = initLatch;
        this.isActive = isActive;
    }

    @Override
    public void initThenReport() {
        initLatch.countDown();
    }

    @Override
    public TerminateStatus call() throws Exception {
        initThenReport();
        while (this.isActive.get()) {
            Thread.sleep(TimeUnit.SECONDS.toMillis(5));
        }
        return TerminateStatus.OK;
    }
}
