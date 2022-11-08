package hse.ce.jameskok.jigsawmultiplayer;

import java.io.Serializable;

/**
 * Message with type and data from server or from client.
 */
public record Message(MessageType type,
                      Object data) implements Serializable {

    public Object getData() {
        return data;
    }

    public MessageType getType() {
        return type;
    }
}

