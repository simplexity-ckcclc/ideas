package com.ckcclc.springboot.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * Created by ckcclc on 26/10/2017.
 */
public class Response {

    private int code = 0;
    private String msg;
    private Object result;

    public Response() {
    }

    public Response(ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.msg = errorCode.getMsg();
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

    public void setResult(Object result) {
        this.result = result;
    }

    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("error_code", code);
        if (code != 0) {
            jsonObject.put("msg", msg);
        } else if (result != null){
            String jsonString = JSON.toJSONString(result);
            jsonObject.put("result", jsonString);
        }
        return jsonObject.toJSONString();
    }
}
