package io.dsub.resource;

import io.dsub.model.Connection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

class AppStateTest {

    private AppState appState;
    @BeforeEach
    void beforeEach() {
        this.appState = AppState.getInstance();
    }

    @Test
    void testGetInstance() {
        AppState test = AppState.getInstance();
        assertNotNull(test);
        assertSame(appState, test);
    }

    @Test
    @Order(2)
    void testGetIsActiveAndIsTrue() {
        assertNotNull(this.appState.getIsActive());
        assertTrue(this.appState.getIsActive().get());
    }

    @Test
    void testGetLatch() {
        CountDownLatch latch = this.appState.getLatch();
        assertNotNull(latch);
        assertEquals(AppConfig.getInstance().getLatchCount(), latch.getCount());
    }

    @Test
    void testGetExecService() {
        ExecutorService executorService = this.appState.getExecService();
        assertNotNull(executorService);
        assertFalse(executorService.isTerminated());
        assertFalse(executorService.isShutdown());
    }

    @Test
    void testGetConnMap() {
        ConcurrentMap<UUID, Connection> map = appState.getConnMap();
        assertNotNull(map);
        assertEquals(0, map.size());
    }

    @Test
    void testGetCloseConnQueue() {
        BlockingQueue<UUID> queue = appState.getCloseConnectionQueue();
        assertNotNull(queue);
        assertEquals(0, queue.size());
        assertEquals(AppConfig.getInstance().getQueueSize(), queue.remainingCapacity());
    }

    @Test
    @Order(1)
    void testCloseApp() {
        AtomicBoolean isActive = appState.getIsActive();
        appState.closeApp();
        assertFalse(isActive.get());
        assertSame(isActive, appState.getIsActive());
        assertEquals(isActive.get(), appState.getIsActive().get());
        appState.getIsActive().set(true);
        assertTrue(appState.getIsActive().get());
    }
}