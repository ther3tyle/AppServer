package io.dsub.service;

import io.dsub.config.AppConfig;
import io.dsub.model.Status;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class Application implements Callable<Status> {

    private final CountDownLatch latch = AppConfig.getLatch();
    /**
     * Computes a result, or throws an exception if unable to do so.
     *
     * @return computed result
     * @throws Exception if unable to compute a result
     */
    @Override
    public Status call() throws Exception {
        this.latch.await(5, TimeUnit.SECONDS);
        System.out.println("main application initialized...");
        while(AppConfig.getIsActive().get()) {
            print("running");
            Thread.sleep(TimeUnit.SECONDS.toMillis(1));
        }
        return Status.OK;
    }

    private void print(String s) {
        System.out.println(getClass().getSimpleName() + ": " + s);
    }
}
