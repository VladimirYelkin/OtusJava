package ru.otus.model;

import java.util.ArrayList;

public class HelperMessage {

    private HelperMessage() {
    }

    public static Message deepCopy (Message message) {
        ObjectForMessage objectForMessage = new ObjectForMessage();
        objectForMessage.setData(new ArrayList<>(message.getField13().getData()));
        return message.toBuilder().field13(objectForMessage).build();
    }
}
