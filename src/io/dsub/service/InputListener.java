package io.dsub.service;

import io.dsub.TerminateStatus;

import java.io.InputStream;
import java.util.Scanner;

public class InputListener implements Service {

    private final Scanner sc;

    public InputListener(InputStream inputStream) {
        this.sc = new Scanner(inputStream);
    }

    @Override
    public void initThenReport() {
        System.out.println("");
    }

    @Override
    public TerminateStatus call() throws Exception {
        initThenReport();
        return null;
    }


}