package io.dsub;

import io.dsub.service.AppService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        try {
            executorService.submit(new AppService());
        } finally {
            executorService.shutdown();
        }

    }
}
