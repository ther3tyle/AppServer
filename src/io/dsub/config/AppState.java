package io.dsub.config;

import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;

public class AppState {
    private AppState() {}

    private static final AppState instance = new AppState();

    public static AppState getInstance() {
        return instance;
    }

    public static boolean active = true;

    private static ExecutorService execService;

    private static ServerSocket serverSocket;

    public static ExecutorService getExecService() {
        return execService;
    }

    public static void setExecService(ExecutorService execService) {
        AppState.execService = execService;
    }

    public static ServerSocket getServerSocket() {
        return serverSocket;
    }

    public static void setServerSocket(ServerSocket serverSocket) {
        AppState.serverSocket = serverSocket;
    }
}
