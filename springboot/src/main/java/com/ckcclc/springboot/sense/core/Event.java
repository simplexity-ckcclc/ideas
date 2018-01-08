/**
 * Author:  huangyucong <huangyucong@sensetime.com>
 * Created: 18-1-5
 */

package com.ckcclc.springboot.sense.core;

import com.google.common.collect.Maps;

import java.util.Map;
import java.util.Set;

public enum Event {

    CREATE_EXAMPLE(0),
    CANCEL_EXAMPLE(1);

    Event(int type) {
        this.type = type;
    }

    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    private static final Map<Integer, Event> events = Maps.newHashMap();
    static {
        for (Event event : Event.values()) {
            events.put(event.getType(), event);
        }
    }

    public static Event fromType(Integer type) {
        return events.get(type);
    }

    public static Set<Integer> types() {
        return events.keySet();
    }
}
