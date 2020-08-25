package io.dsub;

import io.dsub.config.AppState;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

public class SocketListener implements Runnable {

    private static final ServerSocket SERVER_SOCKET = AppState.getServerSocket();
    private static final ExecutorService EXECUTOR_SERVICE = AppState.getExecService();

    private static final SocketListener instance = new SocketListener();
    private SocketListener(){}
    public static SocketListener getInstance() {
        return instance;
    }

    @Override
    public void run() {
        System.out.printf("Started SocketListener at port %s...\n", SERVER_SOCKET.getLocalPort());
        while (AppState.active) {
            try {
                Socket socket = SERVER_SOCKET.accept();
                Client client = new Client(socket);
                MessageHandler.addClient(client);
                EXECUTOR_SERVICE.submit(client);
            } catch (IOException e) {
                System.out.println(e.getMessage());
                AppState.active = false;
            }
        }
        System.out.println("Shutdown SocketListener...");
    }
}
