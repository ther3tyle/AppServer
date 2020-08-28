package io.dsub.service;

import io.dsub.config.AppConfig;
import io.dsub.model.Message;
import io.dsub.model.Status;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

public class MessageService implements Service {

    private BlockingQueue<Message> messageArrayBlockingQueue;
    private final CountDownLatch latch;
    private final AtomicBoolean isActive;

    public MessageService(AtomicBoolean isActive, CountDownLatch latch) {
        this.isActive = isActive;
        this.latch = latch;
    }

    @Override
    public void initThenReport() {
        print("initializing...");
        this.messageArrayBlockingQueue = new ArrayBlockingQueue<>(50);
        this.latch.countDown();
        print("initialized");
        print("reported to latch");
    }

    @Override
    public Status call() throws Exception {
        initThenReport();
        while (true) {
            if (messageArrayBlockingQueue.isEmpty()) {
                continue;
            }
            while (!messageArrayBlockingQueue.isEmpty()) {
                Message m = messageArrayBlockingQueue.take();
                handleMessage(m);
            }
            break;
        }
        this.messageArrayBlockingQueue.clear();
        return Status.OK;
    }

    private void handleMessage(Message m) {
        if (m.getDest().equals(AppConfig.getApplicationId())) {
            handleInboundMessage(m);
        } else {
            handleOutboundMessage(m);
        }
    }

    private void handleInboundMessage(Message m) {
        System.out.println(m.getData());
    }

    private void handleOutboundMessage(Message m) {

    }

    private void print(String s) {
        System.out.println(getClass().getSimpleName() + ": " + s);
    }
}
