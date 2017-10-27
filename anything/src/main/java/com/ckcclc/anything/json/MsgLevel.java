/**
 * Copyright (c) 2017, TP-Link Co.,Ltd.
 * Author:  huangyucong <huangyucong@tp-link.com.cn>
 * Created: 2017-2-14
 */

package com.ckcclc.anything.json;

public enum MsgLevel {

    HIGH("high"),
    MEDIUM("medium"),
    LOW("low");

    private String value;

    MsgLevel(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static MsgLevel fromValue(String value) {
        if (value == null) return null;
        for (MsgLevel level : MsgLevel.values()) {
            if (value.equals(level.value)) {
                return level;
            }
        }
        return null;
    }
}
