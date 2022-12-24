package com.awang.util;

public class Event {
    private EventType type;
    private Object object;

    public Event(EventType type, Object object) {
        this.type = type;
        this.object = object;
    }

    @Override
    public String toString() {
        return "Event{" +
                "type=" + type +
                ", json=" + object +
                '}';
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
