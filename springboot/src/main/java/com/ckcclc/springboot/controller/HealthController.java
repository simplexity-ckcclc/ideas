package com.ckcclc.springboot.controller;

import com.ckcclc.springboot.common.CompleteMark;
import com.ckcclc.springboot.service.CacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    private static final Logger logger = LoggerFactory.getLogger(HealthController.class);

    @Autowired
    private CacheService cacheService;

    @CompleteMark
    @RequestMapping(value = "/health", produces = "application/json")
    public String example() {
        logger.info("/health");
        return "Healthy";
    }


}
