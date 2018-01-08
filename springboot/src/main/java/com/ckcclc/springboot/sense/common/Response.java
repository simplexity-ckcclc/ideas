package com.ckcclc.springboot.sense.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * Created by ckcclc on 26/10/2017.
 */
public class Response<T> {

    private String requestUrl;
    private int code;
    private String msg;
    private T result;

    public Response() {
        this(ErrorCode.SUCCESS);
    }

    public Response(ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.msg = errorCode.getMsg();
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
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

    public Object getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("errorCode", code);
        jsonObject.put("errorMsg", msg);
        jsonObject.put("requestUrl", requestUrl);
        if (code == ErrorCode.SUCCESS.getCode()) {
            jsonObject.put("result", JSON.toJSONString(result));
        }

        return jsonObject.toJSONString();
    }
}
