package com.ckcclc.springboot.common;

/**
 * Created by ckcclc on 26/10/2017.
 */
public enum ErrorCode {


    SUCCESS(0, ""),
    UNKNOWN(2000, "unknown");

    private int code;
    private String msg;

    ErrorCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
