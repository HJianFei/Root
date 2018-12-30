package com.hjianfei.utils;

import java.io.Serializable;

public class EventBean implements Serializable {

    public int eventType;
    public int type;

    public EventBean(int eventType, int type) {
        this.eventType = eventType;
        this.type = type;
    }
}
