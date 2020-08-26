package io.dsub.model;

import io.dsub.Readable;
import io.dsub.Trackable;

import java.util.UUID;

public interface Message extends Trackable, Readable {
    String read();
    UUID getUUID();
}
