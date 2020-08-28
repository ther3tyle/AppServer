package io.dsub.service;

import io.dsub.model.Connection;
import io.dsub.model.Status;

public class ConnectionListenerService implements Service {

    private final Connection connection;

    public ConnectionListenerService(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void initThenReport() {

    }

    /**
     * Computes a result, or throws an exception if unable to do so.
     *
     * @return computed result
     * @throws Exception if unable to compute a result
     */
    @Override
    public Status call() throws Exception {
        return null;
    }
}
