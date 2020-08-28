package io.dsub.service;

import io.dsub.config.AppConfig;
import io.dsub.model.Connection;
import io.dsub.model.Status;

import java.io.IOException;
import java.net.PortUnreachableException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.InvalidParameterException;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class ConnectionService implements Service {

    private int port;
    private ServerSocket serverSocket;
    private ConcurrentMap<UUID, Connection> connMap;
    private BlockingQueue<UUID> connCloseRequestQueue;
    private final AtomicBoolean isActive;
    private final CountDownLatch latch;

    public ConnectionService(int initialPort, AtomicBoolean isActive, CountDownLatch latch) {
        this.isActive = isActive;
        this.port = initialPort;
        this.latch = latch;
    }

    @Override
    public void initThenReport() throws IOException, BrokenBarrierException, InterruptedException {
        print("opening on port " + port);
        openServerSocket(port);
        this.connMap = new ConcurrentHashMap<>();
        this.connCloseRequestQueue = new ArrayBlockingQueue<>(50);
        print("init completed!");
        this.latch.countDown();
        print("reported to latch");
    }

    /**
     * Computes a result, or throws an exception if unable to do so.
     *
     * @return computed result
     * @throws Exception if unable to compute a result
     */
    @Override
    public Status call() throws Exception {

        initThenReport();

        print(String.format("listening on port %d...", this.serverSocket.getLocalPort()));

        try {
            while (this.isActive.get()) {
                handleConnCloseRequests();
                Future<Socket> futureSocket = AppConfig.getExecService().submit(this::getFutureSocket);
                Socket socket;
                try {
                    socket = futureSocket.get(100, TimeUnit.MILLISECONDS);
                } catch (TimeoutException e) {
                    continue;
                }
                if (socket == null) continue;
                Connection connection = new Connection(socket);
                this.connMap.put(connection.getUuid(), connection);
            }
        } finally {
            this.serverSocket.close();
        }

        return Status.OK;
    }

    public void close() {
        this.isActive.set(false);
    }

    public void closeConnection(UUID uuid) throws InterruptedException {
        this.connCloseRequestQueue.put(uuid);
    }

    private Socket getFutureSocket() throws IOException {
        return this.serverSocket.accept();
    }

    private void handleConnCloseRequests() throws InterruptedException, IOException {
        while (!this.connCloseRequestQueue.isEmpty()) {
            UUID targetID = this.connCloseRequestQueue.take();
            Connection targetConn = this.connMap.get(targetID);

            if (targetConn == null) continue;

            targetConn.getSocket().close();
            this.connMap.remove(targetID);
            print("removed connection from " + targetID.toString());
        }
    }

    private void openServerSocket(int port) throws PortUnreachableException {
        boolean notOpened = true;

        if (port < 0 || port > 65535)
            throw new InvalidParameterException("invalid port range. port range must be between 0 to 65535");

        while (notOpened) {
            if (port > 65535) throw new PortUnreachableException("failed to open port at " + port);
            try {
                this.serverSocket = new ServerSocket(port);
                notOpened = false;
            } catch (IOException e) {
                print(String.format("failed open port at %d. retrying with %d...", port, port + 1));
                port++;
            }
        }
        this.port = port;
    }

    private void pruneConnections() {
        this.connMap.keySet().forEach(key -> {
            this.connMap.remove(key);
        });
    }

    private void print(String s) {
        System.out.println(getClass().getSimpleName() + ": " + s);
    }
}
