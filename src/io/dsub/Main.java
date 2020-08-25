package io.dsub;

import io.dsub.config.AppState;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static io.dsub.config.AppConfig.MAX_THREAD_COUNT;
import static io.dsub.config.AppConfig.SERVER_PORT;

public class Main {
    public static void main(String[] args) {

        if (args.length > 0) {
            MAX_THREAD_COUNT = Integer.parseInt(args[0]);
            System.out.printf("applying implicit thread count: %d\n", MAX_THREAD_COUNT);
        }
        if (args.length > 1) {
            SERVER_PORT = Integer.parseInt(args[1]);
            System.out.printf("applying implicit server port value: %d\n", SERVER_PORT);
        }

        try {
            AppState.setExecService(Executors.newFixedThreadPool(MAX_THREAD_COUNT));
            AppState.setServerSocket(new ServerSocket(SERVER_PORT));
            System.out.println();
            System.out.println("=".repeat(6) + " INIT REPORT " + "=".repeat(6));
            System.out.printf("MAX_THREAD [%d]\nPORT [%d]\n", MAX_THREAD_COUNT, SERVER_PORT);
            System.out.println("=".repeat(25) + "\n");
        } catch (IOException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }

        ExecutorService execService = AppState.getExecService();
        execService.submit(SocketListener.getInstance());
        execService.submit(MessageHandler.getInstance());
    }
}
