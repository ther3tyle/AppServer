package io.dsub.resource;

public class AppConfig {
    private AppConfig(){}
    private static final AppConfig instance = new AppConfig();
    public static AppConfig getInstance() {
        return instance;
    }

    private int port = 5000;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
