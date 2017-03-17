package com.ckcclc.springboot.controller;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MockAppIntlController {

    @RequestMapping("/internal")
    public String internal() {
        JSONObject obj = new JSONObject();
        obj.put("error_code", 0);

        JSONObject result = new JSONObject();
        result.put("region", "ap-southeast-1");
        result.put("verifyUrl", "https://dev-info.tplinkcloud.com");
        obj.put("result", result);

        System.out.println("return: " + obj.toString());

        return obj.toString();
    }
}
