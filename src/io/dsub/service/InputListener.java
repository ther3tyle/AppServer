package io.dsub.service;

import io.dsub.TerminateStatus;
import io.dsub.model.Connection;

import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Logger;

public class InputListener implements Service {

    private Scanner sc;
    private final Connection connection;
    private final Logger logger;

    public InputListener(Connection connection) {
        this.connection = connection;
        this.logger = Logger.getLogger(InputListener.class.getName());
    }

    @Override
    public void initThenReport() throws IOException {
        this.sc = new Scanner(connection.getSocket().getInputStream());
        logger.info(String.format("listening to input stream[%s]", connection.getUuid().toString()));
    }

    @Override
    public TerminateStatus call() throws Exception {
        try {
            initThenReport();
        } finally {
            this.sc.close();
        }
        return TerminateStatus.OK;
    }
}