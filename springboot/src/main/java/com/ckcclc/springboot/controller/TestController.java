package com.ckcclc.springboot.controller;

import com.ckcclc.springboot.dao.PersonMapper;
import com.ckcclc.springboot.entity.Person;
import com.ckcclc.springboot.entity.Target;

import com.ckcclc.springboot.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

//    @RequestMapping("/person")
//    public Person person(@RequestParam int age) {
//        return personMapper.findByName(name);
//    }

    @RequestMapping("/person/{name}")
    public Person personName(@PathVariable("name") String name) {
        return personMapper.findByName(name);
    }

    @RequestMapping("/cache/set/{key}/{value}")
    public ResponseEntity<?> cacheSet(@PathVariable String key, @PathVariable String value) {
        cacheService.set(key, value);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping("/cache/get/{key}")
    public ResponseEntity<String> cacheGet(@PathVariable String key) {
        String country = cacheService.get(key);
        return new ResponseEntity<>(country, HttpStatus.OK);
    }

    @RequestMapping("/cache/del/{key}")
    public ResponseEntity<?> cacheDelete(@PathVariable String key) {
        cacheService.del(key);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
