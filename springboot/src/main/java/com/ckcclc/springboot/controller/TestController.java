package com.ckcclc.springboot.controller;

import com.ckcclc.springboot.common.ErrorCode;
import com.ckcclc.springboot.common.Response;
import com.ckcclc.springboot.dao.PersonMapper;
import com.ckcclc.springboot.entity.Person;
import com.ckcclc.springboot.entity.Target;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ckcclc on 21/10/2017.
 */

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private PersonMapper personMapper;

    @RequestMapping("/target")
    public String target(@RequestBody Target target) {
        return target.getName() + "." + String.valueOf(target.getAge());
    }

    @RequestMapping("/person/{name}")
    public Person person(@PathVariable("name") String name) {
        return personMapper.findByName(name);
    }

    @RequestMapping("/successVal/{value}")
    public String successVal(@PathVariable("value") String value) {
        Response response = new Response();
        Map<String, Object> map = new HashMap<>();
        map.put("foo", 1);
        map.put("bar", value);
        response.setResult(map);
        return response.toString();
    }

    @RequestMapping("/exception")
    public String exception() {
        return new Response(ErrorCode.UNKNOWN).toString();
    }

    @RequestMapping("/success")
    public String success() {
        return new Response().toString();
    }


}
