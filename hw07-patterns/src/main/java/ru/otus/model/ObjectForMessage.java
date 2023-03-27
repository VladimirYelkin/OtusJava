package ru.otus.model;

import java.util.*;


public class ObjectForMessage implements Copyable<ObjectForMessage> {
    private List<String> data;

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    @Override
    @SuppressWarnings("unchecked")
    public ObjectForMessage copy() {
        ObjectForMessage objectForCopy = new ObjectForMessage();
        try {
            objectForCopy.data = data.getClass().getConstructor(Collection.class).newInstance(data);
        } catch (Exception e) {
            objectForCopy.data = Optional.ofNullable(data).stream().flatMap(Collection::stream).toList();
        }
        return objectForCopy;
    }
}
