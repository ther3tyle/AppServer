package io.dsub;

import io.dsub.config.AppConfig;
import io.dsub.service.ApplicationService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        ExecutorService service = AppConfig.getExecService();
        ApplicationService applicationService = new ApplicationService();
        try {
            service.submit(applicationService);
            while(true) {
                Thread.sleep(TimeUnit.SECONDS.toMillis(10
                ));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            service.shutdown();
        }
    }
}
