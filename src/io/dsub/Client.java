package io.dsub;

import io.dsub.model.Message;
import io.dsub.model.RemoteMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.UUID;

public class Client implements Trackable, Readable, Runnable {

    private final UUID id;
    private final Socket socket;
    private final BufferedReader reader;
    private final PrintWriter writer;

    private String inputMessage;

    public Client(Socket socket) throws IOException {
        this.id = UUID.randomUUID();
        this.socket = socket;
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.writer = new PrintWriter(socket.getOutputStream());
    }

    public UUID getUUID() {
        return id;
    }

    public Socket getSocket() {
        return socket;
    }

    public BufferedReader getReader() {
        return reader;
    }

    public PrintWriter getWriter() {
        return writer;
    }

    public void sendMessage(Message message) {

    }

    public void close() {
        try {
            this.socket.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public String read() {
        return this.inputMessage;
    }

    private void fetchAvailableMessage() {
        try {
            if (this.reader.ready())
                this.inputMessage = this.reader.readLine();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void run() {
        MessageHandler.addClient(this);
        try {
            while (this.socket.isConnected()) {
                fetchAvailableMessage();
                if (this.inputMessage != null) {
                    Message m;
                    m = new RemoteMessage(this.inputMessage, this.id);
                    MessageHandler.enqueueOutbound(m);
                    this.inputMessage = null;
                }
            }
        } finally {
            MessageHandler.removeClient(this);
            this.close();
        }
    }
}
