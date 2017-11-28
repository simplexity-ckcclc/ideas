package com.ckcclc.springboot.controller;

import com.alibaba.fastjson.JSON;
import com.ckcclc.springboot.common.ErrorCode;
import com.ckcclc.springboot.common.Response;
import com.ckcclc.springboot.dao.PersonMapper;
import com.ckcclc.springboot.entity.Face;
import com.ckcclc.springboot.entity.People;
import com.ckcclc.springboot.entity.Person;
import com.ckcclc.springboot.entity.Target;
import com.ckcclc.springboot.exception.BusinessException;
import com.ckcclc.springboot.service.CacheService;
import com.ckcclc.springboot.service.RetryService;
import com.thoughtworks.xstream.XStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
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

    @Autowired
    private RetryService retryService;

    @RequestMapping("/target")
    public String target(@RequestBody Target target) {
        return target.getName() + "." + String.valueOf(target.getAge());
    }

//    @RequestMapping("/person")
//    public Person person(@RequestParam int age) {
//        return personMapper.findByName(name);
//    }

    @RequestMapping("/targetperson")
    public String targetPerson(@RequestBody Target target) {
        return target.getPerson().getName() + "." + String.valueOf(target.getPerson().getAge());
    }

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

    @RequestMapping("/successVal/{value}")
    public ResponseEntity<Response> successVal(@PathVariable("value") String value) {
        Response response = new Response();
        Map<String, Object> map = new HashMap<>();
        map.put("foo", 1);
        map.put("bar", value);
        response.setResult(map);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping("/exception")
    public ResponseEntity<String> exception() {
        return new ResponseEntity<>(new Response(ErrorCode.UNKNOWN).toString(), HttpStatus.OK);
    }

    @RequestMapping("/success")
    public ResponseEntity<Response> success() {
        return new ResponseEntity<>(new Response(), HttpStatus.OK);
    }

    @RequestMapping(value = "/multipart")
    public ResponseEntity<Response> multipart(@RequestPart("file") MultipartFile file,
                            @RequestPart("person") Person person) {
        System.out.println(file.getName());
        System.out.println(person.getAge());

        return new ResponseEntity<>(new Response(), HttpStatus.OK);
    }

    @RequestMapping(value = "/multipart_params")
    public ResponseEntity<Response> multipartParams(@RequestParam("file") MultipartFile file,
                                                    @RequestParam("name") String name) {
        System.out.println(file.getSize());
        System.out.println(file.getOriginalFilename());
        System.out.println(name);
        return new ResponseEntity<>(new Response(), HttpStatus.OK);
    }

    @RequestMapping("/chinese")
    public String chinese(@RequestParam String name) {
        return "中文： " + name;
    }

    @RequestMapping("/retry/{name}")
    public ResponseEntity<String> retry(@PathVariable String name) {
        retryService.remoteCall(name);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/xml", method = RequestMethod.POST, produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<?> xml() {
        People people = new People();
        people.setPeopleId("foo");
        Face face1 = new Face();
        face1.setFaceId("bar");
        face1.setState("ok");

        Face face2 = new Face();
        face2.setFaceId("foobar");
        face2.setState("ok");

        List<Face> faceList = new ArrayList<>();
        faceList.add(face1);
        faceList.add(face2);
        people.setFaceList(faceList);

        XStream xstream = new XStream(); // DomDriver and StaxDriver instances also can be used with constructor
        xstream.alias("people", People.class); // this will remove the Country class package name
        xstream.alias("face", Face.class); // this will remove the Country class package name
        return new ResponseEntity<>(xstream.toXML(people), HttpStatus.OK);
    }

    @RequestMapping("/throw")
    public ResponseEntity<String> throwException(@RequestParam String name) throws Exception {
        throw new BusinessException();
    }


    @RequestMapping("/hold")
    public ResponseEntity<String> hold() throws Exception {
        System.out.println("receive request");
        TimeUnit.SECONDS.sleep(10);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping("/sensetime/alarm")
    public ResponseEntity<String> alarm(@RequestBody Map<String, Object> params) throws Exception {
        System.out.println(JSON.toJSONString(params));
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
