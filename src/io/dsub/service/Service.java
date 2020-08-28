package io.dsub.service;

import io.dsub.model.Status;

import java.io.IOException;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeoutException;

public interface Service extends Callable<Status> {
    void initThenReport() throws IOException, InterruptedException, BrokenBarrierException, TimeoutException;
}
