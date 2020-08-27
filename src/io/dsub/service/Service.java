package io.dsub.service;

import java.io.IOException;

public interface Service extends Runnable {
    void initThenReport() throws IOException;
}
