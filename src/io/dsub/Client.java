package io.dsub;

import io.dsub.model.RemoteMessage;

import java.io.*;
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

    public UUID getId() {
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

    public void close() {
        try {
            this.socket.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void read() {
        try {
            if (this.reader.ready()){
                this.inputMessage = reader.readLine();
            }
        } catch (IOException e) {
            this.inputMessage = e.getMessage();
        }
    }

    @Override
    public void run() {
        MessageHandler.addClient(this);
        try {
            while (this.socket.isConnected()) {
                read();
                if (this.inputMessage != null) {
                    RemoteMessage m;
                    m = new RemoteMessage(this.inputMessage, this.id);
                    MessageHandler.enqueue(m);
                    this.inputMessage = null;
                }
            }
        } finally {
            MessageHandler.removeClient(this);
            this.close();
        }
    }
}
