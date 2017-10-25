package com.ckcclc.springboot.controller;

import com.ckcclc.springboot.dao.PersonMapper;
import com.ckcclc.springboot.entity.Person;
import com.ckcclc.springboot.entity.Target;

import com.ckcclc.springboot.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Autowired
    private CacheService cacheService;

    @RequestMapping("/target")
    public String target(@RequestBody Target target) {
        return target.getName() + "." + String.valueOf(target.getAge());
    }

    @RequestMapping("/person/{name}")
    public Person person(@PathVariable("name") String name) {
        return personMapper.findByName(name);
    }

    @RequestMapping("/cache/set")
    public ResponseEntity<?> cacheSet(@RequestBody Person person) {
        cacheService.set(person.getName(), person.getCountry());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping("/cache/get/{name}")
    public ResponseEntity<String> cacheGet(@PathVariable String name) {
        String country = cacheService.get(name);
        return new ResponseEntity<>(country, HttpStatus.OK);
    }
}
