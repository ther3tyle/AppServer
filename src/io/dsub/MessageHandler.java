package io.dsub;

import io.dsub.config.AppConfig;
import io.dsub.config.AppState;
import io.dsub.model.LocalMessage;
import io.dsub.model.Message;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;

public class MessageHandler implements Runnable {

    private static final MessageHandler instance = new MessageHandler();

    private MessageHandler() {}

    public static MessageHandler getInstance() {
        return instance;
    }

    // utility instance of System.out as PrintWriter
    private static final PrintWriter local = new PrintWriter(System.out);

    // contains sustained set of input, output stream.
    private static final Map<UUID, PrintWriter> printWriterMap = new HashMap<>();

    // contains messages as inbox, postbox; separated queue of IO queue.
    private static final ArrayBlockingQueue<Message> inputQueue = new ArrayBlockingQueue<>(30);
    private static final ArrayBlockingQueue<Message> outputQueue = new ArrayBlockingQueue<>(30);

    // add client to printWriterMap
    public static void addClient(Client client) {
        printWriterMap.put(client.getUUID(), client.getWriter());
    }

    // remove client from printWriterMap
    public static void removeClient(Client client) {
        printWriterMap.remove(client.getUUID());
    }

    public static void enqueueOutbound(Message message) {
        try {
            outputQueue.put(message);
        } catch (InterruptedException e) {
            System.out.println("MessageHandler: " + e.getMessage());
        }
    }

    public static void enqueueInbound(Message message) {
        try {
            inputQueue.put(message);
        } catch (InterruptedException e) {
            System.out.println("MessageHandler: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        System.out.println("Started MessageHandler...");
        while (AppState.active) {
            try {
                processOutputQueue();
                processInputQueue();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
                AppState.active = false;
            }
        }
        System.out.println("Shutdown MessageHandler...");
    }

    // processing output queue (OUT from THIS)
    public void processOutputQueue() throws InterruptedException {
        while (outputQueue.size() > 0) {
            Message message = outputQueue.take();
            PrintWriter writer =
                    message.getUUID().equals(AppConfig.APP_UUID) ? local : printWriterMap.get(message.getUUID());

            if (writer == null) { // skips as the message is to be sent to unknown destination
                continue;
            }

            writer.println(message.read());
        }
    }


    // processing input queue (IN from client (either local or remote))
    public void processInputQueue() throws InterruptedException {
        while (inputQueue.size() > 0) {
            Message message = inputQueue.take();
            System.out.printf("%s: %s\n", message.getUUID(), message.read());
        }
    }

    // print message to THIS local machine console
    private void printLocalMessage(LocalMessage localMessage) {
        System.out.println(localMessage.read());
    }
}
