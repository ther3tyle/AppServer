package io.dsub.service;

import io.dsub.TerminateStatus;
import io.dsub.model.Connection;
import io.dsub.resource.AppConfig;
import io.dsub.resource.AppState;
import io.dsub.resource.ConnectionMap;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

public class ConnectionService implements Service {

    ///////////////////////////////////////////////////////////////////////////
    // singleton
    ///////////////////////////////////////////////////////////////////////////
    private ConnectionService(){}
    private static final ConnectionService instance = new ConnectionService();
    public static ConnectionService getInstance() {
        return instance;
    }

    ///////////////////////////////////////////////////////////////////////////
    // global config and resources
    ///////////////////////////////////////////////////////////////////////////
    private final ConnectionMap connectionMap
            = ConnectionMap.getInstance();

    private final BlockingQueue<UUID> closeConnQueue =
            AppState.getInstance().getCloseConnectionQueue();

    private final AtomicBoolean isActive =
            AppState.getInstance().getIsActive();

    ///////////////////////////////////////////////////////////////////////////
    // class variables
    ///////////////////////////////////////////////////////////////////////////
    private static final Logger logger = Logger.getLogger(ConnectionService.class.getName());

    ///////////////////////////////////////////////////////////////////////////
    // fields
    ///////////////////////////////////////////////////////////////////////////
    private ServerSocket serverSocket;
    private final CountDownLatch latch = AppState.getInstance().getLatch();

    @Override
    public void initThenReport() throws IOException {
        this.serverSocket = new ServerSocket(AppConfig.getInstance().getPort());
        logger.info("instantiated ServerSocket");
        latch.countDown();
    }

    @Override
    public TerminateStatus call() throws Exception {
        initThenReport();

        latch.await();
        logger.info(String.format("listening to port %d...", AppConfig.getInstance().getPort()));

        while (isActive.get()) {
            handleCloseConnRequest();
            handleConnRequest();
        }

        return TerminateStatus.OK;
    }

    ///////////////////////////////////////////////////////////////////////////
    // private methods
    ///////////////////////////////////////////////////////////////////////////
    private void handleConnRequest() {
        Future<Connection> future = getFutureConn();
        Connection connection = getConnectionWithTimeout(future);
        this.connectionMap.put(connection);
    }

    private Future<Connection> getFutureConn() {
        ExecutorService execService = AppState.getInstance().getExecService();
        return execService.submit(() -> new Connection(UUID.randomUUID(), serverSocket.accept()));
    }

    private Connection getConnectionWithTimeout(Future<Connection> future) {
        try {
            return future.get(200, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | TimeoutException | ExecutionException e ) {
            if (!(e instanceof TimeoutException))
            logger.severe(e.getMessage());
        }
        return null;
    }

    private void handleCloseConnRequest() throws InterruptedException, IOException {
        while (!closeConnQueue.isEmpty()) {
            UUID id = closeConnQueue.take();
            if (connectionMap.containsKey(id)) {
                connectionMap.get(id).getSocket().close();
                connectionMap.remove(id);
            }
        }
    }
}
