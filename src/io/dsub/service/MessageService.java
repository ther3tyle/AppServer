package io.dsub.service;

import io.dsub.model.HumanPlayer;
import io.dsub.model.Player;
import io.dsub.model.RemotePlayer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class MessageService extends Thread implements Service {
    private static AtomicLong id = new AtomicLong();
    private ServerSocket serverSocket;
    private final CountDownLatch latch;
    private boolean isActive;
    private final Map<Long, HumanPlayer> players;
    private final ReadWriteLock readWriteLock;
    private final Lock readLock;
    private final Lock writeLock;

    public MessageService(CountDownLatch latch, Map<Long, HumanPlayer> players) {
        this.latch = latch;
        this.players = players;
        this.readWriteLock = new ReentrantReadWriteLock();
        this.readLock = this.readWriteLock.readLock();
        this.writeLock = this.readWriteLock.writeLock();
    }

    private void put(Long id, HumanPlayer player) {
        try {
            writeLock.lock();
            players.put(id, player);
        } finally {
            writeLock.unlock();
        }
    }

    private HumanPlayer get(Long id) {
        HumanPlayer queryPlayer;
        try {
            readLock.lock();
            queryPlayer = players.get(id);
        } finally {
            readLock.unlock();
        }
        return queryPlayer;
    }

    @Override
    public void initThenReport() {
        try {
            this.serverSocket = new ServerSocket(5000);
            this.latch.countDown();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            this.isActive = false;
        }
    }

    @Override
    public void run() {
        this.initThenReport();
        while (isActive) {
            try {
                Socket socket = this.serverSocket.accept();
                HumanPlayer player = new RemotePlayer(socket);
                this.put(id.getAndIncrement(), player);
            } catch (IOException e) {
                this.isActive = false;
            }
        }
    }
}
