package com.ckcclc.springboot.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @RequestMapping(value = "/health", produces = "application/json")
    public String example() {
        return "Healthy";
    }
}
