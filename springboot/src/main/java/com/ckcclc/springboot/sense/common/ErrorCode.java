package com.ckcclc.springboot.sense.common;

/**
 * Created by ckcclc on 26/10/2017.
 */
public enum ErrorCode {

    SUCCESS(0, "success"),      //

    UNKNOWN(10000, "unknown"),      // 未知错误
    INTERNAL_SERVER_ERROR(20000, "internal server error"),      // 系统内部错误
    REQUEST_PARAMETER_ERROR(30000, "request parameter error");      // 接口参数错误

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
