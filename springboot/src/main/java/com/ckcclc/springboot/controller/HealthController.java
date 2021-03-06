package com.ckcclc.springboot.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    private static final Logger logger = LoggerFactory.getLogger(HealthController.class);

    @RequestMapping(value = "/health", produces = "application/json")
    public String example() {
        logger.info("/health");
        return "Healthy";
    }
}
