/**
 * Copyright (c) 2017, TP-Link Co.,Ltd.
 * Author:  huangyucong <huangyucong@tp-link.com.cn>
 * Created: 2017-2-15
 */

package com.ckcclc.anything.json;

import java.io.Serializable;
import java.util.Map;

public class MalMessage {

    private MsgLevel msgLevel;
    private MsgType msgType;
    private Map<String, Serializable> data;

    public MalMessage(Builder builder) {
        this.msgLevel = builder.msgLevel;
        this.msgType = builder.msgType;
        this.data = builder.data;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private MsgLevel msgLevel;
        private MsgType msgType;
        private Map<String, Serializable> data;

        public Builder msgLevel(MsgLevel msgLevel) {
            this.msgLevel = msgLevel;
            return this;
        }

        public Builder msgType(MsgType msgType) {
            this.msgType = msgType;
            return this;
        }

        public Builder data(Map<String, Serializable> data) {
            this.data = data;
            return this;
        }

        public MalMessage build() {
//            checkNotNull(this.msgType, "msgType");
//            checkNotNull(this.data, "data");

            return new MalMessage(this);
        }
    }

    @Override
    public String toString() {
        return JsonUtil.toJson(this);
    }

    public MsgLevel getMsgLevel() {
        return msgLevel;
    }

    public void setMsgLevel(MsgLevel msgLevel) {
        this.msgLevel = msgLevel;
    }

    public MsgType getMsgType() {
        return msgType;
    }

    public Map getData() {
        return data;
    }

}
