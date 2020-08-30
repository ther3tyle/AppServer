package io.dsub.model;

import java.util.UUID;

public interface Message {
    UUID getDest();
    UUID getSource();
    String getData();
}
