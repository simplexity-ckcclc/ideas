package com.ckcclc.springboot.controller;

import com.ckcclc.springboot.dao.PersonMapper;
import com.ckcclc.springboot.entity.Person;
import com.ckcclc.springboot.entity.Target;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
