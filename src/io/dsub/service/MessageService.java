package io.dsub.service;

import io.dsub.model.Message;
import io.dsub.model.Status;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ConnectionService implements Service {

    private BlockingQueue<Message> messageArrayBlockingQueue;

    @Override
    public void initThenReport() {
        this.messageArrayBlockingQueue = new ArrayBlockingQueue<>(50);
    }

    @Override
    public Status call() throws Exception {

        while (true) {
            if (messageArrayBlockingQueue.isEmpty()) continue;

            while (!messageArrayBlockingQueue.isEmpty()) {
                Message m = messageArrayBlockingQueue.take();
                handleMessage(m);
            }

            break;
        }
        return Status.OK;
    }

    private void handleMessage(Message m) {
        if (m.getDest().equals(ApplicationService.APPLICATION_ID)) {
            handleInboundMessage(m);
        } else {
            handleOutboundMesage(m);
        }
    }

    private void handleInboundMessage(Message m) {
        System.out.println(m.getData());
    }

    private void handleOutboundMesage(Message m) {

    }
}
