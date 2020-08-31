package io.dsub.service;

import io.dsub.TerminateStatus;
import io.dsub.model.Connection;

import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

public class InputListener implements Callable<TerminateStatus> {

    private final Connection connection;
    private final Logger logger;

    public InputListener(Connection connection) {
        this.connection = connection;
        this.logger = Logger.getLogger(InputListener.class.getName() + connection.getUuid().toString());
    }

    @Override
    public TerminateStatus call() throws Exception {
        Socket socket = connection.getSocket();
        Scanner sc = new Scanner(socket.getInputStream());
        while(socket.isConnected()) {
            String s = sc.nextLine();
            logger.info("received " + s);
            if (isEndSignal(s)) break;
        }
        logger.info(String.format("terminating connection [%s]", this.connection.getUuid()));
        return TerminateStatus.OK;
    }

    private boolean isEndSignal(String input) {
        if (input == null) return true;
        if (input.matches("^[qQ]$")) return true;
        return isEquals(input, "exit", "quit");
    }

    private boolean isEquals(String target, String... cases) {
        for (String aCase : cases) {
            if (target.equalsIgnoreCase(aCase)) return true;
        }
        return false;
    }
}
