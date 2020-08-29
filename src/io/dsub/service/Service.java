package io.dsub.service;

import io.dsub.TerminateStatus;

import java.io.IOException;
import java.util.concurrent.Callable;

public interface Service extends Callable<TerminateStatus> {
    void initThenReport() throws IOException;
}
